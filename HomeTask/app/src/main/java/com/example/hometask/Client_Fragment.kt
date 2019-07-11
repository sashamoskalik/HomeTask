package com.example.hometask

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.j256.ormlite.android.apptools.OpenHelperManager


class Client_Fragment : Fragment() {
    var dataBase: DataBase? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        dataBase = OpenHelperManager.getHelper(context, DataBase::class.java)

        val productRecycler = inflater.inflate(R.layout.fragment_client, container, false) as RecyclerView

        val runtimeExceptionDao = dataBase?.getRuntimeExceptionDao()
        val clientList = runtimeExceptionDao!!.queryForAll()

        val surname = arrayOfNulls<String>(clientList.size)

        for (i in clientList.indices) {
            surname[i] = clientList[i].surname
        }

        val id = IntArray(clientList.size)

        for (i in clientList.indices) {
            id[i] = clientList[i].id
            Log.d("ARRAYID", id[i].toString())
        }

        val name = arrayOfNulls<String>(clientList.size)

        for (i in clientList.indices) {
            name[i] = clientList[i].name
        }

        val patronymic = arrayOfNulls<String>(clientList.size)

        for (i in clientList.indices) {
            patronymic[i] = clientList[i].patronymic
        }

        val position = arrayOfNulls<String>(clientList.size)

        for (i in clientList.indices) {
            position[i] = clientList[i].position
        }

        val date = arrayOfNulls<String>(clientList.size)

        for (i in clientList.indices) {
            date[i] = clientList[i].date
        }

        val adapter = Adapter(surname, name, patronymic, position, date, id)
        productRecycler.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        productRecycler.layoutManager = layoutManager

        adapter.setListener(object : Adapter.Listener {
            override fun onClick(i: Int) {
                val intent = Intent(activity, AddActivity::class.java)
                intent.putExtra(AddActivity.EXTRA_CLIENT_ID, i)
                activity!!.startActivity(intent)
            }
        })
        return productRecycler

    }
}
