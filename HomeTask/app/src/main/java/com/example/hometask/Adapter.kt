package com.example.hometask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.io.File

class Adapter(private val arraySurname: Array<String?>, private val arrayName: Array<String?>, private val arrayPatronymic: Array<String?>, private val arrayPosition: Array<String?>, private val arrayDate: Array<String?>, private val arrayId: IntArray) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var listener: Listener? = null

    interface Listener {
        fun onClick(i: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Adapter.ViewHolder {
        val cardView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_client, null) as CardView
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val cardView = viewHolder.cardView
        val image = cardView.findViewById<View>(R.id.image) as ImageView
        image.setImageBitmap(Image("/data/data/com.example.hometask/app_imageDir", arrayId[i].toString()))
        image.contentDescription = arrayName[i]
        val fio = cardView.findViewById<View>(R.id.fio) as TextView
        fio.text = arraySurname[i] + " " + arrayName[i] + " " + arrayPatronymic[i]
        val position = cardView.findViewById<View>(R.id.position) as TextView
        position.text = arrayPosition[i]
        val date = cardView.findViewById<View>(R.id.date) as TextView
        date.text = arrayDate[i]
        cardView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(arrayId[i])
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayName.size
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    private fun Image(directory: String, name: String): Bitmap {
        val file = File(directory, "$name.png")
        return BitmapFactory.decodeFile(file.absolutePath)
    }
}
