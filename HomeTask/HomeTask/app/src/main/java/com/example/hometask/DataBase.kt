package com.example.hometask

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.RuntimeExceptionDao
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

import java.sql.SQLException

class DataBase(context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var runtimeExceptionDao: RuntimeExceptionDao<Client, Int>? = null
    private var childDao: Dao<Child, Int>? = null
    private var relationDao: Dao<Relation, Int>? = null

    val dao: Dao<Child, Int>?
        @Throws(SQLException::class)
        get() {
            if (childDao == null) {
                childDao = getDao(Child::class.java)
            }
            return childDao!!
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable(connectionSource, Client::class.java)
            TableUtils.createTable(connectionSource, Child::class.java)
            TableUtils.createTable(connectionSource, Relation::class.java)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, connectionSource: ConnectionSource, i: Int, i1: Int) {

    }

    fun getRuntimeExceptionDao(): RuntimeExceptionDao<Client, Int>? {
        if (runtimeExceptionDao == null) {
            runtimeExceptionDao = getRuntimeExceptionDao(Client::class.java)
        }
        return runtimeExceptionDao!!
    }

    fun getRelationDao(): Dao<Relation, Int>?{
        if (relationDao == null){
            relationDao = getDao(Relation::class.java)
        }
        return relationDao!!
    }

    companion object {

        private val DATABASE_NAME = "orm.db"
        private val DATABASE_VERSION = 8
    }
}
