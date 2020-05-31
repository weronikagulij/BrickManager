package com.ubi.bricklist.utilities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.ubi.bricklist.classes.inventory.Inventory
import com.ubi.bricklist.classes.inventory.InventoryPart
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class DBHandler {
    lateinit var db: SQLiteDatabase

    companion object {
        val dbName = "BrickList.db"
    }

    fun connect(context: Context) {
        if (this::db.isInitialized) {
            return
        }
        val dbFile = File(context.getExternalFilesDir(null),
            dbName
        )
        if (!dbFile.exists()) {
            val input = context.assets.open(dbName)
            dbFile.createNewFile()
            val output = dbFile.outputStream()
            val buffer = ByteArray(1024)
            var read = input.read(buffer)
            while (read != -1) {
                output.write(buffer, 0, read)
                read = input.read(buffer)
            }
            output.close()
        }
        db = SQLiteDatabase.openDatabase(dbFile.path, null, 0)
    }

    fun addInventory(inventory: Inventory): Long {
        val values = ContentValues()
        values.put("Name", inventory.name)
        values.put("Active", inventory.active.toString())
        values.put("LastAccessed", inventory.lastAccesed)

        return db.insert("Inventories", null, values)
    }

    fun addInventoryPart(inventoryPart: InventoryPart): Long {
        val values = ContentValues()
        values.put("InventoryID", inventoryPart.inventoryId)
        values.put("TypeID", inventoryPart.typeId)
        values.put("ItemID", inventoryPart.itemId)
        values.put("QuantityInSet", inventoryPart.quantityInSet)
        values.put("QuantityInStore", inventoryPart.quantityInStore)
        values.put("ColorID", inventoryPart.colorId)
        values.put("Extra", inventoryPart.extra)

        return db.insert("InventoriesParts", null, values)
    }

    fun getInventories(): MutableList<Inventory> {
        var inventories: MutableList<Inventory> = ArrayList()
        val cursor = db.rawQuery("select * from Inventories", null)

        while (cursor.moveToNext()) {
            val active = cursor.getString(2).toBoolean()
            if(active || UserSettings.showingActive) {
                val inventory =
                    Inventory(
                        cursor.getString(0),
                        cursor.getString(1),
                        active,
                        cursor.getString(3)
                    )
                inventories.add(inventory)
            }
        }
        cursor.close()

        val formatter = DateTimeFormatter.ofPattern(GlobalVariables.dateFromat)

        inventories = inventories.sortedWith(compareBy { LocalDateTime.parse(it.lastAccesed, formatter) }).toMutableList()
        inventories = inventories.reversed().toMutableList()

        return inventories
    }

    fun getInventoriesPartsById(inventoryId: String): MutableList<InventoryPart> {
        var inventoriesParts: MutableList<InventoryPart> = ArrayList()
        val cursor = db.rawQuery("" +
                "select \n" +
                "InventoriesParts.InventoryID,\n" +
                "InventoriesParts.TypeID, \n" +
                "InventoriesParts.ItemID,\n" +
                "InventoriesParts.QuantityInSet,\n" +
                "InventoriesParts.QuantityInStore,\n" +
                "InventoriesParts.ColorID,\n" +
                "InventoriesParts.Extra,\n" +
                "Parts.Name,\n" +
                "Parts.NamePL,\n" +
                "InventoriesParts.id\n" +
                "from \n" +
                "InventoriesParts join Parts\n" +
                "on InventoriesParts.ItemID = Parts.Code\n" +
                "where InventoryID=\"${inventoryId}\"", null)

        while (cursor.moveToNext()) {
            val name =
                if( cursor.getString(8) == null
                || cursor.getString(8) == "" )
                    cursor.getString(7)
                else
                    cursor.getString(8)

            val id = cursor.getString(2)
            val colorId = cursor.getString(5)

            val inventoryPart =
                InventoryPart(
                    cursor.getString(0).toLong(),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3).toInt(),
                    cursor.getString(4).toInt(),
                    cursor.getString(5),
                    cursor.getString(6),
                    name,
                    cursor.getString(9)
                )

            val codeCursor = db.rawQuery("select codes.code \n" +
                    "from parts join codes on parts.id = codes.ItemID\n" +
                    "where parts.code=\"${id}\" and codes.ColorID=\"${colorId}\"", null)

            if(codeCursor.moveToFirst()) {
                inventoryPart.addBrickCode(codeCursor.getString(0))
            }

            inventoriesParts.add(inventoryPart)
            codeCursor.close()
        }
        cursor.close()

        return inventoriesParts
    }

    fun updateActivity(inventoryId: String, value: String) {
        val values = ContentValues()
        values.put("Active", value)
        db.update("Inventories", values, "id="+inventoryId, null)
    }

    fun updateQuantityInSet(itemId: String, currentQuantityInSet: String) {
        val values = ContentValues()
        values.put("QuantityInSet", currentQuantityInSet)
        db.update("InventoriesParts", values, "id="+itemId, null)
    }

    fun updateLastAccessed(inventoryId: String, value: String) {
        val values = ContentValues()
        values.put("LastAccessed", value)
        db.update("Inventories", values, "id="+inventoryId, null)
    }
}