package com.sofudev.sipphcheck.utils

class Fuzzy(private val r : Int, private val g : Int, private val b : Int) {
    private var init = 0
    private val minPh1r = 185
    private val minPh1g = 13
    private val minPh1b = 23

    private val maxPh1r = 215
    private val maxPh1g = 39
    private val maxPh1b = 45

    private val minPh2r = 205
    private val minPh2g = 44
    private val minPh2b = 30

    private val maxPh2r = 215
    private val maxPh2g = 59
    private val maxPh2b = 30

    fun checkRange() : String {
        val ph1 = isPh1()
        val ph2 = isPh2()

        if (ph1){
            init = 1
        }

        if (ph2){
            init = 2
        }

        return when{
            ph1 -> "1"
            ph2 -> "2"
            else -> "0"
        }
    }

    fun checkStatus() : String {
        return when(init){
            1 -> "Acid"
            2 -> "Acid"
            3 -> "Acid"
            4 -> "Acid"
            5 -> "Acid"
            6 -> "Acid"
            7 -> "Netral"
            8 -> "Alkaline"
            9 -> "Alkaline"
            10 -> "Alkaline"
            11 -> "Alkaline"
            12 -> "Alkaline"
            13 -> "Alkaline"
            14 -> "Alkaline"
            else -> "Tidak ditemukan"
        }
    }

    private fun isPh1() : Boolean{
        if (r in minPh1r..maxPh1r)
        {
            if (g in minPh1g..maxPh1g)
            {
                if (b in minPh1b..maxPh1b)
                {
                    return true
                }
            }
        }

        return false
    }

    private fun isPh2() : Boolean{
        if (r in minPh2r..maxPh2r)
        {
            if (g in minPh2g..maxPh2g)
            {
                if (b in minPh2b..maxPh2b)
                {
                    return true
                }
            }
        }

        return false
    }
}