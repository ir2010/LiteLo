package com.ir_sj.litelo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton fab;
    DatabaseReference ref;
    private String saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ref = FirebaseDatabase.getInstance().getReference("Groups");

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);
                alertDialog.setTitle("Start a conversation..");
                alertDialog.setMessage("Enter New Circle Name");

                final EditText input = new EditText(ChatActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.ic_chat_black_24dp);

                alertDialog.setPositiveButton("Proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Calendar calFordDate= Calendar.getInstance();
                                SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
                                saveCurrentDate= currentDate.format(calFordDate.getTime());

                                Calendar calFordTime= Calendar.getInstance();
                                SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
                                saveCurrentTime= currentTime.format(calFordTime.getTime());

                                String key = ref.push().getKey();

                                ref.child(key).setValue(new Chat(input.getText().toString(),
                                        FirebaseAuth.getInstance().getCurrentUser()
                                                .getUid(), saveCurrentDate, saveCurrentTime));

                                Intent intent = new Intent(ChatActivity.this, ChatActivity2.class);
                                intent.putExtra("group_name", input.getText());
                                intent.putExtra("group_key", key);
                                startActivity(intent);
                            }
                        });

                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });


            }



    }

