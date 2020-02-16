package com.ir_sj.litelo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ir_sj.litelo.R;


public class MyAdapter extends ArrayAdapter {
    String[] names;

    String[] ans1;
    String[] ans2;
    Button but;
    AppCompatActivity context;
    public MyAdapter(AppCompatActivity context, String[] names, String[] ans1,String[] ans2)
    {
        super(context, R.layout.activity_survey,names);
        this.context = context;
        this.names=names;
        this.ans1=ans1;
        this.ans2=ans2;
        //this.lead= lead;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li=context.getLayoutInflater();
        View rowview = li.inflate(R.layout.activity_survey,null,true);
        TextView name = rowview.findViewById(R.id.ques);
        TextView anss=rowview.findViewById(R.id.radioButton);
        TextView ansss=rowview.findViewById(R.id.radioButton1);
        //ImageView image =  rowview.findViewById(R.id.images);
        //TextView leadText = rowview.findViewById(R.id.lead);
        RadioButton rb1 = (RadioButton)rowview.findViewById(R.id.radioButton);
       RadioButton rb2 = (RadioButton)rowview.findViewById(R.id.radioButton1);
        name.setText(names[position]);
        anss.setText(ans1[position]);
        ansss.setText(ans2[position]);
        but=(Button)rowview.findViewById(R.id.button5);

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// set Yes values in ArrayList if RadioButton is checked
                if (isChecked)
                    SurveyListView.count++;
            }
        });
// perf
        return rowview;
    }



    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}


