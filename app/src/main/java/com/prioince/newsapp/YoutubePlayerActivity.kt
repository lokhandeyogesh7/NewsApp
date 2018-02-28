package com.prioince.newsapp

import android.os.Bundle
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_video_view.*
import kotlinx.android.synthetic.main.activity_youtube_player.*
import kotlinx.android.synthetic.main.custom_top.*
import android.content.pm.ActivityInfo
import android.os.Build
import android.annotation.SuppressLint
import android.provider.Settings




class YoutubePlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {
    override fun onFullscreen(p0: Boolean) {
        if (p0) {
            requestedOrientation = LANDSCAPE_ORIENTATION;
        } else {
            requestedOrientation = PORTRAIT_ORIENTATION;
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPlayer?.setFullscreen(true)
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPlayer?.setFullscreen(false)
        }
    }

    private val RECOVERY_DIALOG_REQUEST = 1
    val DEVELOPER_KEY = "AIzaSyBKKV_8QmYn9l6Nsac-MN6I7jKrUrrpN1I"

    var title: String? = null
    var link: String? = null
    @SuppressLint("InlinedApi")
    private val PORTRAIT_ORIENTATION = if (Build.VERSION.SDK_INT < 9)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    else
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

    @SuppressLint("InlinedApi")
    private val LANDSCAPE_ORIENTATION = if (Build.VERSION.SDK_INT < 9)
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    else
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

    private val mPlayer: YouTubePlayer? = null
    private var mAutoRotation = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        title = intent.getStringExtra("title")
        link = intent.getStringExtra("link")
        tvTitleHeader.text = title

        youtube_view.initialize(DEVELOPER_KEY, this)

        mAutoRotation = Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

        ivBackButton.setOnClickListener {
            onBackPressed()
        }
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("691D88AFF4BA3627C25D11013C04ECC3")
                .build()

        adViewYoutube.adListener = object : AdListener() {
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

        adViewYoutube.loadAd(adRequest)

    }


    override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                         errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(
                    ("error"), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                         player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            player.loadVideo(link)

            // Hiding player controls
            //player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
        }

        player.setOnFullscreenListener(this)

        if (mAutoRotation) {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    or YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        } else {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    or YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    or YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(DEVELOPER_KEY, this)
        }
    }

    private fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return findViewById<View>(R.id.youtube_view) as YouTubePlayerView
    }


    override fun onPause() {
        super.onPause()
        if (adViewYoutube != null) {
            adViewYoutube.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (adViewYoutube != null) {
            adViewYoutube.resume()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        if (adViewYoutube != null) {
            adViewYoutube.destroy()
        }
        super.onDestroy()
    }
}
