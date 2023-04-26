package com.example.rgbmanager

class RGB(var name: String, var red: Int, var green: Int, var blue: Int): java.io.Serializable {
    fun getHex(): String {
        return "#${this.red.toString(16)}${this.green.toString(16)}${this.blue.toString(16)}"
    }
}