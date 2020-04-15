package com.example.debtapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.SaveSharedPreference;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.scanQr_button)
    MaterialButton scanQrBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private DebtFactory mDebtFactory;
    private Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/257f79e9b33946abb69bd1adb3e108e4"));
    private String mPrivateKey;
    private Credentials mCredentials;
    private String mUnitString = "WEI";

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

        mCredentials = Credentials.create(mPrivateKey);

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){
                assert data != null;
                String qrResult;
                qrResult = data.getStringExtra(ScanQrActivity.QR_CODE_RESULT);
                if (WalletUtils.isValidAddress(qrResult)){
                    assert borrowerTextInput.getEditText() != null;
                    borrowerTextInput.getEditText().setText(qrResult);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);

        scanQrBtn.setOnClickListener(v -> {
            Intent scanIntent = new Intent(getContext(), ScanQrActivity.class);
            startActivityForResult(scanIntent, 2);
        });

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

            new Thread(() -> {
                try {
                    TransactionReceipt receipt = null;
                    CompletableFuture<TransactionReceipt> call = Transfer.sendFunds(web3j,
                            mCredentials,
                            borrowerAddress,
                            BigDecimal.valueOf(amount),
                            Convert.Unit.ETHER).sendAsync();
                    TransactionReceipt etherReceipt = call.get();
                    if (etherReceipt.isStatusOK()){
                        String txHash = etherReceipt.getTransactionHash();
                        Log.i(TAG, "onViewCreated: Tx hash: " + txHash);
                        receipt = mDebtFactory.createDebt(BigInteger.valueOf(amount),
                                borrowerAddress, description, txHash).send();
                    }
                    assert receipt != null;
                    List<DebtFactory.ContractCreatedEventResponse> events = mDebtFactory.getContractCreatedEvents(receipt);
                    Log.i(TAG, "updateUI: event test: " + events.get(0).newAddress + "\t" + events.get(0).log.getAddress());
                    Log.i(TAG, "updateUI: " + receipt.getFrom() + "\n" + receipt.getTransactionHash());
                    newDebt.setLender(receipt.getFrom());
                    Bundle debtBundle = new Bundle();
                    debtBundle.putString("address", events.get(0).newAddress);
                    debtBundle.putString("txHash", receipt.getTransactionHash());
                    debtBundle.putString("unit", mUnitString);
                    if (receipt.isStatusOK()) {
                        assert getActivity() != null;
                        Thread.yield();
                        getActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            mNavController.popBackStack();
                            mNavController.navigate(R.id.details_dest, debtBundle);
                        });
                    }
                }catch (Exception e){
                    Log.e(TAG, "onViewCreated: ", e);
                }
            }).start();
        });
    }
}
