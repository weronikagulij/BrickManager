package com.ubi.bricklist.classes.inventory

import android.annotation.SuppressLint
import com.ubi.bricklist.utilities.GlobalVariables
import java.text.SimpleDateFormat
import java.util.*

class Inventory {
    var id: String = ""
        get() = field

    var name: String = ""
        get() = field

    var active: Boolean = true
        get() = field

    var lastAccesed: String = ""
        get() = field

    @SuppressLint("SimpleDateFormat")
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
}