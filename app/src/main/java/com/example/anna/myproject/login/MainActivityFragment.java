package com.example.anna.myproject.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.anna.myproject.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get the arguments that was supplied  by the getItem()
        // when the fragment was instantiated in the CustomPagerAdapter
        Bundle args = getArguments();

        int pageNo = args.getInt("page_position");

        switch (pageNo) {

            case 1:
                rootView = inflater.inflate(R.layout.fragment_1, container, false);
                return rootView;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_2, container, false);
                return rootView;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_3, container, false);
                return rootView;
            case 4:
                rootView = inflater.inflate(R.layout.fragment_4, container, false);
                return rootView;
            case 5:
                rootView = inflater.inflate(R.layout.fragment_5, container, false);
                return rootView;
            default:
                return null;
        }


    }

}