package com.example.android.labwork_2_7_3;

import static com.example.android.labwork_2_7_3.DBHelper.TABLE_NAME;
import static com.example.android.labwork_2_7_3.DBHelper.TEXT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Context context;

    private EditText textSaveLoad;

    SQLiteDatabase db;
    DBHelper dbHelper;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();

        textSaveLoad = findViewById(R.id.text_save_load);
        Button btnSave = findViewById(R.id.button_save);
        Button btnLoad = findViewById(R.id.button_load);

        // Создание Базы данных
        dbHelper = new DBHelper(context, "myDB", null, 1);
        // Подключаемся к базе данных
        db = dbHelper.getWritableDatabase();

        // INSERT
        btnSave.setOnClickListener(v -> {

            ContentValues cv = new ContentValues();
            // Вставляем в ContentValues текст из EditText
            cv.put(TEXT, textSaveLoad.getText().toString());
            // Вставляем в db
            long rowId = db.insert(TABLE_NAME, null, cv);
            if (rowId > 0) {
                Toast.makeText(MainActivity.this, "Тект вставлен с Id= "
                        + rowId, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Что-то пошло не так",
                        Toast.LENGTH_SHORT).show();
            }

            textSaveLoad.setText("");
        });

        // QUERY
        btnLoad.setOnClickListener(v -> {

            cursor = db.query(TABLE_NAME, null, null, null,
                    null, null, null);

            textSaveLoad.setText(""); // Очистили окно

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int idCurrent =
                        cursor.getInt(cursor.getColumnIndex("_id"));

                @SuppressLint("Range") String textCurrent =
                        cursor.getString(cursor.getColumnIndex(TEXT));
                textSaveLoad.append("ID= " + idCurrent + ". TEXT: " + textCurrent + "\n");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cursor.close();
        db.close();
    }
}