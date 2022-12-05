package com.udacity.project4.utils

import androidx.test.espresso.idling.CountingIdlingResource



inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {

    EspressoIdlingResource.increment()
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement()
    }
}

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
    fun increment() {
        countingIdlingResource.increment()
    }


}