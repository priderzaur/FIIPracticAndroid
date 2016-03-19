package com.example.riordachioaia.fiips1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.ContactDatabase;

/**
 * Created by riordachioaia on 19.03.2016.
 */
public class ContactDetailsActivity extends AppCompatActivity{
    private static final String TAG = "ContactDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Contact contact = (Contact) getIntent().getSerializableExtra(Contact.contact_key);
        ContactDatabase.contacts.add(contact);

    }
}
