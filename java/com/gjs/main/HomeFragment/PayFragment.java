package com.gjs.opentable.HomeFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gjs.opentable.QrActivity;
import com.gjs.opentable.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment {

    Button btnPay;

    public PayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pay, container, false);
            btnPay=(Button)view.findViewById(R.id.buttonqr);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),QrActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
