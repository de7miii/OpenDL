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
import android.widget.Toast;

import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

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
    TextInputLayout mnemonicInput;


    private String username;
    private String password;
    private String mnemonic;


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

        signupButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_logintosignup));

        loginButton.setOnClickListener(v -> {
            assert usernameInput.getEditText() != null;
            assert passwordInput.getEditText() != null;
            assert mnemonicInput.getEditText() != null;

            username = usernameInput.getEditText().getText().toString();
            mnemonic = mnemonicInput.getEditText().getText().toString();
            password = passwordInput.getEditText().getText().toString();
            if (username.isEmpty()) {
                usernameInput.setError("Username can't be empty");
            }
            if (password.isEmpty() || password.length() + 1 < 8) {
                passwordInput.setError("Password must be at least 8 characters");
            }
            loginViewModel.authenticate(username, password);
            SaveSharedPreference.setUsername(getContext(), username);
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginViewModel.authenticateState.observe(getViewLifecycleOwner(), authenticationState -> {
            switch (authenticationState) {
                case AUTHENTICATED:
                    Bundle bundle = new Bundle();
                    bundle.putString("mnemonic", mnemonic);
                    mNavController.navigate(R.id.action_logintohome, bundle);
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
