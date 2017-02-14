package com.smaspe.shopmaquette;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaspe.shopmaquette.model.StoreItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created on 13/02/17.
 */
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    private List<StoreItem> items;
    private OnStoreItemSelectedListener listener;

    public CatalogAdapter(List<StoreItem> items, OnStoreItemSelectedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public CatalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CatalogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CatalogViewHolder holder, int position) {
        final StoreItem storeItem = items.get(position);
        holder.name.setText(storeItem.getName());
        try {
            InputStream ims = holder.image.getResources().getAssets().open(storeItem.getImageURL());
            holder.image.setImageDrawable(Drawable.createFromStream(ims, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(view -> listener.onStoreItemSelected(storeItem.getId()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CatalogViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public CatalogViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
        }
    }
}
