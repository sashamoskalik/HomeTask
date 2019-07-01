package com.example.hometask;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Client_Fragment extends Fragment {
    DataBase dataBase;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBase = new DataBase(getActivity());

        final SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor cursor = db.query(DataBase.TABLE_NAME, null, null, null, null, null, null);

        RecyclerView productRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_client, container, false);

        if (cursor.moveToFirst()) {
            int surnameIndex = cursor.getColumnIndex(DataBase.KEY_SURNAME);
            int nameIndex = cursor.getColumnIndex(DataBase.KEY_NAME);
            int patronymicIndex = cursor.getColumnIndex(DataBase.KEY_PATRONYMIC);
            int positionIndex = cursor.getColumnIndex(DataBase.KEY_POSITION);
            int dateIndex = cursor.getColumnIndex(DataBase.KEY_DATA);



            String[] surname = new String[cursor.getCount()];

            int i =0;
            do {
                Log.d("product","2");
                surname[i] = cursor.getString(surnameIndex);
                i++;
            }
            while (cursor.moveToNext());

            int[] id = new int[cursor.getCount()];
            cursor.moveToFirst();
            i = 0;
            do {
                id[i] = cursor.getInt(0);
                i++;
            }
            while (cursor.moveToNext());

            String[] name = new String[cursor.getCount()];
            cursor.moveToFirst();
            i = 0;
            do {
                name[i] = cursor.getString(nameIndex);
                i++;
            }
            while (cursor.moveToNext());

            String[] patronymic = new String[cursor.getCount()];
            cursor.moveToFirst();
            i = 0;
            do {
                patronymic[i] = cursor.getString(patronymicIndex);
                i++;
            }
            while (cursor.moveToNext());


            String[] date = new String[cursor.getCount()];
            cursor.moveToFirst();
            i = 0;
            do {
                date[i] = cursor.getString(dateIndex);
                i++;
            }
            while (cursor.moveToNext());


            String[] position = new String[cursor.getCount()];
            cursor.moveToFirst();
            i = 0;
            do {
                position[i] = cursor.getString(positionIndex);
                i++;
            }
            while (cursor.moveToNext());

            Adapter adapter = new Adapter(surname, name, patronymic, position, date, id);
            productRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            productRecycler.setLayoutManager(layoutManager);

            adapter.setListener(new Adapter.Listener() {
                @Override
                public void onClick(int i) {
                    Cursor cursor = db.query(DataBase.TABLE_NAME, null, "ID = ?", new String[]{String.valueOf(i)}, null, null, null);
                    if (cursor.moveToFirst()){
                        Log.d("ID", String.valueOf(i));
                    }
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(AddActivity.EXTRA_CLIENT_ID, i);
                    getActivity().startActivity(intent);
                }
            });
        }
        return productRecycler;
    }
}
