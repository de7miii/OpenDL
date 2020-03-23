package com.example.debtapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateFragment extends Fragment {
    private final String TAG = CreateFragment.class.getSimpleName();

    @BindView(R.id.amount_text_field)
    TextInputLayout amountTextInput;
    @BindView(R.id.borrower_text_field)
    TextInputLayout borrowerTextInput;
    @BindView(R.id.description_text_field)
    TextInputLayout descriptionTextInput;
    @BindView(R.id.create_debt_btn)
    Button createBtn;


    private final String PRIVATE_KEY = "3f30d2588cad03f8557c936db0e3af06afab6557a8af27f34a0c5f0993be09e3";
    private DebtFactory mDebtFactory;
    private Credentials credentials = Credentials.create(PRIVATE_KEY);
    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.183:7545"));

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

        try {
            if (SaveSharedPreference.getDeployedContractAddress(getContext()).equals("null")) {
                Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
//                    DebtFactory factory = deploy.deployNewContract(getContext());
                CompletableFuture<DebtFactory> factoryCompletableFuture = DebtFactory.deploy(web3j, credentials, new DefaultGasProvider()).sendAsync();
                mDebtFactory = factoryCompletableFuture.get();
                Log.i(TAG, "deployed contract address: " + mDebtFactory.getContractAddress());
//                mNavController.navigate(R.id.create_dest);
            } else {
                Log.i(TAG, "saved contract address: " + SaveSharedPreference.getDeployedContractAddress(getContext()));
//                    DebtFactory factory = deploy.loadDeployedContract(getContext());
                mDebtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(getContext())
                        ,web3j, credentials, new DefaultGasProvider());
                Log.i(TAG, "loaded contract address: " + mDebtFactory.getContractAddress());
//                mNavController.navigate(R.id.create_dest);
            }
        }catch (Exception e){
            Log.e(TAG, "onActivityCreated: ", e);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Deploy deploy = new Deploy();

        createBtn.setOnClickListener(v -> {
            int amount = Integer.parseInt(amountTextInput.getEditText().getText().toString());
            String borrowerAddress = borrowerTextInput.getEditText().getText().toString();
            String description = descriptionTextInput.getEditText().getText().toString();

            try {
                CompletableFuture<TransactionReceipt> call = mDebtFactory.createDebt(BigInteger.valueOf(amount), borrowerAddress, description).sendAsync();
                TransactionReceipt receipt = call.get();
                Log.i(TAG, "onViewCreated: " + receipt.getTransactionHash() + "\n" + receipt.getBlockNumber());
            }catch (Exception e){
                Log.e(TAG, "onViewCreated: ", e);
            }
        });
    }
}
