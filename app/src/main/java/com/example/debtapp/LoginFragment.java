package com.example.debtapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
    private String TAG = LoginFragment.class.getSimpleName();

    private NavController mNavController;


    private LoginViewModel loginViewModel;

    @BindView(R.id.login_button)
    MaterialButton loginBtn;

    @BindView(R.id.scanQr_button)
    MaterialButton scanQrBtn;

    @BindView(R.id.privateKey_text_input)
    TextInputLayout privateKeyInput;


    private String mPrivateKey;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        assert privateKeyInput.getEditText() != null;

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

        loginBtn.setOnClickListener(v -> {

            assert !mPrivateKey.isEmpty();
            if (privateKeyInput.getEditText().getText().toString().isEmpty()){
                Snackbar.make(view, "Private Key Can't Be Empty", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            assert getContext() != null;
            assert getActivity() != null;

            //Hide Keyboard.
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

            loginViewModel.authenticate(mPrivateKey);
            SaveSharedPreference.setPrivateKey(getContext(), mPrivateKey);
        });

        scanQrBtn.setOnClickListener(v -> {
            Intent scannerIntent = new Intent(getContext(), ScanQrActivity.class);
            startActivityForResult(scannerIntent, 1);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String qrResult;
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                qrResult = data.getStringExtra(ScanQrActivity.QR_CODE_RESULT);
                Log.i(TAG, "onActivityResult: " + qrResult);
                if (WalletUtils.isValidPrivateKey(qrResult)) {
                    mPrivateKey = qrResult;
                    loginViewModel.authenticate(mPrivateKey);
                    SaveSharedPreference.setPrivateKey(getContext(), mPrivateKey);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loginViewModel.authenticateState.observe(getViewLifecycleOwner(), authenticationState -> {
            switch (authenticationState) {
                case AUTHENTICATED:
                    if (!SaveSharedPreference.getPrivateKey(getContext()).isEmpty()) {
                        mNavController.popBackStack();
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
