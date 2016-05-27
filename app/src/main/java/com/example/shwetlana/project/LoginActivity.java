package com.example.shwetlana.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterLink= (TextView) findViewById(R.id.tvRegisterHere);

        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //chek if username n pwd is admin

                Intent homeMapIntent = new Intent(LoginActivity.this, HomeMapsActivity.class);
                LoginActivity.this.startActivity(homeMapIntent);

            }


        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }

        });

    }
}
