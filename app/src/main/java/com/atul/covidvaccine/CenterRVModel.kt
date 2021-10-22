package com.atul.covidvaccine

data class CenterRVModel(
    val centerName: String,
    val centerAddress: String,
    val centerFromTime: String,
    val centerToTime: String,
    val fee_type: String,
    val ageLimit:Int,
    var vaccineName: String,
    val availableCapacity: Int
    )