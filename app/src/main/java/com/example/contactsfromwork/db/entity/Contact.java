package com.example.contactsfromwork.db.entity;

public class Contact {
    // Constants for Database
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID = "contact_id";
    public static final String COLUMN_NAME = "contact_name";
    public static final String COLUMN_TG = "contact_tg";

    private String name;
    private String tg;
    private int id;

    public Contact() {
    }

    public Contact(String name, String tg, int id) {
        this.name = name;
        this.tg = tg;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // SQL Query: Creating the Table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT,"
            + COLUMN_TG + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
}
