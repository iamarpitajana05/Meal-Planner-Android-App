package com.example.meal;

public class Model {
    private int id; // Optional, for primary key
    private int weekNo;
    private String dayOfWeek;
    private String breakfast;
    private String lunch;
    private String dinner;

    // Default constructor
    public Model() {
    }

    // Parameterized constructor
    public Model(int weekNo, String dayOfWeek, String breakfast, String lunch, String dinner) {
        this.weekNo = weekNo;
        this.dayOfWeek = dayOfWeek;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    // Another constructor with ID
    public Model(int id, int weekNo, String dayOfWeek, String breakfast, String lunch, String dinner) {
        this.id = id;
        this.weekNo = weekNo;
        this.dayOfWeek = dayOfWeek;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(int weekNo) {
        this.weekNo = weekNo;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", weekNo=" + weekNo +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", breakfast='" + breakfast + '\'' +
                ", lunch='" + lunch + '\'' +
                ", dinner='" + dinner + '\'' +
                '}';
    }
}
