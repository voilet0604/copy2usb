package com.gm.cn.synclogthrid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.gm.cn.synclogthrid.service.CopyLogService


class UsbReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                device?.run {
                    val service = CopyLogService.start(context, this)
                    context.startService(service)
                }
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                val service = CopyLogService.stop(context.applicationContext)
                context.applicationContext.startService(service)
            }
        }
    }
}