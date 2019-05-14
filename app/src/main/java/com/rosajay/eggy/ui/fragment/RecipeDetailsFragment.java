package com.rosajay.eggy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rosajay.eggy.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class RecipeDetailsFragment extends Fragment {
    private JSONObject recipeData;
    public static RecipeDetailsFragment newInstance(JSONObject recipeData){
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.recipeData = recipeData;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details, container, false);
        TextView title = view.findViewById(R.id.recipe_title);
        ImageView recipeImage = view.findViewById(R.id.recipe_picture);
        TextView allRecipes = view.findViewById(R.id.all_recipes);
        TextView instructions = view.findViewById(R.id.recipe_instructions);

        return view;
    }
}
