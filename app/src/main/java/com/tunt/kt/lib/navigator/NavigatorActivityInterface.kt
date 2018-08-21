package com.tunt.kt.lib.navigator

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
interface NavigatorActivityInterface {

    fun isStateSaved(): Boolean

    fun getNavigator(): Navigator

    fun onFinish()
}