package com.smaspe.shopmaquette;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnStoreItemSelectedListener {

    public static final String ITEM_ID_EXTRA = "item_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView catalog = (RecyclerView) findViewById(R.id.catalog_list);
        catalog.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        catalog.setAdapter(new CatalogAdapter(DataManager.getInstance().getCatalog(), this));
        RecyclerView wishlist = (RecyclerView) findViewById(R.id.wishlist);
        wishlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wishlist.setAdapter(new WishlistAdapter(DataManager.getInstance().getWishlist(), this));
    }

    @Override
    public void onStoreItemSelected(String id) {
        startActivity(new Intent(this, DetailsActivity.class).putExtra(ITEM_ID_EXTRA, id));
    }
}
