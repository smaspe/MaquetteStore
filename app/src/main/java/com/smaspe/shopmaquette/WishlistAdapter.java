package com.smaspe.shopmaquette;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaspe.iterables.FuncIter;
import com.smaspe.shopmaquette.model.StoreItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created on 13/02/17.
 */
public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 1;
    public static final int FOOTER = 2;
    private List<StoreItem> items;
    private OnStoreItemSelectedListener listener;

    public WishlistAdapter(List<StoreItem> items, OnStoreItemSelectedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_header, parent, false));
            case FOOTER:
                return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_footer, parent, false));
            default:
                return new WishlistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                WishlistViewHolder wishlistViewHolder = (WishlistViewHolder) holder;
                final StoreItem storeItem = items.get(position - HEADER);
                wishlistViewHolder.name.setText(storeItem.getName());
                try {
                    InputStream ims = wishlistViewHolder.image.getResources().getAssets().open(storeItem.getImageURL());
                    wishlistViewHolder.image.setImageDrawable(Drawable.createFromStream(ims, null));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                wishlistViewHolder.price.setText(wishlistViewHolder.price.getResources().getString(R.string.price, storeItem.getPrice()));
                wishlistViewHolder.shortDescription.setText(storeItem.getShortDescription());
                if (storeItem.isInStock()) {
                    wishlistViewHolder.outOfStock.setVisibility(View.GONE);
                    wishlistViewHolder.colors.removeAllViews();
                    for (String colour : storeItem.getColours()) {
                        View view = LayoutInflater.from(wishlistViewHolder.colors.getContext()).inflate(R.layout.small_colours, wishlistViewHolder.colors, false);
                        view.setBackgroundColor(Color.parseColor(colour));
                        wishlistViewHolder.colors.addView(view);
                    }
                } else {
                    wishlistViewHolder.outOfStock.setVisibility(View.VISIBLE);
                    wishlistViewHolder.colors.setVisibility(View.GONE);

                }
                wishlistViewHolder.itemView.setOnClickListener(view -> listener.onStoreItemSelected(storeItem.getId()));
                break;
            case HEADER:
                HeaderViewHolder header = (HeaderViewHolder) holder;
                header.total.setText(header.total.getResources().getString(R.string.total,
                        FuncIter.from(items)
                                .map(StoreItem::getPrice)
                                .reduce((a, b) -> a + b, 0)));
                break;
            case FOOTER:
                FooterViewHolder footer = (FooterViewHolder) holder;
                footer.total.setText(footer.total.getResources().getString(R.string.price,
                        FuncIter.from(items)
                                .map(StoreItem::getPrice)
                                .reduce((a, b) -> a + b, 0)));
                footer.proceed.setEnabled(!items.isEmpty());
                footer.proceed.setOnClickListener(item -> {
                    new AlertDialog.Builder(item.getContext())
                            .setMessage(R.string.confirmation_proceed_to_checkout)
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // TODO Actually proceed to checkout
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }
        if (position == items.size() + 1) {
            return FOOTER;
        }
        return super.getItemViewType(position);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView total;

        HeaderViewHolder(View itemView) {
            super(itemView);
            total = (TextView) itemView.findViewById(R.id.header_total);
        }
    }

    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView total;
        Button proceed;

        FooterViewHolder(View itemView) {
            super(itemView);
            total = (TextView) itemView.findViewById(R.id.footer_total);
            proceed = (Button) itemView.findViewById(R.id.footer_proceed);
        }
    }

    private static class WishlistViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, shortDescription, outOfStock;
        LinearLayout colors;

        WishlistViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_image);
            name = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
            shortDescription = (TextView) itemView.findViewById(R.id.item_short_description);
            outOfStock = (TextView) itemView.findViewById(R.id.item_out_of_stock);
            colors = (LinearLayout) itemView.findViewById(R.id.item_colors);
        }

    }
}