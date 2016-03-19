package com.example.riordachioaia.fiips1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.ContactDatabase;

/**
 * Created by riordachioaia on 19.03.2016.
 */
public class ContactDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ContactDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        final String intentType= getIntent().getStringExtra(ContactsActivity.DETAILS_TYPE_KEY);
        if (intentType.equals(ContactsActivity.DETAILS_TYPE_EDIT)) {
            Contact contact = (Contact) getIntent().getSerializableExtra(Contact.contact_key);
            ((EditText) findViewById(R.id.et_contact_name)).setText(contact.getName());
            ((EditText) findViewById(R.id.et_contact_surname)).setText(contact.getSurname());
            ((EditText) findViewById(R.id.et_contact_phoneNumber)).setText(contact.getPhoneNumber());
            ((EditText) findViewById(R.id.et_contact_group)).setText(contact.getGroup());
        }

        findViewById(R.id.btn_contact_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name=findViewById(R.id.et_contact_name).toString();
                String surname=      findViewById(R.id.et_contact_surname).toString();
                String phoneNumber=    findViewById(R.id.et_contact_phoneNumber).toString();
                String group=   findViewById(R.id.et_contact_group).toString();

                Contact savedContact = new Contact(name,surname,phoneNumber,group);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(Contact.contact_key, savedContact);
                resultIntent.putExtra(ContactsActivity.DETAILS_TYPE_KEY,intentType);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
