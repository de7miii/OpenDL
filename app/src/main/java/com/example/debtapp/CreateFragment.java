package com.example.debtapp;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import butterknife.Action;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {
    private final String TAG = CreateFragment.class.getSimpleName();

    private NavController mNavController;

    @BindView(R.id.amount_text_field)
    TextInputLayout amountTextInput;
    @BindView(R.id.borrower_text_field)
    TextInputLayout borrowerTextInput;
    @BindView(R.id.description_text_field)
    TextInputLayout descriptionTextInput;
    @BindView(R.id.create_debt_btn)
    Button createBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private DebtFactory mDebtFactory;
    private Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/257f79e9b33946abb69bd1adb3e108e4"));
    private String mPrivateKey;
    private Credentials mCredentials;

    private DebtPOJO newDebt;

    public CreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null){
            if (getArguments().containsKey("priKey")) {
                mPrivateKey = getArguments().getString("priKey");
                Log.i(TAG, "onCreate: new private key: " + mPrivateKey);
            }
        }

        mCredentials = Credentials.create(mPrivateKey);;

        try {
            if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                CompletableFuture<DebtFactory> factoryCompletableFuture = DebtFactory.deploy(web3j, mCredentials, new DefaultGasProvider()).sendAsync();
                new Thread(() -> {
                    try {
                        mDebtFactory = factoryCompletableFuture.get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
                Log.i(TAG, "deployed contract address: " + mDebtFactory.getContractAddress());
            } else {
                Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
                mDebtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(getContext())
                        ,web3j, mCredentials, new DefaultGasProvider());
                Log.i(TAG, "loaded contract address: " + mDebtFactory.getContractAddress());
            }
        }catch (Exception e){
            Log.e(TAG, "onCreate: ", e);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        createBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            newDebt = new DebtPOJO();

            assert amountTextInput.getEditText() != null;
            int amount = Integer.parseInt(amountTextInput.getEditText().getText().toString());
            newDebt.setAmount(amount);

            assert borrowerTextInput.getEditText() != null;
            String borrowerAddress = borrowerTextInput.getEditText().getText().toString();
            newDebt.setBorrower(borrowerAddress);

            assert descriptionTextInput.getEditText() != null;
            String description = descriptionTextInput.getEditText().getText().toString();
            newDebt.setDescription(description);
            CompletableFuture<TransactionReceipt> call = mDebtFactory.createDebt(BigInteger.valueOf(amount), borrowerAddress, description).sendAsync();
            new Thread(() -> {
                try {
                    TransactionReceipt receipt = call.get();
                    List<DebtFactory.ContractCreatedEventResponse> events = mDebtFactory.getContractCreatedEvents(receipt);
                    Log.i(TAG, "updateUI: event test: " + events.get(0).newAddress + "\t" + events.get(0).log.getAddress());
                    Log.i(TAG, "updateUI: " + receipt.getFrom() + "\n" + receipt.getTransactionHash());
                    newDebt.setLender(receipt.getFrom());

                    Bundle debtBundle = new Bundle();
                    debtBundle.putString("address", events.get(0).newAddress);
                    if (receipt.isStatusOK()) {
                        assert getActivity() != null;
                        getActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            mNavController.popBackStack();
                            mNavController.navigate(R.id.details_dest, debtBundle);
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
