package com.ubi.bricklist.utilities

import java.net.HttpURLConnection
import java.net.URL

class URLTaskManager {
    fun checkIfPageExsists(urlString: String): Boolean {
        var response = false
        var exists = false

        val thread = Thread(Runnable {
            val u = URL(urlString)
            val huc = u.openConnection() as HttpURLConnection
            huc.requestMethod = "GET"
            huc.connect()
            val code = huc.responseCode
            if(code == 200 || code == 201 || code == 202) {
                exists = true
            }

            response = true
        })

        thread.start()

        while(!response) {
            Thread.sleep(10)
        }

        return exists
    }

    fun checkIfPageExistsNonBlocking(urlString: String): Boolean {
        var exists = false
        val u = URL(urlString)
        val huc = u.openConnection() as HttpURLConnection
        huc.requestMethod = "GET"
        huc.connect()
        val code = huc.responseCode
        if(code == 200 || code == 201 || code == 202) {
            exists = true
        }

        return exists
    }
}