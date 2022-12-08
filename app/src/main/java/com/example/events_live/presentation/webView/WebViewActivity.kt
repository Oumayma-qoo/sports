package com.example.events_live.presentation.webView

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.events_live.R
import com.example.events_live.common.utils.SPApp
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val sp = SPApp(this)
        sp.web_was_opened = true

        try {
            webView = findViewById(R.id.webvieww)
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()
            webView.settings.javaScriptEnabled = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.domStorageEnabled = true
            val url = sp.URL
            webView.loadUrl(url)
        } catch (e: Exception) {
            println(e.message)
        }
        back_btn_webb.setOnClickListener {
            sp.web_was_opened = false
            finish()
        }

        if(sp.init)
        {
            Log.d("Innnit", sp.init.toString())
            back_btn_webb.visibility= GONE
        }

    }


    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            finishAffinity()
    }
}

