package com.example.myapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.ThumbUp

sealed class BottomNavItem(val route: String, val icon: Int, val label: String){
    object Send : BottomNavItem("Send", R.drawable.hand, "send")
    object Lister: BottomNavItem("List", R.drawable.listicon, "list")
    object IP: BottomNavItem("IP",R.drawable.wifi,"kek")
}
