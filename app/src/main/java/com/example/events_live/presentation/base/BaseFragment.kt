package com.example.events_live.presentation.base

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.events_live.R


open class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })
    }

    private val dialog: android.app.Dialog by lazy {
        LoadingDialog.Builder(
            requireContext(),
            "",
            0,
            0,
            false
        ).build()
    }

    fun showToast(message: String) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun handleLoading(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    protected fun hideLoading() {
        Log.d("QOO", " ........................hide loading.............")
        if (dialog.isShowing)
            dialog.dismiss()
    }

    fun showLoading() {
        if (!dialog.isShowing)
            dialog.show()
    }



    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
    }


    open fun onBackPressed(){
        finishAffinity(activity!!.parent)

    }
    fun backBtn() {
        NavigationUI.navigateUp(findNavController(), null)
    }

}