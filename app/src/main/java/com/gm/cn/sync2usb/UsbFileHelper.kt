package com.gm.cn.sync2usb

import android.icu.text.SimpleDateFormat
import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.UsbFile
import me.jahnen.libaums.core.fs.UsbFileStreamFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.Locale


private const val TARGET_FOLDER = "navi_log"

private val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE)

@Throws(IOException::class)
fun copyDirectory2Usb(sourceFolder: File, fileSystem: FileSystem) {

    val root = fileSystem.rootDirectory

    var naviLogFolder = root.search(TARGET_FOLDER)

    // 如果 navi_log 目录不存在，就创建
    if (naviLogFolder == null) {
        naviLogFolder = root.createDirectory(TARGET_FOLDER)
    }

    val subFolderName = dateFormat.format(System.currentTimeMillis())
    var subLogFolder = naviLogFolder.search(subFolderName)

    // 如果 navi_log/yyyyMMdd_HHmmss目录不存在，就创建
    if (subLogFolder == null) {
        subLogFolder = naviLogFolder.createDirectory(subFolderName)
    }

    copyDirectory(sourceFolder, subLogFolder, fileSystem)
}

@Throws(IOException::class)
private fun copyDirectory(sourceDir: File, targetParentDir: UsbFile, fileSystem: FileSystem) {
    if (sourceDir.isDirectory) {
        // directory
        var targetDir = targetParentDir.search(sourceDir.name)
        if(targetDir == null) {
            targetDir = targetParentDir.createDirectory(sourceDir.name)
        }

        val children = sourceDir.list()
        children?.forEach { child ->
            val sourceChildDir = File(sourceDir, child)
            copyDirectory(sourceChildDir, targetDir!!, fileSystem)
        }
    } else {
        // file
        FileInputStream(sourceDir).use { fis ->
            var targetDir = targetParentDir.search(sourceDir.name)
            if(targetDir == null) {
                targetDir = targetParentDir.createFile(sourceDir.name)
            }
            UsbFileStreamFactory.createBufferedOutputStream(targetDir!!, fileSystem).use { fos ->
                val buffer = ByteArray(4096)
                var byteRead: Int
                while (fis.read(buffer).also { byteRead = it } != -1) {
                    fos.write(buffer, 0, byteRead)
                }
            }
        }
    }
}

