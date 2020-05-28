package com.ubi.bricklist.utilities

import com.ubi.bricklist.classes.DBHandler
import com.ubi.bricklist.classes.inventory.Inventory

object GlobalVariables {
    val xmlUrl = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    val legoImgUrl = "https://www.lego.com/service/bricks/5/2/"
    val bricklinkImgUrl = "http://img.bricklink.com/P/"
    val bricklinkImgOldUrl = "https://www.bricklink.com/PL/"
    val defaultLegoImg = "https://i.imgur.com/xEh9Vnt.jpg"
    val dbHandler: DBHandler = DBHandler()
    var currentInventory: Inventory = Inventory("")
}