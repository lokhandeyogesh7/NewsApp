package com.prioince.newsapp

import android.os.Bundle
import android.content.Intent
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


class YoutubePlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val RECOVERY_DIALOG_REQUEST = 1
    val DEVELOPER_KEY = "AIzaSyBKKV_8QmYn9l6Nsac-MN6I7jKrUrrpN1I";

    // YouTube video id
    val YOUTUBE_VIDEO_CODE = "z5JTPPcMJyw";
    var title: String? = null
    var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        title = intent.getStringExtra("title")
        link = intent.getStringExtra("link")
        tvTitleHeader.text = title

        youtube_view.initialize(DEVELOPER_KEY, this)

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
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
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

    override fun onDestroy() {
        if (adViewYoutube != null) {
            adViewYoutube.destroy()
        }
        super.onDestroy()
    }

}
