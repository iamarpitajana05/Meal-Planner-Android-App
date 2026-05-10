package com.example.meal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private EditText edtWeek, edtBreakfast, edtLunch, edtDinner;
    private Spinner daySpinner;
    private Button btnSubmit, btnDisplay, btnEdit;
    private ImageButton btnLogout;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private int mealId = -1; // To track if we are editing an existing record

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        initViews();

        // Initialize Database
        dbHelper = new DBHelper(this);

        // Setup Day Spinner
        setupSpinner();

        // Check if we are coming from the Edit button in the Adapter
        checkIncomingData();

        // Button Click Listeners
        btnSubmit.setOnClickListener(v -> insertData());
        btnDisplay.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DisplayDAta.class));
        });

        // The attractive icon logout
        btnLogout.setOnClickListener(v -> performLogout());

        btnEdit.setOnClickListener(v -> updateData());
    }

    private void initViews() {
        edtWeek = findViewById(R.id.week);
        daySpinner = findViewById(R.id.day_spinner);
        edtBreakfast = findViewById(R.id.breakfast);
        edtLunch = findViewById(R.id.lunch);
        edtDinner = findViewById(R.id.dinner);
        btnSubmit = findViewById(R.id.submit_btn);
        btnDisplay = findViewById(R.id.display_btn);
        btnEdit = findViewById(R.id.edit_btn);
        btnLogout = findViewById(R.id.logout_btn);
    }

    private void setupSpinner() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, days);
        daySpinner.setAdapter(adapter);
    }

    private void checkIncomingData() {
        if (getIntent().getBundleExtra("Mealdata") != null) {
            Bundle bundle = getIntent().getBundleExtra("Mealdata");
            mealId = bundle.getInt("id");
            edtWeek.setText(String.valueOf(bundle.getInt("Weekno")));
            edtBreakfast.setText(bundle.getString("breakfast"));
            edtLunch.setText(bundle.getString("lunch"));
            edtDinner.setText(bundle.getString("dinner"));

            // Set Spinner Selection
            String day = bundle.getString("day");
            ArrayAdapter adapter = (ArrayAdapter) daySpinner.getAdapter();
            daySpinner.setSelection(adapter.getPosition(day));

            // UI Changes for Edit Mode
            btnSubmit.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        }
    }

    private void insertData() {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        String week = edtWeek.getText().toString();
        String day = daySpinner.getSelectedItem().toString();
        String breakfast = edtBreakfast.getText().toString();
        String lunch = edtLunch.getText().toString();
        String dinner = edtDinner.getText().toString();

        if (week.isEmpty() || breakfast.isEmpty() || lunch.isEmpty() || dinner.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.insertMeal(Integer.parseInt(week), day, breakfast, lunch, dinner);
        if (result != -1) {
            Toast.makeText(this, "Meal Saved Successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Error Saving Meal", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        // Use dbHelper.updateMeal logic here (similar to insert but with ID)
        // After update, toggle visibility back
        btnSubmit.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        clearFields();
        Toast.makeText(this, "Meal Updated", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        edtWeek.setText("");
        edtBreakfast.setText("");
        edtLunch.setText("");
        edtDinner.setText("");
        daySpinner.setSelection(0);
    }
}