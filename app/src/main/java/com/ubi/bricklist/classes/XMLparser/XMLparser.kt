package com.ubi.bricklist.classes.XMLparser

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.ubi.bricklist.classes.inventory.Inventory
import com.ubi.bricklist.classes.inventory.InventoryPart
import com.ubi.bricklist.utilities.GlobalVariables
import com.ubi.bricklist.utilities.UserSettings
import kotlinx.android.synthetic.main.activity_add_project.*
import java.net.HttpURLConnection
import java.net.URL

class XMLparser(
    private val activity: Activity,
    private val fieldToUpdate: TextView,
    private val inventoryName: String,
    private val projectName: String
) {
    var response: Boolean = false
    var projectList: MutableList<ProjectXML> = ArrayList()

    private val successMsg = "Success! Go back to see your new project"
    private val errorMsg = "Error! File not found"
    private val loadingMsg = "Loading..."

    fun getProjectListFromUrl() {
        val url = URL(UserSettings.xmlUrl + projectName + ".xml")

        activity.runOnUiThread {
            fieldToUpdate.text = loadingMsg
        }

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
                                projectXML.extra = line
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

                val inventory = Inventory(inventoryName)

                val id = GlobalVariables.dbHandler.addInventory(inventory)

                for (project in projectList) {
                    val inventoryPart =
                        InventoryPart(
                            id,
                            project.itemType,
                            project.itemId,
                            project.qty.toInt(),
                            project.color,
                            project.extra
                        )
                    GlobalVariables.dbHandler.addInventoryPart(inventoryPart)
                }
                updateMessage(fieldToUpdate, this.successMsg)
            } catch (e: Exception) {
                updateMessage(fieldToUpdate, this.errorMsg)
            } finally {
                response = true
            }
        })

        thread.start()
    }

    private fun updateMessage(fieldToUpdate: TextView, message: String) {
        activity.runOnUiThread {
            fieldToUpdate.text = message
        }
    }
}