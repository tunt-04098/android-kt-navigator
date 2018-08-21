package com.tunt.kt.lib.navigator

import android.annotation.SuppressLint
import android.support.annotation.AnimRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.tunt.android_kt_navigator.R
import com.tunt.kt.lib.navigator.internal.LayoutType

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
class Navigator(activity: FragmentActivity?, fragmentManager: FragmentManager) {

    private var activity: FragmentActivity?

    private var fragmentManager: FragmentManager?

    /**
     * if true: No animation will return on [Fragment.onCreateAnimation]
     * So no animation for transition.
     */
    private var disableAnimation: Boolean = false

    @IdRes
    private var contentId: Int = 0

    @AnimRes
    private var animEnter: Int = 0

    @AnimRes
    private var animExit: Int = 0

    @AnimRes
    private var animPopEnter: Int = 0

    @AnimRes
    private var animPopExit: Int = 0

    init {
        this.activity = activity
        this.fragmentManager = fragmentManager
        setDefaultAnim(R.anim.navigator_slide_in_right, R.anim.navigator_slide_out_left,
                R.anim.navigator_slide_in_left, R.anim.navigator_slide_out_right)
    }

    companion object {
        fun fromActivity(activity: FragmentActivity): Navigator {
            return Navigator(activity, activity.supportFragmentManager)
        }

        fun fromFragment(fragment: Fragment): Navigator {
            return Navigator(fragment.activity, fragment.childFragmentManager)
        }
    }

    /**
     * set default layout id whenever using a short version of [.openFragment]
     *
     * @param contentId your layout id to hold fragment
     */
    fun setDefaultContentId(@IdRes contentId: Int) {
        this.contentId = contentId
    }

    /**
     * set default animation when opening fragment
     */
    fun setDefaultAnim(@AnimRes animEnter: Int, @AnimRes animExit: Int,
                       @AnimRes animPopEnter: Int, @AnimRes animPopExit: Int) {
        this.animEnter = animEnter
        this.animExit = animExit
        this.animPopEnter = animPopEnter
        this.animPopExit = animPopExit
    }

    /**
     * @param fragment              A fragment you next open
     * @param contentId             A layout id hosts the fragment
     * @param backToCurrentFragment If you want after open new fragment and no need to back
     * to current fragment set it false. default we will back to current fragment
     * @param layoutType            Add or Replace
     * @param animEnter             Enter animation
     * @param animExit              Exit animation
     * @param animPopEnter          Pop enter animation
     * @param animPopExit           Pop exit animation
     */
    @SuppressLint("ResourceType")
    fun openFragment(fragment: Fragment, @IdRes contentId: Int, backToCurrentFragment: Boolean, layoutType: LayoutType,
                     @AnimRes animEnter: Int = this.animEnter, @AnimRes animExit: Int = this.animExit,
                     @AnimRes animPopEnter: Int = this.animPopEnter, @AnimRes animPopExit: Int = this.animPopExit) {
        if (fragmentManager == null) return
        ensureAnimationForFragment(fragment)
        ensureAnimationForFragmentsInBackStack(1)
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        if (animEnter > 0 || animExit > 0 || animPopEnter > 0 || animPopExit > 0) {
            fragmentTransaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit)
        }
        if (layoutType === LayoutType.ADD) {
            fragmentTransaction.add(contentId, fragment)
        } else {
            fragmentTransaction.replace(contentId, fragment)
        }

        if (!backToCurrentFragment) {
            popBackStack()
        }
        fragmentTransaction.addToBackStack(Integer.toString((2147483646.0 * Math.random()).toInt()))
        fragmentTransaction.commit()
    }

    /**
     * a bit shorter of full version with enable or disable animation when changing fragment
     */
    fun openFragment(fragment: Fragment, @IdRes contentId: Int = this.contentId, backToCurrentFragment: Boolean = true, layoutType: LayoutType = LayoutType.ADD, animation: Boolean = true) {
        if (contentId == 0) {
            throw IllegalStateException("call setDefaultContentId first")
        }
        if (animation) {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType)
        } else {
            openFragment(fragment, contentId, backToCurrentFragment, layoutType, 0, 0, 0, 0)
        }
    }

    /**
     * get current fragment laid out on layout with contentId
     */
    fun getCurrentFragment(@IdRes contentId: Int): Fragment? {
        return fragmentManager?.findFragmentById(contentId)
    }

    fun goBack(forceBack: Boolean): Boolean {
        val isNavigateFromActivity = activity?.supportFragmentManager === fragmentManager
        if (!isBackStackEmpty() && !forceBack) {
            val lastFragment = getLastFragmentInBackStack()

            if (lastFragment != null && lastFragment is NavigatorFragmentInterface) {
                val currentFragment = lastFragment as NavigatorFragmentInterface?
                // Check if current fragment need back
                if (currentFragment!!.handleBackIfNeeded()) return true
            }
        }

        val pop = !isNavigateFromActivity && !isBackStackEmpty() || isNavigateFromActivity && !isRoot()

        if (pop) {
            ensureAnimationForFragmentsInBackStack(2)
            popBackStack()
            return true
        }
        return false

    }

    /**
     * @param fragment: ensure disable animation for this fragment or not(from navigator)
     * @return true: if fragment implement [NavigatorFragmentInterface] and we can set
     * disableAnimation from Navigator. other wise return false
     */
    private fun ensureAnimationForFragment(fragment: Fragment): Boolean {
        if (fragment is NavigatorFragmentInterface) {
            (fragment as NavigatorFragmentInterface).getNavigatorDispatcher().isDisableAnimation = disableAnimation
            return true
        }
        return false
    }

    /**
     * @param count: number of fragment if back stack will apply disableAnimation flag from Navigator. count is always 1 or 2.
     * When 1: when user open a new fragment.
     * When 2: when we pop back stack the "last in" and the previous one will create animation for popAnimIn and exitAnimOut effect.
     * So ensure enable or disable animation from navigator here must be apply to at least for 2 fragments.
     *
     *
     * -1: for all
     * positive: for count fragment
     */
    private fun ensureAnimationForFragmentsInBackStack(count: Int) {
        val fragments = fragmentManager?.fragments
        if (fragments == null || fragments.isEmpty()) return
        var sum = 0
        val size = fragments.size
        var i = size - 1
        while (i >= 0) {
            val fragment = fragments[i]
            if (fragment == null) {
                i--
                continue
            }
            if (ensureAnimationForFragment(fragment)) {
                sum++
                if (sum == count) return
            }
            i--
        }
    }

    private fun getLastFragmentInBackStack(): Fragment? {
        val fragments = fragmentManager?.fragments
        if (fragments == null || fragments.isEmpty()) return null
        val size = fragments.size
        var i = size - 1
        while (i >= 0) {
            val fragment = fragments[i]
            if (fragment == null) {
                i--
                continue
            }
            if (fragment.isVisible) return fragment
            i--
        }
        return null
    }

    fun backToRoot() {
        ensureAnimationForFragmentsInBackStack(-1)
        if (!isRoot()) {
            val backStackCount = fragmentManager?.backStackEntryCount ?: 0
            for (i in (backStackCount - 1) downTo 1) {
                popBackStack()
            }
        }
    }

    fun isBackStackEmpty(): Boolean {
        return fragmentManager?.backStackEntryCount == 0
    }

    fun isRoot(): Boolean {
        return fragmentManager?.backStackEntryCount ?: 0 <= 1
    }

    fun clean() {
        activity = null
        fragmentManager = null
    }

    fun popBackStack() {
        if (activity == null) {
            return
        }
        if (activity is NavigatorActivityInterface && (activity as NavigatorActivityInterface).isStateSaved()) {
            return
        }
        fragmentManager?.popBackStack()
    }
}