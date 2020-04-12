package com.example.dailyactualstats.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.dailyactualstats.R

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class ProgressDialog(context:Context) : Dialog(context, R.style.ProgressDialogTheme) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}