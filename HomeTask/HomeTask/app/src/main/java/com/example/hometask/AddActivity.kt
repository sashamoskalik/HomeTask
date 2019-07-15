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

    lateinit var surname: EditText
    lateinit var name: EditText
    lateinit var patronymic: EditText
    lateinit var url: EditText
    lateinit var childName: EditText
    lateinit var position: Spinner
    lateinit var child: Spinner
    lateinit var date: Button
    lateinit var save: Button
    lateinit var delete: Button
    lateinit var addChild: Button
    lateinit var deleteChild: Button
    lateinit var dateText: TextView
    lateinit var childText: TextView
    internal var dateAndTime = Calendar.getInstance()
    internal var URL: String = ""
    lateinit var dataBase: DataBase
    lateinit var context: Context
    lateinit var image: ImageView

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
        surname = findViewById(R.id.surname)
        name = findViewById(R.id.name)
        patronymic = findViewById(R.id.patronymic)
        url = findViewById(R.id.url)
        childName = findViewById(R.id.nameChild)

        dateText = findViewById(R.id.dateText)
        childText = findViewById(R.id.childText)

        image = findViewById(R.id.imageNew)

        date = findViewById(R.id.dateButton)
        position = findViewById(R.id.position)
        child = findViewById(R.id.child)
        save = findViewById(R.id.save)
        delete = findViewById(R.id.delete)
        addChild = findViewById(R.id.addChild)
        deleteChild = findViewById(R.id.deleteChild)

        context = this@AddActivity

        setInitialDate()
    }


    override fun onResume() {
        super.onResume()
        val id = intent.extras!!.get(EXTRA_CLIENT_ID) as Int
        dataBase = OpenHelperManager.getHelper(this, DataBase::class.java)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        position.adapter = adapter


        val onClickListener = View.OnClickListener { v ->
            val runtimeExceptionDao = dataBase.getRuntimeExceptionDao()
            val surnameText = surname.text.toString()
            val nameText = name.text.toString()
            val patronymicText = patronymic.text.toString()
            val urlText = url.text.toString()
            val word = "[a-zA-Zа-яА-Я]+"

            when (v.id) {

                R.id.save -> {

                    if (surnameText.length > 0 && surnameText.matches(word.toRegex()) && nameText.length > 0 && nameText.matches(word.toRegex())
                            && patronymicText.length > 0 && patronymicText.matches(word.toRegex())) {

                    if (id > 0) {
                        val updateBuilder = runtimeExceptionDao!!.updateBuilder()
                        try {
                            updateBuilder.where().eq("id", id)
                            updateBuilder.updateColumnValue("surname", surname.text.toString())
                            updateBuilder.updateColumnValue("name", name.text.toString())
                            updateBuilder.updateColumnValue("patronymic", patronymic.text.toString())
                            updateBuilder.updateColumnValue("position", item)
                            updateBuilder.updateColumnValue("date", dateText.text.toString())
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
                            goHome()
                        }
                    } else {
                        if (urlText.length > 0 && URLUtil.isHttpsUrl(urlText)) {
                            val client = Client(surname.text.toString(), name.text.toString(),
                                    patronymic.text.toString(), item, dateText.text.toString())
                            runtimeExceptionDao!!.create(client)
                            val clients = runtimeExceptionDao.queryForAll()

                            URL = url.text.toString()
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
                        client.name = name.toString()

                        if (childName.text.toString().length > 0 && childName.text.toString().matches(word.toRegex())) {
                            val childDao = dataBase.dao
                            val relationDao = dataBase.getRelationDao()
                            val child = Child(childName.text.toString())
                            childDao!!.create(child)
                            val relation = Relation(client, child)
                            relationDao?.create(relation)
                            Log.d("CHILDNAME", childName.text.toString())
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
                        val deleteRelationDao = dataBase.getRelationDao()
                        val deleteChildDao = dataBase.dao
                        val deleteRelation = deleteRelationDao?.queryForAll()
                        for (i in deleteRelation!!.indices) {
                            if (childItem == deleteRelation[i].child?.nameChild) {
                                deleteRelationDao.deleteById(deleteRelation[i].id)
                                deleteChildDao?.deleteById(deleteRelation[i].child?.id)
                            }
                        }
                    } catch (e: SQLException) {
                        e.printStackTrace()
                    }

                    goHome()
                }
            }
        }
        save.setOnClickListener(onClickListener)
        delete.setOnClickListener(onClickListener)
        addChild.setOnClickListener(onClickListener)
        deleteChild.setOnClickListener(onClickListener)

        val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                item = parent.getItemAtPosition(position) as String
                Log.d("Item", item)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        position.onItemSelectedListener = onItemSelectedListener
        val onItemSelected = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                childItem = parent.getItemAtPosition(position) as String
                Log.d("Item", childItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        child.onItemSelectedListener = onItemSelected

        OpenHelperManager.releaseHelper()

        val runtimeExceptionDao = dataBase.getRuntimeExceptionDao()
        val clients1 = listOf(runtimeExceptionDao!!.queryForId(id))
        Log.d("PROLIST", clients1.toString())
        if (id > 0) {
            surname.setText(clients1[0].surname)
            name.setText(clients1[0].name)
            patronymic.setText(clients1[0].patronymic)
            dateText.text = clients1[0].date
            Log.d("ID", id.toString())

            try {
                val relationDao = dataBase.getRelationDao()
                val relationList = relationDao?.queryForAll()
                val childName = ArrayList<String>()
                Log.d("RELATIONSIZE", relationList?.size.toString())
                for (i in relationList!!.indices) {
                    if (clients1[0].id == relationList[i].client?.id) {
                        childName.add(relationList[i].child?.nameChild.toString())
                    }
                }

                val childAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, childName)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                child.adapter = childAdapter

            } catch (e: SQLException) {
                e.printStackTrace()
            }

        } else {
            delete.visibility = View.GONE
            addChild.visibility = View.GONE
            deleteChild.visibility = View.GONE
            childName.visibility = View.GONE
            child.visibility = View.GONE
            childText.visibility = View.GONE
        }
    }

    fun setDate(v: View) {
        DatePickerDialog(this@AddActivity, d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun setInitialDate() {
        dateText.text = DateUtils.formatDateTime(this,
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