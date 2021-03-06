package com.rosajay.eggy.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.adapter.GroceryListAdapter;
import com.rosajay.eggy.ui.util.NavSetup;
import com.rosajay.eggy.ui.util.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroceryFrag extends Fragment {
    private RecyclerView recyclerView;
    private GroceryListAdapter adapter;
    private LinearLayoutManager layoutManager;

    Set<String> items = new HashSet<>();
    List<Model> adaptedItems = new ArrayList<>();
    ImageView gudetama;
    TextView noListText;
    View loading;

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

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        NavSetup n = new NavSetup(view, Screen.GROCERY, getActivity().getSupportFragmentManager());
        n.setup();

        gudetama = view.findViewById(R.id.gudetama);
        noListText = view.findViewById(R.id.noListText);
        gudetama.setVisibility(View.GONE);
        noListText.setVisibility(View.GONE);

        loading = view.findViewById(R.id.loading);

        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new GroceryListAdapter();
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference groceryListReference = db.child("list");

        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> newData = new HashSet<>();
                if(dataSnapshot.getValue() != null) {
                    newData = ((HashMap<String, String>) dataSnapshot.getValue()).keySet();
                } else {
                    Log.d("Warning", "Nothing to show");
                }

                //only animate if more than 1 item was added or removed
                if(Math.abs(items.size() - newData.size()) != 1) {
                    recyclerView.scheduleLayoutAnimation();
                }

                items = newData;
                // log the list from db
//                for(String s: items) {
//                    Log.d("HELLOO", s);
//                }
                refresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting list failed, log a message
                Log.w("ERROR", "loadString:onCancelled", databaseError.toException());
            }
        };

        groceryListReference.addValueEventListener(itemListener);

        final EditText input = view.findViewById(R.id.input);
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = input.getText().toString();
                groceryListReference.child(s).setValue("");
                input.setText("");
                hideSoftKeyboard();
            }
        });

        recyclerView.scheduleLayoutAnimation();
        return view;
    }

    private void fillItems() {
        adaptedItems.clear();
        int i = 0;
        for (String s: items) {
            Model model = new Model();
            model.setPosition(i+1);
            model.setName(s);
            model.setChecked(false);

            adaptedItems.add(model);
            i++;
        }
    }

    private void refresh() {
        fillItems();
        if(adaptedItems.size() == 0) {
            gudetama.setVisibility(View.VISIBLE);
            noListText.setVisibility(View.VISIBLE);
        } else {
            gudetama.setVisibility(View.GONE);
            noListText.setVisibility(View.GONE);
        }
        loading.setVisibility(View.GONE);
        adapter.loadItems(adaptedItems);
    }

    private void hideSoftKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity) getContext()).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
