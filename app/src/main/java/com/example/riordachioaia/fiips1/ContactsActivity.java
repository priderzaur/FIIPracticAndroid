package com.example.riordachioaia.fiips1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.ContactDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by riordachioaia on 14.03.2016.
 */
public class ContactsActivity extends Activity implements Serializable {

    private static final String TAG = "ContactsActivity";
    private static final int INTENT_CONTACT_DETAILS= 1;

    public static final String DETAILS_TYPE_KEY="DetailType";
    public static final String DETAILS_TYPE_EDIT="edit";
    public static final String DETAILS_TYPE_NEW="new";
    public static final String POSITION_KEY="position";

    ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ListView contactListView = (ListView) findViewById(R.id.contacts_list);
        contactsAdapter = new ContactsAdapter(ContactDatabase.contacts);
        contactListView.setAdapter(contactsAdapter);

        findViewById(R.id.btn_new_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                intent.putExtra(DETAILS_TYPE_KEY,DETAILS_TYPE_NEW);
                startActivityForResult(intent, INTENT_CONTACT_DETAILS);
            }
        });

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("Confirm");
                dialogBuilder.setMessage("Do you want to edit " + ((Contact)contactsAdapter.getItem(position)).getName()
                        + " " + ((Contact)contactsAdapter.getItem(position)).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                        intent.putExtra(DETAILS_TYPE_KEY,DETAILS_TYPE_EDIT);
                        intent.putExtra(Contact.contact_key, ((Contact)contactsAdapter.getItem(position)));
                        intent.putExtra(POSITION_KEY,position);
                        startActivityForResult(intent, INTENT_CONTACT_DETAILS);
                        dialog.dismiss();
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
        });

        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("Confirm");
                dialogBuilder.setMessage("Do you want to delete " + ((Contact)contactsAdapter.getItem(position)).getName() +
                        " " + ((Contact)contactsAdapter.getItem(position)).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        contactsAdapter.removeContact(position);
                        contactsAdapter.notifyDataSetChanged();
                        dialog.dismiss();
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
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == INTENT_CONTACT_DETAILS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String intentType = data.getStringExtra(DETAILS_TYPE_KEY);
                Contact contact = (Contact) data.getSerializableExtra(Contact.contact_key);

                if (intentType.equals(DETAILS_TYPE_EDIT)) {
                    int contactPosition=data.getIntExtra(POSITION_KEY,0);
                    contactsAdapter.updateContact(contact,contactPosition);
                } else {
                    contactsAdapter.addContact(contact);
                }

                contactsAdapter.notifyDataSetChanged();
            }
        }
    }

    class ContactsAdapter extends BaseAdapter{
        private List<Contact> contactList;

        public ContactsAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View contactListItem = getLayoutInflater().inflate(R.layout.contactlist_item,parent,false);
           // TextView textView = new TextView(ContactsActivity.this);
           // textView.setText(contactList.get(position).getName());
            Contact contact = contactList.get(position);
            ((TextView)contactListItem.findViewById(R.id.name_tv)).setText(contact.getName());
            ((TextView)contactListItem.findViewById(R.id.surname_tv)).setText(contact.getSurname());
            ((TextView)contactListItem.findViewById(R.id.phoneNumber_tv)).setText(contact.getPhoneNumber());
            ((TextView)contactListItem.findViewById(R.id.group_tv)).setText(contact.getGroup());
            return contactListItem;
        }

        public void removeContact(int position){
            contactList.remove(position);
            Log.d(TAG, String.valueOf(contactList.size()));
        }

        public void addContact(Contact contact) {
            contactList.add(contact);
        }

        public void updateContact(Contact contact, int position){
            contactList.set(position,contact);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_login_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged","no");
        editor.commit();
        this.finish();
    }
}

