package com.gjs.opentable.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.gjs.opentable.Bean.TableBean;
import com.gjs.opentable.R;
import com.gjs.opentable.Util;

import java.util.ArrayList;

/**
 * Created by Gagan on 11/25/2016.
 */

public class BillAdapter extends ArrayAdapter<TableBean> {
    Context cxt;
    int res;
    ArrayList<TableBean> tableList;
    int total,price;


    public BillAdapter(Context context, int resource, ArrayList<TableBean> objects) {
        super(context, resource, objects);

        cxt = context;
        res = resource;
       tableList = objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        view = LayoutInflater.from(cxt).inflate(R.layout.listviewtable,parent,false);

        TextView txtSno = (TextView)view.findViewById(R.id.textViewsno);
        TextView txtItem = (TextView)view.findViewById(R.id.textViewiten);
        TextView txtquantity = (TextView)view.findViewById(R.id.textViewquantity);

        TextView txtprice = (TextView)view.findViewById(R.id.textViewprice);
       // TextView txttotal=(TextView)view.findViewById(R.id.textViewtotal) ;
        TableBean p = tableList.get(position);
        p.toString();

        txtSno.setText(String.valueOf(p.getSno_id()));
        txtItem.setText(String.valueOf(p.getItem()));
        txtquantity.setText(String.valueOf(p.getQuantity()));
        txtprice.setText(String.valueOf(p.getPrice()));
       Util.total=Util.total+p.getPrice();

       Util.quantity=Util.quantity+p.getQuantity();

        return view;
    }

}
