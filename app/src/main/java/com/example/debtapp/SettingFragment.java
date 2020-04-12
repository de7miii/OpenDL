package com.example.debtapp;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragmentCompat {
    private final String TAG = SettingFragment.class.getSimpleName();

    private NavController mNavController;

    private String mPrivateKey;
    private String mPublicKey;

    private EditTextPreference publicKeyPref;
    private EditTextPreference privateKeyPref;

    private LoginViewModel mLoginViewModel;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preference_menu, s);

        assert getArguments() != null;
        mPrivateKey = getArguments().getString("priKey");
        mPublicKey = getArguments().getString("pubKey");

        publicKeyPref = findPreference("pubKey");
        privateKeyPref = findPreference("priKey");
        if (privateKeyPref != null){
            privateKeyPref.setText(mPrivateKey);
            privateKeyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (o.toString().startsWith("0x")){
                        Log.i(TAG, "onPreferenceChange: case 1 pending new value: " + o);
                        mPrivateKey = o.toString();
                        privateKeyPref.setText(mPrivateKey);
                    }else{
                        Log.i(TAG, "onPreferenceChange: case 2 pending new value: " + "0x" + o);
                        mPrivateKey = "0x" + o.toString();
                        privateKeyPref.setText(mPrivateKey);
                    }
                    return false;
                }
            });
        }
        if (publicKeyPref != null){
            publicKeyPref.setText(mPublicKey);
            publicKeyPref.setEnabled(false);
        }
        Preference logOutPref = findPreference("logOut");
        assert logOutPref != null;
        logOutPref.setOnPreferenceClickListener(preference -> {
            if (preference.getKey().equals("logOut")){
                if (SaveSharedPreference.isLoggedIn(getContext())) {
                    mLoginViewModel.logOut(getContext());
                    mNavController.popBackStack();
                    mNavController.navigate(R.id.home_dest);
                } else {
                    mNavController.popBackStack();
                    mNavController.navigate(R.id.home_dest);
                }
            }
            return false;
        });
        setHasOptionsMenu(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mNavController.popBackStack();
                Bundle bundle = new Bundle();
                bundle.putString("priKey", mPrivateKey);
                mNavController.navigate(R.id.home_dest, bundle);
            }
        });
    }
}
