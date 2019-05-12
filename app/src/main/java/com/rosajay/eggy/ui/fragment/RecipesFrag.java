package com.rosajay.eggy.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rosajay.eggy.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RecipesFrag extends Fragment {
    private EditText recipeSearch;
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
        recipeSearch = view.findViewById(R.id.search_recipes);
        Button clickSearch = view.findViewById(R.id.doSearch);
        clickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("melon", recipeSearch.getText().toString());
                new GetRecipesBySearch().execute();
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
//                String appID = "067c3b25";
//                String apiKey = "dbfb438452bdeda0a94ee52531283a5c";
//
//                //URL url = new URL(API_URL + "q=" + recipeKeyWord + "&app_id=${" + appID + "}&app_key=${" + apiKey + "}");
//                URL url = new URL("https://api.edamam.com/search?q=chicken&app_id=${067c3b25}&app_key=${dbfb438452bdeda0a94ee52531283a5c}&from=0&to=3&calories=591-722&health=alcohol-free");
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
            }
            Log.i("INFO", response);
            Log.d("response", response);
        }
    }
}
