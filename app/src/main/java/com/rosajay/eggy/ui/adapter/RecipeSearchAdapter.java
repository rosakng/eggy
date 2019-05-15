package com.rosajay.eggy.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rosajay.eggy.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder> {
    private JSONObject mRecipes;
    private static Context context;
    private static Activity mActivity;
    private static FragmentManager fragm;
    private static String URL = "", titleString = "";
    private Set<String> favouriteRecipes;
    public RecipeSearchAdapter(JSONObject allData) {
        this.mRecipes = allData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        FragmentActivity f = (FragmentActivity) (context) ;
        fragm = f.getSupportFragmentManager();
        int layoutForItem = R.layout.recipe_result;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new ViewHolder(view);
    }

    public int getNumIngredients(int position) {
        try {
            for (int i=20; i>0; i--) {
                String search = "strIngredient" + i;
                if (!mRecipes.getJSONArray("meals").getJSONObject(position).get(search).toString().equals("null") && !mRecipes.getJSONArray("meals").getJSONObject(position).get(search).toString().equals("")) {
                    return i;
                }
            }
        } catch (JSONException e) {

        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecipeSearchAdapter.ViewHolder holder, final int position) {
        final TextView title = holder.title;
        TextView number = holder.number;
        Button seeMore = holder.seeMore;
        final ImageView image = holder.image;
        int numberIngredients;
        try{
            numberIngredients = getNumIngredients(position);
            number.setText(String.valueOf(numberIngredients));
            URL = mRecipes.getJSONArray("meals").getJSONObject(position).get("strMealThumb").toString();
            titleString = mRecipes.getJSONArray("meals").getJSONObject(position).get("strMeal").toString();
            title.setText(titleString);

            final DatabaseReference db;
            db = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference favouriteRecipesReference = db.child("recipes");
            ValueEventListener v = new ValueEventListener() {
                Set<String> favouriteRecipesFromDb = new HashSet<>();
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        favouriteRecipesFromDb = ((HashMap<String, String>) dataSnapshot.getValue()).keySet();
                    }

                    favouriteRecipes = favouriteRecipesFromDb;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            favouriteRecipesReference.addValueEventListener(v);

            seeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        String recipeId = mRecipes.getJSONArray("meals").getJSONObject(position).get("idMeal").toString();
                        String title = mRecipes.getJSONArray("meals").getJSONObject(position).get("strMeal").toString();
                        String instructions = mRecipes.getJSONArray("meals").getJSONObject(position).get("strInstructions").toString();
                        String urlString = mRecipes.getJSONArray("meals").getJSONObject(position).get("strMealThumb").toString();
                        ArrayList<String> ingredients = new ArrayList<>();

                        Boolean isFavourite = favouriteRecipes.contains(recipeId);
                        Log.d("ASDLKFJASLKDFSLAKJDF", isFavourite.toString());
                        for(int i = 1; i <= getNumIngredients(position); i++) {
                            ingredients.add(mRecipes.getJSONArray("meals").getJSONObject(position).get("strIngredient"+i).toString());
                        }
                        RecipeDetails recipeDetails = RecipeDetails.newInstance(recipeId, URL, title, ingredients, instructions, isFavourite);

                        recipeDetails.show(fragm, null);
                    }catch (JSONException e){

                    }
                }
            });
            getImageByURL(image, URL);
        }catch (JSONException e){
        }
    }

    @Override
    public int getItemCount() {
        int size = -1;
        try{
            size = mRecipes.getJSONArray("meals").length();
        }catch (JSONException e){
        }

        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;
        private TextView number;
        private Button seeMore;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            image = itemView.findViewById(R.id.recipe_picture);
            number = itemView.findViewById(R.id.number);
            seeMore = itemView.findViewById(R.id.see_more);
        }

    }

    private static void getImageByURL(ImageView imageView, String url){
        Picasso.with(context).load(url).into(imageView);
    }
    public static class RecipeDetails extends DialogFragment {
        String recipeId;
        String url;
        String title;
        String instructions;
        ArrayList<String> ingredients;
        Boolean isFavourite = false;
        public static RecipeDetails newInstance(String recipeId, String imageUrl, String title, ArrayList<String> ingredients, String instructions, Boolean isFavourite){
            RecipeDetails fragment = new RecipeDetails();
            fragment.recipeId = recipeId;
            fragment.url=imageUrl;
            fragment.title = title;
            fragment.instructions=instructions;
            fragment.ingredients=ingredients;
            fragment.isFavourite = isFavourite;
            for(String s : ingredients) {
                Log.d("INGREDIENT", s);
            }
            Log.d("FIANSDFASNDFLA", isFavourite.toString());
            return fragment;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.recipe_details, null);

            Button closeDetails = view.findViewById(R.id.close_details);
            closeDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().cancel();
                }
            });

            TextView recipeTitle = view.findViewById(R.id.recipe_title);
            recipeTitle.setText(title);

            TextView recipeInstructions = view.findViewById(R.id.recipe_instructions);
            recipeInstructions.setText(instructions);

            ImageView imageView = view.findViewById(R.id.recipe_image);
            Picasso.with(context).load(url).into(imageView);

            TextView recipeIngredients = view.findViewById(R.id.recipe_ingredients);
            StringBuilder sb = new StringBuilder();
            for(String s: ingredients) {
                sb.append(s);
                sb.append(", ");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            recipeIngredients.setText(sb.toString());
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final DatabaseReference db;
            db = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference groceryListReference = db.child("list");
            final DatabaseReference favouriteRecipesReference = db.child("recipes");

            view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (String s : ingredients) {
                        groceryListReference.child(s).setValue("");
                    }

                    Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
                }
            });

            final ImageButton favourite = view.findViewById(R.id.favourite);
            favourite.setImageResource(isFavourite ? R.drawable.favourite_full : R.drawable.favourite_empty);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isFavourite) {
                        favouriteRecipesReference.child(recipeId).removeValue();
                    } else {
                        DatabaseReference thisRecipe = favouriteRecipesReference.child(recipeId);
                        thisRecipe.child("title").setValue(title);
                        thisRecipe.child("ingredients").setValue(ingredients);
                        thisRecipe.child("instructions").setValue(instructions);
                        thisRecipe.child("imageURL").setValue(url);
                    }
                    isFavourite = !isFavourite;
                    favourite.setImageResource(isFavourite ? R.drawable.favourite_full : R.drawable.favourite_empty);
                }
            });

            builder.setView(view);
            return builder.create();
        }
    }
}
