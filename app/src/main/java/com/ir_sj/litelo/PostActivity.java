package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private ImageButton selectPostImage;
    private Button updatePostButton;
    private ProgressDialog loadingBar;
    private EditText Postdescription;
    private static final int Gallery_Pick=1;
    private Uri ImageUri;
    private String Description;
    private StorageReference Postimagesreference;
    private DatabaseReference UsersRef,PostsRef;
    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadurl, current_user_id;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postactivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        mAuth= FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();

        Postimagesreference= FirebaseStorage.getInstance().getReference();
        UsersRef= FirebaseDatabase.getInstance().getReference("UserData");
        PostsRef= FirebaseDatabase.getInstance().getReference("Posts");

        selectPostImage =(ImageButton) findViewById(R.id.select_post_image);
        updatePostButton =(Button) findViewById(R.id.update_button);
        Postdescription=(EditText) findViewById(R.id.post_description);
        loadingBar=new ProgressDialog(this);

        selectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        updatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validatepostinfo();
            }
        });
    }



    private void Validatepostinfo()
    {
        Description=Postdescription.getText().toString();
        if(ImageUri==null && TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Both fields empty",Toast.LENGTH_SHORT).show();
        }
        /*else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Kindly insert a caption",Toast.LENGTH_SHORT).show();
        }*/
        else
        {
            loadingBar.setTitle("Adding new post..");
            loadingBar.setMessage("Updating....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            StoringImagetoFirebaseStorage();
        }
    }

    private void StoringImagetoFirebaseStorage()
    {
        Calendar calFordDate= Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate= currentDate.format(calFordDate.getTime());

        Calendar calFordTime= Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        saveCurrentTime= currentTime.format(calFordTime.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        //StorageReference filePath = Postimagesreference.child("Post Images").child(ImageUri.getLastPathSegment()+ postRandomName + ".jpg");
        StorageReference filePath = Postimagesreference.child("Post Images/"+current_user_id+"/"+postRandomName);

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {

                    downloadurl= task.getResult().getStorage().getDownloadUrl().toString();
                    Toast.makeText(PostActivity.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                    SavingPostInformationToDatabase();

                }
                else
                {
                    String message=task.getException().getMessage();
                    Toast.makeText(PostActivity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void SavingPostInformationToDatabase()
    {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String UserFullName=dataSnapshot.child("name").getValue(String.class);
                    String UserProfileImage=dataSnapshot.child("image").getValue(String.class);

                    HashMap postsMap=new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("postimage", downloadurl);
                    postsMap.put("dp", UserProfileImage);
                    postsMap.put("username", UserFullName);
                    PostsRef.child(current_user_id+postRandomName).setValue(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(PostActivity.this,"New post is updated sucessfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        SendUserToMainActivity();
                                    }
                                    else
                                    {
                                        Toast.makeText(PostActivity.this,"Error occured",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }

                            });

                }
                else
                {
                    Toast.makeText(PostActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity()
    {
        Intent i =  new Intent(PostActivity.this, MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            selectPostImage.setImageURI(ImageUri);
        }
    }

    private void OpenGallery() {
        Intent galleryIntent= new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);
    }
}

