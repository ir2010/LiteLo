package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    ImageButton fab;
    DatabaseReference ref, usersRef, groupsRef, userGroupRef, currentUserRef;
    private String saveCurrentDate, saveCurrentTime, mood, currentUserUid;
    RecyclerView chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //ref = FirebaseDatabase.getInstance().getReference("Groups");
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mood = RegisterActivity.mood;
        chatList = (RecyclerView)findViewById(R.id.all_user_chat_list);
        usersRef = FirebaseDatabase.getInstance().getReference("UserData").child("happy");
        groupsRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        if(mood.equals("happy"))
            currentUserRef = FirebaseDatabase.getInstance().getReference("UserData").child("happy");
        else
            currentUserRef = FirebaseDatabase.getInstance().getReference("UserData").child("sad");

        userGroupRef = currentUserRef.child("groups");


        /*usersRef.child("mood").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals("happy"))
                    mood = "happy";
                else
                    mood = "sad";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        displayChatViews();

        fab = (ImageButton) findViewById(R.id.fab);
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
                layout.addView(input);

                final EditText input2 = new EditText(context);
                input2.setHint("Enter your display name for the conversation");
                layout.addView(input2);

                alertDialog.setView(layout);
                alertDialog.setIcon(R.drawable.ic_chat_black_24dp);

                alertDialog.setPositiveButton("Proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Calendar calFordDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                                saveCurrentDate = currentDate.format(calFordDate.getTime());

                                Calendar calFordTime = Calendar.getInstance();
                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                saveCurrentTime = currentTime.format(calFordTime.getTime());

                                final String key = groupsRef.push().getKey();

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
                                groupsMap.put("users", "");
                                groupsMap.put("chats", "");

                                groupsRef.child(key).setValue(groupsMap)
                                        .addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    currentUserRef.child(currentUserUid).child("groups").setValue(key);
                                                    Query usersQuery = usersRef.orderByChild("priority").limitToFirst(2);

                                                    usersQuery.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference("UserData").child("happy").child(postSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        groupsRef.child(key).child("users").push().setValue(dataSnapshot.child("uid").getValue());
                                                                        String prio = dataSnapshot.child("priority").getValue().toString();
                                                                        int i = Integer.parseInt(prio);
                                                                        usersRef.child(dataSnapshot.child("uid").getValue().toString()).child("priority").setValue(i + 1);
                                                                        usersRef.child(dataSnapshot.child("uid").getValue().toString()).child("groups").setValue(key);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Toast.makeText(ChatActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    Toast.makeText(ChatActivity.this, "New circle is updated sucessfully!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ChatActivity.this, ChatActivity2.class);
                                                    intent.putExtra("group_name", input.getText().toString());
                                                    intent.putExtra("user_name", input2.getText().toString());
                                                    intent.putExtra("group_key", key);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(ChatActivity.this, "Error occured!", Toast.LENGTH_SHORT).show();
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

    public void displayChatViews() {
        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>().setQuery(userGroupRef, Chat.class).build();
        FirebaseRecyclerAdapter<Chat, ChatActivity.ChatsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatActivity.ChatsViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatActivity.ChatsViewHolder chatsViewHolder, int i, @NonNull final Chat chat) {
                chatsViewHolder.userName.setText(chat.getUserName());
                chatsViewHolder.chatTime.setText(chat.getChatTime());
                chatsViewHolder.chatDate.setText(chat.getChatDate());
                chatsViewHolder.chatTitle.setText(chat.getChatTitle());
            }

            @NonNull
            @Override
            public ChatActivity.ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_chatview_layout, parent, false);

                return new ChatActivity.ChatsViewHolder(view);
            }
        };
        chatList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

        public static class ChatsViewHolder extends RecyclerView.ViewHolder {
            TextView chatTitle, userName, chatTime, chatDate;
            int chatStatus;

            public ChatsViewHolder(@NonNull View view) {
                super(view);
                chatTitle = view.findViewById(R.id.groupname);
                userName = view.findViewById(R.id.user_name);
                chatTime = view.findViewById(R.id.time);
                chatDate = view.findViewById(R.id.date);
            }
        }
    }


