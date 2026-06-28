package com.sveis.karbonekvivalent.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

/**
 * Android-spesifikk kontekst-holder for å kunne utføre dele-handlinger fra commonMain.
 */
object AndroidContext {
    private var contextRef: WeakReference<Context>? = null
    private var activityRef: WeakReference<Activity>? = null
    
    fun set(context: Context) {
        this.contextRef = WeakReference(context.applicationContext)
        if (context is Activity) {
            this.activityRef = WeakReference(context)
        }
    }
    
    fun getContext(): Context? = contextRef?.get()
    fun getActivity(): Activity? = activityRef?.get()
}

/**
 * Android-implementasjon av deling av backup-fil.
 */
actual fun shareBackupFile(jsonContent: String, fileName: String) {
    performNativeShare(jsonContent, fileName)
}

/**
 * Android-implementasjon av lagring av backup-fil til disk.
 */
actual fun saveBackupFile(jsonContent: String, fileName: String) {
    val activity = AndroidContext.getActivity()
    if (activity is MainActivityInterface) {
        activity.triggerSaveToDisk(jsonContent, fileName)
    }
}

/**
 * Grensesnitt for å kommunisere med MainActivity fra felleskoden.
 */
interface MainActivityInterface {
    fun triggerSaveToDisk(jsonContent: String, fileName: String)
}

/**
 * Selve delings-logikken (brukes av både dialogen og fallback).
 */
fun performNativeShare(jsonContent: String, fileName: String) {
    val context = AndroidContext.getContext() ?: return
    
    try {
        val cacheFile = File(context.cacheDir, "backups").apply { mkdirs() }
        val file = File(cacheFile, fileName)
        
        FileOutputStream(file).use { 
            it.write(jsonContent.toByteArray()) 
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Del backup-fil").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
        
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
