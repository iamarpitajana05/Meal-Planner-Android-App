package com.example.meal;

import static com.example.meal.DBHelper.TABLENAME;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Currency;

public class DisplayDAta extends AppCompatActivity {
    DBHelper dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        dBmain=new DBHelper(this);
        findid();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void displayData() {
        sqLiteDatabase=dBmain.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLENAME+"",null);
        ArrayList<Model>modelArrayList=new ArrayList<>();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data available to display.", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            int weekNo = cursor.getInt(1);
            String dayOfWeek = cursor.getString(2);
            String breakfast = cursor.getString(3);
            String lunch = cursor.getString(4);
            String dinner = cursor.getString(5);
            modelArrayList.add(new Model(id, weekNo, dayOfWeek, breakfast, lunch, dinner));
        }
        cursor.close();
        myAdapter = new MyAdapter(this,R.layout.singledata,modelArrayList,sqLiteDatabase);
        recyclerView.setAdapter(myAdapter);
    }

    public void findid(){
        recyclerView=findViewById(R.id.rv);
    }
}