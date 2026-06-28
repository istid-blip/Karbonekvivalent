package com.sveis.karbonekvivalent.util

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSString
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSURL
import kotlinx.cinterop.ExperimentalForeignApi

/**
 * iOS-implementasjon av deling av backup-fil.
 * Bruker UIActivityViewController for å vise systemets dele-ark.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun shareBackupFile(jsonContent: String, fileName: String) {
    // 1. Opprett en midlertidig filsti
    val tempDir = NSTemporaryDirectory()
    val filePath = (tempDir as NSString).stringByAppendingPathComponent(fileName)
    
    // 2. Skriv innholdet til filen
    (jsonContent as NSString).writeToFile(
        path = filePath,
        atomically = true,
        encoding = NSUTF8StringEncoding,
        error = null
    )
    
    // 3. Opprett en URL for filen
    val fileUrl = NSURL.fileURLWithPath(filePath)
    
    // 4. Opprett og vis UIActivityViewController
    val activityController = UIActivityViewController(
        activityItems = listOf(fileUrl),
        applicationActivities = null
    )
    
    // Finn rot-viewcontrolleren for å vise dele-arket
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootViewController?.presentViewController(
        viewControllerToPresent = activityController,
        animated = true,
        completion = null
    )
}

/**
 * iOS-implementasjon av lagring av backup-fil.
 * Gjenbruker dele-arket da det inkluderer "Lagre i Filer".
 */
actual fun saveBackupFile(jsonContent: String, fileName: String) {
    shareBackupFile(jsonContent, fileName)
}
