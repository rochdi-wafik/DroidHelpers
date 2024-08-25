package com.iorgana.droidhelpers.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class InstancesManager {
    private static volatile InstancesManager INSTANCE;

    Map<Class<?>, Object> instanceList = new ConcurrentHashMap<>();


    /**
     * Constructor
     * -------------------------------------------------------------------------
     * [-] Its recommended to use getInstance() for singleton
     * [-] Otherwise use this Constructor fo none-singleton
     */
    public InstancesManager(){}



    /**
     * Get Instance
     * -------------------------------------------------------------------------
     * [-] Use this method for Singleton
     */
    public static InstancesManager getInstance() {
        if (INSTANCE == null) {
            synchronized (InstancesManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InstancesManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Add Instance
     * -------------------------------------------------------------------------
     * - Add instance only if not exists in the List
     */
    public <T> void add(T object){
        Class<?> mClass = object.getClass();
        if(!instanceList.containsKey(mClass)){
            instanceList.put(mClass, object);
        }
    }


    /**
     * Get Instance
     * -------------------------------------------------------------------------
     * - The @SuppressWarnings("unchecked") is not necessary but for clean code
     * - Without it, java warn as because we may try to use the wrong class Type
     * @return instance OR null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> instanceClass){
        Object obj = instanceList.get(instanceClass);
        if(obj!=null && instanceClass.isInstance(obj)){
            return (T) obj;
        }
        return null;
    }

    /*
     *
     * Get Instance
     * -------------------------------------------------------------------------
     * - If object already saved in List, retrieve it
     * - If not: save the given object in list, and return
     */
    @SuppressWarnings("unchecked")
    public <T> T addAndGet(T instance){
        // Check if already saved, if not? save it first.
        Class<?> mClass = instance.getClass();
        if (!instanceList.containsKey(mClass)) {
            add(instance);
        }
        return (T) get(instance.getClass());
    }


    /**
     * Replace fragment
     * -------------------------------------------------------------------------
     * - Replace old instance with new instance (instance with same Type)
     */
    public <T> T replace(T newInstance){
        // Get old fragment
        try{
            Class<?> mClass = newInstance.getClass();
            // Replace it with new fragment
            Object obj = instanceList.get(mClass);
            if(obj!=null && mClass.isInstance(obj)){
                instanceList.remove(mClass);
                instanceList.put(mClass, newInstance);
            }
        }catch (Exception ignored){
            // ignored
        }

        return newInstance;
    }

    /**
     * Remove Instance
     * -------------------------------------------------------------------------
     */
    public <T> void remove(Class<T> instanceClass){
        try{
            instanceList.remove(instanceClass);
        }catch (Exception ignored){
            // ignored
        }
    }

    /**
     * Remove All
     * -------------------------------------------------------------------------
     */
    public void removeAll(){
        try {
            instanceList.clear();
        }catch (Exception ignored){
            // ignored
        }
    }

}