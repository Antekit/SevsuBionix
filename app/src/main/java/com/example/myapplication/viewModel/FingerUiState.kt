package com.example.myapplication.viewModel

data class FingerUiState(
    var fingerOne: Int = 0,
    var fingerTwo: Int = 0,
    var fingerThree: Int = 0,
    var fingerFour: Int = 0,
    var fingerFive: Int = 0,
)



data class SentGesture(
    var fingerOne: Int = 0,
    var fingerTwo: Int = 0,
    var fingerThree: Int = 0,
    var fingerFour: Int = 0,
    var fingerFive: Int = 0,
    var mode: Boolean = false
)
