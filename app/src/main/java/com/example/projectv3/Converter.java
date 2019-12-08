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
import android.widget.ImageButton;
import android.widget.TextView;

public class Converter extends AppCompatActivity {

    TextView nameBox;
    TextView rozmirBox;
    TextView vagaBox;
    TextView vaga2Box;
    TextView vagaBottle;
    TextView volume;
    ImageButton convert;
    Double volumeInBottle;
    Double bottleW;
    Double hundred;
    Double bottleV;
    Double used;





    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        nameBox = (TextView) findViewById(R.id.name);
        rozmirBox = (TextView) findViewById(R.id.weight);
        vagaBox = (TextView) findViewById(R.id.weight2);
        vaga2Box = (TextView) findViewById(R.id.weight3);
        vagaBottle = (EditText) findViewById(R.id.vagaBottle);
        volume = (TextView) findViewById(R.id.volume);
        convert = (ImageButton) findViewById(R.id.convert);
        rozmirBox.setInputType(InputType.TYPE_CLASS_PHONE);
        vagaBox.setInputType(InputType.TYPE_CLASS_PHONE);
        vaga2Box.setInputType(InputType.TYPE_CLASS_PHONE);



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
                nameBox.setText("Назва : "+userCursor.getString(1));
            }
            if(userCursor.getDouble(2)!=0) {
                rozmirBox.setText("Об'єм : "+String.valueOf(userCursor.getDouble(2)));
                bottleV = userCursor.getDouble(2);
            }
            if(userCursor.getDouble(3)!=0){
                vagaBox.setText("Вага : "+String.valueOf(userCursor.getDouble(3)));
                bottleW = userCursor.getDouble(3);
            }
            if(userCursor.getDouble(4)!=0) {
                vaga2Box.setText("Вага 100мл : "+String.valueOf(userCursor.getDouble(4)));
                hundred = userCursor.getDouble(4);
            }
            userCursor.close();
        }

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                used = Double.parseDouble(vagaBottle.getText().toString());
                volumeInBottle=(used-(bottleW-(hundred*bottleV*10)))/(hundred/100);


                volume.setText(volumeInBottle.intValue()+"мл");
            }
        });
    }




    private void goHome(){
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}