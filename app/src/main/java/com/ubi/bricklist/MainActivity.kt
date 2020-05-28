package com.ubi.bricklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ubi.bricklist.utilities.GlobalVariables

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private val tag = "mymsg"
    val REQUEST_CODE = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        val listItems = ArrayList<String>()
//
//        listItems.add("Item 1\nLast modified: 22222")
//        listItems.add("Item 2")
//
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
//        inventoryList.adapter = adapter
//
//        inventoryList.setOnItemClickListener { _, _, arg2, _ ->
//            Log.d(tag , "Items $arg2")
//        }

        GlobalVariables.dbHandler.connect(this)
//        Log.d(tag, this.getExternalFilesDir(null).toString())

        this.getProjectsFromDb()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // odśwież widok i zapisz do pliku
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if((requestCode==REQUEST_CODE)
            && (resultCode == Activity.RESULT_OK)) {
            if(data != null) {
                this.getProjectsFromDb()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun addProject(v: View) {
        val intent = Intent(this, AddProjectActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun getProjectsFromDb() {
        val inventories = GlobalVariables.dbHandler.getInventories()
        val listItems = ArrayList<String>()

        for (inventory in inventories) {
            listItems.add("${inventory.name}\nLast modified: ${inventory.lastAccesed}")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        inventoryList.adapter = adapter

        inventoryList.setOnItemClickListener { _, _, arg2, _ ->
            GlobalVariables.currentInventory = inventories[arg2]
            val intent = Intent(this, ProjectActivity::class.java)
            startActivity(intent)
        }
    }
}
