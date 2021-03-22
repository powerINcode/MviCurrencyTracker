package com.example.core.domain.routing

interface Navigator {
    fun navigate(command: NavigationCommand)
}