package com.example.debtapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    private static final String DEPLOYED_CONTRACT_ADDRESS = "deployed_contract_address";
    private static final String LOGIN_STATUS = "login_status";
    private static final String USERNAME = "username";
    private static final String PRIVATE_KEY = "private_key";

    static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDeployedContractAddress (Context context, String deployedContractAddress){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(DEPLOYED_CONTRACT_ADDRESS, deployedContractAddress);
        editor.apply();
    }

    public static String getDeployedContractAddress (Context context){
        return getSharedPreference(context).getString(DEPLOYED_CONTRACT_ADDRESS, "null");
    }

    public static void setLoginStatus (Context context, boolean status){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(LOGIN_STATUS, status);
        editor.apply();
    }

    public static Boolean isLoggedIn(Context context){
        return getSharedPreference(context).getBoolean(LOGIN_STATUS, false);
    }

    public static void setUsername(Context context, String username){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static String getUsername(Context context){
        return getSharedPreference(context).getString(USERNAME, "");
    }

    public static void removeUsername(Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(USERNAME);
        editor.apply();
    }

    public static void setPrivateKey(Context context, String privateKey){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(PRIVATE_KEY, privateKey);
        editor.apply();
    }

    public static String getPrivateKey(Context context){
        return getSharedPreference(context).getString(PRIVATE_KEY, "null");
    }

    public static void removePrivateKey(Context context){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(PRIVATE_KEY);
        editor.apply();
    }
}
