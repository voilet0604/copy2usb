package com.gm.cn.synclogplugin

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.gm.cn.synclogplugin.receiver.ACTION_USB_PERMISSION
import com.gm.cn.synclogplugin.receiver.UsbPermissionReceiver

class UsbController private constructor(private val appContext: Context) {

    private val mUsbManager = appContext.getSystemService(UsbManager::class.java)

    private var mStoreUsbDevice: UsbDevice? = null

    companion object {

        private val lock = Any()

        private var instance: UsbController? = null

        fun getInstance(appContext: Context): UsbController {
            if(instance == null) {
                synchronized(lock) {
                    if(instance == null) {
                        instance = UsbController(appContext)
                    }
                }
            }
            return instance!!
        }
    }

    fun hasPermission(intent: Intent):Boolean {
        val device = getStoreUsbDevice(intent) ?: return false
        return mUsbManager.hasPermission(device)
    }

    fun requestPermission(context: Context, intent: Intent): Boolean {
        if(!hasPermission(intent)) {
            val device = getStoreUsbDevice()
            if(device != null) {
                val filter = IntentFilter(ACTION_USB_PERMISSION)
                context.applicationContext.registerReceiver(UsbPermissionReceiver(), filter)
                val permissionIntent = PendingIntent.getBroadcast(
                    context, 0, Intent(
                        ACTION_USB_PERMISSION
                    ), PendingIntent.FLAG_IMMUTABLE
                )
                mUsbManager.requestPermission(device, permissionIntent)
            }
            return false
        }
        return true
    }

    fun getStoreUsbDevice(intent: Intent): UsbDevice? {
        mStoreUsbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
        return mStoreUsbDevice
    }

    fun getStoreUsbDevice(): UsbDevice? {
        return mStoreUsbDevice
    }


}