package com.example.projectv3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    EditText nameBox;
    EditText rozmirBox;
    EditText vagaBox;
    EditText vaga2Box;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameBox = (EditText) findViewById(R.id.name);
        rozmirBox = (EditText) findViewById(R.id.weight);
        vagaBox = (EditText) findViewById(R.id.weight2);
        vaga2Box = (EditText) findViewById(R.id.weight3);
        rozmirBox.setInputType(InputType.TYPE_CLASS_PHONE);
        vagaBox.setInputType(InputType.TYPE_CLASS_PHONE);
        vaga2Box.setInputType(InputType.TYPE_CLASS_PHONE);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        if (userId > 0) {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            if(userCursor.getString(1)!=null) {
                nameBox.setText(userCursor.getString(1));
            }
            if(userCursor.getDouble(2)!=0) {
                rozmirBox.setText(String.valueOf(userCursor.getDouble(2)));
            }
            if(userCursor.getDouble(3)!=0){
                vagaBox.setText(String.valueOf(userCursor.getDouble(3)));
            }
            if(userCursor.getDouble(4)!=0) {
                vaga2Box.setText(String.valueOf(userCursor.getDouble(4)));
            }
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Double.parseDouble(rozmirBox.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_VAGA, Double.parseDouble(vagaBox.getText().toString()));
        cv.put(DatabaseHelper.COLUMN_VAGA2, Double.parseDouble(vaga2Box.getText().toString()));
            if (userId > 0) {
                db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
            } else {
                db.insert(DatabaseHelper.TABLE, null, cv);
            }
            goHome();

    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}