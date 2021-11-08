package com.sofudev.sipphcheck.utils

import com.sofudev.sipphcheck.model.MyColor

class Fuzzy(private val r : Int, private val g : Int, private val b : Int) {
    private var init = 0
//    private val minPh1r = 185
//    private val minPh1g = 13
//    private val minPh1b = 23
//
//    private val maxPh1r = 215
//    private val maxPh1g = 39
//    private val maxPh1b = 45
//
//    private val minPh2r = 205
//    private val minPh2g = 44
//    private val minPh2b = 30
//
//    private val maxPh2r = 215
//    private val maxPh2g = 59
//    private val maxPh2b = 43
//
//    private val minPh3r = 193
//    private val minPh3g = 38
//    private val minPh3b = 20
//
//    private val maxPh3r = 208
//    private val maxPh3g = 53
//    private val maxPh3b = 35
//
//    private val minPh4r = 212
//    private val minPh4g = 84
//    private val minPh4b = 25
//
//    private val maxPh4r = 220
//    private val maxPh4g = 98
//    private val maxPh4b = 39
//
//    private val minPh5r = 215
//    private val minPh5g = 120
//    private val minPh5b = 30
//
//    private val maxPh5r = 225
//    private val maxPh5g = 130
//    private val maxPh5b = 45
//
//    private val minPh6r = 215
//    private val minPh6g = 130
//    private val minPh6b = 38
//
//    private val maxPh6r = 225
//    private val maxPh6g = 145
//    private val maxPh6b = 54
//
//    private val minPh7r = 180
//    private val minPh7g = 123
//    private val minPh7b = 29
//
//    private val maxPh7r = 193
//    private val maxPh7g = 138
//    private val maxPh7b = 45
//
//    private val minPh8r = 60
//    private val minPh8g = 49
//    private val minPh8b = 0
//
//    private val maxPh8r = 73
//    private val maxPh8g = 63
//    private val maxPh8b = 5
//
//    private val minPh9r = 40
//    private val minPh9g = 36
//    private val minPh9b = 0
//
//    private val maxPh9r = 60
//    private val maxPh9g = 53
//    private val maxPh9b = 5
//
//    private val minPh10r = 25
//    private val minPh10g = 10
//    private val minPh10b = 16
//
//    private val maxPh10r = 43
//    private val maxPh10g = 23
//    private val maxPh10b = 32
//
//    private val minPh11r = 25
//    private val minPh11g = 0
//    private val minPh11b = 0
//
//    private val maxPh11r = 39
//    private val maxPh11g = 5
//    private val maxPh11b = 15
//
//    private val minPh12r = 5
//    private val minPh12g = 0
//    private val minPh12b = 0
//
//    private val maxPh12r = 18
//    private val maxPh12g = 5
//    private val maxPh12b = 5
//
//    private val minPh13r = 60
//    private val minPh13g = 0
//    private val minPh13b = 0
//
//    private val maxPh13r = 73
//    private val maxPh13g = 5
//    private val maxPh13b = 18
//
//    private val minPh14r = 18
//    private val minPh14g = 0
//    private val minPh14b = 0
//
//    private val maxPh14r = 30
//    private val maxPh14g = 5
//    private val maxPh14b = 5

    private fun getDistance(currColor: MyColor, matchColor: MyColor) : Int {
        val redDiff = currColor.r - matchColor.r
        val greenDiff = currColor.g - matchColor.g
        val blueDiff = currColor.b - matchColor.b

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff
    }

    fun findNearest(item : ArrayList<MyColor>, currColor: MyColor) : Int{
        var shortdistance = Int.MAX_VALUE
        var index = -1

        for (i in 0 until item.size){
            var distance : Int

            val matchColor : MyColor = item[i]
            distance = getDistance(currColor, matchColor)

            if (distance < shortdistance){
                index = i
                shortdistance = distance
            }
        }

        return index
    }

//    fun checkRange() : String {
//        val ph1 = isPh1()
//        val ph2 = isPh2()
//        val ph3 = isPh3()
//        val ph4 = isPh4()
//        val ph5 = isPh5()
//        val ph6 = isPh6()
//        val ph7 = isPh7()
//        val ph8 = isPh8()
//        val ph9 = isPh9()
//        val ph10 = isPh10()
//        val ph11 = isPh11()
//        val ph12 = isPh12()
//        val ph13 = isPh13()
//        val ph14 = isPh14()
//
//        if (ph1){
//            init = 1
//        }
//
//        if (ph2){
//            init = 2
//        }
//
//        if (ph3){
//            init = 3
//        }
//
//        if (ph4){
//            init = 4
//        }
//
//        if (ph5){
//            init = 5
//        }
//
//        if (ph6){
//            init = 6
//        }
//
//        if (ph7){
//            init = 7
//        }
//
//        if (ph8){
//            init = 8
//        }
//
//        if (ph9){
//            init = 9
//        }
//
//        if (ph10){
//            init = 10
//        }
//
//        if (ph11){
//            init = 11
//        }
//
//        if (ph12){
//            init = 12
//        }
//
//        if (ph13){
//            init = 13
//        }
//
//        if (ph14){
//            init = 14
//        }
//
//        return when{
//            ph1 -> "1"
//            ph2 -> "2"
//            ph3 -> "3"
//            ph4 -> "4"
//            ph5 -> "5"
//            ph6 -> "6"
//            else -> "0"
//        }
//    }

    fun checkStatus(input : Int) : String {
        return when(input){
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

//    private fun isPh1() : Boolean{
//        if (r in minPh1r..maxPh1r)
//        {
//            if (g in minPh1g..maxPh1g)
//            {
//                if (b in minPh1b..maxPh1b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh2() : Boolean{
//        if (r in minPh2r..maxPh2r)
//        {
//            if (g in minPh2g..maxPh2g)
//            {
//                if (b in minPh2b..maxPh2b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh3() : Boolean{
//        if (r in minPh3r..maxPh3r)
//        {
//            if (g in minPh3g..maxPh3g)
//            {
//                if (b in minPh3b..maxPh3b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh4() : Boolean{
//        if (r in minPh4r..maxPh4r)
//        {
//            if (g in minPh4g..maxPh4g)
//            {
//                if (b in minPh4b..maxPh4b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh5() : Boolean{
//        if (r in minPh5r..maxPh5r)
//        {
//            if (g in minPh5g..maxPh5g)
//            {
//                if (b in minPh5b..maxPh5b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh6() : Boolean{
//        if (r in minPh6r..maxPh6r)
//        {
//            if (g in minPh6g..maxPh6g)
//            {
//                if (b in minPh6b..maxPh6b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh7() : Boolean{
//        if (r in minPh7r..maxPh7r)
//        {
//            if (g in minPh7g..maxPh7g)
//            {
//                if (b in minPh7b..maxPh7b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh8() : Boolean{
//        if (r in minPh8r..maxPh8r)
//        {
//            if (g in minPh8g..maxPh8g)
//            {
//                if (b in minPh8b..maxPh8b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh9() : Boolean{
//        if (r in minPh9r..maxPh9r)
//        {
//            if (g in minPh9g..maxPh9g)
//            {
//                if (b in minPh9b..maxPh9b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh10() : Boolean{
//        if (r in minPh10r..maxPh10r)
//        {
//            if (g in minPh10g..maxPh10g)
//            {
//                if (b in minPh10b..maxPh10b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh11() : Boolean{
//        if (r in minPh11r..maxPh11r)
//        {
//            if (g in minPh11g..maxPh11g)
//            {
//                if (b in minPh11b..maxPh11b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh12() : Boolean{
//        if (r in minPh12r..maxPh12r)
//        {
//            if (g in minPh12g..maxPh12g)
//            {
//                if (b in minPh12b..maxPh12b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh13() : Boolean{
//        if (r in minPh13r..maxPh13r)
//        {
//            if (g in minPh13g..maxPh13g)
//            {
//                if (b in minPh13b..maxPh13b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
//
//    private fun isPh14() : Boolean{
//        if (r in minPh14r..maxPh14r)
//        {
//            if (g in minPh14g..maxPh14g)
//            {
//                if (b in minPh14b..maxPh14b)
//                {
//                    return true
//                }
//            }
//        }
//
//        return false
//    }
}