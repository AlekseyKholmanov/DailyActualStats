package com.example.dailyactualstats.base

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.dailyactualstats.extension.hideSoftKeybord

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
abstract class BaseFragment : Fragment {
    constructor() : super()

    constructor(@LayoutRes layoutRes: Int) : super(layoutRes)

    protected val appCompatActivity
        get() = requireActivity() as AppCompatActivity


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().hideSoftKeybord(requireView())
    }
}