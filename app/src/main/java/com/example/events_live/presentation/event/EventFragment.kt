package com.example.events_live.presentation.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.events_live.R
import com.example.events_live.common.constant.Constants.ANIMATE_URL
import com.example.events_live.common.constant.Constants.HAVE_ANIMATE
import com.example.events_live.common.constant.Constants.HAVE_EVENT
import com.example.events_live.common.constant.Constants.MATCH_ID
import com.example.events_live.common.utils.CustomBindingAdapters
import com.example.events_live.common.utils.Formatters
import com.example.events_live.common.utils.GeneralTools
import com.example.events_live.common.utils.SPApp
import com.example.events_live.databinding.FragmentEventBinding
import com.example.events_live.domain.models.event.Event
import com.example.events_live.domain.models.event.EventBase
import com.example.events_live.domain.models.event.formatted.EventKind
import com.example.events_live.domain.models.event.formatted.FormattedEventG_S_F
import com.example.events_live.domain.models.event.formatted.FormattedTecnicEvent
import com.example.events_live.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.toolbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.android.synthetic.main.item_home.view.*
import kotlinx.android.synthetic.main.view_top_bar.view.*
import kotlinx.android.synthetic.main.web_view.view.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EventFragment : BaseFragment() {


    private val viewModel by viewModels<EventViewModel>()
    val TAG: String = "EventFragment"
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    var technicList = ArrayList<FormattedTecnicEvent>()
    var formattedEvents = ArrayList<FormattedEventG_S_F>()
    var eventsList = ArrayList<Event>()
    var hashMap: HashMap<String, String> = HashMap<String, String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        val sp = SPApp(context!!)
        sp.startApp = false
        lifecycleScope.launch {
            viewModel.getEvents()
        }
        toolbar.ivBackArrow.setOnClickListener {
            backBtn()
        }
        toolbar.ivMenuArrow.setOnClickListener {
            navigation.visibility = VISIBLE


            val tabStrip = tabLayoutEvent.getChildAt(0) as LinearLayout
            for (i in 0 until tabStrip.childCount) {
                tabStrip.getChildAt(i).setOnTouchListener { v, event -> true }
            }

        }
        app_bar_drawer.img_back.setOnClickListener {
            navigation.visibility = GONE
        }
        sp.showPopUp = false
        initObserver()
        tabLayout()
        handleTeamSelected()
        integrateWebView()
        languageClick()
        shareClick()
        rateClick()
        exitClick()
        addwebView()
        privacyClick()
        feedbackClick()
        sp.init = false

    }

    private fun initObserver() {
        viewModel.mState.flowWithLifecycle(
            this.lifecycle, Lifecycle.State.STARTED
        ).onEach {
            handleState(it)
        }.launchIn(this.lifecycleScope)
    }

    private fun handleState(state: EventScreenState) {
        when (state) {
            is EventScreenState.IsLoading -> handleIsLoadingState(state.isLoading)
            is EventScreenState.Response -> handleEventResponse(state.events)
            is EventScreenState.NoInternetException -> handleNetworkFailure(state.message)
            is EventScreenState.GeneralException -> handleException(state.message)
            is EventScreenState.StatusFailed -> handleFailure(state.message)
            else -> {
                Log.d(TAG, " no state run ")
            }
        }
    }


    private fun handleEventResponse(response: EventBase) {
        Log.d(TAG, " response event success  " + response)
        eventsList.addAll(response.eventList)
        if (!eventsList.isNullOrEmpty())
            initRV(eventsList, response)


    }


    private fun handleIsLoadingState(loading: Boolean) {
        val have_animation = arguments?.getBoolean(HAVE_ANIMATE)

        if (have_animation!! && loading) {
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

    private fun handleTeamSelected() {
        hashMap = arguments?.getSerializable("HashMap") as HashMap<String, String>
        Log.d(TAG, "hashMap" + hashMap.toString())

        tv_league_name.text = hashMap.getValue("leagueName")
        tv_home_team.text = hashMap.getValue("homeName")
        tv_away_team.text = hashMap.getValue("awayName")
        tv_home_score.text = hashMap.getValue("homeScore")
        tv_away_score.text = hashMap.getValue("AwayScore")
        tv_time.text = hashMap.getValue("time")
        CustomBindingAdapters.loadImage(teamlayout.img_home_team, hashMap.getValue("LogoHome"))
        CustomBindingAdapters.loadAwayImage(teamlayout.img_away_team, hashMap.getValue("AwayLogo"))

    }

    private fun initRV(eventsList: ArrayList<Event>, eventBase: EventBase) {
        var filteredList = ArrayList<FormattedEventG_S_F>()
        binding.itemTrchniq.layoutManager = LinearLayoutManager(activity)
        binding.itemEvent.layoutManager = LinearLayoutManager(activity)
        (binding.itemEvent.layoutManager as LinearLayoutManager).reverseLayout = true
        (binding.itemEvent.layoutManager as LinearLayoutManager).stackFromEnd = true
        var eventListFomatter = EventBase()
        for (event in eventsList) {
            val matchId = arguments?.getInt(MATCH_ID)
            Log.d(TAG, "match Id" + matchId.toString())
            if (event.matchId == matchId) {
                try {


                    if (!eventBase.technic.isNullOrEmpty() || !eventBase.eventList.isNullOrEmpty() || !eventBase.penalty.isNullOrEmpty()) {

                        technicList = Formatters.formatTechnic(
                            Formatters.filterEvents(
                                matchId,
                                eventBase
                            ).technic[0].technicCount
                        )

                    }
                } catch (e: Exception) {
                }
                if (!technicList.isNullOrEmpty()) {

                    binding.itemTrchniq.adapter = TechnicAdapter(technicList)
                    tv_no_technic.visibility = GONE
                }






                eventListFomatter = Formatters.filterEvents(matchId!!, eventBase)
                formattedEvents = Formatters.formatEvents(matchId!!, eventListFomatter)
                if (!formattedEvents.isNullOrEmpty()) {

                    for (event in formattedEvents) {
                        if (event.eventType == EventKind.GOAL || event.eventType == EventKind.FOUL || event.eventType == EventKind.SUBSTITUTION)
                            filteredList.add(event)


                    }
                }


                binding.itemEvent.adapter = EventAdapter(filteredList)
            }


        }


    }

    fun addwebView() {
        val have_animation = arguments?.getBoolean(HAVE_ANIMATE)
        if (have_animation!!) {
            webview.visibility = VISIBLE
            noAnimation.visibility = GONE
            item_event.visibility = GONE
            item_trchniq.visibility = GONE
            tv_technic.visibility = GONE
            tv_event.visibility = GONE
            tv_no_technic.visibility = GONE

            integrateWebView()
        } else {
            hideLoading()
            webview.visibility = GONE
            noAnimation.visibility = VISIBLE
            tv_no_technic.visibility = GONE
            noAnimation.setText(R.string.no_animation)
            item_event.visibility = GONE
            item_trchniq.visibility = GONE
            tv_technic.visibility = GONE
            tv_event.visibility = GONE
            tv_no_technic.visibility = GONE

        }
    }

    private fun tabLayout() {
        val have_event = arguments?.getBoolean(HAVE_EVENT)
        val have_animation = arguments?.getBoolean(HAVE_ANIMATE)

        tabLayoutEvent.addTab(tabLayoutEvent.newTab().setText(R.string.animation))
        tabLayoutEvent.addTab(tabLayoutEvent.newTab().setText(R.string.event))



            tabLayoutEvent.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        if (have_animation!!) {
                            tab.text = getString(R.string.animation)
                            webview.visibility = VISIBLE
                            noAnimation.visibility = GONE
                            item_event.visibility = GONE
                            item_trchniq.visibility = GONE
                            tv_technic.visibility = GONE
                            tv_event.visibility = GONE
                            tv_no_technic.visibility = GONE

                        } else {
                            webview.visibility = GONE
                            noAnimation.visibility = VISIBLE
                            tv_no_technic.visibility = GONE
                            noAnimation.setText(R.string.no_animation)
                            item_event.visibility = GONE
                            item_trchniq.visibility = GONE
                            tv_technic.visibility = GONE
                            tv_event.visibility = GONE
                            tv_no_technic.visibility = GONE

                        }
                    }
                    1 -> {
                        if (have_event!!) {

                            tab.text = getString(R.string.event)
                            webview.visibility = GONE
                            noAnimation.visibility = GONE
                            item_event.visibility = VISIBLE
                            item_trchniq.visibility = VISIBLE
                            tv_technic.visibility = VISIBLE
                            tv_event.visibility = VISIBLE
                            tv_no_technic.visibility = GONE

                            if (technicList.isNullOrEmpty()) {
                                tv_no_technic.visibility = VISIBLE
                                tv_technic.visibility = VISIBLE
                            }


                        } else {
                            tab.text = getString(R.string.event)
                            webview.visibility = GONE
                            noAnimation.visibility = VISIBLE
                            noAnimation.setText(R.string.no_events)
                            item_event.visibility = GONE
                            item_trchniq.visibility = GONE
                            tv_technic.visibility = GONE
                            tv_event.visibility = GONE
                            tv_no_technic.visibility = GONE


                        }
                    }

                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        tab.text = getString(R.string.animation)
                        webview.visibility = INVISIBLE
                        item_event.visibility = VISIBLE
                        item_trchniq.visibility = VISIBLE
                        tv_technic.visibility = VISIBLE
                        tv_event.visibility = VISIBLE
                        noAnimation.visibility = VISIBLE
                        noAnimation.setText(R.string.no_animation)
                        noAnimation.setText(R.string.no_events)
                        tv_no_technic.visibility = GONE

                    }
                    1 -> {
                        tab.text = getString(R.string.event)
                        webview.visibility = VISIBLE
                        item_event.visibility = INVISIBLE
                        item_trchniq.visibility = INVISIBLE
                        tv_technic.visibility = INVISIBLE
                        tv_event.visibility = INVISIBLE
                        noAnimation.visibility = VISIBLE
                        tv_no_technic.visibility = GONE

                    }

                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    fun integrateWebView() {
        hideLoading()
        val animateUrl = arguments?.getString(ANIMATE_URL)
        Log.d(TAG, "animateUrl1" + animateUrl.toString())

        try {
            webview.main_webview.webViewClient = WebViewClient()
            webview.main_webview.settings.javaScriptEnabled = true
            webview.main_webview?.settings?.domStorageEnabled = true
            webview.main_webview.loadUrl(animateUrl!!)
        } catch (e: Exception) {
        }
    }

    fun languageClick() {

        img_lang.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.HomeFragment)
                navController.navigate(R.id.action_homeFragment_to_languageFragment)
        }

        tv_language.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.HomeFragment)
                navController.navigate(R.id.action_homeFragment_to_languageFragment)
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





}