package com.gjs.opentable.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.gjs.opentable.Bean.PhotosBean;
import com.gjs.opentable.Bean.RestaurentBean;
import com.gjs.opentable.R;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/18/2016.
 */

public class Restaurentadapter extends ArrayAdapter<RestaurentBean> {
    Context cxt;
    int res;
    ArrayList<RestaurentBean> resturantList;


    public Restaurentadapter(Context context, int resource, ArrayList<RestaurentBean> objects) {
        super(context, resource, objects);

        cxt = context;
        res = resource;
        resturantList = objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(cxt).inflate(R.layout.listviewmain,parent,false);

        ImageView ivIcon = (ImageView)view.findViewById(R.id.imageView);
        TextView txtName = (TextView)view.findViewById(R.id.textViewRestaurantName);
        TextView txttype = (TextView)view.findViewById(R.id.textViewRestaurantopen);
        RatingBar rb=(RatingBar)view.findViewById(R.id.ratingBar);
        TextView txtaddress = (TextView)view.findViewById(R.id.textViewRestaurantaddress);
        RestaurentBean p = resturantList.get(position);
        p.toString();
        ivIcon.setImageResource(R.drawable.restaurant);
        try {
            PhotosBean p1 = p.photos.get(0);

          //  new DownloadImageTask(ivIcon).execute(p.getIcon());
            new DownloadImageTask(ivIcon).execute("https://maps.googleapis.com/maps/api/place/photo?maxwidth=70&photoreference=" + p1.getPhoto_reference() + "&key=AIzaSyDGtQbIzeOGRk0kGq3rCy3xAvo1xVNB304");
        }catch (Exception e){

        }
        txtName.setText(p.getName());
        rb.setRating(p.getRating().floatValue());
try {
    if (p.opening_hours.getOpen_now().equals("true")) {
        txttype.setText("Open");
    } else {
        txttype.setText("Closed");
    }
}catch (Exception e){
    txttype.setText("Info Not Present");

}
        txtaddress.setText(p.getVicinity());
        return view;
    }
    public void clearData() {
        // clear the data
        resturantList.clear();
    }

}




