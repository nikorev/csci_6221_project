package com.example.androidscoreboard

import java.io.Serializable

class ScoreboardEntry : Serializable {
    var color: Int
    var name:String
    var score:Int

    constructor(color:Int, name:String, score:Int) {
        this.color = color
        this.name = name
        this.score = score
    }

}
//
//data class ScoreboardEntry(
//    var color: Int,
//    var name:String,
//    var score:Int
//){
//}