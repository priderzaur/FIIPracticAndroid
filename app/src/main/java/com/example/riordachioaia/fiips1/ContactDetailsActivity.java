package com.example.riordachioaia.fiips1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.ContactDatabase;

import java.io.Serializable;

/**
 * Created by riordachioaia on 19.03.2016.
 */
public class ContactDetailsActivity extends AppCompatActivity implements Serializable{
    private static final String TAG = "ContactDetailsActivity";

    String intentType;
    int contactPosition;
    String initialName, initialSurname, initialPhoneNumber, initialGroup, name, surname, phoneNumber, group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        intentType = getIntent().getStringExtra(ContactsActivity.DETAILS_TYPE_KEY);
        if (intentType.equals(ContactsActivity.DETAILS_TYPE_EDIT)) {
            Contact contact = (Contact) getIntent().getSerializableExtra(Contact.contact_key);
            contactPosition = getIntent().getIntExtra(ContactsActivity.POSITION_KEY,0);
            ((EditText) findViewById(R.id.et_contact_name)).setText(contact.getName());
            ((EditText) findViewById(R.id.et_contact_surname)).setText(contact.getSurname());
            ((EditText) findViewById(R.id.et_contact_phoneNumber)).setText(contact.getPhoneNumber());
            ((EditText) findViewById(R.id.et_contact_group)).setText(contact.getGroup());
        }
        initialName=((EditText)findViewById(R.id.et_contact_name)).getText().toString();
        initialSurname=((EditText)findViewById(R.id.et_contact_surname)).getText().toString();
        initialPhoneNumber=((EditText)findViewById(R.id.et_contact_phoneNumber)).getText().toString();
        initialGroup= ((EditText)findViewById(R.id.et_contact_group)).getText().toString();

        findViewById(R.id.btn_contact_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name=((EditText)findViewById(R.id.et_contact_name)).getText().toString();
                surname=((EditText)findViewById(R.id.et_contact_surname)).getText().toString();
                phoneNumber=((EditText)findViewById(R.id.et_contact_phoneNumber)).getText().toString();
                group= ((EditText)findViewById(R.id.et_contact_group)).getText().toString();
                Log.d(TAG,"after click " + name);
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname)||TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(group)) {
                    Toast.makeText(ContactDetailsActivity.this, "No empty fields allowed", Toast.LENGTH_LONG).show();
                } else {

                    Contact savedContact = new Contact(name, surname, phoneNumber, group);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Contact.contact_key, savedContact);
                    resultIntent.putExtra(ContactsActivity.DETAILS_TYPE_KEY, intentType);
                    if (intentType.equals(ContactsActivity.DETAILS_TYPE_EDIT)){
                        resultIntent.putExtra(ContactsActivity.POSITION_KEY,contactPosition);
                    }
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        name = ((EditText) findViewById(R.id.et_contact_name)).getText().toString();
        surname = ((EditText) findViewById(R.id.et_contact_surname)).getText().toString();
        phoneNumber = ((EditText) findViewById(R.id.et_contact_phoneNumber)).getText().toString();
        group = ((EditText) findViewById(R.id.et_contact_group)).getText().toString();

        if (!initialName.equals(name) || !initialSurname.equals(surname) || !initialPhoneNumber.equals(phoneNumber) || !initialGroup.equals(group)) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactDetailsActivity.this);
            dialogBuilder.setTitle("Confirm");
            dialogBuilder.setMessage("Changes made\nCancel?");
            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    ContactDetailsActivity.super.onBackPressed();
                    dialog.dismiss();
                    ContactDetailsActivity.this.finish();
                }

            });
            dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing
                    dialog.dismiss();
                }
            });
            dialogBuilder.show();
        }
        else {
            ContactDetailsActivity.super.onBackPressed();
            ContactDetailsActivity.this.finish();
        }
    }
}
