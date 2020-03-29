package com.example.debtapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private DebtPOJO mDebt;

    // TODO: 3/26/2020 Design the details page (DONE)

    // TODO: 3/27/2020 implement the settling logic + add button to the layout
    // TODO: 3/27/2020 add a QRCode with the contract address
    // TODO: 3/27/2020 add etherscan support and show the contract on etherscan.

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mDebt = new DebtPOJO(getArguments().getString("lender"),
                getArguments().getString("borrower"),
                getArguments().getString("description"),
                getArguments().getDouble("amount"),
                getArguments().getBoolean("status"));

        Log.i(TAG, "onActivityCreated: Amount " + getArguments().getDouble("amount") + "\n");
        Log.i(TAG, "onActivityCreated: Lender " + getArguments().getString("lender") + "\n");
        Log.i(TAG, "onActivityCreated: Borrower " + getArguments().getString("borrower") + "\n");
        Log.i(TAG, "onActivityCreated: Description " + getArguments().getString("description"));
        Log.i(TAG, "onActivityCreated: Status " + getArguments().getBoolean("status"));
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
        if (mDebt != null){
            lenderTextView.setText(mDebt.getLender());
            borrowerTextView.setText(mDebt.getBorrower());
            descriptionTextView.setText(mDebt.getDescription());
            StringBuilder amount = new StringBuilder();
            amount.append(mDebt.getAmount()).append(" WEI");
            amountTextView.setText(amount.toString());
            statusTextView.setText(String.valueOf(mDebt.isSettled()));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
