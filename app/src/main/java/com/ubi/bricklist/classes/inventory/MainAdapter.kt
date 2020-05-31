package com.ubi.bricklist.classes.inventory

import android.app.Activity
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ubi.bricklist.R
import com.ubi.bricklist.utilities.GlobalVariables
import com.ubi.bricklist.utilities.URLTaskManager
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter(var items: MutableList<InventoryPart>, val activity: Activity, val callback: Callback) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position], activity)
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.itemName)
        private val currentNumber = itemView.findViewById<TextView>(R.id.currentNumber)
        private val requiredNumber = itemView.findViewById<TextView>(R.id.requiredNumber)

        private val addButton = itemView.findViewById<FloatingActionButton>(R.id.addButton)
        private val subtractButton = itemView.findViewById<FloatingActionButton>(R.id.subtractButton)

        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

        private val tag = "mymsg"

        fun bind(item: InventoryPart, activity: Activity) {
            name.text = item.displayableName
            currentNumber.text = item.quantityInSet.toString()
            requiredNumber.text = item.quantityInStore.toString()

            var url = GlobalVariables.defaultLegoImg
            val urlTaskManager = URLTaskManager()

            val thread = Thread(Runnable {
                if(item.brickCode != "") {
                    if(urlTaskManager.checkIfPageExsists(GlobalVariables.legoImgUrl + item.brickCode)) {
                        url = GlobalVariables.legoImgUrl + item.brickCode
                    } else if(urlTaskManager.checkIfPageExsists(GlobalVariables.bricklinkImgUrl + item.colorId + "/" + item.itemId+ ".jpg")) {
                        url = GlobalVariables.bricklinkImgUrl + item.colorId + "/" + item.brickCode
                    }
                } else if(urlTaskManager.checkIfPageExsists(GlobalVariables.bricklinkImgOldUrl + item.itemId + ".jpg")) {
                    url = GlobalVariables.bricklinkImgOldUrl + item.itemId + ".jpg"
                }

                try {
                    val urlHttp = URL(url)
                    val bmp = BitmapFactory.decodeStream(urlHttp.openConnection().getInputStream())

                    activity.runOnUiThread {
                        imageView.setImageBitmap(bmp)
                    }
                } catch (e: Exception) {
                    val urlHttp = URL(GlobalVariables.defaultLegoImg)
                    val bmp = BitmapFactory.decodeStream(urlHttp.openConnection().getInputStream())

                    activity.runOnUiThread {
                        imageView.setImageBitmap(bmp)
                    }
                }
            })

            thread.start()

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }

            addButton.setOnClickListener {
                if(currentNumber.text.toString().toInt() < requiredNumber.text.toString().toInt()) {
                    val newCurr = currentNumber.text.toString().toInt() + 1
                    currentNumber.text = newCurr.toString()
                    GlobalVariables.dbHandler.updateQuantityInSet(item.id, newCurr.toString())

                    val dateFormat = SimpleDateFormat(GlobalVariables.dateFromat)
                    val newDate = dateFormat.format(Date())

                    GlobalVariables.dbHandler.updateLastAccessed(item.inventoryId.toString(), newDate)
                }
            }

            subtractButton.setOnClickListener {
                if(currentNumber.text.toString().toInt() > 0) {
                    val newCurr = currentNumber.text.toString().toInt() - 1
                    currentNumber.text = newCurr.toString()
                    GlobalVariables.dbHandler.updateQuantityInSet(item.id, newCurr.toString())

                    val dateFormat = SimpleDateFormat(GlobalVariables.dateFromat)
                    val newDate = dateFormat.format(Date())

                    GlobalVariables.dbHandler.updateLastAccessed(item.inventoryId.toString(), newDate)
                }
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: InventoryPart)
    }

}
