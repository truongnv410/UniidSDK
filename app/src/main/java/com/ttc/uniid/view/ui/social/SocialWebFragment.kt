package com.ttc.uniid.view.ui.social

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.ttc.uniid.R
import kotlinx.android.synthetic.main.fragment_fb_web.*


class SocialWebFragment : Fragment() {
    private var httpUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fb_web, container, false)
    }

    companion object {
        const val MAX_PROGRESS = 100
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initWebView()
        setWebClient()
        loadUrl()
    }

    private fun loadUrl() {
        val headers: MutableMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["device"] = Build.MODEL
        headers["deviceType"] = "MOBILE"

        arguments?.let {
            httpUrl = it.getString("httpUrl") as String
        }
        httpUrl?.let {
            webView.loadUrl(it, headers)
        }
    }
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.userAgentString= "com.unitel.loginsignupsdk"
        webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("kevin", "onPageFinished: " + url)
                url?.let {
                    if (url.contains("unitelsdk://ttc")) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }
}