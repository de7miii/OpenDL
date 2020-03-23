package com.example.debtapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.RegistrationState;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.example.debtapp.ViewModels.SignupViewModel;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    private NavController mNavController;
    private LoginViewModel mLoginViewModel;
    private SignupViewModel mSignupViewModel;

    @BindView(R.id.deploy_btn)
    Button deployBtn;

    @BindView(R.id.logout_btn)
    Button logoutBtn;

    private final String PRIVATE_KEY = "3f30d2588cad03f8557c936db0e3af06afab6557a8af27f34a0c5f0993be09e3";
    private DebtFactory mDebtFactory;
    private Credentials credentials = Credentials.create(PRIVATE_KEY);
    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.183:7545"));


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
            try {
                if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
//                    DebtFactory factory = deploy.deployNewContract(getContext());
                    CompletableFuture<DebtFactory> factoryCompletableFuture = DebtFactory.deploy(web3j, credentials, new DefaultGasProvider()).sendAsync();
                    mDebtFactory = factoryCompletableFuture.get();
                    Log.i(TAG, "deployed contract address: " + mDebtFactory.getContractAddress());
                    mNavController.navigate(R.id.create_dest);
                } else {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
//                    DebtFactory factory = deploy.loadDeployedContract(getContext());
                    mDebtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(getContext())
                            ,web3j, credentials, new DefaultGasProvider());
                    Log.i(TAG, "loaded contract address: " + mDebtFactory.getContractAddress());
                    mNavController.navigate(R.id.create_dest);
                }
            }catch (Exception e){
                Log.e(TAG, "onActivityCreated: ", e);
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
