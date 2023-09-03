package com.gm.cn.synclogthrid.service

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.IBinder
import android.util.Log
import com.gm.cn.synclogthrid.UsbFileHelper
import com.gm.cn.synclogthrid.copyDirectory2Usb
import me.jahnen.libaums.core.UsbMassStorageDevice
import java.util.concurrent.Executors

class CopyLogService: Service() {

    private val usbManager by lazy { applicationContext.getSystemService(UsbManager::class.java) }

    private val massStorageDevices by lazy {
        UsbMassStorageDevice.getMassStorageDevices(applicationContext)
    }

    private val executor = Executors.newFixedThreadPool(3)

    private val receiver = PermissionReceiver()

    private var status = ATTACHED

    private var isRegister: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        status = intent.getStringExtra(KEY_STATUS) ?: DETACHED
        if(status == ATTACHED) {
            val device: UsbDevice? = intent.getParcelableExtra(KEY_DATA)
            device?.let {
                if(usbManager.hasPermission(device)) {
                    startCopy(device)
                } else {
                    if(isRegister) {
                        applicationContext.unregisterReceiver(receiver)
                        isRegister = false
                    }
                    applicationContext.registerReceiver(receiver, IntentFilter(ACTION_USB_PERMISSION))
                    isRegister = true
                    val perIntent = Intent(ACTION_USB_PERMISSION)
                    perIntent.putExtra(UsbManager.EXTRA_DEVICE, device)
                    val permissionIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        0,
                        perIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    usbManager.requestPermission(device, permissionIntent)
                }

            }
        } else {
            UsbFileHelper.close()
        }
        return START_STICKY
    }

    private fun startCopy(usbDevice: UsbDevice) {
        val aumsDevice = massStorageDevices.find { it.usbDevice == usbDevice }
        aumsDevice?.let { aDevice ->
            aDevice.init()
            val currentFs = aDevice.partitions[0].fileSystem
            UsbFileHelper.start()
            executor.submit {
                try {
                    Log.d("ACTION_USB_PERMISSION", "copy开始")
                    copyDirectory2Usb(applicationContext.filesDir, currentFs)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.d("ACTION_USB_PERMISSION", "copy结束")
            }
        }
    }


    inner class PermissionReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if(intent.action == ACTION_USB_PERMISSION) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                device?.run {
                    if(usbManager.hasPermission(device)) {
                        startCopy(device)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UsbFileHelper.close()
        if(isRegister) {
            applicationContext.unregisterReceiver(receiver)
        }
    }

    companion object {

        private const val ACTION_USB_PERMISSION = "com.gm.cn.synclogplugin.action_usb_permission"

        private const val KEY_STATUS = "key_status"

        private const val KEY_DATA = UsbManager.EXTRA_DEVICE

        private const val ATTACHED = UsbManager.ACTION_USB_DEVICE_ATTACHED

        private const val DETACHED = UsbManager.ACTION_USB_DEVICE_DETACHED

        fun start(context: Context, usbDevice: UsbDevice): Intent {
            return Intent(context, CopyLogService::class.java).apply {
                putExtra(KEY_STATUS, ATTACHED)
                putExtra(KEY_DATA, usbDevice)
            }
        }

        fun stop(context: Context): Intent {
            return Intent(context, CopyLogService::class.java).apply {
                putExtra(KEY_STATUS, DETACHED)
            }
        }

    }
}