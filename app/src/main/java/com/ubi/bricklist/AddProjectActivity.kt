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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)
    }

    fun addProject(v: View) {
        Log.d(tag, nameInput.text.toString())
        val xmlParser = XMLparser(this, addMessage, nameInput.text.toString(), xmlInput.text.toString())
        xmlParser.getProjectListFromUrl()
    }

    override fun finish() {
        val data = Intent()
        setResult(Activity.RESULT_OK, data)

        super.finish()
    }
}
