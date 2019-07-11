package com.example.hometask

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.*

import com.j256.ormlite.android.apptools.OpenHelperManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.SQLException
import java.util.ArrayList
import java.util.Calendar

class AddActivity : AppCompatActivity() {

    internal var surname: EditText? = null
    internal var name: EditText? = null
    internal var patronymic: EditText? = null
    internal var url: EditText? = null
    internal var childName: EditText? = null
    internal var position: Spinner? = null
    internal var child: Spinner? = null
    internal var date: Button? = null
    internal var save: Button? = null
    internal var delete: Button? = null
    internal var addChild: Button? = null
    internal var deleteChild: Button? = null
    internal var dateText: TextView? = null
    internal var childText: TextView? = null
    internal var dateAndTime = Calendar.getInstance()
    internal var URL: String = ""
    internal var dataBase: DataBase? = null
    internal var context: Context? = null
    internal var image: ImageView? = null

    internal var array = arrayOf("Junior", "Middle", "Senior")
    internal var item: String = ""
    internal var childItem: String = ""

    internal var d: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, monthOfYear)
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        setInitialDate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        surname = findViewById<View>(R.id.surname) as EditText
        name = findViewById<View>(R.id.name) as EditText
        patronymic = findViewById<View>(R.id.patronymic) as EditText
        url = findViewById<View>(R.id.url) as EditText
        childName = findViewById<View>(R.id.nameChild) as EditText

        dateText = findViewById<View>(R.id.dateText) as TextView
        childText = findViewById<View>(R.id.childText) as TextView

        image = findViewById<View>(R.id.imageNew) as ImageView

        date = findViewById<View>(R.id.dateButton) as Button
        position = findViewById<View>(R.id.position) as Spinner
        child = findViewById<View>(R.id.child) as Spinner
        save = findViewById<View>(R.id.save) as Button
        delete = findViewById<View>(R.id.delete) as Button
        addChild = findViewById<View>(R.id.addChild) as Button
        deleteChild = findViewById<View>(R.id.deleteChild) as Button

        context = this@AddActivity

        setInitialDate()
    }


    override fun onResume() {
        super.onResume()
        val id = intent.extras!!.get(EXTRA_CLIENT_ID) as Int
        dataBase = OpenHelperManager.getHelper(this, DataBase::class.java)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        position?.adapter = adapter


        val onClickListener = View.OnClickListener { v ->
            val runtimeExceptionDao = dataBase?.getRuntimeExceptionDao()
            val surnameText = surname?.text.toString()
            val nameText = name?.text.toString()
            val patronymicText = patronymic?.text.toString()
            val urlText = url?.text.toString()
            val word = "[a-zA-Zа-яА-Я]+"

            when (v.id) {

                R.id.save -> {

                    if (surnameText.length > 0 && surnameText.matches(word.toRegex()) && nameText.length > 0 && nameText.matches(word.toRegex())
                            && patronymicText.length > 0 && patronymicText.matches(word.toRegex())) {

                    if (id > 0) {
                        val updateBuilder = runtimeExceptionDao!!.updateBuilder()
                        try {
                            updateBuilder.where().eq("id", id)
                            updateBuilder.updateColumnValue("surname", surname?.text.toString())
                            updateBuilder.updateColumnValue("name", name?.text.toString())
                            updateBuilder.updateColumnValue("patronymic", patronymic?.text.toString())
                            updateBuilder.updateColumnValue("position", item)
                            updateBuilder.updateColumnValue("date", dateText?.text.toString())
                            updateBuilder.update()

                        } catch (e: SQLException) {
                            e.printStackTrace()
                        }

                        if (urlText.length != 0 && URLUtil.isHttpsUrl(urlText)) {
                            Log.d("UPDATE IMAGE", urlText)
                            Picasso.with(context)
                                    .load(urlText)
                                    .placeholder(R.drawable.ic_android_black_24dp)
                                    .into(getTarget(urlText, id.toString()))
                            goHome()
                        } else {
                            val toast = Toast.makeText(context, "Проверьте url", Toast.LENGTH_LONG)
                            toast.show()
                        }
                    } else {
                        if (urlText.length > 0 && URLUtil.isHttpsUrl(urlText)) {
                            val client = Client(surname?.text.toString(), name?.text.toString(),
                                    patronymic?.text.toString(), item, dateText?.text.toString())
                            runtimeExceptionDao!!.create(client)
                            val clients = runtimeExceptionDao.queryForAll()

                            URL = url?.text.toString()
                            Log.d("URL", URL)
                            Log.d("CLIENTS", clients[clients.size - 1].id.toString())
                            Picasso.with(context)
                                    .load(URL)
                                    .placeholder(R.drawable.ic_android_black_24dp)
                                    .into(getTarget(URL, clients[clients.size - 1].id.toString()))
                            Log.d("PICASSO", "PICASSO")
                            goHome()
                        } else {
                            val toast = Toast.makeText(context, "Проверьте url", Toast.LENGTH_LONG)
                            toast.show()
                        }
                    }
                    } else {
                        val toast = Toast.makeText(context, "Проверьте данные", Toast.LENGTH_LONG)
                        toast.show()
                    }

                }

                R.id.delete -> {
                    Log.d("DELETEID", id.toString())
                    runtimeExceptionDao!!.deleteById(id)
                    val file = File("/data/data/com.example.hometask/app_imageDir", "$id.png")
                    file.delete()
                    goHome()
                }

                R.id.addChild -> {
                    try {
                        val client = Client()
                        client.id = id

                        if (childName?.text.toString().length > 0 && childName?.text.toString().matches(word.toRegex())) {
                            val childDao = dataBase?.dao
                            val child = Child(client, childName?.text.toString())
                            Log.d("CHILDNAME", childName?.text.toString())
                            childDao!!.create(child)
                        } else {
                            val toast = Toast.makeText(context, "Проверьте имя подчиненного", Toast.LENGTH_LONG)
                            toast.show()
                        }

                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                    goHome()
                }

                R.id.deleteChild -> {
                    try {
                        val deleteChildDao = dataBase?.dao
                        val deleteChild = deleteChildDao!!.queryForAll()
                        for (i in deleteChild.indices) {
                            if (childItem == deleteChild[i].nameChild) {
                                deleteChildDao.deleteById(deleteChild[i].id)
                            }
                        }
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                    goHome()
                }
            }
        }
        save?.setOnClickListener(onClickListener)
        delete?.setOnClickListener(onClickListener)
        addChild?.setOnClickListener(onClickListener)
        deleteChild?.setOnClickListener(onClickListener)

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                item = parent.getItemAtPosition(position) as String
                Log.d("Item", item)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        position?.onItemSelectedListener = onItemSelectedListener
        val onItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                childItem = parent.getItemAtPosition(position) as String
                Log.d("Item", childItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        child?.onItemSelectedListener = onItemSelected

        OpenHelperManager.releaseHelper()

        val runtimeExceptionDao = dataBase?.getRuntimeExceptionDao()
        val clients1 = listOf(runtimeExceptionDao!!.queryForId(id))
        Log.d("PROLIST", clients1.toString())
        if (id > 0) {
            surname?.setText(clients1[0].surname)
            name?.setText(clients1[0].name)
            patronymic?.setText(clients1[0].patronymic)
            dateText?.text = clients1[0].date
            Log.d("ID", id.toString())

            try {
                val childIntegerDao = dataBase?.dao
                val childList = childIntegerDao!!.queryForAll()
                Log.d("CHILDLIST IN YXXXX", childList.toString())
                val childName = ArrayList<String>()
                for (i in childList.indices) {
                    if (clients1[0].id == childList[i].client?.id) {
                        childName.add(childList[i].nameChild)
                    }
                }

                val childAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, childName)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                child?.adapter = childAdapter

            } catch (e: SQLException) {
                e.printStackTrace()
            }

        } else {
            delete?.visibility = View.GONE
            addChild?.visibility = View.GONE
            deleteChild?.visibility = View.GONE
            childName?.visibility = View.GONE
            child?.visibility = View.GONE
            childText?.visibility = View.GONE
        }
    }

    fun setDate(v: View) {
        DatePickerDialog(this@AddActivity, d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setInitialDate() {
        dateText?.text = DateUtils.formatDateTime(this,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
    }

    private fun goHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {

        val EXTRA_CLIENT_ID = "id"

        private fun getTarget(url: String, name: String): Target? {
            return object : Target {

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    Thread(Runnable {
                        val file = File("/data/data/com.example.hometask/app_imageDir", "$name.png")
                        try {
                            file.createNewFile()
                            val ostream = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.PNG, 85, ostream)
                            ostream.flush()
                            ostream.close()
                        } catch (e: IOException) {
                            Log.e("IOException", e.localizedMessage)
                        }
                    }).start()

                }

                override fun onBitmapFailed(errorDrawable: Drawable) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                }
            }
        }
    }

}