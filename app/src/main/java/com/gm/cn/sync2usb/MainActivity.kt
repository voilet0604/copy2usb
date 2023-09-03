package com.gm.cn.sync2usb

import android.app.PendingIntent
import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.jahnen.libaums.core.UsbMassStorageDevice
import me.jahnen.libaums.core.fs.UsbFileOutputStream
import me.jahnen.libaums.core.fs.UsbFileStreamFactory.createBufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream


const val ACTION_USB_PERMISSION = "com.gm.cn.synclogplugin.action_usb_permission"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_name)
        btn.setOnClickListener {

            val file = File("${filesDir.absoluteFile}/myapp")
            if(!file.exists()) {
                file.mkdirs()
            }

            val ff = File("${file.absoluteFile}/1.txt")
            if(!ff.exists()) {
                ff.createNewFile()
            }

            val sss = File("mnt/media_rw")

            btn.text = (sss.list() == null).toString()

        }


//        btn.setOnClickListener {
//
//            val usbManager = getSystemService(UsbManager::class.java)
//
//            val devices = UsbMassStorageDevice.getMassStorageDevices(this /* Context or Activity */)
//
//            for (device in devices) {
//
////                if(!usbManager.hasPermission(device.usbDevice)) {
////
////                    val permissionIntent = PendingIntent.getBroadcast(
////                        this,
////                        0,
////                        Intent(ACTION_USB_PERMISSION),
////                        PendingIntent.FLAG_IMMUTABLE
////                    )
////                    usbManager.requestPermission(device.usbDevice, permissionIntent)
////                    return@setOnClickListener
////                }
//
//
//                // before interacting with a device you need to call init()!
////                device.init()
////                val currentFs = device.partitions[0].fileSystem
////
////                copyDirectory2Usb(filesDir, currentFs)
//
//
//                // Only uses the first partition on the device
////                val currentFs = device.partitions[0].fileSystem
//
//
////                val root = currentFs.rootDirectory
////
////                val files = root.listFiles()
//
////                var fileFolder = root.search("foo")
////                if(fileFolder != null) {
////
////                    btn.text = "foo 已经存在"
////                } else {
////                    fileFolder = root.createDirectory("foo")
////                }
////
////                val file = fileFolder.createFile("a.pdf")
////
////                val os: OutputStream = createBufferedOutputStream(file, currentFs)
////
////
////                val sourceFile = File("${filesDir}/myapp/a.pdf")
////
////                val inp = FileInputStream(sourceFile)
////
////                val buffer = ByteArray(4096)
////
////                var byteRead: Int
////
////                while (inp.read(buffer).also { byteRead = it } != -1) {
////                    os.write(buffer, 0, byteRead)
////                }
//
//
////                inp.use {
////
////                    os.use {
////
////                    }
////
////                }
//
////                val file = fileFolder.createFile("${System.currentTimeMillis()}.txt")
////
////
////                val os = UsbFileOutputStream(file)
//
////                os.write("hello".toByteArray())
//
////                inp.close()
////                os.close()
//
//
//                device.close()
//
//            }
//
//
//        }

    }
}