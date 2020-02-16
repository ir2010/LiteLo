package com.ir_sj.litelo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SurveyListView extends AppCompatActivity {

    ListView listView;
    String[] names={"What songs would you prefer to listen to now?","More preferable vacation activity","Would you rather plan nightouts with your chums or stayback home","What about your the next day look?",
            "Lets say....Goa trip tomorrow?","What fits better?","Fault in our stars or Endgame?","According to your present situation","If you can pass a messgae to ypur younger self what would it be? ","" +
            "Is there anything standing between you and your happiness?"};
    // Integer[] images={R.drawable.billi,R.drawable.billi};
    //String[] lead={"Lead Developer"," "};
    String[] opt1={"Pop/Rock","Hiking","Nightouts","Anything but look awesome","Definitelyy","Zindagi Gulzar hai","Endgame","Ofcourse it does","Embrace every moment","Nothin..happy enough"};
    //String s="";
    String[] opt2={"Slow/Soothing","Meadow","Better stay home","Naah..not in the mood","Zinda hu yaar kaafi hai","Fault in our stars","Not anymore","Duniya badi zalim hai","Every damn thing"
    };
    int[] ans = {1,2};
    static int count=0;
    static String i="sad";
    RadioButton rb1, rb2;
    //RadioGroup rbg;
    Button but,butt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list_view);

        this.setTitle("Mood");
        listView = findViewById(R.id.survey_list);

        MyAdapter adap=new MyAdapter(this,names,opt1,opt2);
        listView.setAdapter(adap);

        rb1 = (RadioButton)findViewById(R.id.radioButton);
        rb2 = (RadioButton)findViewById(R.id.radioButton1);

        but=(Button)findViewById(R.id.bp);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rb1.isChecked())
                    count++;
            }
        });
        butt=(Button)findViewById(R.id.bp);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>5)
                {
                    i="happy";
                    // SendToFirebase();
                    startActivity(new Intent(SurveyListView.this, RegisterActivity.class));

                }
                else
                {
                    i="sad";
                    // SendToFirebase();
                    startActivity(new Intent(SurveyListView.this, RegisterActivity.class));
                }
            }

        } );
    }

    /* private void SendToFirebase()
    {
        StorageReference filePath = Postimagesreference.child("Post Images/"+current_user_id+"/"+postRandomName);

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {


                    Toast.makeText(SurveyListView.this,"Survey completed",Toast.LENGTH_SHORT).show();



                }
                else
                {
                    String message=task.getException().getMessage();
                    Toast.makeText(SendToFirebase().this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    } */

}


