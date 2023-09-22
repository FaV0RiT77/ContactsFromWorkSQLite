package com.example.contactsfromwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsfromwork.adapter.ContactsAdapter;
import com.example.contactsfromwork.db.DatabaseHelper;
import com.example.contactsfromwork.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Work Contacts");
        // RecyclerView
        recyclerView = findViewById(R.id.contacts_rv);
        db = new DatabaseHelper(this);

        // Contacts List
        contactArrayList.addAll(db.getAllContacts());
        contactsAdapter = new ContactsAdapter(this, contactArrayList, MainActivity.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            addAndEditContacts(false, null, -1);
        });

    }

    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactTg = view.findViewById(R.id.tg);

        contactTitle.setText(!isUpdated ? "Add New Contact" : "Edit Contact");

        if (isUpdated && contact != null) {
            newContact.setText(contact.getName());
            contactTg.setText(contact.getTg());
        }

        alertDialogBuilder.setCancelable(true).setPositiveButton(isUpdated ? "Update" : "Save",
                        (dialog, which) -> {

        })
                .setNegativeButton("Delete", (dialog, which) -> {
                    if (isUpdated) {
                        deleteContact(contact, position);
                    } else {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (TextUtils.isEmpty(newContact.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.dismiss();
            }

            if (isUpdated && contact != null) {
                updateContact(newContact.getText().toString(), contactTg.getText().toString(), position);
            } else {
                createContact(newContact.getText().toString(), contactTg.getText().toString());
            }
        });
    }

    private void deleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactsAdapter.notifyItemRemoved(position); // or use notify dataset changed method
    }

    private void updateContact(String name, String tg, int position) {

        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setTg(tg);

        db.updateContact(contact);

        contactArrayList.set(position, contact);
        contactsAdapter.notifyItemChanged(position, contact);
    }

    private void createContact(String name, String tg) {

        long id = db.insertContact(name, tg);
        Contact contact = db.getContact(id);

        if (contact != null) {
            contactArrayList.add(0, contact);
            contactsAdapter.notifyItemInserted(0);
        } else {
            Toast.makeText(this, "Insert the data", Toast.LENGTH_SHORT).show();
        }
    }

    // Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}