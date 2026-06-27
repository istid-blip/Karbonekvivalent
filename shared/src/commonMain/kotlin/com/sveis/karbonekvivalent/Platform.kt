package com.sveis.karbonekvivalent

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform