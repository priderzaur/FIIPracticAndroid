package com.example.riordachioaia.fiips1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.riordachioaia.fiips1.persistance.CredentialDatabase;
import com.example.riordachioaia.fiips1.util.Utils;

/**
 * Created by riordachioaia on 12.03.2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                } else if (CredentialDatabase.verifyExistingCredentials(email, password)) {
                    Toast.makeText(RegisterActivity.this, "Email already used", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidNewPassword(password)){
                    Toast.makeText(RegisterActivity.this, "Password must have at least 6 characters", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidAge(Integer.parseInt(age))){
                    Toast.makeText(RegisterActivity.this, "Age must be at least 12", Toast.LENGTH_LONG).show();
                }else {
                    CredentialDatabase.credentials.put(email,password);
                    Toast.makeText(RegisterActivity.this, "Registration successfull. Welcome!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
