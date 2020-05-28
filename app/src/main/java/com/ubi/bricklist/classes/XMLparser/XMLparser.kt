package com.ubi.bricklist.classes.XMLparser

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class XMLparser {
    var response: Boolean = false
    var projectList: MutableList<ProjectXML> = ArrayList()

    constructor() {}

    fun getProjectListFromUrl(url: String): MutableList<ProjectXML> {
        val url = URL(url)
        val thread = Thread(Runnable {
            try {
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    inputStream.bufferedReader().use {
                        projectList = ArrayList()
                        var projectXML = ProjectXML()

                        it.lines().forEach { line ->
                            if("<ITEM>" in line) {
                                projectXML = ProjectXML()
                            } else if ("<ITEMTYPE>" in line) {
                                projectXML.itemType = line
                                    .replace("<ITEMTYPE>", "")
                                    .replace("</ITEMTYPE>", "")
                                    .replace(" ", "")
                            } else if ("<ITEMID>" in line) {
                                projectXML.itemId = line
                                    .replace("<ITEMID>", "")
                                    .replace("</ITEMID>", "")
                                    .replace(" ", "")
                            } else if ("<QTY>" in line) {
                                projectXML.qty = line
                                    .replace("<QTY>", "")
                                    .replace("</QTY>", "")
                                    .replace(" ", "")
                            } else if ("<COLOR>" in line) {
                                projectXML.color = line
                                    .replace("<COLOR>", "")
                                    .replace("</COLOR>", "")
                                    .replace(" ", "")
                            } else if ("<EXTRA>" in line) {
                                projectXML.itemType = line
                                    .replace("<EXTRA>", "")
                                    .replace("</EXTRA>", "")
                                    .replace(" ", "")
                            } else if ("<ALTERNATE>" in line) {
                                projectXML.alternate = line
                                    .replace("<ALTERNATE>", "")
                                    .replace("</ALTERNATE>", "")
                                    .replace(" ", "")
                            } else if("</ITEM>" in line && projectXML.alternate == "N") {
                                projectList.add(projectXML)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                response = true
            }
        })

        thread.start()
        while(!this.response) {
            Thread.sleep(10)
        }

        return projectList
    }
}