package com.ir_sj.litelo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreatePasswordActivity extends AppCompatActivity {


    EditText e1,e2;
    Button b;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        e1= (EditText) findViewById(R.id.editText);
        e2= (EditText) findViewById(R.id.editText2);
        b=(Button) findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text1=e1.getText().toString();
                String text2=e2.getText().toString();

                if(text1.equals("") || text2.equals(""))
                {
                    Toast.makeText(CreatePasswordActivity.this,"No password entered",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    if(text1.equals(text2))
                    {
                        SharedPreferences settings= getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor= settings.edit();
                        editor.putString("password",text1);
                        editor.apply();

                        startActivity(new Intent(CreatePasswordActivity.this, FirstPage.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(CreatePasswordActivity.this,"Pass doesnt match",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


    }
}
