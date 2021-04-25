package com.example.filmenthusiasts.webview

import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.filmenthusiasts.R

class MovieWebView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateWebView(url = getString(R.string.zocdoc), context = this)
        }
    }

    @Composable
    fun CreateWebView(url: String, context: Context) {
        val webViewClient = WebViewClient()
        AndroidView(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            factory = {
            WebView(context).apply {
                this.webViewClient = webViewClient
                loadUrl(url)
            }
        })
    }
}