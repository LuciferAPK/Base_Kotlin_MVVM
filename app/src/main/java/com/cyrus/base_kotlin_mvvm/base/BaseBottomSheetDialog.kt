package com.cyrus.base_kotlin_mvvm.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.cyrus.base_kotlin_mvvm.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog<BINDING : ViewDataBinding> : BottomSheetDialogFragment() {

    lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, getLayoutResource(), container, false
        );
        if (this.dialog!!.window != null) {
            this.dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            this.dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        initView(savedInstanceState, binding.root)
        return binding.root
    }

    /**
     * Set size for dialog bottom sheet
     */
    fun setWidthForDialog(width: Int) {
        dialog?.window?.setLayout(
            (activity?.window?.decorView?.width ?: 0) * width / 10,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun setHeightForDialog(height: Int) {
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            height
        )
    }

    private var timeStartLoading: Long = 0
    private val timeDelayLoading: Long = 2000 // milliseconds
    private var timeNeedForDelay: Long = 0
    fun showLoading(isLoading: Boolean, callback: (() -> Unit?)? = null) {
        if (isLoading) timeStartLoading = System.currentTimeMillis()
        if (!isLoading) timeNeedForDelay =
            timeDelayLoading - (System.currentTimeMillis() - timeStartLoading)
        getLayoutLoading()?.setupLoading(isLoading = isLoading, timeNeedForDelay, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener(view)
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initView(saveInstanceState: Bundle?, view: View?)

    protected abstract fun initListener(view: View?)

    abstract fun getLayoutLoading(): BaseLoadingView?

    override fun show(
        fragmentManager: FragmentManager,
        tag: String?
    ) {
        val transaction =
            fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    fun dismissDialog(tag: String?) {
        dismissAllowingStateLoss()
    }
}