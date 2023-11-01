
package com.appbyabhi.practicequiz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class AdminLoginActivity extends AppCompatActivity {
    ImageButton back, login;
    TextInputLayout ti_username, ti_password;
    EditText et_username, et_password;
    String username, password;
    SQLiteDatabase d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        back = findViewById(R.id.backBtn_adminLogin);
        login = findViewById(R.id.adminLoginBtn);
        ti_username = findViewById(R.id.textInput_username_admin);
        ti_password = findViewById(R.id.textInput_password_admin);
        et_username = findViewById(R.id.username_admin);
        et_password = findViewById(R.id.password_admin);
        d = openOrCreateDatabase("quiz_db",MODE_PRIVATE,null);
        d.execSQL("create table if not exists admin (adminid int primary key,admin_name varchar(20),password varchar(20))");
        d.execSQL("replace into admin values(1,'admin','1234')");

        et_username.addTextChangedListener(createTextWatcher());
        et_password.addTextChangedListener(createTextWatcher());

        back.setOnClickListener(v -> finish());

        login.setOnClickListener(v -> {
            username = et_username.getText().toString();
            password = et_password.getText().toString();
            doLogin();
            finish();
        });
    }

    public void doLogin(){
        if(checkValid()){
            Cursor c = d.rawQuery("select * from admin where admin_name = '" + username + "' ",null);
            int found=0;
            while(c.moveToNext())
            {
                found=1;
                String db_pass = c.getString(2);
                if (db_pass.equals(password))
                {
                    Intent i = new Intent(AdminLoginActivity.this, AdminOperationActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
            if (found==0)
            {
                Toast.makeText(AdminLoginActivity.this, "Incorrect Username", Toast.LENGTH_SHORT).show();
            }
            c.close();
        }
    }
    public boolean checkValid() {
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        if(username.isEmpty()) {
            ti_username.setError("Username is required");
            return false;
        }

        if(password.isEmpty()) {
            ti_password.setError("Password can't be empty");
            return false;
        }
        ti_username.setError(null);
        ti_password.setError(null);
        return true;
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int
                    i, int i1, int i2) {
                checkValid();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
}