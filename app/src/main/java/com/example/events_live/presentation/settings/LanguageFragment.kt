package com.example.events_live.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.events_live.R
import com.example.events_live.common.constant.Constants
import com.example.events_live.common.utils.ContextUtils
import com.example.events_live.common.utils.SPApp
import com.example.events_live.databinding.FragmentLanguageBinding
import com.example.events_live.presentation.MainActivity
import com.example.events_live.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_language.*
import java.util.*


class LanguageFragment : BaseFragment() {

    val TAG: String = "LanguageFragment"
    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = view.findNavController()
        img_backL.setOnClickListener {
            backBtn()
        }
        //   selectLanguage()
        setupView()
        selectLanguage()

    }

    private fun setupView() {
        var sp = SPApp(requireContext())
        val lang = sp.language
        Log.d("QOO", " lang   selected  "+lang)
        when (lang) {
            Constants.SharedPreferenceKeys.ENGLISH -> radio_lang1.isChecked = true
            Constants.SharedPreferenceKeys.CHINESE -> radio_lang2.isChecked = true
            Constants.SharedPreferenceKeys.VIETNAMESE -> radio_lang3.isChecked = true
            Constants.SharedPreferenceKeys.INDONESIAN -> radio_lang4.isChecked = true
        }

    }

    fun selectLanguage() {
        var res = requireContext().resources
        var conf = res.configuration
        var dm = res.displayMetrics
        var sp = SPApp(requireContext())

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_lang1 -> {
                    ContextUtils.updateLocale(
                        requireContext(),
                        Locale(Constants.SharedPreferenceKeys.ENGLISH)
                    )
                    sp.language = Constants.SharedPreferenceKeys.ENGLISH
                    res.updateConfiguration(conf, dm)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
                R.id.radio_lang2 -> {
                    ContextUtils.updateLocale(
                        requireContext(),
                        Locale(Constants.SharedPreferenceKeys.CHINESE)
                    )
                    sp.language = Constants.SharedPreferenceKeys.CHINESE
                    res.updateConfiguration(conf, dm)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()

                }
                R.id.radio_lang3 -> {
                    ContextUtils.updateLocale(
                        requireContext(),
                        Locale(Constants.SharedPreferenceKeys.VIETNAMESE)
                    )
                    sp.language = Constants.SharedPreferenceKeys.VIETNAMESE
                    res.updateConfiguration(conf, dm)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
                R.id.radio_lang4 -> {
                    ContextUtils.updateLocale(
                        requireContext(),
                        Locale(Constants.SharedPreferenceKeys.INDONESIAN)
                    )
                    sp.language = Constants.SharedPreferenceKeys.INDONESIAN
                    res.updateConfiguration(conf, dm)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity!!.finish()
                }
            }
        }



    }


}






