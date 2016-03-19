package com.example.riordachioaia.fiips1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riordachioaia.fiips1.persistance.Contact;

//modificare element din lista?, cu click, activity nou!//dialog la submit
//adapter notifyDataSetChanged()
// la long click, dialog care sa spuna "are you sure you want to delete?"


//buton add la fundul listei
//adresa sub telefon sau altceva, nu neaparat adresa
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOGIN = 1;
    private static String TAG = "MainActiviy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_login_file_key), Context.MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "").equals("yes")){
            Intent intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch(v.getId()){
            case R.id.btn_login:
                Log.d(TAG,"Login onclick");
                intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, RESULT_LOGIN);
                break;
            case R.id.btn_register:
                Log.d(TAG,"Register onclick");
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_LOGIN) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, ContactsActivity.class);
                //Contact contact = new Contact ("nou", "nou", "123");
               // intent.putExtra(Contact.contact_key,contact);
                startActivity(intent);
                finish();
            }
        }
    }
}
