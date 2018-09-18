package com.example.android.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ReportAdapter extends ArrayAdapter<Report> {
    int idNoImage = R.drawable.no_image;

    public ReportAdapter(@NonNull Context context, @NonNull List<Report> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get the view is created before in rootView
        View rootView = convertView;

        //check if is null
        if (rootView == null) {
            //set view by my item list view
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_view_news, parent, false);
        }
        //assocate with xml by id
        TextView title = rootView.findViewById(R.id.title_news_text_view);
        TextView nameAuthor = rootView.findViewById(R.id.author_name_text_view);
        TextView date = rootView.findViewById(R.id.date_text_view);
        TextView clock = rootView.findViewById(R.id.clock_text_view);
        TextView section = rootView.findViewById(R.id.section_text_view);
        ImageView imageView = rootView.findViewById(R.id.image_view_web);

        //get the data is selcted by user
        Report currentReport = getItem(position);

        //set text in textview
        title.setText(currentReport.getTitle());
        nameAuthor.setText(currentReport.getNameAuthor());
        date.setText(currentReport.getDate());
        clock.setText(currentReport.getClock());
        section.setText(currentReport.getSection());
        Bitmap bitmap = currentReport.getmImageView();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(idNoImage);
        }


        return rootView;
    }
}
