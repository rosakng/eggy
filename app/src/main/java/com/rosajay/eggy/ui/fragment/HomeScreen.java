package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.util.NavSetup;
import com.rosajay.eggy.ui.util.Screen;

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

        NavSetup n = new NavSetup(view, Screen.HOME, getActivity().getSupportFragmentManager());
        n.setup();
        return view;
    }
}
