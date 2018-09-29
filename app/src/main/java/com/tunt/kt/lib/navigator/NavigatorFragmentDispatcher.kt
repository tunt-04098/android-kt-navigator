package com.tunt.kt.lib.navigator

import android.support.v4.app.Fragment
import android.view.animation.Animation

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
interface NavigatorFragmentDispatcher {

    var isDisableAnimation: Boolean

    val rootNavigator: Navigator

    val parentNavigator: Navigator

    val ownNavigator: Navigator

    fun onActivityCreated(fragment: Fragment)

    fun onDestroy()

    fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation?
}