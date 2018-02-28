package com.prioince.newsapp

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.prioince.newsapp.enablevideoview.VideoEnabledWebChromeClient
import kotlinx.android.synthetic.main.activity_video_view.*
import kotlinx.android.synthetic.main.content_main.*


class WebActivity : AppCompatActivity() {

    private var webChromeClient: VideoEnabledWebChromeClient = VideoEnabledWebChromeClient()

    var title: String? = null
    var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(android.graphics.Color.WHITE)

        link = intent.getStringExtra("link")
        title = intent.getStringExtra("title")

        println("link is $link and title is $title")

        supportActionBar?.title = title
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back))

        setUpViewForVideo(link)

        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("691D88AFF4BA3627C25D11013C04ECC3")
                .build()

        adViewVideoView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdLeftApplication() {
                super.onAdLeftApplication()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }
        }

        adViewVideoView.loadAd(adRequest)

    }

    //For VideoWebView
    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun setUpViewForVideo(strUrl: String?) {

        val userAgent = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36"
        enablevideoview.settings.userAgentString = userAgent
        enablevideoview.settings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= 19) {
            enablevideoview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            enablevideoview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        val loadingView = layoutInflater.inflate(R.layout.view_loading_video, null) // Your own view, read class comments
        webChromeClient = object : VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, enablevideoview) // See all available constructors...
        {
            override fun onProgressChanged(view: WebView, progress: Int) {
            }
        }
        webChromeClient.setOnToggledFullscreen { fullscreen ->
            if (fullscreen) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                val attrs = window.attributes
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                window.attributes = attrs
                if (Build.VERSION.SDK_INT >= 14) {

                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                val attrs = window.attributes
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                window.attributes = attrs
                if (Build.VERSION.SDK_INT >= 14) {

                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
        enablevideoview.webChromeClient = webChromeClient
        enablevideoview.webViewClient = InsideWebViewClient()

        if (strUrl!!.contains("iframe")) {
            val strUrl1 = "<iframe width=\"100%\" height=\"100%\"" + strUrl.substring(strUrl.indexOf("src="), strUrl.length)
            enablevideoview.loadData(strUrl1, "text/html", "utf-8");
        } else {
            enablevideoview.loadUrl(strUrl)
        }
    }

    private inner class InsideWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, url: WebResourceRequest): Boolean {
            view.loadUrl(url.url.toString())
            return true
        }

        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            val animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate)
            progressBarCourseVideo.visibility = View.VISIBLE
            progressBarCourseVideo.text = "$title लोड होत आहे"
            progressBarCourseVideo.animation = animRotate
            progressBarCourseVideo.animate()

            view!!.visibility = View.GONE
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            progressBarCourseVideo.visibility = View.GONE
            progressBarCourseVideo.animation = null
            view!!.visibility = View.VISIBLE
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun onPause() {
        super.onPause()
        if (adViewVideoView != null) {
            adViewVideoView.pause()
        }
        enablevideoview.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        enablevideoview.resumeTimers()
        if (adViewVideoView != null) {
            adViewVideoView.resume()
        }
    }

    override fun onDestroy() {
        enablevideoview.stopLoading()
        enablevideoview.destroy()
        if (adViewVideoView != null) {
            adViewVideoView.destroy()
        }
        super.onDestroy()
    }
}
