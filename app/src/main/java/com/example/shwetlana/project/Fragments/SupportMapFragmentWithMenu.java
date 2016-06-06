/*
package com.example.shwetlana.project.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shwetlana.project.R;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

*/
/**
 * Created by Shwetlana on 6/5/2016.
 *//*

public class SupportMapFragmentWithMenu extends Fragment {

    final Handler handler = new Handler();


    Random random = new Random();
    Runnable runner = new Runnable() {
        @Override
        public void run() {
            setHasOptionsMenu(true);
        }
    };

    public static SupportMapFragmentWithMenu newInstance(int position,String title) {
        SupportMapFragmentWithMenu fragment = new SupportMapFragmentWithMenu();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("title", title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       // handler.postDelayed(runner, random.nextInt(2000));
        //setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.simple_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(),
                "Menu id  \"" + item.getItemId() + "\" clicked.",
                Toast.LENGTH_SHORT).show();
        return true;
    }

*/
