package com.example.riordachioaia.fiips1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.List;

/**
 * Created by riordachioaia on 14.03.2016.
 */
public class ContactsActivity extends Activity implements Serializable {

    private static final String TAG = "ContactsActivity";
    private static final int RESULT_CONTACT_DETAILS= 1;
    private static final int RESULT_NEW = 2;
    public static String DETAILS_TYPE_KEY="DetailType";
    public static String DETAILS_TYPE_EDIT="edit";
    public static String DETAILS_TYPE_NEW="new";

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
                startActivityForResult(intent, RESULT_CONTACT_DETAILS);
            }
        });

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("Confirm");
                dialogBuilder.setMessage("Do you want to edit " + ContactDatabase.contacts.get(position).getName() + " " + ContactDatabase.contacts.get(position).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                        intent.putExtra(DETAILS_TYPE_KEY,DETAILS_TYPE_EDIT);
                        intent.putExtra(Contact.contact_key, ContactDatabase.contacts.get(position));
                        startActivityForResult(intent, RESULT_CONTACT_DETAILS);
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
                dialogBuilder.setMessage("Do you want to delete " + ContactDatabase.contacts.get(position).getName() + " " + ContactDatabase.contacts.get(position).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        contactsAdapter.removeContact(position);
                        contactsAdapter.notifyDataSetChnged();
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

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_CONTACT_DETAILS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
               // contactsAdapter.updateContact(contact,position);
            }
        }
    }

    class ContactsAdapter extends BaseAdapter{
        private List<Contact> contactList;

        public ContactsAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        public void notifyDataSetChnged(){

        }

        @Override
        public int getCount() {
            return ContactDatabase.contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
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

