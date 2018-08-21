package com.tunt.kt.lib.navigator

import android.support.v4.app.FragmentActivity

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
class NavigatorActivityDispatcherImpl : NavigatorActivityDispatcher {

    private var isStateSaved: Boolean = false

    private lateinit var navigator: Navigator

    private lateinit var activity: FragmentActivity

    override fun onCreate(activity: FragmentActivity) {
        this.activity = activity
        this.navigator = Navigator.fromActivity(activity)
    }

    override fun onResume() {
        isStateSaved = false
    }

    override fun onSaveInstanceState() {
        isStateSaved = true
    }

    override fun onDestroy() {
        navigator.clean()
    }

    override fun onBackPressed() {
        if (!navigator.goBack(false) && navigator.isRoot()) {
            onFinish()
        }
    }

    override fun onFinish() {
        if (activity is NavigatorActivityInterface) {
            (activity as NavigatorActivityInterface).onFinish()
        } else {
            activity.finish()
        }
    }

    override fun isStateSaved(): Boolean = isStateSaved

    override fun getNavigator(): Navigator = navigator
}