package com.cyrus.base_kotlin_mvvm.base

import android.app.SharedElementCallback
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.NetworkUtils
import com.cyrus.base_kotlin_mvvm.R
import com.cyrus.base_kotlin_mvvm.utils.CommonUtils
import com.cyrus.base_kotlin_mvvm.utils.KeyboardUtils.hideKeyboard
import com.cyrus.base_kotlin_mvvm.utils.StatusBarUtils

abstract class BaseActivity<BINDING : ViewDataBinding> : AppCompatActivity() {
    lateinit var binding: BINDING
    private val connectionLiveData by lazy { ConnectionLiveData(this) }
    private var connectInternet = true
    protected lateinit var view: View

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!handleClickBack()) {
                isEnabled = false
                hideKeyboard(this@BaseActivity)
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    open fun isObserveNetworkStatus(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = DataBindingUtil.setContentView(this, getContentLayout())
        binding.lifecycleOwner = this
        view = layoutInflater.inflate(getContentLayout(), null)

        initView()
        getLayoutLoading()
        initListener()
        observerLiveData()
        if (isObserveNetworkStatus())
            showConnectInternet()
    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onStop() {
        super.onStop()
        backPressedCallback.remove()
    }

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    abstract fun getLayoutLoading(): BaseLoadingView?

    /**@param event detect clear focus edittext when touch outside */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view: View? = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboard(this)
                    view.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
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

    private fun showConnectInternet() {
        connectionLiveData.observe(this) { isConnected ->
            if (connectInternet != isConnected) {
                if (isConnected)
                    CommonUtils.showSnackBarNoInternet(
                        view = binding.root,
                        message = getString(R.string.internet_restored),
                        imageResource = R.drawable.ic_internet_connected,
                        duration = 6000,
                        layoutInflater = layoutInflater,
                        marginBottom = resources.getDimensionPixelOffset(R.dimen.dp25).toFloat()
                    )
                else showSnackBarNoInternet()
                connectInternet = isConnected
            }
        }
    }

    fun showSnackBarNoInternet() {
        if (!NetworkUtils.isConnected())
            CommonUtils.showSnackBarNoInternet(
                view = binding.root,
                message = getString(R.string.no_internet),
                imageResource = R.drawable.ic_internet_disconnect,
                duration = 6000,
                layoutInflater = layoutInflater,
                marginBottom = resources.getDimensionPixelOffset(R.dimen.dp25).toFloat()
            )
    }

    protected fun paddingStatusBar(view: View) {
        view.setPadding(0, StatusBarUtils.getStatusBarHeight(baseContext), 0, 0)
    }

    open fun onBackPressedLoading() {}

    open fun handleClickBack(): Boolean {
        return false
    }

    fun isInitializedOfBinding(): Boolean = this::binding.isInitialized

    fun showToast(str: String) {
        if (isFinishing) return
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes id: Int) {
        if (isFinishing) return
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show()
    }

    fun setExitSharedElementCallback(key: String, value: View?) {
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                if (value == null) {
                    return
                }
                sharedElements?.put(key, value)
            }
        })
    }
}