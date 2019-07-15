package com.example.hometask

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.io.File

class Adapter(private val clientList: MutableList<Client>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var listener: Listener? = null


    interface Listener {
        fun onClick(i: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val cardView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_client, null) as CardView


        return ViewHolder(cardView, listener, clientList)
    }

    @SuppressLint("SetTextI18n", "SdCardPath")
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val cardView = viewHolder.cardView
        val image = cardView.findViewById<ImageView>(R.id.image)
        image.setImageBitmap(Image("/data/data/com.example.hometask/app_imageDir", clientList[i].id.toString()))
        image.contentDescription = clientList[i].name
        val fio = cardView.findViewById<TextView>(R.id.fio)
        fio.text = "${clientList[i].surname} ${clientList[i].name} ${clientList[i].patronymic}"
        val position = cardView.findViewById<TextView>(R.id.position)
        position.text = clientList[i].position
        val date = cardView.findViewById<TextView>(R.id.date)
        date.text = clientList[i].date
    }

    override fun getItemCount(): Int {
        return clientList.size
    }

    class ViewHolder(val cardView: CardView, listener: Listener?, clientList: MutableList<Client>) : RecyclerView.ViewHolder(cardView){

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                if (listener != null){
                    listener.onClick(clientList[position].id)
                }
            }
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    private fun Image(directory: String, name: String): Bitmap {
        val file = File(directory, "$name.png")
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}
