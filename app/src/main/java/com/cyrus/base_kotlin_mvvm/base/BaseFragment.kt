package com.cyrus.base_kotlin_mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cyrus.base_kotlin_mvvm.utils.LoggerUtils
import com.cyrus.base_kotlin_mvvm.utils.StatusBarUtils

abstract class BaseFragment<BINDING : ViewDataBinding> : Fragment() {
    lateinit var binding: BINDING

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getContentLayout(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLayoutLoading()
        initListener()
        observerLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    abstract fun getLayoutLoading(): BaseLoadingView?

    protected fun paddingStatusBar(view: View) {
        view.setPadding(0, StatusBarUtils.getStatusBarHeight(requireContext()), 0, 0)
    }

    private var timeStartLoading: Long = 0
    private val timeDelayLoading: Long = 2000
    private var timeNeedForDelay: Long = 0

    fun showLoading(isLoading: Boolean, callback: (() -> Unit?)? = null) {
        if (isLoading) timeStartLoading = System.currentTimeMillis()
        if (!isLoading) timeNeedForDelay =
            timeDelayLoading - (System.currentTimeMillis() - timeStartLoading)
        getLayoutLoading()?.setupLoading(isLoading = isLoading, timeNeedForDelay, callback)
    }

    fun showSnackBarNoInternet() {
        (activity as BaseActivity<*>).showSnackBarNoInternet()
    }

    protected fun <T : Any> handleResultWithLoading(
        result: Result<T>,
        onSuccess: (response: T) -> Unit
    ) {
        when (result) {
            is Result.InProgress -> {}
            is Result.Success<T> -> {
                onSuccess.invoke(result.data)
            }

            is Result.Failure -> {}
            is Result.Error -> {}
            is Result.Failures<*> -> {}
        }
    }

    protected fun <T : Any> handleResultWithoutLoading(
        result: Result<T>,
        onSuccess: (response: T) -> Unit
    ) {
        when (result) {
            is Result.InProgress -> {
                LoggerUtils.d("GET_RING_TONES", "IN PROGRESS")
            }

            is Result.Success<T> -> {
                onSuccess.invoke(result.data)
            }

            is Result.Failure -> {}
            is Result.Error -> {}
            is Result.Failures<*> -> {}
        }
    }

    fun setExitSharedElementCallback(key: String, value: View?) {
        if (activity is BaseActivity<*>) (activity as BaseActivity<*>).setExitSharedElementCallback(
            key,
            value
        )
    }

}