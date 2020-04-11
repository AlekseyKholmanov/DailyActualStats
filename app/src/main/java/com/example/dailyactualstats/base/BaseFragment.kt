package com.example.dailyactualstats.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.dailyactualstats.R
import com.example.dailyactualstats.extension.bindView
import com.example.dailyactualstats.extension.hideSoftKeybord

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
abstract class BaseFragment : Fragment {
    constructor() : super()

    constructor(@LayoutRes layoutRes: Int) : super(layoutRes)

    private val appbarConfig = AppBarConfiguration(setOf(R.id.mainFragment))

    protected fun initToolbar(title: String = ""): Toolbar {
        setHasOptionsMenu(true)
        val toolbar: Toolbar = requireView().findViewById(R.id.toolbar)
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(title.isNotEmpty())
            if (title.isNotEmpty()) {
                setTitle(title)
            }
        }
        NavigationUI.setupWithNavController(
            toolbar,
            findNavController(),
            appbarConfig
        )
        return toolbar
    }
}