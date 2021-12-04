package com.sofudev.sipphcheck.utils

import com.sofudev.sipphcheck.model.MyColor

class Fuzzy {
    private val colorSets : ArrayList<MyColor> = ArrayList()

    private fun fuzzySet(){
        colorSets.add(MyColor(185, 13, 23))
        colorSets.add(MyColor(205, 44, 30))
        colorSets.add(MyColor(193, 38, 20))
        colorSets.add(MyColor(212, 84, 25))
        colorSets.add(MyColor(215, 120, 30))
        colorSets.add(MyColor(215, 130, 38))
        colorSets.add(MyColor(180, 123, 29))
        colorSets.add(MyColor(60, 49, 0))
        colorSets.add(MyColor(40, 36, 0))
        colorSets.add(MyColor(25, 10, 16))
        colorSets.add(MyColor(25, 0, 0))
        colorSets.add(MyColor(5, 0, 0))
        colorSets.add(MyColor(60, 0, 0))
        colorSets.add(MyColor(18, 0, 0))
    }

    private fun getDistance(currColor: MyColor, matchColor: MyColor) : Int {
        val redDiff = currColor.r - matchColor.r
        val greenDiff = currColor.g - matchColor.g
        val blueDiff = currColor.b - matchColor.b

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff
    }

    fun findNearest(currColor: MyColor) : Int{
        fuzzySet()
        var shortdistance = Int.MAX_VALUE
        var index = -1

        for (i in 0 until colorSets.size){
            var distance : Int

            val matchColor : MyColor = colorSets[i]
            distance = getDistance(currColor, matchColor)

            if (distance < shortdistance){
                index = i
                shortdistance = distance
            }
        }

        return index
    }

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
}