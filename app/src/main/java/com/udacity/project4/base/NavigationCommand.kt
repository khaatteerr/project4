package com.udacity.project4.base

import androidx.navigation.NavDirections

sealed class NavigationCommand {

    data class BackTo(val destId: Int) : NavigationCommand()
    object Back : NavigationCommand()
    data class To(val directions: NavDirections) : NavigationCommand()

}