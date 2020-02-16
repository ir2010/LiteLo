package com.ir_sj.litelo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ir_sj.litelo.R;


public class MyAdapter extends ArrayAdapter {
    String[] names;

    String[] ans1;
    String[] ans2;
    AppCompatActivity context;
    public MyAdapter(AppCompatActivity context, String[] names, String[] ans1,String[] ans2)
    {
        super(context, R.layout.activity_survey_list_view,names);
        this.names=names;
        this.ans1=ans1;
        this.ans2=ans2;
        //this.lead= lead;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li=context.getLayoutInflater();
        View rowview = li.inflate(R.layout.activity_survey_list_view,null,true);
        TextView name = rowview.findViewById(R.id.ques);
        TextView anss=rowview.findViewById(R.id.radioButton);
        TextView ansss=rowview.findViewById(R.id.radioButton1);
        //ImageView image =  rowview.findViewById(R.id.images);
        //TextView leadText = rowview.findViewById(R.id.lead);
        name.setText(names[position]);
        anss.setText(ans1[position]);
        ansss.setText(ans2[position]);
        //image.setImageResource(images[position]);
        //leadText.setText(lead[position]);
        return rowview;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}


