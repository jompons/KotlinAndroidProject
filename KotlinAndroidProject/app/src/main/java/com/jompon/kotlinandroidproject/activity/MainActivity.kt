package com.jompon.kotlinandroidproject.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.jompon.kotlinandroidproject.fragment.GalleryFragment
import com.jompon.kotlinandroidproject.fragment.MapFragment
import com.jompon.kotlinandroidproject.R
import com.jompon.kotlinandroidproject.base.BaseActivity
import com.jompon.kotlinandroidproject.fragment.SlideshowFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setBindingData()
        if( savedInstanceState == null ){
            onNavigationItemSelected(R.id.nav_gallery)
        }
    }

    override fun setBindingData() {
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        onNavigationItemSelected(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onNavigationItemSelected(itemId: Int) {

        // Handle navigation view item clicks here.
        when (itemId) {

            R.id.nav_gallery -> {

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_frame, GalleryFragment(), "gallery")
                fragmentTransaction.commit()
            }
            R.id.nav_slideshow -> {

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_frame, SlideshowFragment(), "slideshow")
                fragmentTransaction.commit()
            }
            R.id.nav_map -> {

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.content_frame, MapFragment(), "map")
                fragmentTransaction.commit()
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
    }
}
