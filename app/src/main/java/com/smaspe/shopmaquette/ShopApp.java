package com.smaspe.shopmaquette;

import android.app.Application;

import java.io.IOException;

/**
 * Created on 13/02/17.
 */
public class ShopApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DataManager.init(this);
        } catch (IOException e) {
            // Could not load data.
            e.printStackTrace();
        }
    }
}
