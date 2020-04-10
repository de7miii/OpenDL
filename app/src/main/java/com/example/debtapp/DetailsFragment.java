package com.example.debtapp;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.debtapp.Contracts.Debt;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    private final String TAG = DetailsFragment.class.getSimpleName();

    @BindView(R.id.lender_text)
    TextView lenderTextView;
    @BindView(R.id.borrower_text)
    TextView borrowerTextView;
    @BindView(R.id.description_text)
    TextView descriptionTextView;
    @BindView(R.id.amount_text)
    TextView amountTextView;
    @BindView(R.id.status_text)
    TextView statusTextView;
    @BindView(R.id.settle_btn)
    Button settleBtn;

    private DebtPOJO mDebt;
    private Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/257f79e9b33946abb69bd1adb3e108e4"));
    private Debt mDebtContract = null;
    private  String mPrivateKey;
    private Credentials mCredentials;

    private NavController mNavController;
    // TODO: 3/27/2020 implement the settling logic + add button to the layout
    // TODO: 3/27/2020 add a QRCode with the contract address
    // TODO: 3/27/2020 add etherscan support and show the contract on etherscan.

    public DetailsFragment() {
        // Required empty public constructor
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

        assert getArguments() != null;
        mDebtContract = Debt.load(getArguments().getString("address"), web3j, mCredentials, new DefaultGasProvider());
        new Thread(() -> {
            String lender = "", borrower = "", description = "";
            BigInteger amount = BigInteger.ZERO;
            Boolean status = false;
            try {
                Tuple5 debtDetails = mDebtContract.getDetails().sendAsync().get();
                lender = (String) debtDetails.component1();
                borrower = (String) debtDetails.component2();
                amount = (BigInteger) debtDetails.component3();
                description = (String) debtDetails.component4();
                status = (Boolean) debtDetails.component5();
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.i(TAG, "onViewCreated: " + description + " " + status);
            double amountInEther = Convert.fromWei(amount.toString(), Convert.Unit.WEI).doubleValue();
            mDebt = new DebtPOJO(lender, borrower, description, amountInEther, status);
            mDebt.setAddress(getArguments().getString("address"));
            assert getActivity() != null;
            getActivity().runOnUiThread(() -> {
                if (mDebt != null){
                    lenderTextView.setText(mDebt.getLender());
                    borrowerTextView.setText(mDebt.getBorrower());
                    descriptionTextView.setText(mDebt.getDescription());
                    StringBuilder amountString = new StringBuilder();
                    amountString.append(mDebt.getAmount()).append(" WEI");
                    amountTextView.setText(amountString.toString());
                    statusTextView.setText(String.valueOf(mDebt.isSettled()));
                    if (mDebt.isSettled()){
                        settleBtn.setEnabled(false);
                    }else{
                        settleBtn.setEnabled(true);
                    }
                }
            });
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        settleBtn.setOnClickListener((v) ->{
            if (mDebtContract != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Private Key");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(mPrivateKey);
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    if (input.getText().toString().startsWith("0x")){
                        mPrivateKey = input.getText().toString();
                        mCredentials = Credentials.create(mPrivateKey);
                    }else{
                        mPrivateKey = "0x" + input.getText().toString();
                        mCredentials = Credentials.create(mPrivateKey);
                    }
                    if (mCredentials.getAddress().equals(mDebt.getBorrower())){
                        Log.i(TAG, "onViewCreated: Settle Button: " + mCredentials.getAddress());
                        mDebtContract = Debt.load(mDebt.getAddress(), web3j, mCredentials, new DefaultGasProvider());
                        CompletableFuture<TransactionReceipt> call = mDebtContract.settleDebt().sendAsync();
                        new Thread(() ->{
                            try {
                                TransactionReceipt receipt = call.get();
                                Log.i(TAG, "onViewCreated: transaction status: " + receipt.isStatusOK());
                                assert getActivity() != null;
                                getActivity().runOnUiThread(() -> {
                                    mNavController.popBackStack();
                                    mNavController.navigate(R.id.home_dest);
                                });
                            }catch (Exception e){
                                Log.e(TAG, "onViewCreated: Settle Button: ", e);
                            }
                        }).start();
                    }else{
                        Snackbar.make(view, "Only Borrower Allowed to Settle His Debt", BaseTransientBottomBar.LENGTH_LONG).show();
                        Log.i(TAG, "onViewCreated: Only Borrower Allowed to settle his debt");
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mNavController.popBackStack();
                mNavController.navigate(R.id.home_dest);
            }
        });
    }
}
