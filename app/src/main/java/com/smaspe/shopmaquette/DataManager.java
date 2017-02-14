package com.smaspe.shopmaquette;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smaspe.iterables.FuncIter;
import com.smaspe.shopmaquette.model.StoreItem;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 13/02/17.
 */

public class DataManager {
    private List<StoreItem> catalog;

    private List<String> wishlist = new ArrayList<>();

    private Map<String, Float> ratings = new HashMap<>();

    private static DataManager INSTANCE;

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Init the data manager before using it");
        }
        return INSTANCE;
    }

    public static void init(Context context) throws IOException {
        if (INSTANCE != null) {
            throw new IllegalStateException("Data manager already initialized");
        }
        INSTANCE = new DataManager(context);
    }

    private DataManager(Context context) throws IOException {
        Gson deserializer = new Gson();
        Type type = new TypeToken<List<StoreItem>>() {
        }.getType();
        catalog = deserializer.fromJson(new InputStreamReader(context.getAssets().open("catalog.json")), type);
    }

    public List<StoreItem> getCatalog() {
        return Collections.unmodifiableList(catalog);
    }

    public StoreItem getStoreItemById(String id) {
        for (StoreItem storeItem : catalog) {
            if (storeItem.getId().equals(id)) {
                return storeItem;
            }
        }
        return null;
    }

    public List<StoreItem> getWishlist() {
        return Collections.unmodifiableList(
                FuncIter.from(wishlist)
                        .map(this::getStoreItemById)
                        .filter(item -> item != null)
                        .collect());
    }

    public void addToWishlist(String id) {
        if (!wishlist.contains(id)) {
            wishlist.add(id);
        }
    }

    public void removeFromWishlist(String id) {
        wishlist.remove(id);
    }

    public boolean isInWishlist(String id) {
        return wishlist.contains(id);
    }

    public void setRating(String id, Float value) {
        ratings.put(id, value);
    }

    public Float getRating(String id) {
        if (!ratings.containsKey(id)) {
            return 0f;
        }
        return ratings.get(id);
    }
}
