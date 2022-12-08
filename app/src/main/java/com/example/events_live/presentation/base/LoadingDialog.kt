package com.example.events_live.presentation.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.events_live.R


class LoadingDialog private constructor(
    context: Context,
    message: String,
    theme: Int,
    cancelable: Boolean,
) : Dialog(context,theme) {
    class Builder(
        private var context: Context,
        private var message: String = "",
        private var messageId:Int = 0,
        private var themeId :Int = 0,
        private var cancelable:Boolean = true
    ) {
        fun build(): Dialog {
            return LoadingDialog(
                context,
                if (messageId != 0) context.getString(messageId) else message,
                if (themeId != 0) themeId else R.style.LoadingDialogDefault,
                cancelable
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.loading_dialog_layout)
    }

    init {
        setCancelable(cancelable)
    }

}