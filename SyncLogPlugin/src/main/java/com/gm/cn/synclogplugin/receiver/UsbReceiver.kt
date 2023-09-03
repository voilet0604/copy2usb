package com.gm.cn.synclogplugin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log
import com.gm.cn.synclogplugin.UsbController

class UsbReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val usbController = UsbController.getInstance(context)

        when(intent.action) {

            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                // 需要是否有权限
                if(usbController.requestPermission(context, intent)) {

                    // open

                    // write

                    Log.e("UsbController", "xxx ${usbController.getStoreUsbDevice()}")


                } else {

                    Log.e("UsbController", "授权过了 ${usbController.getStoreUsbDevice()}")

                }


            }

            UsbManager.ACTION_USB_DEVICE_DETACHED -> {

            }
        }

    }
}