package com.example.events_live.presentation.home

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.events_live.common.constant.Constants
import com.example.events_live.common.constant.Constants.ANIMATE_URL
import com.example.events_live.common.constant.Constants.HAVE_ANIMATE
import com.example.events_live.common.constant.Constants.HAVE_EVENT
import com.example.events_live.common.constant.Constants.MATCH_ID
import com.example.events_live.common.constant.Constants.URL
import com.example.events_live.common.response.ListResponse
import com.example.events_live.common.response.ListResponse.adsArrayList
import com.example.events_live.common.response.ListResponse.mapArrayList
import com.example.events_live.common.utils.GeneralTools
import com.example.events_live.common.utils.SPApp
import com.example.events_live.databinding.FragmentHomeBinding
import com.example.events_live.domain.models.BaseClassIndexNew
import com.example.events_live.domain.models.Match
import com.example.events_live.domain.models.TeamInfo
import com.example.events_live.presentation.base.BaseFragment
import com.example.events_live.presentation.webView.WebViewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.toolbar
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.view_top_bar.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val viewModel by viewModels<HomeViewModel>()
    val TAG: String = "HomeFragment"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    var matchesList = ArrayList<Match>()

    private val homeAdapter by lazy { HomeAdapter(matchesList, object  :HomeAdapter.LoadMoreItems{
        override fun loadMore() {
            loadMoreMatches()
        }

    })
    }
    private var bundle = Bundle()
    private lateinit var timer: CountDownTimer


    private var pageNumber = 1
    private var pageCount = 0


    companion object {
        var suggestionTeamsList = ArrayList<String>()
        var teamsList = ArrayList<TeamInfo>()
        var hashMap: HashMap<String, String> = HashMap<String, String>()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        val sp=SPApp(requireContext())

        initRV()
        initObserver()
        val language = sp.language
        lifecycleScope.launch {
            viewModel.getMatches(language, pageNumber.toString())

        }



        searchonEditText()
        toolbar.ivMenuArrow.setOnClickListener {
            navigationlayout.visibility = VISIBLE
            search_edt.isEnabled=false

        }



        app_bar_drawer_test1.img_back.setOnClickListener {
            navigationlayout.visibility = GONE
            search_edt.isEnabled=true

        }




        sp.showPopUp= true
        showPopup()
        languageClick()
        shareClick()
        rateClick()
        exitClick()

        slideImage()
        clickOnWeb()
        checkUser()
        privacyClick()
        feedbackClick()


        checkInit()



    }




    // click on web icon
    fun clickOnWeb(){
        val sp=SPApp(requireContext())
        ivWebHOme.setOnClickListener {
            if (sp.WEB_OPENED) {
                val url = sp.URL
                if (sp.WEB_OPTION)
                    goToWeb(url, "0")
                else
                    goToWeb(url, "1")
            }
        }
    }

    // if user was on web page
    fun checkUser(){
        val sp=SPApp(requireContext())
        val url= sp.URL
        bundle.putString(URL, url)

        Log.d("url1===========", sp.URL)


        if (sp.web_was_opened) {

            stopTimer()
            activity.let {
                val intent = Intent(it, WebViewActivity::class.java)
                startActivity(intent)
            }

        }


    }

    private fun checkInit()
    {
        val spApp=SPApp(requireContext())
        if( !ListResponse.redirect_url.isNullOrEmpty() && ! ListResponse.redirect_url.isNullOrEmpty())
            goToWeb(ListResponse.redirect_url!!, ListResponse.open_type!!)
        Log.d(TAG, "Innit ${spApp.init}")

    }





    // slider
    fun slideImage()
    {

            val imageList = ArrayList<SlideModel>()
            for (banner in adsArrayList) {
                imageList.add(SlideModel(banner.image_path, ScaleTypes.CENTER_CROP))
            }

            if (imageList.isNotEmpty()) {
                imageSlider.startSliding(4000)
                imageSlider.visibility = VISIBLE
                imageSlider.setImageList(imageList)
            } else
                imageSlider.visibility = GONE


            imageSlider.setItemClickListener(object : ItemClickListener {


                override fun onItemSelected(position: Int) {
                    if (!navigationlayout.isVisible) {
                        goToWeb(
                            adsArrayList[position].redirect_url,
                            adsArrayList[position].open_type
                        )
                    }
                    else
                        navigationlayout.visibility= GONE
                }

            })




    }




    fun loading(){
        val sp=SPApp(requireContext())
        if (sp.startApp)
            showLoading()
        else
            hideLoading()
        sp.startApp=false
    }


    private fun initRV() {

        val sp=SPApp(context!!)


        binding.rvMatchs.adapter = homeAdapter
        binding.rvMatchs.layoutManager = LinearLayoutManager(activity)

        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(context!!.resources.getDrawable(com.example.events_live.R.drawable.sk_line_divider))
        binding.rvMatchs.addItemDecoration(dividerItemDecoration)

        homeAdapter.setItemTapListener(object : HomeAdapter.OnItemTap {
            override fun onTap(position: Int) {
                if(!navigationlayout.isVisible)
                {





                val matchId = homeAdapter.getMatchId(position)
                val havanimation = homeAdapter.CheckAnim(position)
                val havevent = homeAdapter.CheckEvent(position)
                try {
                    if (sp.language == Constants.SharedPreferenceKeys.ENGLISH) {
                        val animateUrl = homeAdapter.currentList[position].animateURLEn
                        bundle.putString(ANIMATE_URL, animateUrl)

                    } else if (sp.language == Constants.SharedPreferenceKeys.CHINESE) {
                        val animateUrl = homeAdapter.currentList[position].animateURLCn
                        bundle.putString(ANIMATE_URL, animateUrl)

                    }
                } catch (e: Exception) {
                    val animateUrl = homeAdapter.currentList[position].animateURLEn
                    bundle.putString(ANIMATE_URL, animateUrl)
                }

                bundle.putInt(MATCH_ID, matchId)
                bundle.putBoolean(HAVE_ANIMATE, havanimation)
                bundle.putBoolean(HAVE_EVENT, havevent)
                hashMap = homeAdapter.getInformation(position)
                bundle.putSerializable("HashMap", hashMap)

                if (homeAdapter.currentList[position].havAnim || homeAdapter.currentList[position].havEvent) {

                    if (findNavController().currentDestination?.id == com.example.events_live.R.id.HomeFragment)
                        navController.navigate(
                            com.example.events_live.R.id.action_homeFragment_to_eventFragment,
                            bundle
                        )
                } else
                    Toast.makeText(
                        requireContext(),
                        com.example.events_live.R.string.no_events,
                        Toast.LENGTH_SHORT
                    ).show()
            }
                else
                    navigationlayout.visibility = GONE
        }
        })

    }

    private fun initObserver() {
        viewModel.mState.flowWithLifecycle(
            this.lifecycle, Lifecycle.State.STARTED

        ).onEach {
            handleState(it)
        }.launchIn(this.lifecycleScope)
    }

    private fun handleState(state: HomeScreenState) {
        when (state) {
            is HomeScreenState.IsLoading -> handleIsLoadingState(state.isLoading)
            is HomeScreenState.Response -> handleTeamResponse(state.videos)
            is HomeScreenState.NoInternetException -> handleNetworkFailure(state.message)
            is HomeScreenState.GeneralException -> handleException(state.message)
            is HomeScreenState.StatusFailed -> handleFailure(state.message)
            else -> {
                Log.d(TAG, " no state run ")
            }
        }
    }


    private fun handleTeamResponse(response: BaseClassIndexNew) {
        Log.d(TAG, " response success  " + response.matchList.count())
        pageCount = response.meta.total
        matchesList.addAll(response.matchList)
        handleTeamsData(matchesList)
        homeAdapter.submitList(matchesList)
    }


    private fun handleTeamsData(matchesList: ArrayList<Match>) {

        for (match in matchesList) {
            if (match.homeName.isNotEmpty() && match.leagueName.isNotEmpty() && match.homeLogo.isNotEmpty() && match.location.isNotEmpty() && match.state == 1 || match.state == 2 || match.state == 3 || match.state == 4 || match.state == 5) {
                teamsList.add(
                    TeamInfo(
                        match.homeName,
                        match.leagueName,
                        match.homeLogo,
                        match.location
                    )
                )
            }

            if (match.homeName.isNotEmpty()) {
                if (!suggestionTeamsList.contains(match.homeName))
                    suggestionTeamsList.add(match.homeName)
            }

        }

    }


    // pagination
    private fun loadMoreMatches() {
        val sp = SPApp(requireContext())
        val language = sp.language
        if (pageNumber > pageCount)
            return
        else {
            pageNumber += 1
            lifecycleScope.launch {
                viewModel.getMatches(language, pageNumber.toString())

            }
        }

    }


    private fun handleIsLoadingState(loading: Boolean) {
        if (loading) {
            showLoading()
            Log.d(TAG, "show loader....")
        } else {
            hideLoading()
            Log.d(TAG, "..... stop loader")
        }
    }

    private fun handleFailure(message: String) {
        Log.d(TAG, "failure    $message")
        showToast(message)
        hideLoading()
    }


    private fun handleNetworkFailure(message: String) {
        Log.d(TAG, "network    $message")
        showToast(message)
        hideLoading()
    }

    private fun handleException(message: String) {
        Log.d(TAG, "exception    $message")
        showToast(message)
        hideLoading()
    }


    private fun filter(text: String) {
        val matchAfterFilterArrayList = ArrayList<Match>()

        for (matchs in matchesList) {
            if (matchs.homeName!!.toLowerCase()
                    .contains(text.lowercase(Locale.getDefault())) || matchs.awayName!!.toLowerCase()
                    .contains(text.lowercase(Locale.getDefault())) || matchs.leagueName!!.toLowerCase()
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                matchAfterFilterArrayList.add(matchs)
            }
        }
        try {
            if (matchAfterFilterArrayList.isEmpty()) homeAdapter!!.filterList(matchesList) else
                homeAdapter!!.filterList(matchAfterFilterArrayList)
        } catch (e: Exception) {

        }
    }


    fun searchonEditText() {

        //search on edittext
        binding.searchEdt?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, arg1: Int, arg2: Int, count: Int) {

                if (count > 0) {

                    for (i in mapArrayList) {
                        if (cs.toString() == i.map_key) {
                            goToWeb(i.map_link, i.open_type.toString())
                        }
                    }

                }
            }

            override fun beforeTextChanged(s: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
//                for (i in mapArrayList) {
//                    if (search_edt?.editableText.toString() == i.map_key) {
//                        showDialogWebView(i.map_link)
//                    }
//                }
            }
        })
    }

    fun showDialogWebView(url: String) {
        var shouldRefresh = false
        val dialog = Dialog(requireContext(), android.R.style.ThemeOverlay)
        dialog.setContentView(com.example.events_live.R.layout.web_view_dialog)

        val web_view = dialog.findViewById<WebView>(com.example.events_live.R.id.web_vew)

//        Log.i("TAG","getUrlFromSP(this): "+getUrlFromSP(this))
//getUrlFromSP(this)
        web_view.setWebViewClient(WebViewClient())
        web_view.settings.javaScriptEnabled = true
        web_view.loadUrl(url)

        dialog.findViewById<View>(com.example.events_live.R.id.back_btn_rl_web_view).setOnClickListener {
            dialog.dismiss()
            if (shouldRefresh)
                recreate(activity!!)
        }

        dialog.show()
        dialog.setCancelable(false)
    }

    fun languageClick() {

        img_lang.setOnClickListener {
            if (findNavController().currentDestination?.id == com.example.events_live.R.id.HomeFragment) {
                navController.navigate(com.example.events_live.R.id.action_homeFragment_to_languageFragment)
            }
        }

        tv_language.setOnClickListener {
            if (findNavController().currentDestination?.id == com.example.events_live.R.id.HomeFragment)
                navController.navigate(com.example.events_live.R.id.action_homeFragment_to_languageFragment)
        }
    }

    fun shareClick() {

        img_share.setOnClickListener {
            GeneralTools.shareApp(requireActivity())

        }

        tv_share.setOnClickListener {
            GeneralTools.shareApp(requireActivity())

        }
    }

    fun rateClick() {

        img_rate.setOnClickListener {
            GeneralTools.rateUs(requireActivity())

        }

        tv_rate.setOnClickListener {
            GeneralTools.rateUs(requireActivity())

        }
    }

    fun feedbackClick() {

        img_feedback.setOnClickListener {
            GeneralTools.feedback(requireActivity())

        }

        tv_feedback.setOnClickListener {
            GeneralTools.feedback(requireActivity())

        }
    }

    fun privacyClick() {

        img_policy.setOnClickListener {
            GeneralTools.privacyPolicy(requireActivity())

        }

        tv_privacy.setOnClickListener {
            GeneralTools.privacyPolicy(requireActivity())

        }
    }

    fun exitClick() {

        img_exit.setOnClickListener {
            GeneralTools.exitDialog(requireActivity())

        }

        tv_exit.setOnClickListener {
            GeneralTools.exitDialog(requireActivity())

        }
    }


    private fun goToWeb(redirectUrl: String, openType: String) {
        val sp=SPApp(context!!)
        stopTimer()
        sp.WEB_OPENED=true
        sp.URL= redirectUrl

        if (openType == "0") { // open web view
            sp.WEB_OPTION=true
            activity.let {
                val intent = Intent(it, WebViewActivity::class.java)
                startActivity(intent)
            }

        }
        else
        {
            // open browser
            sp.WEB_OPTION=false
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    redirectUrl
                )
            )
            startActivity(browserIntent)
        }
    }
    private fun startTimer(time: Long) {

        if (this::timer.isInitialized)
            timer.cancel()

        timer = object : CountDownTimer(time * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("QOO", ".....tick tick timer is running....")
            }

            override fun onFinish() {
                if (ListResponse.repeat_status == 1)
                    showPopup()
            }
        }
        timer.start()
    }








    private fun showPopup() {
        val sp=SPApp(requireContext())

        if (ListResponse.prompt_title != null && sp.showPopUp)
            if (ListResponse.prompt_title!!.isNotEmpty()) {
                messageDialog(requireActivity())
            }
//        else
//            loading()

    }
    private fun messageDialog(activity: Activity) {
        val dialog = Dialog(activity, android.R.style.ThemeOverlay)
        dialog.setContentView(com.example.events_live.R.layout.settings_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.findViewById<TextView>(com.example.events_live.R.id.heading_text).text = ListResponse.prompt_title
        dialog.findViewById<TextView>(com.example.events_live.R.id.body_text).text = ListResponse.prompt_message

        dialog.findViewById<ImageView>(com.example.events_live.R.id.ivClosePopup).setOnClickListener {
            if (ListResponse.repeat_status == 1)
                startTimer(ListResponse.repeat_time.toLong())
            dialog.dismiss()
        }

        dialog.findViewById<TextView>(com.example.events_live.R.id.yes_bt).text = ListResponse.button
        dialog.findViewById<View>(com.example.events_live.R.id.yes_bt).setOnClickListener {
            goToWeb(ListResponse.redirect_url!!, ListResponse.open_type!!)
            dialog.dismiss()
        }
        dialog.show()
    }



    private fun stopTimer() {
        val sp=SPApp(context!!)
        if (this::timer.isInitialized)
            timer.cancel()
        sp.Timer= true


    }

    override fun onResume() {
        val sp=SPApp(context!!)
        // check if web view or web browser is opened
        if (sp.WEB_OPENED) {
            ivWebHOme.visibility = VISIBLE
        }
        else
            ivWebHOme.visibility = GONE

        // check if timer was stop
        //  if (Utils.getBooleanValue(preferences, Constants.Timer).first() == true) {
        if (sp.Timer) {
            if (this::timer.isInitialized)
                timer.cancel() // if there is already timer running
            // if webPage was ON don't start timer
            if (!sp.web_was_opened) {
                if (ListResponse.repeat_time != 0)
                    startTimer(ListResponse.repeat_time.toLong())
                sp.Timer=false
            }
        }

        super.onResume()
    }






}