package com.tunt.kt.lib.navigator.internal

/**
 * Created by TuNT on 8/21/2018.
 * tunt.program.04098@gmail.com
 */
enum class LayoutType {
    /**
     * Replace current fragment by new fragment.
     * In layout, current fragment is removed and the new fragment is replaced
     */
    REPLACE,

    /**
     * Add new fragment on top of current fragment.
     * In layout, current fragment is under the new fragment
     */
    ADD
}