package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.adapter.GroceryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroceryFrag extends Fragment {
    public RecyclerView recyclerView;
    private GroceryListAdapter adapter;
    private LinearLayoutManager layoutManager;

    Set<String> items = new HashSet<>();
    List<Model> adaptedItems = new ArrayList<>();

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

        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new GroceryListAdapter();
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference groceryListReference = db.child("list");

        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items = ((HashMap<String, String>) dataSnapshot.getValue()).keySet();
                for(String s: items) {
                    Log.d("HELLOO", s);
                }
                refresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("ERROR", "loadString:onCancelled", databaseError.toException());
                // ...
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
            }
        });

        refresh();

        return view;
    }

    private void fillItems() {
        int i = 0;
        adaptedItems.clear();
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
        adapter.loadItems(adaptedItems);
    }
}
