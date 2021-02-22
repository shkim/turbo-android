package dev.hotwire.turbo.views

import android.net.Uri
import android.os.Message
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebView.HitTestResult
import dev.hotwire.turbo.session.TurboSession
import dev.hotwire.turbo.util.TurboLog
import dev.hotwire.turbo.util.toJson
import dev.hotwire.turbo.visit.TurboVisitOptions

open class TurboWebChromeClient(val session: TurboSession) : WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        return session.fileChooserDelegate.onShowFileChooser(
            filePathCallback = filePathCallback,
            params = fileChooserParams
        )
    }

    override fun onCreateWindow(webView: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
        val location = hitTestResult(webView)
            ?: messageResult(webView)
            ?: return false

        session.visitProposedToLocation(
            location = location,
            optionsJson = TurboVisitOptions().toJson()
        )

        return false
    }

    private fun hitTestResult(webView: WebView): String? {
        return when (webView.hitTestResult.type) {
            HitTestResult.SRC_ANCHOR_TYPE -> webView.hitTestResult.extra
            else -> null
        }
    }

    private fun messageResult(webView: WebView): String? {
        val message = webView.handler.obtainMessage()
        webView.requestFocusNodeHref(message)

        return message.data.getString("url")
    }
}
