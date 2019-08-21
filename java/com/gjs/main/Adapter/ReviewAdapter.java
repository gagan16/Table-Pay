package com.gjs.opentable.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;


import com.gjs.opentable.Bean.ReviewBean;
import com.gjs.opentable.R;

import java.util.ArrayList;

/**
 * Created by Gagan on 12/7/2016.
 */

public class ReviewAdapter extends ArrayAdapter<ReviewBean> {

    Context cxt;
    int res;
    ArrayList<ReviewBean> reviewList;


    public ReviewAdapter(Context context, int resource, ArrayList<ReviewBean> objects) {
        super(context, resource, objects);

        cxt = context;
        res = resource;
        reviewList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        view = LayoutInflater.from(cxt).inflate(R.layout.listviewreview, parent, false);
        TextView txtauthorname = (TextView) view.findViewById(R.id.textViewreviewerName);
        RatingBar rt = (RatingBar) view.findViewById(R.id.ratingBar2);
        TextView text = (TextView) view.findViewById(R.id.textViewreviwertext);
        TextView time = (TextView) view.findViewById(R.id.textViewreviewtime);
        ReviewBean p = reviewList.get(position);
        txtauthorname.setText(p.getAuthor_name());
        rt.setRating(p.getRating().floatValue());
        time.setText(p.getTime());
        text.setText(p.getText());

        return view;

    }
}