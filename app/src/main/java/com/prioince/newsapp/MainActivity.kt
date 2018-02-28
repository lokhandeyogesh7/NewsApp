package com.prioince.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ShareCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    var arrChannel = ArrayList<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        setupViewPager()

        val adRequest = AdRequest.Builder()
               /* .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("691D88AFF4BA3627C25D11013C04ECC3")*/
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

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.nav_live_tv -> {
                val intLive = Intent(this@MainActivity, LiveChannelActivity::class.java)
                startActivity(intLive)
            }
            R.id.nav_share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                val text = "सर्व मराठी वृत्तवाहिन्या एकाच अॅप मध्ये इन्स्टॉल करा "
                val link = "https://play.google.com/store/apps/details?id=" + this.packageName
                i.putExtra(Intent.EXTRA_TEXT, text + " " + link)
                startActivity(Intent.createChooser(i, "निवडा"))
            }
            R.id.nav_rate_us -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.packageName)))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + this.packageName)))
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(p0: View?) {
        val holder = p0!!.tag as? ChannelAdapter.MyViewHolder
        val position = arrChannel[holder?.adapterPosition!!]
        println("clicked position is ${arrChannel[holder?.adapterPosition!!].channelName}")
        if (position.channelName == "टीव्ही ९ मराठी ") {
            val intent = Intent(this@MainActivity, YoutubePlayerActivity::class.java)
            intent.putExtra("link", position.link)
            intent.putExtra("title", position.channelName)
            startActivity(intent)
        } else {
            val intent = Intent(this@MainActivity, WebActivity::class.java)
            intent.putExtra("link", position.link)
            intent.putExtra("title", position.channelName)
            startActivity(intent)
        }
    }

    private fun setupViewPager() {
        arrChannel = ArrayList<Channel>()
        arrChannel.add(Channel("एबीपी माझा", R.drawable.abp_majha, "http://abpmajha.abplive.in/"))
        arrChannel.add(Channel("झी २४ तास", R.drawable.zee24taas, "http://zeenews.india.com/marathi"))
        arrChannel.add(Channel("न्यूज१८ लोकमत ", R.drawable.news18, "http://lokmat.news18.com/"))
        arrChannel.add(Channel("जय महाराष्ट्र ", R.drawable.jai_maharashtra_news_channel, "http://jaimaharashtranews.tv/"))
        //arrChannel.add(Channel("महाराष्ट्र  १", R.drawable.maharashtra1, "https://maharashtra1news.in/"))
        // arrChannel.add(Channel("टीव्ही ९ मराठी ", R.drawable.tv9, "<iframe width=\"640\" height=\"360\" src=\"https://www.youtube.com/embed/z5JTPPcMJyw\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>"))
        arrChannel.add(Channel("साम टीव्ही", R.drawable.saam, "http://saamtv.com"))

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = ChannelAdapter(arrChannel, this@MainActivity)
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
}
