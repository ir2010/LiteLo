package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
//import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    int REGISTER_CODE = 1;
    private ImageButton AddNewPostButton;

    private Button CommentPostButton;
    TextView userName;
    CircularImageView imgview;
    //FirebaseUser user;
    DatabaseReference ref,PostsRef;
    StorageReference sref_profile_image, sref_post_image;
    String uid;
    String PostKey;
    private RecyclerView postList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //CommentPostButton = (Button) findViewById(R.id.post_comment_bin);

        /*CommentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        PostsRef=FirebaseDatabase.getInstance().getReference().child("Posts");

        /*AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //AddNewPostButton = (ImageButton) findViewById(R.id.add_new_post_button);

        /*AddNewPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendUserToPostactivity();
            }
        });*/

        postList= (RecyclerView)findViewById(R.id.all_user_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager.setReverseLayout(true);
        LinearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(LinearLayoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayAllUsersPosts();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        break;

                    case R.id.jam:
                        SendUserToPostactivity();
                        break;

                    case R.id.talk:
                        startActivity(new Intent(MainActivity.this, ChatActivity.class));

                        break;

                    case R.id.notifications:
                        break;

                }
                return true;
            }
        });

        userName = (TextView)findViewById(R.id.userName);
        imgview = (CircularImageView)findViewById(R.id.image);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);


        //user = FirebaseAuth.getInstance().getCurrentUser();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("UserData/"+uid);
        sref_profile_image = FirebaseStorage.getInstance().getReference("profile_images/");
        sref_post_image = FirebaseStorage.getInstance().getReference("Post Images/");
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();

        ref.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                //Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
                userName.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ref.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                // imgview.setImageURI(Uri.parse(value));
                sref_profile_image.child(uid).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL
                                //Glide.with(getApplicationContext()).load(uri).into(imgview);

                                //Picasso.get().load(uri).into(imgview);
                                Picasso.get().load(uri).fit().centerCrop().into(imgview);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void DisplayAllUsersPosts()
    {
        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(PostsRef, Posts.class).build();
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostsViewHolder postsViewHolder, int i, @NonNull final Posts posts) {
                postsViewHolder.userName.setText(posts.getUsername());
                postsViewHolder.time.setText(posts.getTime());
                postsViewHolder.date.setText(posts.getDate());
                postsViewHolder.description.setText(posts.getDescription());

                postsViewHolder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostKey = posts.getUid()+posts.getDate()+posts.getTime();
                        //Intent commentsIntent = new Intent(MainActivity.this, CommentActivity.class);
                       // commentsIntent.putExtra("PostKey", PostKey);
                        //startActivity(commentsIntent);
                    }
                });

                sref_post_image.child(posts.getUid()).child(posts.getDate()+posts.getTime()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(uri == null)
                        {
                            postsViewHolder.postImage.setVisibility(View.GONE);
                        }
                        else
                        {Picasso.get().load(uri).fit().centerCrop().into(postsViewHolder.postImage);}
                    }
                });

                sref_profile_image.child(posts.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().centerCrop().into(postsViewHolder.dp);
                    }
                });
            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_posts_layout, parent, false);

                return new PostsViewHolder(view);
            }
        };
        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, time, date, description;
        ImageView postImage;
        CircleImageView dp;
        Button like, comment;

        public PostsViewHolder(@NonNull View view)
        {
            super(view);
            userName=view.findViewById(R.id.post_user_name);
            time=view.findViewById(R.id.post_time);
            date=view.findViewById(R.id.post_date);
            description=view.findViewById(R.id.post_description);
            postImage=view.findViewById(R.id.post_image);
            dp=view.findViewById(R.id.post_profile_image);
            like=view.findViewById(R.id.like);
            comment=view.findViewById(R.id.comment);
        }
    }

    private void SendUserToPostactivity()
    {
        Intent addNewPostIntent= new Intent(MainActivity.this, PostActivity.class);
        startActivity(addNewPostIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Do you want to sign out ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(MainActivity.this, "Signed Out!", Toast.LENGTH_SHORT).show();
                                            Intent signedout = new Intent(MainActivity.this, Splashscreen.class);
                                            startActivity(signedout);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.setTitle("Sign Out?");
                            alert.show();
                        }
                    });

        }

        if(item.getItemId() == R.id.menu_change_mode)
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "Successfully signed in!", Toast.LENGTH_SHORT).show();
                //displayChat();
            }
            else
            {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}