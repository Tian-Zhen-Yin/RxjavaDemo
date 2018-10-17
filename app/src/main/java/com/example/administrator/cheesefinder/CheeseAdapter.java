package com.example.administrator.cheesefinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.CheeseViewHolder>
{
    private List<String> mCheeses;

    @Override
    public CheeseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CheeseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheeseViewHolder holder, int position) {
        holder.title.setText(mCheeses.get(position));
}

    @Override
    public int getItemCount() {
        return mCheeses == null ? 0 : mCheeses.size();
    }

    public void setCheeses(List<String> cheeses) {
        mCheeses = cheeses;
        notifyDataSetChanged();
    }

    public static class CheeseViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public CheeseViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
