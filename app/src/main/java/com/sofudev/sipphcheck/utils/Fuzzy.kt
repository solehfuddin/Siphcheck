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
            ph1 -> "Kadar Ph 1"
            ph2 -> "Kadar Ph 2"
            else -> "Kadar Ph tidak ditemukan"
        }
    }

    fun checkStatus() : String {
        return when(init){
            1 -> "Kategori pH Acid"
            2 -> "Kategori pH Acid"
            3 -> "Kategori pH Acid"
            4 -> "Kategori pH Acid"
            5 -> "Kategori pH Acid"
            6 -> "Kategori pH Acid"
            7 -> "Kategori pH Netral"
            8 -> "Kategori pH Alkaline"
            9 -> "Kategori pH Alkaline"
            10 -> "Kategori pH Alkaline"
            11 -> "Kategori pH Alkaline"
            12 -> "Kategori pH Alkaline"
            13 -> "Kategori pH Alkaline"
            14 -> "Kategori pH Alkaline"
            else -> "Kategori pH tidak ditemukan"
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