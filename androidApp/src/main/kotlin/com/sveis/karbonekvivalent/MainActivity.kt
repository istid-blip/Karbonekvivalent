package com.sveis.karbonekvivalent

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.sveis.karbonekvivalent.data.DatabaseDriverFactory
import com.sveis.karbonekvivalent.util.AndroidContext
import com.sveis.karbonekvivalent.util.MainActivityInterface
import com.sveis.karbonekvivalent.util.performNativeShare
import java.io.OutputStream

class MainActivity : ComponentActivity(), MainActivityInterface {

    private var pendingJsonContent: String? = null

    private val createDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream: OutputStream ->
                outputStream.write(pendingJsonContent?.toByteArray() ?: byteArrayOf())
                pendingJsonContent = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        AndroidContext.set(this)

        setContent {
            App(DatabaseDriverFactory(this))
        }
    }

    override fun triggerSaveToDisk(jsonContent: String, fileName: String) {
        pendingJsonContent = jsonContent
        createDocumentLauncher.launch(fileName)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(DatabaseDriverFactory(LocalContext.current))
}
