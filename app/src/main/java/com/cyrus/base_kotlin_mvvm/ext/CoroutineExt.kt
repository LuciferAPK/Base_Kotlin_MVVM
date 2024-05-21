package com.cyrus.base_kotlin_mvvm.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

object CoroutineExt {

    fun runOnIO(onRun: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            onRun()
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

    fun runOnMain(onRun: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            onRun()
        }
    }

    fun runOnIOWithJob(onRun: suspend () -> Unit): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            onRun()
        }
    }

    fun runWithJob(onRun: suspend () -> Unit): Job {
        return CoroutineScope(Job()).launch {
            onRun()
        }
    }

    fun ViewModel.runOnIO(onRun: suspend () -> Unit): Job {
        return this.viewModelScope.launch(Dispatchers.IO) {
            onRun()
        }
    }

    fun ViewModel.runOnMain(onRun: suspend () -> Unit): Job {
        return this.viewModelScope.launch(Dispatchers.Main) {
            onRun()
        }
    }
}