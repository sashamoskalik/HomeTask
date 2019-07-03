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

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;


public class Client_Fragment extends Fragment {
    DataBase dataBase;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBase = OpenHelperManager.getHelper(getContext(), DataBase.class);

        RecyclerView productRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_client, container, false);

        RuntimeExceptionDao<Client, Integer> runtimeExceptionDao = dataBase.getRuntimeExceptionDao();
        List<Client> clientList = runtimeExceptionDao.queryForAll();

        String[] surname = new String[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            surname[i] = clientList.get(i).getSurname();
        }

        int[] id = new int[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            id[i] = clientList.get(i).getId();
            Log.d("ARRAYID", String.valueOf(id[i]));
        }

        String[] name = new String[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            name[i] = clientList.get(i).getName();
        }

        String[] patronymic = new String[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            patronymic[i] = clientList.get(i).getPatronymic();
        }

        String[] position = new String[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            position[i] = clientList.get(i).getPosition();
        }

        String[] date = new String[clientList.size()];

        for (int i=0; i<clientList.size(); i++) {
            date[i] = clientList.get(i).getDate();
        }

            Adapter adapter = new Adapter(surname, name, patronymic, position, date, id);
            productRecycler.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            productRecycler.setLayoutManager(layoutManager);

            adapter.setListener(new Adapter.Listener() {
                @Override
                public void onClick(int i) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(AddActivity.EXTRA_CLIENT_ID, i);
                    getActivity().startActivity(intent);
                }
            });
        return productRecycler;

    }
}
