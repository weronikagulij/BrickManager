package com.ubi.bricklist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ubi.bricklist.utilities.URLTaskManager
import com.ubi.bricklist.classes.inventory.InventoryPart
import com.ubi.bricklist.utilities.GlobalVariables
import kotlinx.android.synthetic.main.activity_project.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ubi.bricklist.classes.inventory.MainAdapter
import java.io.File
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.os.StrictMode
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ProjectActivity : AppCompatActivity() {

    private val tag = "mymsg"
    override fun onCreate(savedInstanceState: Bundle?) {
        title = "BrickList"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        projectName.setText(GlobalVariables.currentInventory.name)

        if(GlobalVariables.currentInventory.active) {
            toggleActivityButton.text = "Archive"
        } else {
            toggleActivityButton.text = "Restore"
        }

        val inventoriesParts = GlobalVariables.dbHandler.getInventoriesPartsById(GlobalVariables.currentInventory.id)

        val myAdapter = MainAdapter(inventoriesParts, this, object : MainAdapter.Callback {
            override fun onItemClicked(item: InventoryPart) {
                //TODO Here comes element, that was clicked on. You can continue to work with it.
            }
        })

        list_recycler_view.adapter = myAdapter
    }

    fun toggleActivity(v: View) {
        if(GlobalVariables.currentInventory.active) {
            GlobalVariables.currentInventory.active = false
            GlobalVariables.dbHandler.updateActivity(GlobalVariables.currentInventory.id, "false")
            toggleActivityButton.text = "Restore"
        } else {
            GlobalVariables.currentInventory.active = true
            GlobalVariables.dbHandler.updateActivity(GlobalVariables.currentInventory.id, "true")
            toggleActivityButton.text = "Archive"
        }

        val dateFormat = SimpleDateFormat(GlobalVariables.dateFromat)
        val newDate = dateFormat.format(Date())

        GlobalVariables.dbHandler.updateLastAccessed(GlobalVariables.currentInventory.id, newDate)
    }

    override fun finish() {
        val data = Intent()
        setResult(Activity.RESULT_OK, data)

        super.finish()
    }

    fun sendWithEmail(v: View) {
        val inventoriesPart =
            GlobalVariables.dbHandler.getInventoriesPartsById(
                GlobalVariables.currentInventory.id
            )

        var line: String

        val file = File(getExternalFilesDir(null), "result.xml")

        file.printWriter().use { out ->

            line = "<INVENTORY>"
            out.println(line)

            for (part in inventoriesPart) {
                if(part.quantityInStore > part.quantityInSet) {
                    line = "  <ITEM>"
                    out.println(line)

                    line =
                        "    <INVENTORY>${GlobalVariables.currentInventory.name}</INVENTORY>"
                    out.println(line)

                    line =
                        "    <MISSING_QTY>${part.quantityInStore - part.quantityInSet}</MISSING_QTY>"
                    out.println(line)

                    line =
                        "    <ITEM_ID>${part.itemId}</ITEM_ID>"
                    out.println(line)

                    if (part.brickCode.isNotEmpty()) {
                        line =
                            "    <CODE>${part.brickCode}</CODE>"
                        out.println(line)
                    }

                    line =
                        "    <NAME>${part.displayableName}</NAME>"
                    out.println(line)

                    line =
                        "    <COLOR_ID>${part.colorId}</COLOR_ID>"
                    out.println(line)

                    line =
                        "    <EXTRA>${part.extra}</EXTRA>"
                    out.println(line)

                    line =
                        "  </ITEM>"
                    out.println(line)
                }
            }

            line = "</INVENTORY>"
            out.println(line)
        }

        val mIntent = Intent(Intent.ACTION_SEND)
        val subject = "BrickList missing bricks"

        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val uri = Uri.fromFile(file)
        mIntent.putExtra(Intent.EXTRA_STREAM, uri)

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }
}

