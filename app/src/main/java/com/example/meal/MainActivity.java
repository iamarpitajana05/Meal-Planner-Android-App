package com.example.meal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText weekInput, breakfastInput, lunchInput, dinnerInput;
    private Spinner daySpinner;
    private int id = 0;
    private Button addButton, display, editButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        dbHelper = new DBHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        addButton.setOnClickListener(v -> addMealToDatabase());
        display.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DisplayDAta.class)));
        editButton.setOnClickListener(v -> updateMealData());

        editData();
    }

    private void findViews() {
        weekInput = findViewById(R.id.week);
        breakfastInput = findViewById(R.id.breakfast);
        lunchInput = findViewById(R.id.lunch);
        dinnerInput = findViewById(R.id.dinner);
        daySpinner = findViewById(R.id.day_spinner);
        addButton = findViewById(R.id.submit_btn);
        display = findViewById(R.id.display_btn);
        editButton = findViewById(R.id.edit_btn);
    }

    private void editData() {
        if (getIntent().getBundleExtra("Mealdata") != null) {
            Bundle bundle = getIntent().getBundleExtra("Mealdata");
            id = bundle.getInt("id");
            weekInput.setText(String.valueOf(bundle.getInt("Weekno")));
            breakfastInput.setText(bundle.getString("breakfast"));
            lunchInput.setText(bundle.getString("lunch"));
            dinnerInput.setText(bundle.getString("dinner"));

            String day = bundle.getString("day");
            if (day != null) {
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) daySpinner.getAdapter();
                int position = adapter.getPosition(day);
                daySpinner.setSelection(position);
            }

            editButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.GONE);
        }
    }

    private void addMealToDatabase() {
        String weekNo = weekInput.getText().toString().trim();
        String selectedDay = daySpinner.getSelectedItem().toString();
        String breakfast = breakfastInput.getText().toString().trim();
        String lunch = lunchInput.getText().toString().trim();
        String dinner = dinnerInput.getText().toString().trim();

        if (weekNo.isEmpty() || selectedDay.equals("Select a day") || breakfast.isEmpty() || lunch.isEmpty() || dinner.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("week_no", Integer.parseInt(weekNo));
            cv.put("day_of_week", selectedDay);
            cv.put("breakfast", breakfast);
            cv.put("lunch", lunch);
            cv.put("dinner", dinner);

            long result = db.insert(DBHelper.TABLENAME, null, cv);
            if (result != -1) {
                Toast.makeText(this, "Meal details added successfully!", Toast.LENGTH_SHORT).show();
                clearInputs();
            } else {
                Toast.makeText(this, "Failed to add meal details. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMealData() {
        if (id != 0) {
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("week_no", Integer.parseInt(weekInput.getText().toString().trim()));
                cv.put("day_of_week", daySpinner.getSelectedItem().toString());
                cv.put("breakfast", breakfastInput.getText().toString().trim());
                cv.put("lunch", lunchInput.getText().toString().trim());
                cv.put("dinner", dinnerInput.getText().toString().trim());

                int rowsUpdated = db.update(DBHelper.TABLENAME, cv, "id=?", new String[]{String.valueOf(id)});

                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Meal details updated successfully!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                    addButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLiteException e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No record selected for update.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        weekInput.setText("");
        daySpinner.setSelection(0);
        breakfastInput.setText("");
        lunchInput.setText("");
        dinnerInput.setText("");
    }
}
