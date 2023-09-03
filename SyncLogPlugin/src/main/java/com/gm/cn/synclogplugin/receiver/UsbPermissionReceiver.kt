package com.gm.cn.synclogplugin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gm.cn.synclogplugin.UsbController


const val ACTION_USB_PERMISSION = "com.gm.cn.synclogplugin.action_usb_permission"

class UsbPermissionReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val usbController = UsbController.getInstance(context)

        if(ACTION_USB_PERMISSION == intent.action) {


            Log.e("UsbController", "${usbController.getStoreUsbDevice()}")



        }
    }
}