package com.example.riordachioaia.fiips1.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by riordachioaia on 31.03.2016.
 */
public class DatabaseHelperFIIPRacticAndroid extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelperFIIPRacticAndroid instance;

    public static final String TABLE_NAME_CONTACTS = "contacts";
    public static final String TABLE_NAME_CREDENTIALS = "credentials";

    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";

    public static final String COLUMN_CONTACT_ID = "id";
    public static final String COLUMN_CONTACT_NAME = "name";
    public static final String COLUMN_CONTACT_SURNAME = "surname";
    public static final String COLUMN_CONTACT_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_CONTACT_GROUP = "contact_group";
    public static final String COLUMN_CONTACT_EMAIL_REF = "email_owner";

    public static final String DATABASE_NAME = "fiipracticandroid";

    public static synchronized DatabaseHelperFIIPRacticAndroid getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "instance null");
            instance = new DatabaseHelperFIIPRacticAndroid(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelperFIIPRacticAndroid(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CREDENTIALS +
                " ( " + COLUMN_USER_EMAIL + " TEXT PRIMARY KEY" +
                ", " + COLUMN_USER_PASSWORD + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS +
                " ( " + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", " + COLUMN_CONTACT_NAME + " TEXT, " + COLUMN_CONTACT_SURNAME + " TEXT, " +
                COLUMN_CONTACT_PHONE_NUMBER + " TEXT, " + COLUMN_CONTACT_GROUP + " TEXT, " +
                COLUMN_CONTACT_EMAIL_REF + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_CONTACT_EMAIL_REF +") REFERENCES "
                + TABLE_NAME_CREDENTIALS + "(" + COLUMN_USER_EMAIL + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"on upgrade");
        if (oldVersion != newVersion) {
            Log.d(TAG,"intra in if version diferit");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CREDENTIALS);
        }
        onCreate(db);
    }


    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            //db.execSQL("DELETE FROM " + TABLE_NAME_CONTACTS + " WHERE id='" + contact.getId() + "'");
            db.delete(TABLE_NAME_CONTACTS,COLUMN_CONTACT_ID + "=" + contact.getId(), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete " + contact.getName() + " " + contact.getSurname());
        } finally {
            db.endTransaction();
        }
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CONTACT_NAME, contact.getName());
            values.put(COLUMN_CONTACT_SURNAME, contact.getSurname());
            values.put(COLUMN_CONTACT_PHONE_NUMBER, contact.getPhoneNumber());
            values.put(COLUMN_CONTACT_GROUP, contact.getGroup());

            int test = db.update(TABLE_NAME_CONTACTS, values,
                    COLUMN_CONTACT_ID + "=?", new String[]{contact.getId() + ""});
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to update " + contact.getName() + " " + contact.getSurname());
        } finally {
            db.endTransaction();
        }
    }



    public void addContact(Contact contact, String email) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CONTACT_NAME, contact.getName());
            values.put(COLUMN_CONTACT_SURNAME, contact.getSurname());
            values.put(COLUMN_CONTACT_PHONE_NUMBER, contact.getPhoneNumber());
            values.put(COLUMN_CONTACT_GROUP, contact.getGroup());
            values.put(COLUMN_CONTACT_EMAIL_REF, email);

            contact.setId((int) db.insert(TABLE_NAME_CONTACTS, null, values));
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Error while trying to add " + contact.getName() + " " + contact.getSurname());
        } finally {
            db.endTransaction();
        }
    }

    public List<Contact> getAllContacts(String email) {

        SQLiteDatabase db = getReadableDatabase();

        List<Contact> contactList = new ArrayList<>();

        Cursor c = db.query(TABLE_NAME_CONTACTS, null, COLUMN_CONTACT_EMAIL_REF+"='"+email+"'", null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(COLUMN_CONTACT_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_CONTACT_NAME));
                String surname = c.getString(c.getColumnIndex(COLUMN_CONTACT_SURNAME));
                String phoneNumber = c.getString(c.getColumnIndex(COLUMN_CONTACT_PHONE_NUMBER));
                String group = c.getString(c.getColumnIndex(COLUMN_CONTACT_GROUP));

                Contact contact = new Contact(id, name, surname, phoneNumber, group);

                contactList.add(contact);
            }
        }

        if (c != null) {
            c.close();
        }
        return contactList;
    }

    public void addUser(String email, String password) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        db.insert(TABLE_NAME_CREDENTIALS, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e){
            Log.d(TAG, "Error while trying to add user " + email);
        } finally {
            db.endTransaction();
        }
    }

    public boolean emailExists(String email){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE_NAME_CREDENTIALS, null, null, null, null, null, null);//new String[]{COLUMN_USER_EMAIL}

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                if (email.equals(c.getString(c.getColumnIndex(COLUMN_USER_EMAIL)))){
                    return true;
                }
            }
        }
        if (c != null) {
            c.close();
        }
        return false;
    }

    public boolean areCredentialsCorrect(String email, String password){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(TABLE_NAME_CREDENTIALS, null, null, null, null, null, null);//new String[]{COLUMN_USER_EMAIL}

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                if (email.equals(c.getString(c.getColumnIndex(COLUMN_USER_EMAIL))) &&
                        password.equals(c.getString(c.getColumnIndex(COLUMN_USER_PASSWORD)))){
                    return true;
                }
            }
        }
        if (c != null) {
            c.close();
        }
        return false;
    }
}
