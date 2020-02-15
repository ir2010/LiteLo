package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity
{
    EditText name;
    Button chooseImage, start;
    CircularImageView dp;
    String dp_name, uid;
    Uri img;
    int RESULT_LOAD_IMAGE=1;
    //FirebaseUser user;
    DatabaseReference ref, ref1;
    StorageReference sref;
    String downloadUri;
    LottieAnimationView anim;
    static String mood="happy";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //this.getSupportActionBar().hide();    //to hide the title bar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);


        //anim.setAnimation("register.json");
        //anim.loop(true);
        //anim.playAnimation();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //mood = getIntent().getStringExtra("mood");

        name = (EditText)findViewById(R.id.name);
        chooseImage = (Button)findViewById(R.id.choose_image);
        start = (Button)findViewById(R.id.start);
        dp = (CircularImageView)findViewById(R.id.imageView);
        anim = (LottieAnimationView)findViewById(R.id.anim);
        anim.setVisibility(View.INVISIBLE);

        //user = FirebaseAuth.getInstance().getCurrentUser();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ref = FirebaseDatabase.getInstance().getReference("UserData");
        sref = FirebaseStorage.getInstance().getReference().child("profile_images/" + uid);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String dname = name.getText().toString();
                if(dname.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Please add a display name!", Toast.LENGTH_SHORT).show();
                }
                else{

                    HashMap<Object, String> hashMap = new HashMap<>();

                    hashMap.put("uid", uid);
                    hashMap.put("name", dname);
                    hashMap.put("image", downloadUri);
                    hashMap.put("priority", "0");
                    hashMap.put("mood", mood);

                    if(mood.equals("happy"))
                    {
                        ref1 = ref.child("happy");
                    }
                    else
                    {
                        ref1 = ref.child("sad");
                    }

                    ref1.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        /*Slide slide = new Slide();
                        slide.setSlideEdge(Gravity.TOP);

                        ViewGroup root = (ViewGroup)findViewById(R.id.anim);
                        TransitionManager.beginDelayedTransition(root, slide);
                        start.setVisibility(View.INVISIBLE);
                        //anim.setVisibility(View.VISIBLE);
                        /*try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                            TextView txt = (TextView)findViewById(R.id.textView3);
                            txt.setVisibility(View.INVISIBLE);
                            start.setVisibility(View.INVISIBLE);
                            anim.setVisibility(View.VISIBLE);
                            Handler handle=new Handler();
                            handle.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            },2000);
                        }
                    });
                }}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data && data.getData() != null)
        {
            img = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), img);
                dp.setImageBitmap(bitmap);
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
        if(img != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            /*sref_profile_image.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                    downloadUri = task
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Failed!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });*/
            sref.putFile(img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                        downloadUri = task.getResult().getStorage().getDownloadUrl().toString();

                    }
                }
            });
        }
    }
}