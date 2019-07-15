package com.example.hometask

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView

import java.io.File

class MainActivity : AppCompatActivity() {

    lateinit var addButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton = findViewById(R.id.addButton)

        Log.d("MAIN", "MAIN")


        val onClickListener = View.OnClickListener { v ->
            when (v.id) {
                R.id.addButton -> {
                    val intent = Intent(this@MainActivity, AddActivity::class.java)
                    intent.putExtra(AddActivity.EXTRA_CLIENT_ID, -1)
                    startActivity(intent)
                }
            }
        }
        addButton.setOnClickListener(onClickListener)

    }

}
