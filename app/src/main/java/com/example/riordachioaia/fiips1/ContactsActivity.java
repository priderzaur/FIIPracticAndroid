package com.example.riordachioaia.fiips1;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.riordachioaia.fiips1.persistance.Contact;
import com.example.riordachioaia.fiips1.persistance.DatabaseHelperFIIPRacticAndroid;
import com.example.riordachioaia.fiips1.service.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by riordachioaia on 14.03.2016.
 */
public class ContactsActivity extends AppCompatActivity implements Serializable {
//public class ContactsActivity extends Activity implements Serializable {

    private static final String TAG = "ContactsActivity";

    private static final int INTENT_CONTACT_DETAILS= 1;
    public static final String POSITION_KEY="position";
    public static final String ACTION_REFRESH = "action_refresh";

    private ListView contactListView;
    private DatabaseHelperFIIPRacticAndroid databaseHelper;
    private String email;
    private ContactsAdapter contactsAdapter;
    MyBroadcastReceiver receiver = new MyBroadcastReceiver();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                ((ContactsAdapter)contactListView.getAdapter()).getFilter().filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                ((ContactsAdapter)contactListView.getAdapter()).getFilter().filter(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ACTION_REFRESH);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        email = getIntent().getStringExtra(MainActivity.KEY_EMAIL);
        if (email == null) {
            email = "q@q.q";
        }

        databaseHelper = DatabaseHelperFIIPRacticAndroid.getInstance(this.getApplicationContext());
        //databaseHelper = new DatabaseHelperFIIPRacticAndroid(this);

        contactsAdapter = new ContactsAdapter(databaseHelper.getAllContacts(email));
        contactListView = (ListView) findViewById(R.id.contacts_list);
        contactListView.setAdapter(contactsAdapter);
        contactListView.setTextFilterEnabled(true);

        findViewById(R.id.btn_new_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                intent.putExtra(POSITION_KEY, -1);
                startActivityForResult(intent, INTENT_CONTACT_DETAILS);
            }
        });

        findViewById(R.id.downloadbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ContactsActivity.this, ImportService.class);
                String url = "http://students.info.uaic.ro/~andrei.corodescu/import.txt";
                intent1.putExtra("url", url);
                intent1.putExtra("email",email);
                startService(intent1);
            }
        });

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("Confirm");
                dialogBuilder.setMessage("Do you want to edit " + ((Contact) contactListView.getAdapter().getItem(position)).getName()
                        + " " + ((Contact) contactListView.getAdapter().getItem(position)).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ContactsActivity.this, ContactDetailsActivity.class);
                        intent.putExtra(Contact.contact_key, ((Contact) contactListView.getAdapter().getItem(position)));
                        intent.putExtra(POSITION_KEY, position);
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
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                contactListView.setItemChecked(position, !contactsAdapter.isPositionChecked(position));
                return false;
            }

            /*
            @Override //Delete contact + dialog confirm
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ContactsActivity.this);
                dialogBuilder.setTitle("Confirm");
                dialogBuilder.setMessage("Do you want to delete " + ((Contact) contactListView.getAdapter().getItem(position)).getName() +
                        " " + ((Contact) contactListView.getAdapter().getItem(position)).getSurname() + "?");
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteContact(((Contact) contactListView.getAdapter().getItem(position)));
                        contactsAdapter.removeContact(position);
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
            */
        });

        contactListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        contactListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    contactsAdapter.setNewSelection(position, checked);
                } else {
                    nr--;
                    contactsAdapter.removeSelection(position);
                }
                mode.setTitle(nr + " selected");
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                nr=0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item_delete:
                        nr = 0;
                        contactsAdapter.removeSelectedContacts();
                        contactsAdapter.clearSelection();
                        mode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                contactsAdapter.clearSelection();
            }
        });

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            contactListView.setFilterText(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == INTENT_CONTACT_DETAILS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Contact contact = (Contact) data.getSerializableExtra(Contact.contact_key);
                int contactPosition=data.getIntExtra(POSITION_KEY,-1);

                if (contactPosition == -1 ){
                    databaseHelper.addContact(contact, email);
                    ((ContactsAdapter)contactListView.getAdapter()).addContact(contact);
                } else {
                    databaseHelper.updateContact(contact);
                    ((ContactsAdapter)contactListView.getAdapter()).updateContact(contact, contactPosition);
                }
            }
        }
    }

    class ContactsAdapter extends BaseAdapter implements Filterable{
        private List<Contact> contactList, originalList;
        private HashMap<Integer, Boolean> mSelection = new HashMap<>();

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
            contactListItem.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this,android.R.color.background_dark)); //default color

            Contact contact = contactList.get(position);
            ((TextView)contactListItem.findViewById(R.id.name_tv)).setText(contact.getName());
            ((TextView)contactListItem.findViewById(R.id.surname_tv)).setText(contact.getSurname());
            ((TextView)contactListItem.findViewById(R.id.phoneNumber_tv)).setText(contact.getPhoneNumber());
            ((TextView)contactListItem.findViewById(R.id.group_tv)).setText(contact.getGroup());

            if (mSelection.get(position) != null) {
                contactListItem.setBackgroundColor(ContextCompat.getColor(ContactsActivity.this, android.R.color.holo_blue_light));// this is a selected position so make it red
            }

            return contactListItem;
        }

        public void removeSelectedContacts(){
            for (Integer position : getCurrentCheckedPosition()){
                databaseHelper.deleteContact(contactList.get(position));
                contactList.remove(position.intValue());
            }
            notifyDataSetChanged();
        }

        public void addContact(Contact contact) {
            contactList.add(contact);
            notifyDataSetChanged();
        }

        public void updateContact(Contact contact, int position){
            contactList.set(position, contact);
            notifyDataSetChanged();
        }

        public void refreshContacts(){
            contactList = databaseHelper.getAllContacts(email);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results=new FilterResults();

                    if (originalList == null)
                    {
                        originalList = new ArrayList<>(contactList); // saves the original data in mOriginalValues
                    }

                    if(constraint!=null && constraint.length()>0) {
                        List<Contact> filterList = new ArrayList<>();
                        for (Contact contact : originalList){
                            if (contact.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                    contact.getSurname().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                    contact.getGroup().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                    contact.getPhoneNumber().toUpperCase().contains(constraint.toString().toUpperCase())){
                                filterList.add(contact);
                            }
                        }
                        results.count=filterList.size();
                        results.values=filterList;
                    } else {
                        results.count=originalList.size();
                        results.values=originalList;
                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    contactList=(List<Contact>) results.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }

        public void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }

        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }

        public void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }

        public void clearSelection() {
            mSelection = new HashMap<Integer, Boolean>();
            notifyDataSetChanged();
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_REFRESH)){
                contactsAdapter.refreshContacts();
                contactsAdapter.notifyDataSetChanged();
            }
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

