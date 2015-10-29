package com.example.anna.myproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;


/**
 * Created by Anna on 28-10-2015.
 */
public class CategoriesDetailsFragment extends Fragment implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout resource that'll be returned
         View rootView = inflater.inflate(R.layout.buy, container, false);


        // Get the arguments that was supplied  by the getItem()
        // when the fragment was instantiated in the CustomPagerAdapter
        Bundle args = getArguments();

        int pageNo = args.getInt("page_position");

        switch (pageNo) {

            case 1:
                 rootView = inflater.inflate(R.layout.buy, container, false);
                 return rootView;
            case 2:
                 rootView = inflater.inflate(R.layout.sell, container, false);
                return rootView;
            case 3:
                 rootView = inflater.inflate(R.layout.rent, container, false);
                return rootView;
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {

                /*


                 */


    }
}
