package com.example.myapplication

import com.example.myapplication.viewModel.FingerUiState

data class GesturePicture(
    val picture: Int,
    val state: FingerUiState
)

val gestures = listOf<GesturePicture>(
    GesturePicture(R.drawable.fist,FingerUiState(20,100,100,100,100)),
    GesturePicture(R.drawable.one,FingerUiState(100,0,100,100,100)),
    GesturePicture(R.drawable.two,FingerUiState(100,0,0,100,100)),
    GesturePicture(R.drawable.three,FingerUiState(100,0,0,0,100)),
    GesturePicture(R.drawable.four,FingerUiState(100,0,0,0,0)),
    GesturePicture(R.drawable.teatime,FingerUiState(70,100,100,100,0)),
    GesturePicture(R.drawable.rock,FingerUiState(100,0,100,100,0)),
    GesturePicture(R.drawable.pinch,FingerUiState(75,75,75,50,50))
)
