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

    private int contactPosition, id=0;
    private String initialName, initialSurname, initialPhoneNumber, initialGroup, name, surname, phoneNumber, group;
    private EditText mFirstNameET, mLastNameET, mPhoneNumberET, mGroupET;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        mFirstNameET = (EditText) findViewById(R.id.et_contact_name);
        mLastNameET = (EditText) findViewById(R.id.et_contact_surname);
        mPhoneNumberET = (EditText) findViewById(R.id.et_contact_phoneNumber);
        mGroupET = (EditText) findViewById(R.id.et_contact_group);

        contact = (Contact) getIntent().getSerializableExtra(Contact.contact_key);
        if (contact != null) {
            id=contact.getId();
            mFirstNameET.setText(contact.getName());
            mLastNameET.setText(contact.getSurname());
            mPhoneNumberET.setText(contact.getPhoneNumber());
            mGroupET.setText(contact.getGroup());
        };

        contactPosition = getIntent().getIntExtra(ContactsActivity.POSITION_KEY, -1);

        initialName=mFirstNameET.getText().toString();
        initialSurname=mLastNameET.getText().toString();
        initialPhoneNumber=mPhoneNumberET.getText().toString();
        initialGroup=mGroupET.getText().toString();

        findViewById(R.id.btn_contact_save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name=mFirstNameET.getText().toString();
                surname=mLastNameET.getText().toString();
                phoneNumber=mPhoneNumberET.getText().toString();
                group=mGroupET.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname)||TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(group)) {
                    Toast.makeText(ContactDetailsActivity.this, "No empty fields allowed", Toast.LENGTH_LONG).show();
                } else {

                    contact = new Contact(name, surname, phoneNumber, group);
                    contact.setId(id);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Contact.contact_key, contact);
                    resultIntent.putExtra(ContactsActivity.POSITION_KEY,contactPosition);
                    setResult(RESULT_OK, resultIntent);
                    Log.d(TAG, name+surname+phoneNumber+group);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        name = mFirstNameET.getText().toString();
        surname = mLastNameET.getText().toString();
        phoneNumber = mPhoneNumberET.getText().toString();
        group = mGroupET.getText().toString();

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
