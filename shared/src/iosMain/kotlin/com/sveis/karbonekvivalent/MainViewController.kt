package com.sveis.karbonekvivalent

import androidx.compose.ui.window.ComposeUIViewController

import com.sveis.karbonekvivalent.data.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController { App(DatabaseDriverFactory()) }