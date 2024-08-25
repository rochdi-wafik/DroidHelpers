package com.iorgana.droidhelpers.db;

import android.app.Application;
import android.content.Context;

/**
 * @deprecated Not implemented yet
 */
public class SecureStorage {
    private static final String TAG = "__SecureStorage";

    private static volatile SecureStorage INSTANCE;
    private Application application;


    /**
     * -------------------------------------------------------------------------
     * Private Constructor
     * -------------------------------------------------------------------------
     * - Use getInstance() to get singleton instance
     */
    private SecureStorage(Context context){
        this.application = (Application) context.getApplicationContext();
    }

    /**
     * -------------------------------------------------------------------------
     * Get Instance
     * -------------------------------------------------------------------------
     * - Use this method to get instance
     */
    public static SecureStorage getInstance(Context anyContext){
        if(INSTANCE==null){
            synchronized (SecureStorage.class){
                if(INSTANCE==null){
                    INSTANCE = new SecureStorage(anyContext);
                }
            }
        }
        return INSTANCE;
    }


}
