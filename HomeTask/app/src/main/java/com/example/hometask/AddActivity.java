package com.example.hometask;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText surname, name, patronymic, url, childName;
    Spinner position, child;
    Button date, download, save, delete, addChild, deleteChild;
    TextView dateText, childText;
    Calendar dateAndTime = Calendar.getInstance();
    String URL;
    DataBase dataBase;
    Context context;

    public static final String EXTRA_CLIENT_ID = "id";

    String[] array = {"Junior", "Middle", "Senior"};
    String item, childItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        surname = (EditText) findViewById(R.id.surname);
        name = (EditText) findViewById(R.id.name);
        patronymic = (EditText) findViewById(R.id.patronymic);
        url = (EditText) findViewById(R.id.url);
        childName = (EditText) findViewById(R.id.nameChild);

        dateText = (TextView) findViewById(R.id.dateText);
        childText = (TextView) findViewById(R.id.childText);

        date = (Button) findViewById(R.id.dateButton);
        position = (Spinner) findViewById(R.id.position);
        child = (Spinner) findViewById(R.id.child);
        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);
        addChild = (Button) findViewById(R.id.addChild);
        deleteChild = (Button) findViewById(R.id.deleteChild);

        context = AddActivity.this;

        setInitialDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        final int id = (int) getIntent().getExtras().get(EXTRA_CLIENT_ID);
        dataBase = OpenHelperManager.getHelper(this, DataBase.class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(adapter);


        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RuntimeExceptionDao<Client, Integer> runtimeExceptionDao = dataBase.getRuntimeExceptionDao();
                String surnameText = surname.getText().toString();
                String nameText = name.getText().toString();
                String patronymicText = patronymic.getText().toString();
                String urlText = url.getText().toString();
                String word = "[a-zA-Zа-яА-Я]+";
                String urlCheck = "[a-zA-Zа-яА-Я]+\\.+[a-z]";

                switch (v.getId()){

                    case R.id.save:

                        if (surnameText.length() > 0 && surnameText.matches(word)) {
                        }
                        else {
                            Toast toast = Toast.makeText(context, "Проверьте фамилию", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }
                        if (nameText.length() > 0 && nameText.matches(word)) {
                        }
                        else {
                            Toast toast = Toast.makeText(context, "Проверьте имя", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }
                        if (patronymicText.length() > 0 && patronymicText.matches(word)) {
                        }
                        else {
                            Toast toast = Toast.makeText(context, "Проверьте отчество", Toast.LENGTH_LONG);
                            toast.show();
                            break;
                        }

                       if (id > 0){
                           UpdateBuilder<Client, Integer> updateBuilder = runtimeExceptionDao.updateBuilder();
                           try {
                               updateBuilder.where().eq("id", id);
                               updateBuilder.updateColumnValue("surname", surname.getText().toString());
                               updateBuilder.updateColumnValue("name", name.getText().toString());
                               updateBuilder.updateColumnValue("patronymic", patronymic.getText().toString());
                               updateBuilder.updateColumnValue("position", item);
                               updateBuilder.updateColumnValue("date", dateText.getText().toString());
                               updateBuilder.update();

                           } catch (SQLException e) {
                               e.printStackTrace();
                           }
                           if (urlText.length() != 0 && URLUtil.isHttpsUrl(urlText)){
                               Log.d("UPDATE IMAGE", urlText);
                               Picasso.with(context)
                                       .load(urlText)
                                       .into(getTarget(urlText, String.valueOf(id)));
                           }
                           else {
                               Toast toast = Toast.makeText(context, "Проверьте url", Toast.LENGTH_LONG);
                               toast.show();
                               break;
                           }
                       }
                       else {
                           if (urlText.length() > 0 && URLUtil.isHttpsUrl(urlText)) {
                               Client client = new Client(surname.getText().toString(), name.getText().toString(),
                                       patronymic.getText().toString(), item, dateText.getText().toString());
                               runtimeExceptionDao.create(client);
                               List<Client> clients = runtimeExceptionDao.queryForAll();

                               URL = url.getText().toString();
                               Log.d("URL", URL);
                               Picasso.with(context)
                                       .load(URL)
                                       .into(getTarget(URL, String.valueOf(clients.get(clients.size()-1).getId())));
                               Log.d("PICASSO", "PICASSO");
                           }
                           else {
                               Toast toast = Toast.makeText(context, "Проверьте url", Toast.LENGTH_LONG);
                               toast.show();
                               break;
                           }
                       }
                        goHome();
                       break;

                    case R.id.delete:
                        Log.d("DELETEID", String.valueOf(id));
                        runtimeExceptionDao.deleteById(id);
                        File file = new File("/data/data/com.example.hometask/app_imageDir", id + ".png");
                        file.delete();
                        goHome();
                        break;

                    case R.id.addChild:
                        try {
                            Client client = new Client();
                            client.setId(id);

                            if (childName.getText().toString().length() > 0 && childName.getText().toString().matches(word)) {
                            }
                            else {
                                Toast toast = Toast.makeText(context, "Проверьте имя подчиненного", Toast.LENGTH_LONG);
                                toast.show();
                                break;
                            }

                            Dao<Child, Integer> childDao = dataBase.getDao();
                            Child child = new Child(client, childName.getText().toString());
                            Log.d("CHILDNAME", childName.getText().toString());
                            childDao.create(child);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        goHome();
                        break;

                    case R.id.deleteChild:
                        try {
                            Dao<Child,Integer> deleteChildDao = dataBase.getDao();
                            List<Child> deleteChild = deleteChildDao.queryForAll();
                            for (int i = 0; i < deleteChild.size(); i++){
                                if (childItem.equals(deleteChild.get(i).getNameChild())){
                                    deleteChildDao.deleteById(deleteChild.get(i).getId());
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        goHome();
                        break;

                }
            }
        };
        save.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);
        addChild.setOnClickListener(onClickListener);
        deleteChild.setOnClickListener(onClickListener);

        AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String) parent.getItemAtPosition(position);
                Log.d("Item", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        position.setOnItemSelectedListener(onItemSelectedListener);
        AdapterView.OnItemSelectedListener onItemSelected = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                childItem = (String) parent.getItemAtPosition(position);
                Log.d("Item", childItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        child.setOnItemSelectedListener(onItemSelected);

        OpenHelperManager.releaseHelper();

        RuntimeExceptionDao<Client, Integer> runtimeExceptionDao = dataBase.getRuntimeExceptionDao();
        List<Client> clients1 = Collections.singletonList(runtimeExceptionDao.queryForId(id));
        Log.d("PROLIST", clients1.toString());
        if (id > 0){
            surname.setText(clients1.get(0).getSurname());
            name.setText(clients1.get(0).getName());
            patronymic.setText(clients1.get(0).getPatronymic());
            dateText.setText(clients1.get(0).getDate());
            Log.d("ID", String.valueOf(id));

            try {
                Dao<Child, Integer> childIntegerDao = dataBase.getDao();
                List<Child> childList = childIntegerDao.queryForAll();
                Log.d("CHILDLIST IN YXXXX", String.valueOf(childList));
                ArrayList<String> childName = new ArrayList<String>();
                for (int i=0; i<childList.size(); i++){
                    if (clients1.get(0).getId() == childList.get(i).getClient().getId()){
                        childName.add(childList.get(i).getNameChild());
                    }
                }

                ArrayAdapter<String> childAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, childName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                child.setAdapter(childAdapter);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            delete.setVisibility(View.GONE);
            addChild.setVisibility(View.GONE);
            deleteChild.setVisibility(View.GONE);
            childName.setVisibility(View.GONE);
            child.setVisibility(View.GONE);
            childText.setVisibility(View.GONE);
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    public void setDate(View v) {
        new DatePickerDialog(AddActivity.this, d, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void setInitialDate() {
        dateText.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    private void goHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private static Target getTarget(final String url, final String name){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file = new File("/data/data/com.example.hometask/app_imageDir",name + ".png");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 85, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }
}