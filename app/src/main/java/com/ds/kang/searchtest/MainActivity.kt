package com.ds.kang.searchtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ds.kang.searchtest.ui.dialog.NetworkConnectionFailDialogFragment
import com.ds.kang.searchtest.ui.track.favorite.FavFragment
import com.ds.kang.searchtest.ui.track.home.HomeFragment
import com.ds.kang.searchtest.util.NetworkChecker
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener { menuItem ->
            this.title = menuItem.title
            when (menuItem.itemId) {
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.nav_host_fragment_activity_main,
                            HomeFragment(), HomeFragment.CLASS_NAME
                        ).commit()
                    true
                }
                R.id.navigation_favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.nav_host_fragment_activity_main,
                            FavFragment(), FavFragment.CLASS_NAME
                        ).commit()
                    true
                }
            }
            false
        }

        navView.selectedItemId = R.id.navigation_search
        if (!NetworkChecker.isConnectedNetwork(applicationContext)) {
            showNetworkConnectionFailDialog()
        }
    }

    private fun showNetworkConnectionFailDialog() {
        NetworkConnectionFailDialogFragment()
            .show(supportFragmentManager, NetworkConnectionFailDialogFragment.CLASS_NAME)
    }
}