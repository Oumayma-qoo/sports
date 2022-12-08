package com.example.events_live.presentation.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.events_live.R
import com.example.events_live.common.response.ListResponse
import com.example.events_live.common.utils.SPApp
import com.example.events_live.data.retrofit.ApiRequestBannerAds
import com.example.events_live.databinding.FragmentSplashBinding
import com.example.events_live.presentation.base.BaseFragment
import com.example.events_live.presentation.webView.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    val TAG: String = "SplashFragment"
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var timer: CountDownTimer
    private  var webBrowser= false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()

        requestBanner()
        val sp = SPApp(context!!)
        sp.startApp = true

        Glide.with(this).load(R.drawable.shakeballs_animated)
            .apply( RequestOptions().override(400, 400))
            .into(splashImg)



    }

    private fun requestBanner() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                ApiRequestBannerAds.sentReqBanner(requireContext())

            } catch (e: Exception) {
                println(e.message)

            } finally {
                withContext(Dispatchers.Main) {
                    lifecycleScope.launchWhenResumed {
                            goToNext()

                    }
                }
            }
        }


    }

    fun goToNext() {
        Handler().postDelayed({
            lifecycleScope.launchWhenResumed {

                if (findNavController().currentDestination?.id == R.id.SplashFragment)
                    navController.navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }, 2000)
       
    }



}