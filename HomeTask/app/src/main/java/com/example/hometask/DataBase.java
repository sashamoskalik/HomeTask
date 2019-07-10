package com.example.hometask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBase extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "orm.db";
    private static final int DATABASE_VERSION = 4;

    private RuntimeExceptionDao<Client, Integer> runtimeExceptionDao = null;
    private Dao<Child, Integer> childDao = null;

    public DataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Client.class);
            TableUtils.createTable(connectionSource, Child.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Client.class, true);
            TableUtils.dropTable(connectionSource, Child.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RuntimeExceptionDao<Client, Integer> getRuntimeExceptionDao(){
        if (runtimeExceptionDao == null){
            runtimeExceptionDao = getRuntimeExceptionDao(Client.class);
        }
        return runtimeExceptionDao;
    }

    public Dao<Child, Integer> getDao() throws SQLException{
        if (childDao == null){
            childDao = getDao(Child.class);
        }
        return childDao;
    }
}
