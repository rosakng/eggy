package com.rosajay.eggy.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.rosajay.eggy.R;
import com.rosajay.eggy.ui.fragment.Model;

import java.util.ArrayList;
import java.util.List;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {

    private List<Model> items = new ArrayList<>();
    public GroceryListAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void loadItems(List<Model> tournaments) {
        this.items = tournaments;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView mCheckedTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = itemView.findViewById(R.id.checked_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            // check the state of the model
            if (!items.get(position).getChecked()) {
                mCheckedTextView.setChecked(false);
            }
            else {
                mCheckedTextView.setChecked(true);
            }
            mCheckedTextView.setText(items.get(position).getName());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!items.get(adapterPosition).getChecked()) {
                mCheckedTextView.setChecked(true);
                mCheckedTextView.setCheckMarkDrawable(R.drawable.checked);
                items.get(adapterPosition).setChecked(true);
            }
            else {
                mCheckedTextView.setChecked(false);
                mCheckedTextView.setCheckMarkDrawable(R.drawable.unchecked);
                items.get(adapterPosition).setChecked(false);
            }
        }

    }
}
