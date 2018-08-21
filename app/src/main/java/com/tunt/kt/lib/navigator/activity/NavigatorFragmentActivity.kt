package com.tunt.kt.lib.navigator.activity

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.tunt.kt.lib.navigator.Navigator
import com.tunt.kt.lib.navigator.NavigatorActivityDispatcherImpl
import com.tunt.kt.lib.navigator.NavigatorActivityInterface

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
class NavigatorFragmentActivity : FragmentActivity(), NavigatorActivityInterface {

    private val dispatcher = NavigatorActivityDispatcherImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatcher.onCreate(this)
    }

    override fun isStateSaved(): Boolean = dispatcher.isStateSaved()

    override fun getNavigator(): Navigator = dispatcher.getNavigator()

    override fun onFinish() = finish()

    override fun onDestroy() {
        super.onDestroy()
        dispatcher.onDestroy()
    }

    override fun onBackPressed() = dispatcher.onBackPressed()
}