package com.example.zamnadocscannerdemo

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.PermissionRequest
import android.os.Build
import android.view.View
import android.webkit.WebViewClient

class MainActivity : CameraPermissionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        // Check for camera permission and request it, if it hasn't yet been granted.
        // Once we have the permission the onCameraPermissionGranted() method will be called.
        requestCameraPermission()
    }

    override fun onCameraPermissionGranted() {
        val webView = findViewById<View>(R.id.webview) as WebView
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        webView.loadUrl("https://testik.zamna.com")
        WebView.setWebContentsDebuggingEnabled(true)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.resources)
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        }
    }
}