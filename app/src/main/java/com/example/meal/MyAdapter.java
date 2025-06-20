package com.example.meal;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ModelViewHolder>{
    private Context context;
    private List<Model> mealList;
    SQLiteDatabase sqLiteDatabase;

    // Constructor
    public MyAdapter(Context context, int singledata, List<Model> mealList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.mealList = mealList;
        this.sqLiteDatabase=sqLiteDatabase;
    }
    @NonNull
    @Override
    public MyAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.singledata,null);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ModelViewHolder holder, int position) {
        final Model meal = mealList.get(position);

        // Set the data to the TextViews
        holder.txtWeekNo.setText("Week No: " + meal.getWeekNo());
        holder.txtDay.setText("Day: " + meal.getDayOfWeek());
        holder.txtBreakfast.setText("Breakfast: " + meal.getBreakfast());
        holder.txtLunch.setText("Lunch: " + meal.getLunch());
        holder.txtDinner.setText("Dinner: " + meal.getDinner());

        //click on imagebutton go to main activity
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("id",meal.getId());
                bundle.putInt("Weekno",meal.getWeekNo());
                bundle.putString("day",meal.getDayOfWeek());
                bundle.putString("breakfast",meal.getBreakfast());
                bundle.putString("lunch",meal.getLunch());
                bundle.putString("dinner",meal.getDinner());
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("Mealdata",bundle);
                context.startActivity(intent);
            }
        });
        // Delete row from database
        holder.btnDelete.setOnClickListener(v -> {
            try {
                int deletedRows = sqLiteDatabase.delete(DBHelper.TABLENAME, "id=?", new String[]{String.valueOf(meal.getId())});
                if (deletedRows > 0) {
                    mealList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mealList.size());
                    Toast.makeText(context, "Meal deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete meal", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLiteException e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {
        TextView txtWeekNo, txtDay, txtBreakfast, txtLunch, txtDinner;
        ImageButton btnEdit,btnDelete;
        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWeekNo = itemView.findViewById(R.id.txtWeekNo);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtBreakfast = itemView.findViewById(R.id.txtBreakfast);
            txtLunch = itemView.findViewById(R.id.txtLunch);
            txtDinner = itemView.findViewById(R.id.txtDinner);
            btnEdit=itemView.findViewById(R.id.btnEdit);
            btnDelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
