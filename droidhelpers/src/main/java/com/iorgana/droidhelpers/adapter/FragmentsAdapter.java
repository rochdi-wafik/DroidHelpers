package com.iorgana.droidhelpers.adapter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragments Adapter
 * -------------------------------------------------------------------------------------------------
 * - This class wil help us save the fragment instance (Like Fields, inner classes, etc)
 * - We'll be able to protect all data stored in fragment, from being lost, like Fields
 * ^
 * - Unless we manually change the data inside the fragment. like using onCreate()
 * - We cannot prevent invoke fragment lifecycle methods, like onCreate(),
 * - Because when  we use beginTransaction().replace(), the manager invoke the lifecycle fragment methods,
 * - Instead, is necessary, we can save data in bundle and retrieve it when fragment re-launched
 * ^
 * - If we want to clear a fragment, we simply replace the exist instance with new instance.
 * - When a fragment is saved in this class, if can get it whenever we want using get()
 * -----------------------------------------------------------------------------------------------
 * [Updates]
 * - ActivityContext has no meaning since we don't want to access the activity
 * - We only want to store instances
 */
public class FragmentsAdapter {
    private static volatile FragmentsAdapter INSTANCE;

    Map<String, Fragment> instanceList = new HashMap<>();
    Map<String, Bundle> fragmentBundles = new HashMap<>(); // not made yet


    /**
     * Constructor
     * -------------------------------------------------------------------------
     * [-] Use this method for none-singleton
     */
    public FragmentsAdapter(){
    }



    /**
     * Get Instance
     * -------------------------------------------------------------------------
     * Use this method for Singleton object
     */
    public static FragmentsAdapter getInstance() {
        if (INSTANCE == null) {
            synchronized (FragmentsAdapter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FragmentsAdapter();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Add Fragment
     * -------------------------------------------------------------------------
     * - When adapter initialized, use this method to store all your fragments
     * - Add fragment only if not exists in the List
     */
    public void add(Fragment fragment){
        Class<?> mClass = fragment.getClass();
        if(instanceList.containsKey(mClass.getName())) return;

        Bundle bundle = fragment.getArguments();
        instanceList.put(mClass.getName(), fragment);
        fragmentBundles.put(mClass.getName(), bundle);
    }


    /**
     * Get Fragment
     * -------------------------------------------------------------------------
     * - Get the fragment that has been state-saved by addFragment
     */
    public Fragment get(Class<?> fragmentClass){
        Fragment fragment = instanceList.get(fragmentClass.getName());
        Bundle bundle = fragmentBundles.get(fragmentClass.getName());

        if(fragment!=null && bundle!=null){
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    /*
     *
     * Get Fragment
     * -------------------------------------------------------------------------
     * - If fragment already saved in List, retrieve it
     * - If not: save the given instance in list, and return
     */
    public Fragment addAndGet(Fragment fragment){
        // Check if already saved, if not? save it first.
        Class<?> mClass = fragment.getClass();
        if (!instanceList.containsKey(mClass.getName())) {
            add(fragment);
        }
        return get(mClass);
    }


    /**
     * Replace fragment
     * -------------------------------------------------------------------------
     * - Replace old fragment with new fragment (fragment with same class)
     */
    public Fragment replace(Fragment newFragment){
        // Get old fragment
        Class<?> mClass = newFragment.getClass();
        if(instanceList.containsKey(mClass.getName())){
            // Replace it with new fragment
            instanceList.remove(mClass.getName());
        }
        instanceList.put(mClass.getName(), newFragment);

        return newFragment;
    }

    /**
     * Remove Fragment
     * -------------------------------------------------------------------------
     */
    public void remove(Class<?> fragmentClass){
        if(instanceList.containsKey(fragmentClass.getName())){
            instanceList.remove(fragmentClass.getName());
        }
    }

    /**
     * Remove All
     * -------------------------------------------------------------------------
     */
    public void removeAll(){
        instanceList.clear();
    }

}
