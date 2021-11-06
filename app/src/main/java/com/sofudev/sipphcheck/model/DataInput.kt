package com.sofudev.sipphcheck.model

data class DataInput(
    val idInput: String,
    val namaUser: String,
    var kodeWarna: String,
    val kodePh: String,
    val kategoriPh: String,
    val tglInput: String
)