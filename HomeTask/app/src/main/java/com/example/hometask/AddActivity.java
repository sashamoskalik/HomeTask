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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    EditText surname, name, patronymic, url;
    Spinner position;
    Button date, download, save, delete;
    TextView dateText;
    ImageView image;
    Calendar dateAndTime = Calendar.getInstance();
    private Context context;
    String URL;
    DataBase dataBase;
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues contentValues;

    public static final String EXTRA_SURNAME = "surname";

    String[] array = {"Junior", "Middle", "Senior"};
    String item;
    long id = -1;

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


        image = (ImageView) findViewById(R.id.image);

        context = AddActivity.this;
        dataBase = new DataBase(getApplicationContext());
        db = dataBase.getWritableDatabase();
        contentValues = new ContentValues();


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getLong("id");
        }
        Log.d("ID", String.valueOf(id));

        if (id > 0){
            String Surname = (String) getIntent().getExtras().get(EXTRA_SURNAME);
            Log.d("SURNAME", Surname);
            cursor = db.rawQuery("select * from " +  DataBase.TABLE_NAME + " where " + DataBase.KEY_ID + "=?", new String[]{String.valueOf(id)});
            cursor.moveToFirst();
            surname.setText(cursor.getString(1));
            name.setText(cursor.getString(2));
            patronymic.setText(cursor.getString(3));
            dateText.setText(cursor.getString(5));
            Image("/data/data/com.example.hometask/app_imageDir", String.valueOf(cursor.getInt(0)));
            cursor.close();
        }
        else {
            delete.setVisibility(View.GONE);
        }
        setInitialDate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(adapter);

        db = dataBase.getWritableDatabase();

        cursor = db.rawQuery("select * from Contacts", null);

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

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.download:
                        URL = url.getText().toString();
                        Log.d("URL", URL);
                        Picasso.with(context)
                                .load(URL)
                                .into(image);
                        break;

                    case R.id.save:
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Log.d("BITMAP", String.valueOf(bitmap));
                        contentValues.put(DataBase.KEY_SURNAME, surname.getText().toString());
                        contentValues.put(DataBase.KEY_NAME, name.getText().toString());
                        contentValues.put(DataBase.KEY_PATRONYMIC, patronymic.getText().toString());
                        contentValues.put(DataBase.KEY_DATA, dateText.getText().toString());
                        contentValues.put(DataBase.KEY_POSITION, item);
                        if (id > 0){
                            db.update("Contacts", contentValues, DataBase.KEY_ID + "=" + id, null);
                        }
                        else {
                            long rowID =db.insert("Contacts", null, contentValues);
                            InternalStorage(bitmap, String.valueOf(rowID));
                            Log.d("ID", String.valueOf(rowID));
                        }
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.delete:
                        db.delete(DataBase.TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)});
                        File file = new File("/data/data/com.example.hometask/app_imageDir", id + ".png");
                        file.delete();
                        Intent intent1 = new Intent(AddActivity.this, MainActivity.class);
                        startActivity(intent1);
                }
            }
        };
        download.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);



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
}