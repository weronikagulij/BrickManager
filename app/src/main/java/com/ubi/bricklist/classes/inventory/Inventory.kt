package com.ubi.bricklist.classes.inventory

import com.ubi.bricklist.utilities.GlobalVariables
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Inventory {
    var id: String = ""
        get() = field

    var name: String = ""
        get() = field

    var active: Boolean = true
        get() = field

    var lastAccesed: String = ""
        get() = field

//    var inventoryParts: MutableList<InventoryPart> = ArrayList()
//        get() = field

    constructor(name: String) {
        this.name = name

        val dateFormat = SimpleDateFormat(GlobalVariables.dateFromat)
        this.lastAccesed = dateFormat.format(Date())
    }

    constructor(
        id: String,
        name: String,
        active: Boolean,
        lastAccessed: String
    ) {
        this.id = id
        this.name = name
        this.active = active
        this.lastAccesed = lastAccessed
    }

//    fun addInventoryPart(part: InventoryPart) {
//        this.inventoryParts.add(part)
//    }

}