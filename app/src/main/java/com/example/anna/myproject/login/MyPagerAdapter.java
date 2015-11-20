package com.example.anna.myproject.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.anna.myproject.R;

/**
 * Created by Anna on 05-11-2015.
 * PagerAdapter is responsible for creating the content for each page
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 5 ;


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //here, we instantiate our Fragment and pass it some arguments data that’ll get used
    // in the fragment’s (categoriesDetailsFragment) onCreateView() method to populate its XML layout.
    // This method returns the fragment associated with the specified position.
    // It is called when the Adapter needs a fragment  and it does not exists.
    @Override
    public Fragment getItem(int position) {
        // Create fragment object
        Fragment fragment = new MainActivityFragment();

        // Attach some data to it that we'll
        // use to populate our fragment layouts

        Bundle args = new Bundle();
        args.putInt("page_position", position + 1);

        // Set the arguments on the fragment
        // that will be fetched in CategoriesDetailsFragment@onCreateView
        fragment.setArguments(args);

        //fragment is returned to the PagerAdapter to display the content
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if(view.getId()== R.id.frag_1){
            view.setTag(4);
        }
        if(view.getId()== R.id.frag_2){
            view.setTag(3);
        }
        if(view.getId()== R.id.frag_3){
            view.setTag(2);
        }
        if(view.getId()== R.id.frag_4){
            view.setTag(1);
        }
        if(view.getId()== R.id.frag_5){
            view.setTag(0);
        }


        return super.isViewFromObject(view, object);
    }
}
