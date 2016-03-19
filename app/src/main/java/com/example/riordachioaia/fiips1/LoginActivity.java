package com.example.riordachioaia.fiips1;

import android.content.Context;
import android.content.SharedPreferences;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_login_file_key), Context.MODE_PRIVATE);

        if (sharedPreferences.getString("logged", "").equals("logged")){
            String email = sharedPreferences.getString("username","");
            String password = sharedPreferences.getString("password", "");
            ((EditText)findViewById(R.id.et_email)).setText(email);
            ((EditText)findViewById(R.id.et_pass)).setText(password);
            Toast.makeText(LoginActivity.this, "Last good credentials loaded", Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_reset_creds).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Log.d(TAG, "Login btn onClick");
                String email = ((EditText) findViewById(R.id.et_email)).getText().toString();
                String password = ((EditText) findViewById(R.id.et_pass)).getText().toString();
                Log.d(TAG, "email = " + email + " ; password = " + password);
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Email or password empty", Toast.LENGTH_LONG).show();
                } else if (!Utils.isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "Invalid email format", Toast.LENGTH_LONG).show();
                } else if (!CredentialDatabase.verifyExistingCredentials(email, password)) {
                    Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                } else {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", email);
                    editor.putString("password", password);
                    editor.putString("logged", "logged");
                    editor.commit();

                    Toast.makeText(LoginActivity.this, "Login successfull", Toast.LENGTH_LONG).show();

                    setResult(RESULT_OK);
                    finish();
                }

                 /*{
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (LoginActivity.this);
                    dialogBuilder.setTitle(getString(R.string.app_name));
                    dialogBuilder.setMessage(getResources().getString(R.string.login_success));
                    dialogBuilder.setPositiveButton("Ok",null);
                    dialogBuilder.show();

                }*/
                break;
            case R.id.btn_reset_creds:
                ((EditText) findViewById(R.id.et_email)).setText("");
                ((EditText) findViewById(R.id.et_pass)).setText("");
                Toast.makeText(LoginActivity.this, "Credentials reset", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                break;
        }
    }

}
