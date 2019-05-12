package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.adapter.GroceryListAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroceryFrag extends Fragment {
    public RecyclerView recyclerView;
    private GroceryListAdapter adapter;
    private LinearLayoutManager layoutManager;

    // dummy list of items to be populated manually
    List<Model> items = new ArrayList<>();


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
        GroceryListAdapter adapter = new GroceryListAdapter();
        layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fillItems();

        adapter.loadItems(items);

        return view;
    }

    private void fillItems() {
        for (int x = 0; x < 30; x++) {
            Model model = new Model();
            model.setPosition(x+1);
            model.setName("item" + x);
            model.setChecked(false);

            items.add(model);
        }
    }
}
