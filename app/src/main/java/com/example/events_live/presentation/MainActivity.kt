package com.example.events_live.presentation

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.events_live.common.utils.ContextUtils
import com.example.events_live.common.utils.SPApp
import com.example.events_live.presentation.home.HomeFragment
import com.example.events_live.presentation.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

import androidx.navigation.fragment.NavHostFragment
import com.example.events_live.R


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavGraph()
    }


    private fun setupNavGraph() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    override fun attachBaseContext(newBase: Context) {
        try {
            val sp = SPApp(newBase)
            val lang = sp.language
            val localeUpdatedContext: ContextWrapper =
                ContextUtils.updateLocale(newBase, Locale(lang))
            super.attachBaseContext(localeUpdatedContext)
        } catch (e: Exception) {
            super.attachBaseContext(newBase)
        }

    }

    override fun onBackPressed() {
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        navHostFragment!!.childFragmentManager.fragments[0]

        when( navHostFragment!!.childFragmentManager.fragments[0]){

            is HomeFragment-> this.finish()

        }
                super.onBackPressed()


    }
}