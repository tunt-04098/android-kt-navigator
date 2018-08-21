package com.tunt.kt.lib.navigator

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
interface NavigatorFragmentInterface {

    fun handleBackIfNeeded(): Boolean

    fun getOwnNavigator(): Navigator

    fun getParentNavigator(): Navigator?

    fun getRootNavigator(): Navigator?

    fun getNavigatorDispatcher(): NavigatorFragmentDispatcher
}