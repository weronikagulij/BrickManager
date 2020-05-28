package com.ubi.bricklist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.ubi.bricklist.classes.URLTaskManager
import com.ubi.bricklist.classes.inventory.InventoryPart
import com.ubi.bricklist.utilities.GlobalVariables
import kotlinx.android.synthetic.main.activity_project.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.net.URL




class ProjectActivity : AppCompatActivity() {

    private val tag = "mymsg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        projectName.setText(GlobalVariables.currentInventory.name)

        val inventoriesParts = GlobalVariables.dbHandler.getInventoriesPartsById(GlobalVariables.currentInventory.id)

//        for (part in inventoriesParts) {
//            Log.d(tag,
//                part.itemId + " "
//                        + part.colorId
//                        +" "
//                        + part.displayableName
//                        +" " + part.extra
//                        +" " + part.inventoryId
//                        +" " + part.itemId
//                        +" " + part.quantityInSet
//                        + " " + part.quantityInStore
//            )
//        }
//        Log.d(tag, GlobalVariables.currentInventory.name)

//        list_recycler_view.apply {
////            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = MyFirstAdapter()
//        }
//        val items = listOf(
//            MainItem("Михаил", "Лермонтов"),
//            MainItem("Александр", "Блок"),
//            MainItem("Николай", "Некрасов"),
//            MainItem("Фёдор", "Тютчев"),
//            MainItem("Сергей", "Есенин"),
//            MainItem("Владимир", "Маяковский")
//        )


        val myAdapter = MainAdapter(inventoriesParts, this, object : MainAdapter.Callback {
            override fun onItemClicked(item: InventoryPart) {
                //TODO Here comes element, that was clicked on. You can continue to work with it.
            }
        })

        list_recycler_view.adapter = myAdapter

    }
}

//data class MainItem(
//    val firstName: String,
//    val lastName: String
//)

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
//            firstName.text = item.firstName
//            lastName.text = item.lastName
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

//                val urlHttp = URL(url)
//                val bmp = BitmapFactory.decodeStream(urlHttp.openConnection().getInputStream())
//                imageView.setImageBitmap(bmp)

                val urlHttp = URL(url)
                val bmp = BitmapFactory.decodeStream(urlHttp.openConnection().getInputStream())

                activity.runOnUiThread(Runnable {
                    imageView.setImageBitmap(bmp)
                })

//                Picasso.get()
//                    .load(url)
//                    .into(imageView, object : com.squareup.picasso.Callback {
//                        override fun onSuccess() { }
//
//                        override fun onError(e: Exception?) {
//                            Log.d(tag, "error")
//                        }
//                    })
//                Log.d("mymsg", url + " 2")
            })

            thread.start()
//            if(item.brickCode != "") {
//                Picasso.get()
//                    .load(GlobalVariables.legoImgUrl + item.brickCode)
//                    .into(imageView, object : com.squareup.picasso.Callback {
//                        override fun onSuccess() { }
//
//                        override fun onError(e: Exception?) {
//                            Picasso.get()
//                                .load(GlobalVariables.bricklinkImgUrl + item.colorId + "/" + item.itemId + ".jpg")
//                                .into(imageView, object : com.squareup.picasso.Callback {
//                                    override fun onSuccess() { }
//
//                                    override fun onError(e: Exception?) {
//                                        Picasso.get()
//                                            .load(GlobalVariables.defaultLegoImg)
//                                            .into(imageView, object : com.squareup.picasso.Callback {
//                                                override fun onSuccess() { }
//
//                                                override fun onError(e: Exception?) {
//                                                    Log.d(tag, "error")
//                                                }
//                                            })
//                                    }
//                                })
//                        }
//                    })
//            } else {
//                Picasso.get()
//                    .load(GlobalVariables.bricklinkImgOldUrl + item.itemId + ".jpg")
//                    .into(imageView, object : com.squareup.picasso.Callback {
//                        override fun onSuccess() { }
//
//                        override fun onError(e: Exception?) {
//                            Picasso.get()
//                                .load(GlobalVariables.defaultLegoImg)
//                                .into(imageView, object : com.squareup.picasso.Callback {
//                                    override fun onSuccess() { }
//
//                                    override fun onError(e: Exception?) {
//                                        Log.d(tag, "error")
//                                    }
//                                })
//                        }
//                    })
//            }

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }

            addButton.setOnClickListener {
                if(currentNumber.text.toString().toInt() < requiredNumber.text.toString().toInt()) {
                    val newCurr = currentNumber.text.toString().toInt() + 1
                    currentNumber.text = newCurr.toString()
                    GlobalVariables.dbHandler.updateQuantityInSet(item.id, newCurr.toString())
                }
            }

            subtractButton.setOnClickListener {
                if(currentNumber.text.toString().toInt() > 0) {
                    val newCurr = currentNumber.text.toString().toInt() - 1
                    currentNumber.text = newCurr.toString()
                    GlobalVariables.dbHandler.updateQuantityInSet(item.id, newCurr.toString())
                }
            }
        }
    }

    interface Callback {
        fun onItemClicked(item: InventoryPart)
    }

}

//class MyFirstAdapter : RecyclerView.Adapter<MyFirstAdapter.ViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun getItemCount() = 30
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }
//
//    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
//
//    }
//}