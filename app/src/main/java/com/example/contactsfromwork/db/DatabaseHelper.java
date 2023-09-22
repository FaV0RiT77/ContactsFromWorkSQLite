package com.example.contactsfromwork.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactsfromwork.db.entity.Contact;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contact_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contact.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);

        onCreate(db);
    }

    // Insert Data into Database
    public long insertContact(String name, String tg) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME, name);
        values.put(Contact.COLUMN_TG, tg);

        long id = database.insert(Contact.TABLE_NAME, null, values);

        database.close();

        return id;
    }

    // Getting Data from Database
    public Contact getContact(long id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Contact.TABLE_NAME,
                new String[]{
                Contact.COLUMN_ID,
                Contact.COLUMN_NAME,
                Contact.COLUMN_TG},
        Contact.COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor != null) cursor.moveToFirst();

        Contact contact = new Contact(
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_TG)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
        cursor.close();
        return contact;
    }

    // Getting all Data (Contacts)
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " +
                Contact.COLUMN_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
                contact.setTg(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_TG)));

                contacts.add(contact);
            } while(cursor.moveToNext());
        }
        database.close();
        cursor.close();
        return contacts;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contact.COLUMN_NAME, contact.getName());
        values.put(Contact.COLUMN_TG, contact.getTg());

        return database.update(Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ? ",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        database.close();
    }
}
