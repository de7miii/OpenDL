package com.example.debtapp;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.web3j.crypto.WalletUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private NavController mNavController;


    private LoginViewModel loginViewModel;

    @BindView(R.id.login_button)
    MaterialButton loginButton;

    @BindView(R.id.login_signup_button)
    MaterialButton signupButton;

    @BindView(R.id.password_text_input)
    TextInputLayout passwordInput;

    @BindView(R.id.username_text_input)
    TextInputLayout usernameInput;

    @BindView(R.id.mnemonic_text_input)
    TextInputLayout privateKeyInput;


    private String mUsername;
    private String mPassword;
    private String mPrivateKey;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

//        signupButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_logintosignup));

        assert usernameInput.getEditText() != null;
        assert passwordInput.getEditText() != null;
        assert privateKeyInput.getEditText() != null;

        usernameInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (newText.isEmpty()){
                    usernameInput.setError("Username can't be empty");
                }else{
                    mUsername = newText;
                }
            }
        });

        passwordInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (newText.isEmpty()) {
                    passwordInput.setError("Password can't be empty");
                }else if ( newText.length() + 1 < 8){
                    passwordInput.setError("Password must be at least 8 characters long");
                }else {
                    mPassword = newText;
                }
            }
        });

        privateKeyInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (WalletUtils.isValidPrivateKey(newText)){
                    mPrivateKey = newText;
                }else{
                    privateKeyInput.setError("Invalid Private Key");
                }
            }
        });

        loginButton.setOnClickListener(v -> {

            assert !mUsername.isEmpty();
            assert !mPassword.isEmpty();
            assert !mPrivateKey.isEmpty();
            if (privateKeyInput.getEditText().getText().toString().isEmpty()){
                Snackbar.make(view, "Private Key Can't Be Empty", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            loginViewModel.authenticate(mUsername, mPassword);
            SaveSharedPreference.setPrivateKey(getContext(), mPrivateKey);
            SaveSharedPreference.setUsername(getContext(), mUsername);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginViewModel.authenticateState.observe(getViewLifecycleOwner(), authenticationState -> {
            switch (authenticationState) {
                case AUTHENTICATED:
                    if (!SaveSharedPreference.getPrivateKey(getContext()).isEmpty()) {
                        mNavController.navigate(R.id.action_logintohome);
                    }
                    break;
                case INVALID_AUTHENTICATION:
                    Toast.makeText(getContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                loginViewModel.refuseAuthentication();
                mNavController.popBackStack();
                requireActivity().finish();
            }
        });

    }
}
