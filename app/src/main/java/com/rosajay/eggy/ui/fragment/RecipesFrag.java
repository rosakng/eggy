package com.rosajay.eggy.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.adapter.RecipeSearchAdapter;
import com.rosajay.eggy.ui.util.NavSetup;
import com.rosajay.eggy.ui.util.Screen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipesFrag extends Fragment {
    private static EditText recipeSearch;
    private JSONObject mData;
    private RecyclerView recipeRecyclerView;
    private RecipeSearchAdapter recipeSearchAdapter;
    private LinearLayoutManager layoutManager;
    private ImageView gudetama;
    private static FragmentManager fragm;
    final private String API_URL = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
    public RecipesFrag() {

    }
    public static RecipesFrag newInstance() {
        RecipesFrag recipesFrag = new RecipesFrag();
        return recipesFrag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipes_screen, container, false);
        ImageButton back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        NavSetup n = new NavSetup(view, Screen.RECIPE, getActivity().getSupportFragmentManager());
        n.setup();

        recipeRecyclerView = view.findViewById(R.id.recipesRecyclerView);
        recipeSearch = view.findViewById(R.id.search_recipes);
        Button clickSearch = view.findViewById(R.id.doSearch);
        gudetama = view.findViewById(R.id.gudetama);

        FragmentActivity f = (FragmentActivity) (getContext()) ;
        fragm = f.getSupportFragmentManager();

        clickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("melon", recipeSearch.getText().toString());
                new GetRecipesBySearch().execute();
                hideSoftKeyboard();
            }
        });
        return view;
    }
    private class GetRecipesBySearch extends AsyncTask<Void, Void, String > {
        protected void onPreExecute() {
            Log.d("lamamma","lamammama");
        }
        @Override
        protected String doInBackground(Void... urls) {
            String recipeKeyWord = recipeSearch.getText().toString();
            // Do some validation here

            try {
//                String API_URL = "https://api.edamam.com/search?";
//                String appID = "0ba00d83";
//                String apiKey = "8d926a43134b7de0d2ce9c39babb4eaa";
//
//                URL url = new URL(API_URL + "q=" + recipeKeyWord + "&app_id=" + appID + "&app_key=" + apiKey);//                URL url = new URL("https://api.edamam.com/search?q=chicken&app_id=${067c3b25}&app_key=${dbfb438452bdeda0a94ee52531283a5c}&from=0&to=3&calories=591-722&health=alcohol-free");
//                Log.d("melonurl", url.toString());
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                Log.e("POOPY", Integer.toString(urlConnection.getResponseCode()));

                URL url = new URL(API_URL + recipeKeyWord);
                Log.d("melonurl", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }else{
                try {
                    mData = new JSONObject(response);
//                    Log.d("pls", mData.getJSONArray("meals").getJSONObject(0).toString());
//                    Log.d("pls", mData.getJSONArray("meals").getJSONObject(0).get("strMeal").toString());
//                    Log.d("pls", Integer.toString(mData.getJSONArray("meals").length()));
//                    JSONArray recipeArr = mData.getJSONArray("meals");
//                    for (int i=0; i <recipeArr.length(); i++){
//                        String title = recipeArr.getJSONObject(i).get("strMeal").toString();
//                        String image = recipeArr.getJSONObject(i).get("strMealThumb").toString();
//                        JSONObject data = new JSONObject();
//                        data.put("titie", title);
//                        data.put("imgURL", image);
//                    }
                    if (mData.get("meals").toString().equals("null")){
                        Log.d("lamamma", "null");
                        NoRecipesFoundDialog noRecipesFoundDialog = NoRecipesFoundDialog.newInstance();
                        noRecipesFoundDialog.show(fragm, null);

                    }else{
                        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recipeRecyclerView.setLayoutManager(layoutManager);
                        recipeSearchAdapter = new RecipeSearchAdapter(mData);
                        recipeRecyclerView.setAdapter(recipeSearchAdapter);
                        gudetama.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
            Log.d("response", response);
        }
    }
    public static class NoRecipesFoundDialog extends DialogFragment {
        public static NoRecipesFoundDialog newInstance(){
            NoRecipesFoundDialog fragment = new NoRecipesFoundDialog();
            return fragment;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.no_results, null);
            Button searchAgain = view.findViewById(R.id.search_again);
            searchAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().cancel();
                    recipeSearch.setText("");
                }
            });

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view);
            return builder.create();
        }
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
