package com.example.anna.myproject.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;

import com.example.anna.myproject.buy.FashionCategoriesActivity;
import com.example.anna.myproject.buy.MobileCategoriesActivity;
import com.example.anna.myproject.R;
import com.example.anna.myproject.buy.TabletCategoriesActivity;
import com.example.anna.myproject.sell.AdFormActivity;


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
                ImageButton mobileCateg= (ImageButton)rootView.findViewById(R.id.mobile_button);
                ImageButton tabletCateg= (ImageButton)rootView.findViewById(R.id.tablet_button);
                ImageButton fashionCateg= (ImageButton)rootView.findViewById(R.id.fashion_button);

                mobileCateg.setOnClickListener(this);
                tabletCateg.setOnClickListener(this);
                fashionCateg.setOnClickListener(this);

                 return rootView;
            case 2:
                 rootView = inflater.inflate(R.layout.sell, container, false);
                Button adPost = (Button)rootView.findViewById(R.id.button_post_ad);
                adPost.setOnClickListener(this);
                return rootView;
            case 3:
                 rootView = inflater.inflate(R.layout.rent, container, false);
                return rootView;
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {

               switch (v.getId())
               {

                   case R.id.mobile_button :
                   {
                       Intent intent = new Intent(getActivity(), MobileCategoriesActivity.class);
                       startActivity(intent);
                   }
                   case R.id.tablet_button :
                   {
                       Intent intent = new Intent(getActivity(), TabletCategoriesActivity.class);
                       startActivity(intent);
                   }
                   case R.id.fashion_button :
                   {
                       Intent intent = new Intent(getActivity(), FashionCategoriesActivity.class);
                       startActivity(intent);
                   }
                   case R.id.button_post_ad:
                   {
                       Intent intent = new Intent(getActivity(), AdFormActivity.class);
                       startActivity(intent);
                   }




               }

    }
}
