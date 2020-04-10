package com.example.debtapp.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.debtapp.Utils.AuthenticationState;
import com.example.debtapp.Utils.SaveSharedPreference;

public class LoginViewModel extends AndroidViewModel {

    public final MutableLiveData<AuthenticationState> authenticateState = new MediatorLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authenticateState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    public void authenticate(String username, String password){
        if (passwordIsValidForUsername(username, password)){
            authenticateState.setValue(AuthenticationState.AUTHENTICATED);
        }else {
            authenticateState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
        }
    }

    // TODO: 4/8/2020 implement a cloud database to store login credentials
    private boolean passwordIsValidForUsername(String username, String password){
        return true;
    }

    public void refuseAuthentication(){
        authenticateState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    public boolean logOut(Context context){
        if (SaveSharedPreference.isLoggedIn(context)){
            SaveSharedPreference.setLoginStatus(context, false);
            SaveSharedPreference.removeUsername(context);
            SaveSharedPreference.removePrivateKey(context);
            authenticateState.setValue(AuthenticationState.UNAUTHENTICATED);
            return true;
        }else{
            return false;
        }
    }
}
