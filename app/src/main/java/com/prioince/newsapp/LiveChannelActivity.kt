package com.prioince.newsapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_live_channel.*
import kotlinx.android.synthetic.main.content_main.*

class LiveChannelActivity : AppCompatActivity(), View.OnClickListener {

    var arrChannel = ArrayList<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_channel)
        setSupportActionBar(tbLive)

        supportActionBar?.title = "थेट प्रक्षेपण"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tbLive.setTitleTextColor(android.graphics.Color.WHITE)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back))

        setupViewPager()
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("691D88AFF4BA3627C25D11013C04ECC3")
                .build()

        adView.adListener = object : AdListener() {
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

        adView.loadAd(adRequest)

    }

    public override fun onPause() {
        if (adView != null) {
            adView.pause()
        }
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        if (adView != null) {
            adView.resume()
        }
    }

    public override fun onDestroy() {
        if (adView != null) {
            adView.destroy()
        }
        super.onDestroy()
    }

    override fun onClick(p0: View?) {
        val holder = p0!!.tag as? ChannelAdapter.MyViewHolder
        val position = arrChannel[holder?.adapterPosition!!]
        println("clicked position is ${arrChannel[holder?.adapterPosition!!].channelName}")
       /* if (holder.adapterPosition == 0) {
            val intent = Intent(this@LiveChannelActivity, WebActivity::class.java)
            intent.putExtra("link", position.link)
            intent.putExtra("title", position.channelName)
            startActivity(intent)
        } else {*/
            val intent = Intent(this@LiveChannelActivity, YoutubePlayerActivity::class.java)
            intent.putExtra("link", position.link)
            intent.putExtra("title", position.channelName)
            startActivity(intent)
       // }
    }


    private fun setupViewPager() {
        arrChannel = ArrayList<Channel>()
        //arrChannel.add(Channel("एबीपी माझा", R.drawable.abp_majha, "http://abpmajha.abplive.in/live-tv"))
        //arrChannel.add(Channel("झी २४ तास", R.drawable.zee24taas, "http://zeenews.india.com/marathi"))
        //arrChannel.add(Channel("न्यूज१८ लोकमत ", R.drawable.news18, "http://lokmat.news18.com/"))
        //arrChannel.add(Channel("जय महाराष्ट्र ", R.drawable.jai_maharashtra_news_channel, "uoz9bOPl7yM"))
        arrChannel.add(Channel("महाराष्ट्र  १", R.drawable.maharashtra1, "vrVMsPPrOY4"))
        arrChannel.add(Channel("टीव्ही ९ मराठी ", R.drawable.tv9, "z5JTPPcMJyw"))
        //  arrChannel.add(Channel("साम टीव्ही", R.drawable.saam, "http://saamtv.com"))

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = ChannelAdapter(arrChannel, this@LiveChannelActivity)
        recyclerView.adapter = adapter
    }

    internal inner class ChannelAdapter(private val arrChannel: List<Channel>, val onClickListener: View.OnClickListener) : RecyclerView.Adapter<ChannelAdapter.MyViewHolder>() {
        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var title: TextView
            var logo: ImageView
            var relativeBg: RelativeLayout

            init {
                title = view.findViewById(R.id.tvChannelName)
                logo = view.findViewById(R.id.ivChannelLogo)
                relativeBg = view.findViewById(R.id.relativeBg)

                view.tag = this
                view.setOnClickListener(onClickListener)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.grid_row, parent, false)

            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val channel = arrChannel.get(position)
            holder.title.text = channel.channelName
            holder.logo.setImageResource(channel.image)
            //holder.relativeBg.setBackgroundColor(Color.parseColor("#696969"))
        }

        override fun getItemCount(): Int {
            return arrChannel.size
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
