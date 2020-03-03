package com.example.debtapp.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.debtapp.Utils.RegistrationState;

public class SignupViewModel extends AndroidViewModel {

    public final MutableLiveData<RegistrationState> registrationState =
            new MutableLiveData<>();

    public SignupViewModel(@NonNull Application application) {
        super(application);
        registrationState.setValue(RegistrationState.COLLECT_USER_DATA);
    }

    public void signUpNewAccount(String username, String password){
        registrationState.setValue(RegistrationState.REGISTRATION_COMPLETED);
    }

    public boolean userCanclledRegistration(){
        registrationState.setValue(RegistrationState.COLLECT_USER_DATA);
        return true;
    }
}
