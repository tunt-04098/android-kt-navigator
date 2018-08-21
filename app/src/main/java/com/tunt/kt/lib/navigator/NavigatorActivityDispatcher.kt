package com.tunt.kt.lib.navigator

import android.support.v4.app.FragmentActivity

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
interface NavigatorActivityDispatcher {

    fun onCreate(activity: FragmentActivity)

    fun onResume()

    fun onSaveInstanceState()

    fun onDestroy()

    fun onBackPressed()

    fun onFinish()

    fun isStateSaved(): Boolean

    fun getNavigator(): Navigator
}