package com.example.hometask;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    ListView listView;
    ImageView image;
    TextView fio, positionList, dateList;
    DataBase dataBase;
    SQLiteDatabase db;
    Cursor cursor;
    String Surname;
    private ContactsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);

        listView = (ListView) findViewById(R.id.list);

        dataBase = new DataBase(getApplicationContext());
        db = dataBase.getWritableDatabase() ;

        cursor = db.rawQuery("select * from Contacts ", null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursorClick = db.query(DataBase.TABLE_NAME, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
                if (cursorClick.moveToFirst()){
                    Surname = cursorClick.getString(1);
                }
                Log.d("MAINSURNAME", Surname);
                //Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                //intent.putExtra(AddActivity.EXTRA_SURNAME, surname);
                //startActivity(intent);
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.addButton:
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        startActivity(intent);
                }
            }
        };
        addButton.setOnClickListener(onClickListener);

        mAdapter = new ContactsAdapter(this);
        listView.setAdapter(mAdapter);
    }

    ///////////////////////////////////////////////////
    private class ContactsAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        ContactsAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            int count = 0;
            cursor.moveToFirst();
            do {
                if (cursor.getCount() != 0)
                count++;
            }
            while (cursor.moveToNext());
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = mLayoutInflater.inflate(R.layout.listview, null);

            image = (ImageView) convertView.findViewById(R.id.image);
            fio = (TextView) convertView.findViewById(R.id.fio);
            dateList = (TextView) convertView.findViewById(R.id.dateList);
            positionList = (TextView) convertView.findViewById(R.id.positionList);

            int count = 0;
            cursor.moveToFirst();
            do {
                count++;
            }
            while (cursor.moveToNext());
            Log.d("COUNT", String.valueOf(count));

            if (count > 1) {

                String[] FIO = new String[count];
                cursor.moveToFirst();
                int i = 0;
                do {
                    FIO[i] = cursor.getString(1) + cursor.getString(2) + cursor.getString(3);
                    i++;
                }
                while (cursor.moveToNext());

                String[] DateList = new String[count];
                cursor.moveToFirst();
                int m = 0;
                do {
                    DateList[m] = cursor.getString(5);
                    m++;
                }
                while (cursor.moveToNext());
                Log.d("DATE", DateList[position]);

                String[] PositionList = new String[count];
                cursor.moveToFirst();
                int k = 0;
                do {
                    PositionList[k] = cursor.getString(4);
                    k++;
                }
                while (cursor.moveToNext());
                Log.d("POSITION", PositionList[position]);

                String[] Image = new String[count];
                cursor.moveToFirst();
                int j = 0;
                do {
                    Image[j] = cursor.getString(0);
                    j++;
                }
                while (cursor.moveToNext());


                Log.d("POSITION", FIO[position]);
                fio.setText(FIO[position]);
                dateList.setText(DateList[position]);
                positionList.setText(PositionList[position]);
                Image("/data/data/com.example.hometask/app_imageDir", Image[position]);
            }
            //Log.d("CURSOR", fio.getText().toString());
            return convertView;
        }
    }
    //////////////////////////////////////////////

    private void Image(String directory, String name){
        File file = new File(directory, name + ".png");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        image.setImageBitmap(bitmap);
    }
}
