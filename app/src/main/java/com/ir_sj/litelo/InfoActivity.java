package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoActivity extends AppCompatActivity {
    ListView list;
    String uid;
    DatabaseReference userRef, currentUserRef, groupRef;
    String mood;
    String one, two, three, groupKey;
    TextView text1, text2, text3, text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String names[]={};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //list = (ListView)findViewById(R.id.list);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("UserData");
        groupRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupKey);
        mood = RegisterActivity.mood;
        groupKey = ChatActivity2.groupKey;
        text1 = (TextView)findViewById(R.id.textView4);
        text2 = (TextView)findViewById(R.id.textView3);
        text3 = (TextView)findViewById(R.id.textView6);
        text4 = (TextView)findViewById(R.id.textView5);

        if(mood.equals("happy"))
            currentUserRef = FirebaseDatabase.getInstance().getReference("UserData").child("happy");
        else
            currentUserRef = FirebaseDatabase.getInstance().getReference("UserData").child("sad");

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                one = dataSnapshot.child(uid).child("name").getValue().toString();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        text1.setText(ChatActivity2.groupName);
        text2.setText(one);
        text3.setText("friend1");
        text4.setText("friend2");
    }

    public void remove1(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setMessage("Do you want to remove friend1 ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        groupRef.child("users").child("friend1").removeValue();
                        Toast.makeText(InfoActivity.this, "Removed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Remove Friend?");
        alert.show();

    }

    public void remove2(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
        builder.setMessage("Do you want to remove friend2 ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        groupRef.child("users").child("friend2").removeValue();
                        Toast.makeText(InfoActivity.this, "Removed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Remove Friend?");
        alert.show();
    }
}
