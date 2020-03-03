package com.example.debtapp;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.debtapp.Utils.RegistrationState;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.example.debtapp.ViewModels.SignupViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;

    @BindView(R.id.signup_button)
    MaterialButton signupButton;

    @BindView(R.id.have_account_button)
    MaterialButton loginButton;

    @BindView(R.id.username_text_input)
    TextInputLayout usernameInput;

    @BindView(R.id.password_text_input)
    TextInputLayout passwordInput;


    private String username;
    private String password;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        signupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController mNavController = Navigation.findNavController(view);

        loginButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signuptologin));

        signupButton.setOnClickListener(v -> {
            username = usernameInput.getEditText().getText().toString();
            password = passwordInput.getEditText().getText().toString();

            if (password.isEmpty() || password.length() + 1 < 8){
                passwordInput.setError("Incorrect Password");
            }else if (username.isEmpty()){
                usernameInput.setError("Username can't be empty");
            }else {
                signupViewModel.signUpNewAccount(username, password);
                SaveSharedPreference.setUsername(getContext(), username);
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        signupViewModel.userCanclledRegistration();
                        mNavController.popBackStack(R.id.login_dest,false);
                    }
                });

        signupViewModel.registrationState.observe(getViewLifecycleOwner(), registrationState -> {
            if (registrationState == RegistrationState.REGISTRATION_COMPLETED){
                loginViewModel.authenticate(username, password);
                mNavController.navigate(R.id.action_logintohome);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
