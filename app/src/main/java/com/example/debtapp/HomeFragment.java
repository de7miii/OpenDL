package com.example.debtapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.debtapp.Adapters.HomeAdapter;
import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.RegistrationState;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.example.debtapp.ViewModels.SignupViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;
import jnr.a64asm.EXTEND_ENUM;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener{
    private final String TAG = HomeFragment.class.getSimpleName();

    private NavController mNavController;
    private LoginViewModel mLoginViewModel;
    private SignupViewModel mSignupViewModel;

    @BindView(R.id.rv_home)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.create_fab)
    FloatingActionButton createFap;

    private HomeAdapter mAdapter;
    private List<DebtPOJO> mDebts;

    private final String PRIVATE_KEY = "3f30d2588cad03f8557c936db0e3af06afab6557a8af27f34a0c5f0993be09e3";
    private final String MNEMONIC = "duck style party style shaft chapter develop catch elbow upgrade city width";
    private DebtFactory mDebtFactory;
    private Credentials mCredentials = null;
//    private Credentials credentials = Credentials.create(PRIVATE_KEY);
    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.183:7545"));
    private String userMnemonic = null;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mSignupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);

        Log.i(TAG, "onCreate: Credentials using mnemonic: " + mCredentials.getAddress());

        assert getArguments() != null;
        userMnemonic = getArguments().getString("mnemonic");

        assert userMnemonic != null;
        mCredentials = WalletUtils.loadBip39Credentials(null, userMnemonic);

        mAdapter = new HomeAdapter(getContext(), this);
        mDebts = new ArrayList<>();

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


        if (mDebtFactory != null && mDebts.isEmpty()){
                CompletableFuture<List> deplyedDebts = mDebtFactory.getDeployedDebts().sendAsync();
                new Thread(() -> {
                    try {
                    List debts = deplyedDebts.get();
                    assert getActivity() != null;
                    getActivity().runOnUiThread(() -> {
                        Log.i(TAG, "onActivityCreated: deployed debts = "  + debts.size());
                        for (int i = 0; i < debts.size(); i++) {
                            String debtAddress = (String) debts.get(i);
                            Debt debt = Debt.load(debtAddress, web3j, mCredentials, new DefaultGasProvider());
                            String lender = "", borrower = "", description = "";
                            BigInteger amount = BigInteger.ZERO;
                            Boolean status = false;
                            try {
                                Tuple5 debtDetails = debt.getDetails().sendAsync().get();
                                 lender = (String) debtDetails.component1();
                                 borrower = (String) debtDetails.component2();
                                 amount = (BigInteger) debtDetails.component3();
                                 description = (String) debtDetails.component4();
                                 status = (Boolean) debtDetails.component5();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            double amountInEther = Convert.fromWei(amount.toString(), Convert.Unit.WEI).doubleValue();
                            DebtPOJO tempDebt = new DebtPOJO(lender, borrower, description, amountInEther, status);
                            tempDebt.setAddress(debtAddress);
                            mDebts.add(tempDebt);
                        }
                        if (!mDebts.isEmpty()){
                            mAdapter.updateDebtsList(mDebts);
                            mAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    }catch (Exception e){
                        Log.e(TAG, "onActivityCreated: fetching debts", e);
                    }
                }).start();
        }

        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        createFap.setOnClickListener(v -> {
            mDebts.clear();
            mNavController.navigate(R.id.create_dest);
        });

        if (!mDebts.isEmpty()){
            progressBar.setVisibility(View.GONE);
        }

//
//        logoutBtn.setOnClickListener(v -> {
//            if (SaveSharedPreference.isLoggedIn(getContext())){
//                mLoginViewModel.logOut(getContext());
//                mNavController.popBackStack(R.id.home_dest, false);
//                requireActivity().finish();
//            }else {
//                mNavController.popBackStack(R.id.home_dest, false);
//                requireActivity().finish();
//            }
//        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        progressBar.setVisibility(View.VISIBLE);

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
        if (mDebts.isEmpty() && mDebtFactory == null) {
            try {
                if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                    CompletableFuture<DebtFactory> factoryCompletableFuture = DebtFactory.deploy(web3j, mCredentials, new DefaultGasProvider()).sendAsync();
                    updateUI(factoryCompletableFuture);
                    Log.i(TAG, "deployed contract address: " + mDebtFactory.getContractAddress());
                } else {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                    mDebtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(getContext())
                            , web3j, mCredentials, new DefaultGasProvider());
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.e(TAG, "onActivityCreated: deploy", e);
            }
        }

    }

    @Override
    public void onItemClickListener(int pos) {
        DebtPOJO currentDebt = mDebts.get(pos);
        Bundle debtBundle = new Bundle();
        debtBundle.putDouble("amount", currentDebt.getAmount());
        debtBundle.putString("borrower", currentDebt.getBorrower());
        debtBundle.putString("lender", currentDebt.getLender());
        debtBundle.putString("description", currentDebt.getDescription());
        debtBundle.putBoolean("status", currentDebt.isSettled());
        debtBundle.putString("address", currentDebt.getAddress());
        mNavController.navigate(R.id.details_dest, debtBundle);
    }

    private void updateUI(CompletableFuture<DebtFactory> completableFuture){
        new Thread(() -> {
            try {
                DebtFactory debtFactory = completableFuture.get();
                if (debtFactory.isValid()) {
                    assert getActivity() != null;
                    getActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        mDebtFactory = debtFactory;
                        mAdapter.notifyDataSetChanged();
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

}
