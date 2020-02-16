package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity2 extends AppCompatActivity {
    int RESULT_LOAD_IMAGE = 1;
    Uri filePath;
    StorageReference sref;
    DatabaseReference ref, chatRef, groupRef;
    static String groupKey, groupName, userName;
    ImageView imgView;
    String uid;
    private FirebaseListAdapter<ChatMessage> adapter;
    ImageButton btn;
    String name, saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        groupKey = getIntent().getStringExtra("group_key");
        groupName = getIntent().getStringExtra("group_name");
        userName = getIntent().getStringExtra("user_name");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("UserData");
        groupRef = FirebaseDatabase.getInstance().getReference("Groups/"+groupKey);
        btn = (ImageButton)findViewById(R.id.send_button);

        sref = FirebaseStorage.getInstance().getReference("group_images");
        chatRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupKey).child("chats");
        imgView = findViewById(R.id.imgView);
        TextView nameView = (TextView)findViewById(R.id.group_name);
        TextView userNameView = (TextView)findViewById(R.id.username);
        nameView.setText(groupName);
        userNameView.setText("started by "+userName);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText msg = (EditText)findViewById(R.id.input);
                Toast.makeText(ChatActivity2.this, "Sent!", Toast.LENGTH_SHORT).show();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name=  dataSnapshot.child(uid).child("groups").child("username").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Calendar calFordDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(calFordDate.getTime());

                Calendar calFordTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                saveCurrentTime = currentTime.format(calFordTime.getTime());

                chatRef.push().setValue(new ChatMessage(msg.getText().toString(), name, saveCurrentTime, saveCurrentDate));
                msg.setText("");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.menu_info)
        {
            startActivity(new Intent(ChatActivity2.this, InfoActivity.class));
        }

        if(item.getItemId() == R.id.menu_leave)
        {
            ref.child(uid).child("groups").child(groupKey).removeValue();
            Toast.makeText(this, "Group left successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChatActivity2.this, ChatActivity.class));
            finish();
        }

        /*if(item.getItemId() == R.id.menu_change_bg)
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
        }*/
        return true;
    }

    private void displayChat()
    {
        ListView listOfMsgs = (ListView)findViewById(R.id.list_of_messages);
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>().setQuery(chatRef, ChatMessage.class).build();
        FirebaseListAdapter<ChatMessage> firebaseListAdapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView msgText = (TextView) v.findViewById(R.id.message_text);
                TextView msgUser = (TextView) v.findViewById(R.id.message_user);
                TextView msgTime = (TextView) v.findViewById(R.id.message_time);

                msgText.setText(model.getMessageText());
                msgUser.setText(model.getMessageUser());
            }
        };
        listOfMsgs.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && data.getData() != null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgView.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FirebaseDatabase.getInstance().getReference("Groups/"+groupKey).child("bg_image_url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(dataSnapshot.getValue().toString());
                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {              //delete old background image
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            sref.child(groupKey).child("bg_image").putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    Toast.makeText(ChatActivity2.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                    Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference("Groups/"+groupKey).child("bg_image_url").setValue(url);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(ChatActivity2.this, "Failed!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
        setImage();
    }

    private void setImage()
    {
        sref.child(groupKey).child("bg_image").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(imgView);

                    }
                });

    }
}
