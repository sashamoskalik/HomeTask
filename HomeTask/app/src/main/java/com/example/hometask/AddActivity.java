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
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
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
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText surname, name, patronymic, url;
    Spinner position;
    Button date, download, save, delete;
    TextView dateText;
    ImageView image;
    Calendar dateAndTime = Calendar.getInstance();
    String URL;
    DataBase dataBase;
    Context context;

    public static final String EXTRA_CLIENT_ID = "id";

    String[] array = {"Junior", "Middle", "Senior"};
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        surname = (EditText) findViewById(R.id.surname);
        name = (EditText) findViewById(R.id.name);
        patronymic = (EditText) findViewById(R.id.patronymic);
        url = (EditText) findViewById(R.id.url);

        dateText = (TextView) findViewById(R.id.dateText);

        date = (Button) findViewById(R.id.dateButton);
        position = (Spinner) findViewById(R.id.position);
        download = (Button) findViewById(R.id.download);
        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);

        context = AddActivity.this;

        image = (ImageView) findViewById(R.id.image);

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

                switch (v.getId()){
                    case R.id.download:
                        URL = url.getText().toString();
                        Log.d("URL", URL);
                        Picasso.with(context)
                                .load(URL)
                                .into(image);
                        break;

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
                           BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                           Bitmap bitmap = bitmapDrawable.getBitmap();
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
                           if (urlText.length() != 0){
                               InternalStorage(bitmap, String.valueOf(id));
                           }
                       }
                       else {
                           if (urlText.length() == 0){
                               Toast toast = Toast.makeText(context, "Введите url", Toast.LENGTH_LONG);
                               toast.show();
                               break;
                           }
                           BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                           Bitmap bitmap = bitmapDrawable.getBitmap();
                           runtimeExceptionDao.create(new Client(surname.getText().toString(), name.getText().toString(),
                                   patronymic.getText().toString(), item, dateText.getText().toString()));
                           List<Client> clients = runtimeExceptionDao.queryForAll();
                           InternalStorage(bitmap, String.valueOf(clients.get(clients.size()-1).getId()));
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
                }
            }
        };
        save.setOnClickListener(onClickListener);
        download.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);

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

        OpenHelperManager.releaseHelper();

        RuntimeExceptionDao<Client, Integer> runtimeExceptionDao = dataBase.getRuntimeExceptionDao();
        List<Client> clients1 = Collections.singletonList(runtimeExceptionDao.queryForId(id));
        Log.d("PROLIST", clients1.toString());
        if (id > 0){
            surname.setText(clients1.get(0).getSurname());
            name.setText(clients1.get(0).getName());
            patronymic.setText(clients1.get(0).getPatronymic());
            dateText.setText(clients1.get(0).getDate());
            Image("/data/data/com.example.hometask/app_imageDir", String.valueOf(clients1.get(0).getId()));
            Log.d("ID", String.valueOf(id));
        }
        else {
            delete.setVisibility(View.GONE);
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

    private String InternalStorage(Bitmap bitmap, String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory,name + ".png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void Image(String directory, String name){
        File file = new File(directory, name + ".png");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        image.setImageBitmap(bitmap);
    }
    private void goHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}