package com.cyrus.base_kotlin_mvvm.utils

import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.os.StatFs
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cyrus.base_kotlin_mvvm.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.Random

object CommonUtils {

    fun randomColor() =
        Color.argb(180, Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))

    fun showSnackBarNoInternet(
        view: View,
        message: String,
        imageResource: Int,
        duration: Int,
        layoutInflater: LayoutInflater,
        marginBottom: Float = 0f
    ) {
        val snackBar = Snackbar.make(view, "", duration)
        val customSnackView: View =
            layoutInflater.inflate(R.layout.view_snackbar_simple, null)
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        val messageText: TextView = customSnackView.findViewById(R.id.tv_message)
        val icon: ImageView = customSnackView.findViewById(R.id.im_action_left)
        messageText.text = message
        icon.setImageResource(imageResource)
        snackBar.view.setBackgroundResource(R.drawable.bg_snack_bar)
        snackBarLayout.setPadding(0, 0, 0, 0)
        snackBar.view.translationY = -(marginBottom)
        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }

    fun showToast(context: Context, message: String, duration: Long) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.show()

        CoroutineScope(Dispatchers.Main).launch {
            delay(duration)
            toast.cancel()
        }
    }

    fun randomDataForSuccess(listData: ArrayList<Any>, results: (ArrayList<Any>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val listDataForSetSuccess: ArrayList<Any> = arrayListOf()
            while (true) {
                try {
                    if (listData.size <= 0) break
                    val randomInt = kotlin.random.Random.nextInt(0, listData.size)
                    if (randomInt >= listData.size || randomInt < 0) continue
                    if (!listDataForSetSuccess.contains(listData[randomInt])) {
                        listDataForSetSuccess.add(listData[randomInt])
                        if (listDataForSetSuccess.size == 5) {
                            break
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    LoggerUtils.d("CommonUtils", e.message.toString())
                } catch (e: IndexOutOfBoundsException) {
                    LoggerUtils.d("CommonUtils", e.message.toString())
                }
            }
            withContext(Dispatchers.Main) {
                results.invoke(listDataForSetSuccess)
            }
        }
    }

    fun runOnMainAfterDelay(timeMs: Long = 200, onRun: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(timeMs)
            CoroutineScope(Dispatchers.Main).launch {
                onRun()
            }
        }
    }

    fun checkFragmentIsVisible(activity: AppCompatActivity, tag: String): Boolean {
        val fragmentManager = activity.supportFragmentManager
        val dialogFragment = fragmentManager.findFragmentByTag(tag)
        return dialogFragment != null
    }

    private fun getAvailableInternalMemorySize(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return getMemorySizeHumanized(availableBlocks * blockSize)
    }

    private fun getTotalInternalMemorySize(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return getMemorySizeHumanized(totalBlocks * blockSize)
    }

    private fun getMemorySizeHumanized(memory: Long): String {
        val twoDecimalForm = DecimalFormat("#.##")
        val kb = memory / 1024.0
        val mb = memory / 1048576.0
        val gb = memory / 1073741824.0
        val tb = memory / 1099511627776.0
        return when {
            tb > 1 -> twoDecimalForm.format(tb) + " TB"
            gb > 1 -> twoDecimalForm.format(gb) + " GB"
            mb > 1 -> twoDecimalForm.format(mb) + " MB"
            kb > 1 -> twoDecimalForm.format(mb) + " KB"
            else -> twoDecimalForm.format(memory) + " Bytes"
        }
    }
}

