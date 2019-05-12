package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rosajay.eggy.R;

public class HomeScreen extends Fragment {
    public HomeScreen() {
    }

    public static HomeScreen newInstance() {
        HomeScreen homeScreen = new HomeScreen();
        return homeScreen;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_screen, container, false);

        final Button grocery = view.findViewById(R.id.groceries);
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = GroceryFrag.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.addToBackStack("grocery");
                t.replace(R.id.content_main, fragment).commit();
            }
        });

        Button recipes = view.findViewById(R.id.recipes);
        recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = RecipesFrag.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction t = fragmentManager.beginTransaction();
                t.addToBackStack("recipes");
                t.replace(R.id.content_main, fragment).commit();
            }
        });
        return view;
    }
}
