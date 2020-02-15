package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton fab;
    DatabaseReference ref,usersRef;
    private String saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ref = FirebaseDatabase.getInstance().getReference("Groups");
        usersRef = FirebaseDatabase.getInstance().getReference("UserData");

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);
                alertDialog.setTitle("Start a conversation..");
                //alertDialog.setMessage("Enter New Circle Name");

                /*final EditText input = new EditText(ChatActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setHint("Enter New Circle Name");
                alertDialog.setView(input);

                final EditText input2 = new EditText(ChatActivity.this);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp2);
                input.setHint("Enter your Display Name for this conversation");
                alertDialog.setView(input2);*/
                Context context = view.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
                final EditText input = new EditText(context);
                input.setHint("Enter New Circle Name");
                layout.addView(input); // Notice this is an add method

// Add another TextView here for the "Description" label
                final EditText input2 = new EditText(context);
                input2.setHint("Enter your display name for the conversation");
                layout.addView(input2); // Another add method

                alertDialog.setView(layout); // Again this is a set method, not add
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

                                final String key = ref.push().getKey();

                                //ref.child(key).setValue(new Chat(input.getText().toString(),
                                       // FirebaseAuth.getInstance().getCurrentUser()
                                                //.getUid(), saveCurrentDate, saveCurrentTime));    //groups created

                                HashMap groupsMap = new HashMap();
                                groupsMap.put("chatTitle", input.getText().toString());
                                groupsMap.put("chatCreator", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                groupsMap.put("chatDate", saveCurrentDate);
                                groupsMap.put("chatTime", saveCurrentTime);
                                groupsMap.put("chatStatus", "1");
                                groupsMap.put("userName", input2.getText().toString());
                                groupsMap.put("users","");
                                groupsMap.put("chats","");
                                
                                ref.child(key).setValue(groupsMap)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task)
                                            {
                                                if(task.isSuccessful())
                                                {
                                                    Query usersQuery = usersRef.orderByChild("priority").limitToFirst(2);

                                                    usersQuery.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                          int i = 0;
                                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference("UserData").child(postSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        ref.child(key).child("users").push().setValue(dataSnapshot.child("uid").getValue());
                                                                        String prio = dataSnapshot.child("priority").getValue().toString();
                                                                        int i = Integer.parseInt(prio);
                                                                        HashMap groupsMap = new HashMap();
                                                                        groupsMap.put("chatDate", saveCurrentDate);


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                                i++;
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            // Getting Post failed, log a message
                                                            //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                                            Toast.makeText(ChatActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                                            // ...
                                                        }
                                                    });
                                                    Toast.makeText(ChatActivity.this,"New circle is updated sucessfully!",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ChatActivity.this, ChatActivity2.class);
                                                    intent.putExtra("group_name", input.getText());
                                                    intent.putExtra("user_name",input2.getText());
                                                    intent.putExtra("group_key", key);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Toast.makeText(ChatActivity.this,"Error occured!",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });



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

