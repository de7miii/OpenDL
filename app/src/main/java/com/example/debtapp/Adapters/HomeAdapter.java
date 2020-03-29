package com.example.debtapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.debtapp.DebtPOJO;
import com.example.debtapp.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CustomViewHolder> {
    private List<DebtPOJO> mDebts;
    private Context mContext;
    final private ItemClickListener mListener;

    public HomeAdapter(Context context, ItemClickListener itemClickListener) {
        mContext = context;
        mListener = itemClickListener;
    }

    // TODO: 3/25/2020 finish the recyclerview and the homepage in total.

    @NonNull
    @Override
    public HomeAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_debt_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.CustomViewHolder holder, int position) {
        DebtPOJO currentDebt = mDebts.get(position);
        holder.bind(currentDebt);
    }

    @Override
    public int getItemCount() {
        return mDebts != null ? mDebts.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView borrowerText, lenderText, amountText, statusText;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            lenderText = itemView.findViewById(R.id.lender_text);
            borrowerText = itemView.findViewById(R.id.borrower_text);
            amountText = itemView.findViewById(R.id.amount_text);
            statusText = itemView.findViewById(R.id.status_text);
            itemView.setOnClickListener(this);
        }

        void bind(DebtPOJO debt){
          if (debt != null){
              lenderText.setText(debt.getLender());
              borrowerText.setText(debt.getBorrower());
              amountText.setText(String.valueOf(debt.getAmount()));
              statusText.setText(String.valueOf(debt.isSettled()));
          }
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClickListener(getAdapterPosition());
        }
    }

    public void updateDebtsList(List<DebtPOJO> debts){
        mDebts = debts;
    }

    public interface ItemClickListener {
        void onItemClickListener(int pos);
    }
}
