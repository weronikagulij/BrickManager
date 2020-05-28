package com.ubi.bricklist.classes.inventory

class InventoryPart {
    var id: String = ""
        get() = field
    var inventoryId: Long = 0
        get() = field
    var typeId: String = ""
        get() = field
    var itemId: String = ""
        get() = field
    var quantityInSet: Int = 0
        get() = field
    var quantityInStore: Int = 0
        get() = field
    var colorId: String = ""
        get() = field
    var extra: String = ""
        get() = field
    var image: String = ""
        get() = field
    var displayableName: String = ""
        get() = field
    var brickCode: String = ""
        get() = field

    constructor(
        inventoryId: Long,
        typeId: String,
        itemId: String,
        quantityInStore: Int,
        colorId: String,
        extra: String
    ) {
        this.typeId = typeId
        this.itemId = itemId
        this.quantityInStore = quantityInStore
        this.colorId = colorId
        this.extra = extra
        this.inventoryId = inventoryId
    }

    constructor(
        inventoryId: Long,
        typeId: String,
        itemId: String,
        quantityInSet: Int,
        quantityInStore: Int,
        colorId: String,
        extra: String,
        displayableName: String,
        id: String
//        image: String
    ) {
        this.typeId = typeId
        this.itemId = itemId
        this.quantityInSet = quantityInSet
        this.quantityInStore = quantityInStore
        this.colorId = colorId
        this.extra = extra
        this.inventoryId = inventoryId
        this.displayableName = displayableName
        this.id = id
//        this.image = image
    }

    fun addImage(url: String) {
        this.image = url
    }

    fun addBrickCode(code: String) {
        this.brickCode = code
    }
}