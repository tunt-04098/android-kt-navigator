package com.tunt.kt.lib.navigator.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.animation.Animation
import com.tunt.kt.lib.navigator.Navigator
import com.tunt.kt.lib.navigator.NavigatorFragmentDispatcher
import com.tunt.kt.lib.navigator.NavigatorFragmentDispatcherImpl
import com.tunt.kt.lib.navigator.NavigatorFragmentInterface

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
abstract class NavigatorFragment : Fragment(), NavigatorFragmentInterface {

    private val dispatcher = NavigatorFragmentDispatcherImpl();

    override fun handleBackIfNeeded(): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dispatcher.onActivityCreated(this)
    }

    override fun getOwnNavigator(): Navigator = dispatcher.ownNavigator

    override fun getParentNavigator(): Navigator = dispatcher.parentNavigator

    override fun getRootNavigator(): Navigator = dispatcher.rootNavigator

    override fun getNavigatorDispatcher(): NavigatorFragmentDispatcher = dispatcher

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return dispatcher.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatcher.onDestroy()
    }
}