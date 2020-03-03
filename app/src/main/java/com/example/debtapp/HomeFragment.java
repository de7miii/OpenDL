package com.example.debtapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.debtapp.Utils.RegistrationState;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.example.debtapp.ViewModels.SignupViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private NavController mNavController;
    private LoginViewModel mLoginViewModel;
    private SignupViewModel mSignupViewModel;

    @BindView(R.id.deploy_btn)
    Button deployBtn;

    @BindView(R.id.logout_btn)
    Button logoutBtn;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mSignupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Deploy deploy = new Deploy();
        deployBtn.setOnClickListener(v -> {
            if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                deploy.deployNewContract(getContext());
            }else{
                deploy.loadDeployedContract(getContext());
            }
        });

        logoutBtn.setOnClickListener(v -> {
            if (SaveSharedPreference.isLoggedIn(getContext())){
                mLoginViewModel.logOut(getContext());
                mNavController.popBackStack(R.id.home_dest, false);
                requireActivity().finish();
            }else {
                mNavController.popBackStack(R.id.home_dest, false);
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        if (!SaveSharedPreference.isLoggedIn(getContext())){
            mLoginViewModel.authenticateState.observe(getViewLifecycleOwner(), authenticationState -> {
                switch (authenticationState){
                    case UNAUTHENTICATED:
                        SaveSharedPreference.setLoginStatus(getContext(), false);
                        mNavController.navigate(R.id.action_hometologin);
                        break;
                    case AUTHENTICATED:
                        mSignupViewModel.registrationState.setValue(RegistrationState.REGISTRED);
                        SaveSharedPreference.setLoginStatus(getContext(), true);
                        break;
                }
            });
        }
    }
}
