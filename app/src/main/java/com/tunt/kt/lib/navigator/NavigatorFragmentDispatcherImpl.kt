package com.tunt.kt.lib.navigator

import android.support.v4.app.Fragment
import android.view.animation.Animation

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
class NavigatorFragmentDispatcherImpl : NavigatorFragmentDispatcher {

    private lateinit var navigator: Navigator

    private lateinit var fragment: Fragment

    override fun onActivityCreated(fragment: Fragment) {
        this.fragment = fragment
        this.navigator = Navigator.fromFragment(fragment)
    }

    /**
     * Disable animation for fragment when transiting
     */
    override var isDisableAnimation: Boolean = false

    override val rootNavigator: Navigator?
        get() = if (fragment.activity is NavigatorActivityInterface) {
            (fragment.activity as NavigatorActivityInterface).getNavigator()
        } else null

    override val parentNavigator: Navigator?
        get() = if (fragment.parentFragment is NavigatorFragmentInterface) {
            (fragment.parentFragment as NavigatorFragmentInterface).getOwnNavigator()
        } else null

    override val ownNavigator: Navigator
        get() = navigator

    override fun onDestroy() = navigator.clean()

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (isDisableAnimation) {
            val animation = object : Animation() {
            }
            animation.duration = 0
            return animation
        }
        return null
    }
}