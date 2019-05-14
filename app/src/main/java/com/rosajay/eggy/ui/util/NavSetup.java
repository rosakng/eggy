package com.rosajay.eggy.ui.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.fragment.GroceryFrag;
import com.rosajay.eggy.ui.fragment.HomeScreen;
import com.rosajay.eggy.ui.fragment.RecipesFrag;

public class NavSetup {

    private View v;
    private Screen s;
    private FragmentManager fm;

    public NavSetup(View v, Screen s, FragmentManager fm) {
        this.v = v;
        this.s = s;
        this.fm = fm;
    }

    public void setup() {
        if(s == Screen.HOME) {
            setupGroceryButton(v.findViewById(R.id.toolbar_home));
            setupRecipeButton(v.findViewById(R.id.toolbar_home));
        } else if (s == Screen.GROCERY) {
            setupRecipeButton(v.findViewById(R.id.toolbar_grocery));
            setupHomeButton(v.findViewById(R.id.toolbar_grocery));
        } else if (s == Screen.RECIPE) {
            setupGroceryButton(v.findViewById(R.id.toolbar_recipe));
            setupHomeButton(v.findViewById(R.id.toolbar_recipe));
        } else {
            //should never happen
        }
    }

    private void setupGroceryButton(View toolbar) {
        toolbar.findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = GroceryFrag.newInstance();
                FragmentTransaction t = fm.beginTransaction();
                t.addToBackStack("grocery");
                t.replace(R.id.content_main, fragment).commit();
            }
        });
    }

    private void setupRecipeButton(View toolbar) {
        toolbar.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = RecipesFrag.newInstance();
                FragmentTransaction t = fm.beginTransaction();
                t.addToBackStack("recipes");
                t.replace(R.id.content_main, fragment).commit();
            }
        });
    }

    private void setupHomeButton(View toolbar) {
        toolbar.findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = HomeScreen.newInstance();
                FragmentTransaction t = fm.beginTransaction();
                t.addToBackStack("home");
                t.replace(R.id.content_main, fragment).commit();
            }
        });
    }
}
