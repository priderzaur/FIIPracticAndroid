package com.example.riordachioaia.fiips1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riordachioaia.fiips1.persistance.DatabaseHelperFIIPRacticAndroid;
import com.example.riordachioaia.fiips1.util.Utils;

/**
 * Created by riordachioaia on 12.03.2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private DatabaseHelperFIIPRacticAndroid database;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_login_file_key), Context.MODE_PRIVATE);

        database = DatabaseHelperFIIPRacticAndroid.getInstance(this.getApplicationContext());// = new DatabaseHelperFIIPRacticAndroid(getApplicationContext());//

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Register btn onClick");
                String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_pass)).getText().toString();
                String age = ((EditText) findViewById(R.id.et_age)).getText().toString();
                Log.d(TAG, "email = " + email + " ; password = " + password + " ; age = " + age);
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(age)) {
                    Toast.makeText(RegisterActivity.this, "All fields are mandatory", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidEmail(email)){
                    Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_LONG).show();
                } else if (database.emailExists(email)) {
                    Toast.makeText(RegisterActivity.this, "Email already used", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidNewPassword(password)){
                    Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidAge(Integer.parseInt(age))){
                    Toast.makeText(RegisterActivity.this, "Age must be at least 12", Toast.LENGTH_LONG).show();
                }else {
                    database.addUser(email, password);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", email);
                    editor.putString("password", password);
                    editor.putString("saved", "yes");
                    editor.putString("logged", "yes");
                    editor.commit();

                    Toast.makeText(RegisterActivity.this, "Registration successfull. Welcome!", Toast.LENGTH_LONG).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.KEY_EMAIL, email);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
