package com.rosajay.eggy.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rosajay.eggy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder> {
    private JSONObject mRecipes = new JSONObject();
    public RecipeSearchAdapter(JSONObject allData) {
        this.mRecipes = allData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.recipe_result;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecipeSearchAdapter.ViewHolder holder, int position) {
        TextView title = holder.title;
        String URL = "";
        final ImageView image = holder.image;
        try{
            URL = mRecipes.getJSONArray("meals").getJSONObject(position).get("strMealThumb").toString();
            title.setText(mRecipes.getJSONArray("meals").getJSONObject(position).get("strMeal").toString());
        }catch (JSONException e){
        }
        final Bitmap bmp;
        new GetImageByURL(image, URL).execute();
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

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_title);
            image = itemView.findViewById(R.id.recipe_picture);
        }

    }
    private class GetImageByURL extends AsyncTask<Void, Void, String > {
        Bitmap bmp;
        ImageView imageView;
        String url;
        public GetImageByURL(ImageView imageView, String url){
            this.imageView = imageView;
            this.url = url;
        }
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Void... urls) {
            try {
                InputStream in = new URL(url).openStream();
                bmp = BitmapFactory.decodeStream(in);
                if (bmp != null) {
                    imageView.setImageBitmap(bmp);
                }
            } catch (Exception e) {
                // log error
            }
            return null;
        }
    }
}
