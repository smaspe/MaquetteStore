package com.smaspe.shopmaquette;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.smaspe.shopmaquette.model.StoreItem;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 13/02/17.
 */

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoreItem item = DataManager.getInstance().getStoreItemById(getIntent().getStringExtra(MainActivity.ITEM_ID_EXTRA));
        if (item == null) {
            Toast.makeText(this, R.string.product_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_details);
        setTitle(item.getName());

        try {
            InputStream ims = getAssets().open(item.getImageURL());
            ((ImageView) findViewById(R.id.item_image)).setImageDrawable(Drawable.createFromStream(ims, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.item_price)).setText(getString(R.string.price, item.getPrice()));
        ((TextView) findViewById(R.id.item_description)).setText(item.getDescription());


        if (item.isInStock()) {
            if (item.getColours() == null || item.getColours().isEmpty()) {
                findViewById(R.id.item_colors_container).setVisibility(View.GONE);
            } else {
                LinearLayout colours = (LinearLayout) findViewById(R.id.item_colors);
                for (String colour : item.getColours()) {
                    View view = LayoutInflater.from(this).inflate(R.layout.large_colours, colours, false);
                    ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{Color.parseColor(colour)});
                    view.setBackgroundTintList(csl);
                    colours.addView(view);
                }
            }
            if (item.getSize() == null || item.getSize().isEmpty()) {
                findViewById(R.id.item_size_container).setVisibility(View.GONE);
            } else {
                ((TextView) findViewById(R.id.item_size)).setText(item.getSize());
            }
        } else {
            findViewById(R.id.item_size_container).setVisibility(View.GONE);
            findViewById(R.id.item_colors_container).setVisibility(View.GONE);
            findViewById(R.id.item_out_of_stock).setVisibility(View.VISIBLE);
        }

        View add = findViewById(R.id.add_to_wishlist);
        View remove = findViewById(R.id.remove_from_wishlist);
        if (DataManager.getInstance().isInWishlist(item.getId())) {
            add.setVisibility(View.GONE);
            remove.setOnClickListener(view -> {
                DataManager.getInstance().removeFromWishlist(item.getId());
                finish();
            });
        } else {
            remove.setVisibility(View.GONE);
            add.setOnClickListener(view -> {
                DataManager.getInstance().addToWishlist(item.getId());
                finish();
            });
        }

        RatingBar rating = (RatingBar) findViewById(R.id.rating);
        rating.setMax(5);
        rating.setNumStars(5);
        rating.setRating(DataManager.getInstance().getRating(item.getId()));
        rating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            DataManager.getInstance().setRating(item.getId(), v);
        });
    }
}
