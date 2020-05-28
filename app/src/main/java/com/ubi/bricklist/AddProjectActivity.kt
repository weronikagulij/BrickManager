package com.ubi.bricklist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ubi.bricklist.utilities.GlobalVariables
import kotlinx.android.synthetic.main.activity_add_project.*
import java.net.HttpURLConnection
import java.net.URL
import com.ubi.bricklist.classes.XMLparser.ProjectXML
import com.ubi.bricklist.classes.XMLparser.XMLparser
import com.ubi.bricklist.classes.inventory.Inventory
import com.ubi.bricklist.classes.inventory.InventoryPart


class AddProjectActivity : AppCompatActivity() {

    private val tag = "mymsg"
    private var dataChanged = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
    }

    fun addProject(v: View) {
//        Log.d(tag, xmlInput.text.toString())
//        Log.d(tag, nameInput.text.toString()) todo

        val xmlParser = XMLparser()
        val projectList = xmlParser.getProjectListFromUrl(GlobalVariables.xmlUrl + "615.xml")
        val inventory = Inventory(nameInput.text.toString())

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

        this.dataChanged = true
    }

    override fun finish() {
        if(this.dataChanged) {
            val data = Intent()
            setResult(Activity.RESULT_OK, data)
        }

        super.finish()
    }
}
