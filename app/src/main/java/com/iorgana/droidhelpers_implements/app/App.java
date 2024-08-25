package com.iorgana.droidhelpers_implements.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.iorgana.droidhelpers.crypto.StringCrypto;
import com.iorgana.droidhelpers.db.SqlPreferences;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {
    private static final String TAG = "__App";
    Context baseContext;
    private static Handler handler;
    public static ExecutorService executors = Executors.newCachedThreadPool();

    /**
     * onCreate
     * ------------------------------------------------------------------------
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        this.baseContext = this;
        // Initialize Logger
        initLogger();

        // Test cipher
        final String key = "my_key";
        String name = "Rochdi Wafik";
        String cipher_encrypted = StringCrypto.cipherEncrypt(name, key);
        Logger.d(TAG + " onCreate(): cipher encrypted name: "+ cipher_encrypted);
        Logger.d(TAG + " onCreate(): cipher decrypted name: "+StringCrypto.cipherDecrypt(cipher_encrypted, key));

        String xor_encrypted = StringCrypto.xorEncrypt(name, key);
        Logger.d(TAG + " onCreate(): xor encrypted name: "+xor_encrypted);
        Logger.d(TAG + " onCreate(): xor decrypted name: "+StringCrypto.xorDecrypt(xor_encrypted, key));

        // Initialize SqlPreferences: Load data from disk to cache (in background)
        SqlPreferences.init(baseContext, () -> Logger.i(TAG + " onLoaded(): Data loaded"));
    }

    /**
     * Get UI Thread
     * ------------------------------------------------------------------------
     * - We use this method to access UI Thread from anywhere
     * - Instead of create new Handler instance each time UI thread needed
     * - We use the same Handler instance in anywhere
     *
     */
    public static void mainThread(Runnable... runnable){
        if(handler==null){
            handler = new Handler(Looper.getMainLooper());
        }
        for(Runnable r: runnable){
            handler.post(r);
        }
    }
    /**
     * Get UI Handler
     * ------------------------------------------------------------------------
     */
    public static Handler getHandler(){
        if(handler==null){
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }


    /**
     * Initialize Logger
     * ------------------------------------------------------------------------
     * Logger will be used only in dev mode
     */
    void initLogger(){

        // Setup
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        // Only run on Debug
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

}
