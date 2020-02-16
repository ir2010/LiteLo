package com.ir_sj.litelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPassword extends AppCompatActivity {

    EditText e1,e2;
    Button b;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);


        SharedPreferences settings=getSharedPreferences("PREFS",0);
        password= settings.getString("password","");

        e2= (EditText) findViewById(R.id.editText2);
        b=(Button) findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text2=e2.getText().toString();

                if( text2.equals(""))
                {
                    Toast.makeText(EnterPassword.this,"No password entered",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(text2.equals(password))
                    {

                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(EnterPassword.this,"Pass doesnt match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } );

    }
}
