package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosajay.eggy.R;

public class GroceryFrag extends Fragment {
    public GroceryFrag(){
    }

    public static GroceryFrag newInstance() {
        GroceryFrag groceryFrag = new GroceryFrag();
        return groceryFrag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groceries_screen, container, false);
        return view;
    }
}
