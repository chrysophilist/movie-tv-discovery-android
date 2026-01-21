package com.prince.cinemon.ui.common

fun formatRuntime(minutes: Int?): String{

    if (minutes == null) return "NA"
    val h = minutes/60
    val m = minutes%60
    return "${h}h ${m}min"
}