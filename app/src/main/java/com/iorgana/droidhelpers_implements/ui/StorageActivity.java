package com.iorgana.droidhelpers_implements.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.iorgana.droidhelpers.db.SimpleDB;
import com.iorgana.droidhelpers.db.SqlPreferences;
import com.iorgana.droidhelpers_implements.databinding.ActivityStorageBinding;
import com.iorgana.droidhelpers_implements.model.UserModel;
import com.orhanobut.logger.Logger;

import java.util.Map;

public class StorageActivity extends AppCompatActivity {
    private static final String TAG = "__StorageActivity";
    ActivityStorageBinding binding;
    Activity context = this;

    /**
     * Storage APIs
     * --------------------------------------------------------------------------
     */
    SimpleDB simpleDB;
    SqlPreferences sqlPreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStorageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /**
         * Initialize Vars
         */
        simpleDB = SimpleDB.getInstance(context);
        sqlPreferences = SqlPreferences.getInstance(context);


        /**
         * Handle Save Action
         */
        binding.btnSave.setOnClickListener(view->{
            actionSave();
        });

        /**
         * Handle Fetch Action
         */
        binding.btnFetch.setOnClickListener(view->{
            actionFetch();
        });

        /**
         * Handle Clear Action
         */
        binding.btnClear.setOnClickListener(view->{
            actionClear();
        });

    }


    /**
     * Action Save
     * -----------------------------------------------------------------------
     * - Check Storage API Type: SimpleDB, LocalStorage, SecureStorage
     * - Validate Inputs
     * - Save Data
     */
    void actionSave(){
        // Get inputs
        String fullname = binding.inputFullname.getText().toString().trim();
        boolean isAdmin = binding.radioAdmin.isChecked();

        int age;
        try{age =  Integer.parseInt(binding.inputAge.getText().toString().trim());}catch (Exception e){
            Toast.makeText(context, "Invalid age", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate Inputs
        if(fullname.isEmpty() || age <=0){
            Toast.makeText(context, "Empty fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!binding.radioAdmin.isChecked() && !binding.radioUser.isChecked()){
            Toast.makeText(context, "Role not set", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Model
        UserModel userModel = new UserModel(fullname, age, isAdmin);
        Logger.i(TAG + " actionSave(): User: "+userModel);

        // IF save in SimpleDB
        if(binding.radioSimpleDB.isChecked()){
            Logger.d(TAG + " actionSave(): Save In SimpleDB");
            simpleDB.saveObject(userModel, UserModel.DB_KEY);

            Toast.makeText(context, "Data is saved", Toast.LENGTH_SHORT).show();
            String result = "Fullname: "+userModel.getFullname();
            result +="\nAge: "+userModel.getAge();
            result+="\nIs admin: "+userModel.isAdmin();
            binding.txtOutput.setText(result);
        }

        // IF save in SqlPreferences
        else if(binding.radioSqlPreferences.isChecked()){
            Logger.d(TAG + " actionSave(): Save In SqlPreferences");
            sqlPreferences.putObject(UserModel.DB_KEY, userModel).apply();

            Toast.makeText(context, "Data is saved", Toast.LENGTH_SHORT).show();

        }

        /*+++++++++++++++++++++ SqlPreferences Examples ++++++++++++++++++++++++++*/
        if(!binding.radioSqlPreferences.isChecked()) return;

        // Show saved data from cache
        UserModel savedUser = sqlPreferences.getObject(UserModel.DB_KEY, UserModel.class);
        if(savedUser==null){
            Toast.makeText(context, "Unable to get saved data from cache", Toast.LENGTH_SHORT).show();
            return;
        }
        String result = "Fullname: "+savedUser.getFullname();
        result +="\nAge: "+savedUser.getAge();
        result+="\nIs admin: "+savedUser.isAdmin();
        binding.txtOutput.setText(result);


        // (SqlPreferences) Save data types individually +++++++++++++++++++++++++++++++
        sqlPreferences.putString("mName", fullname)
                .putInt("mAge", age)
                .putBoolean("mAdmin", isAdmin)
                .apply();
    }

    /**
     * Action Fetch
     * -----------------------------------------------------------------------
     * - Check Storage API Type: SimpleDB, LocalStorage, SecureStorage
     * - Get data from correspond storage
     * - Show Data
     */
    void actionFetch(){
        // Initialize UserModel that should holds Inputs data
        UserModel userModel=null;

        // Get data from corresponded Storage
        if(binding.radioSimpleDB.isChecked()){
            userModel = simpleDB.getObject(UserModel.class, UserModel.DB_KEY);
            Logger.d(TAG + " actionFetch(): fetch by SimpleDB");
        }
        else if(binding.radioSqlPreferences.isChecked()){
            userModel = sqlPreferences.getObject(UserModel.DB_KEY,UserModel.class);
            Logger.d(TAG + " actionFetch(): fetch by LocalStorage");
        }


        // Check if data fetched
        if(userModel==null){
            Toast.makeText(context, "No data is saved!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Display Data
        binding.inputFullname.setText(userModel.getFullname());
        binding.inputAge.setText(String.valueOf(userModel.getAge()));

        boolean isAdmin = userModel.isAdmin();

        if(isAdmin){
            binding.radioAdmin.setChecked(true);
            binding.radioUser.setChecked(false);
        }
        else{
            binding.radioAdmin.setChecked(false);
            binding.radioUser.setChecked(true);
        }

        // Show data as txt
        String result = "Fullname: "+userModel.getFullname();
               result +="\nAge: "+userModel.getAge();
               result+="\nIs admin: "+userModel.isAdmin();
        binding.txtOutput.setText(result);

        /*+++++++++++++++++++++ SqlPreferences Examples ++++++++++++++++++++++++++*/
        if(!binding.radioSqlPreferences.isChecked()) return;

        // (SqlPreferences) Get data types individually +++++++++++++++++++++++++++++++
        String fullname_2 = sqlPreferences.getString("mName", null);
        Integer age_2 = sqlPreferences.getInt("mAge", null);
        Boolean isAdmin_2 = sqlPreferences.getBoolean("mAdmin", null);

        // (SqlPreferences) Get all saved data ++++++++++++++++++++++++++++++++++++++++
        Map<String, Object> allData = sqlPreferences.getAll();
        if(allData!=null){
            Logger.d(TAG + " actionFetch(): Print all saved data -------------");
            for(Map.Entry<String, Object> item: allData.entrySet()){
                Logger.d(TAG + " actionFetch(): item_key => "+item.getKey()+" item_value => "+item.getValue());
            }
        }
    }

    /**
     * Action Clear
     * -----------------------------------------------------------------------
     * - Check Storage API Type: SimpleDB, LocalStorage, SecureStorage
     * - Clear Data
     * - Clear inputs
     */
    void actionClear(){

        // Get data from corresponded Storage
        if(binding.radioSimpleDB.isChecked()){
            // Remove specific item
           simpleDB.removeObject(UserModel.DB_KEY);
           // Remove all data
            simpleDB.clear();
            Toast.makeText(context, "Data is removed", Toast.LENGTH_SHORT).show();
        }
        else if(binding.radioSqlPreferences.isChecked()){
            // Remove specific item
            sqlPreferences.remove("mName");
            // Remove specific object
            sqlPreferences.removeObject(UserModel.DB_KEY);
            // Clear all data
            sqlPreferences.clear();
        }


        // Empty inputs
        binding.inputFullname.setText("");
        binding.inputAge.setText("");
        binding.radioUser.setChecked(false);
        binding.radioAdmin.setChecked(false);

        binding.txtOutput.setText("");

    }



}