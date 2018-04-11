package com.pajato.argusremastered.util

import java.util.*

object IdGenerator {
    var PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz".toCharArray()
    var lastPushTime: Long = 0
    var lastRandChars = Array(12, { 0 })

    fun gen(): String {
        var now = Date().time
        val duplicateTime = (now == lastPushTime)
        lastPushTime = now

        // Assemble the time stamp string.
        val timeStampChars = Array(8, { '0' })
        for (i in 0..7) {
            timeStampChars[i] = PUSH_CHARS[(now % 64).toInt()]
            now = Math.floor((now / 64).toDouble()).toLong()
        }

        if (now != (0).toLong()) throw Error("We should have converted the entire timestamp.")

        // If the timestamp has changed, finish with our 12 random characters.
        if (!duplicateTime) {
            for (i in 0..11) {
                lastRandChars[i] = Math.floor(Math.random() * 64).toInt()
            }
        } else {
            // If the timestamp hasn't changed since last push, use the same random number, except incremented by 1.
            for (i in 11 downTo 0) {
                if (lastRandChars[i] == 63) {
                    lastRandChars[i] = 0
                }
            }
            lastRandChars[11] = Math.floor(Math.random() * 64).toInt()
        }


        // Asssemble the full id string.
        var id = ""
        for (i in 0..7) {
            id += timeStampChars[i]
        }
        for (i in 0..11) {
            id += PUSH_CHARS[lastRandChars[i]]
        }

        return id
    }
}