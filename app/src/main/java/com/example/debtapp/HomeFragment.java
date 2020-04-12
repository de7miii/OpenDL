package com.example.debtapp;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.debtapp.Adapters.HomeAdapter;
import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.RegistrationState;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.example.debtapp.ViewModels.LoginViewModel;
import com.example.debtapp.ViewModels.SignupViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private String mPrivateKey = null;
    private DebtFactory mDebtFactory;
    private Credentials mCredentials;
    private Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/257f79e9b33946abb69bd1adb3e108e4"));

    private MutableLiveData<Boolean> logingStatus = new MutableLiveData<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mSignupViewModel = new ViewModelProvider(requireActivity()).get(SignupViewModel.class);
        setHasOptionsMenu(true);

        Log.i(TAG, "onCreate: Saved Address" + SaveSharedPreference.getDeployedContractAddress(getContext()));

        mAdapter = new HomeAdapter(getContext(), this);
        mDebts = new ArrayList<>();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_settings){
            Bundle bundle = new Bundle();
            assert mPrivateKey != null;
            bundle.putString("priKey", mPrivateKey);
            assert mCredentials != null;
            bundle.putString("pubKey", mCredentials.getAddress());
            mNavController.navigate(R.id.settingFragment, bundle);
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        createFap.setVisibility(View.INVISIBLE);

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        if (mPrivateKey == null){
            mPrivateKey = SaveSharedPreference.getPrivateKey(getContext());
            Log.i(TAG, "onCreate: Private Key" + mPrivateKey);
            if (mPrivateKey.equals("null")){
                mLoginViewModel.logOut(getContext());
                mNavController.popBackStack();
                mNavController.navigate(R.id.action_hometologin);
            }
        }

        if (getArguments() != null){
            if (getArguments().containsKey("priKey")) {
                mPrivateKey = getArguments().getString("priKey");
                Log.i(TAG, "onCreate: new private key: " + mPrivateKey);
            }
        }
        if (mPrivateKey != null && !mPrivateKey.isEmpty() && !mPrivateKey.equals("null")){
            mCredentials = Credentials.create(mPrivateKey);
        }else{
            mLoginViewModel.logOut(getContext());
            mNavController.popBackStack();
            mNavController.navigate(R.id.action_hometologin);
        }

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDebts.isEmpty() && mDebtFactory == null) {
            try {
                if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                    assert mCredentials != null;
                    CompletableFuture<DebtFactory> factoryCompletableFuture = DebtFactory.deploy(web3j, mCredentials, new DefaultGasProvider()).sendAsync();
                    new Thread(() -> {
                        try {
                            mDebtFactory = factoryCompletableFuture.get();
                            assert getActivity() != null;
                            getActivity().runOnUiThread(() -> {
                                assert mDebtFactory != null;
                                SaveSharedPreference.setDeployedContractAddress(getContext(), mDebtFactory.getContractAddress());
                                mAdapter.notifyDataSetChanged();
                                assert getView() != null;
                                Snackbar.make(getView().getRootView(), "Contract Deplyed at: " + mDebtFactory.getContractAddress(), BaseTransientBottomBar.LENGTH_LONG).show();
                            });
                        }catch (Exception e){
                            Log.e(TAG, "onCreateView: ", e);
                        }
                    }).start();
                } else {
                    Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                    assert mCredentials != null;
                    mDebtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(getContext())
                            , web3j, mCredentials, new DefaultGasProvider());
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Log.e(TAG, "onActivityCreated: deploy", e);
            }
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                assert getActivity() != null;
                getActivity().finish();
            }
        });

//        mDebts = new ArrayList<>();
        if (mDebtFactory != null && mDebts.isEmpty()){
            CompletableFuture<List> deplyedDebts = mDebtFactory.getDeployedDebts().sendAsync();
            new Thread(() -> {
                try {
                    List debts = deplyedDebts.get();
                    assert getActivity() != null;
                    getActivity().runOnUiThread(() -> {
                        Log.i(TAG, "onActivityCreated: deployed debts = "  + debts.size());
                        if (debts.size() == 0){
                            Snackbar.make(this.getView().getRootView(), "No Debts Deplyed Yet", BaseTransientBottomBar.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
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
                            createFap.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){
                    Log.e(TAG, "onActivityCreated: fetching debts", e);
                }
            }).start();
        } else {
            mAdapter.updateDebtsList(mDebts);
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            createFap.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        createFap.setOnClickListener(v -> {
            mDebts.clear();
            Bundle bundle = new Bundle();
            bundle.putString("priKey", mPrivateKey);
            mNavController.navigate(R.id.create_dest, bundle);
        });

        if (!mDebts.isEmpty()){
            progressBar.setVisibility(View.GONE);
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
        debtBundle.putString("priKey", mPrivateKey);
        mNavController.popBackStack();
        mNavController.navigate(R.id.details_dest, debtBundle);
    }

    public String getPrivateKey() {
        return mPrivateKey;
    }
}
