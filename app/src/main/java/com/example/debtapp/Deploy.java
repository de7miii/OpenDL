package com.example.debtapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.SaveSharedPreference;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;


public class Deploy {
    private final static String TAG = Deploy.class.getSimpleName();

    private final String PRIVATE_KEY = "3f30d2588cad03f8557c936db0e3af06afab6557a8af27f34a0c5f0993be09e3";
    private DebtFactory mDebtFactory;
    private Credentials credentials = Credentials.create(PRIVATE_KEY);
    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.182:7545"));
    private boolean isDeployed;

    public DebtFactory deployNewContract(Context context) throws ExecutionException, InterruptedException {
        DeployAsyncTask deployAsyncTask = new DeployAsyncTask(context);
        deployAsyncTask.onProgressUpdate(false);
        deployAsyncTask.execute();
        mDebtFactory = deployAsyncTask.get
                ();
        return mDebtFactory;
    }

    public DebtFactory loadDeployedContract(Context context) throws ExecutionException, InterruptedException {
        LoadAsyncTask loadAsyncTask = new LoadAsyncTask(context);
        loadAsyncTask.execute();
        mDebtFactory = loadAsyncTask.get();
        return mDebtFactory;
    }

    public String createNewDebt(DebtFactory debtFactory, int amount, String address, String description) throws ExecutionException, InterruptedException {
        CreateDebtAsyncTask createDebtAsyncTask = new CreateDebtAsyncTask();
        createDebtAsyncTask.execute(debtFactory, amount, address, description);
        return createDebtAsyncTask.get();
    }

    private class DeployAsyncTask extends AsyncTask<Void, Boolean, DebtFactory>{
        Context mContext;

        public DeployAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected DebtFactory doInBackground(Void... voids) {
            DebtFactory debtFactory;
            try {
                debtFactory = DebtFactory.deploy(web3j, credentials, new DefaultGasProvider()).sendAsync().get();
                SaveSharedPreference.setDeployedContractAddress(mContext, debtFactory.getContractAddress());
                publishProgress(debtFactory.isValid());
                return debtFactory;
            }catch (Exception e){
                Log.e(TAG, "Error Deploying Contract: ", e);
                isDeployed = false;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DebtFactory debtFactory) {
            super.onPostExecute(debtFactory);
            Log.i(TAG, "onPostExecute: " + debtFactory + "\n" + debtFactory.getContractAddress());
                mDebtFactory = debtFactory;
                isDeployed = true;
        }
    }

    private class LoadAsyncTask extends AsyncTask<Void, Boolean, DebtFactory> {
        private Context mContext;

        public LoadAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected DebtFactory doInBackground(Void... voids) {
            DebtFactory debtFactory;
            try {
                debtFactory = DebtFactory.load(SaveSharedPreference.getDeployedContractAddress(mContext), web3j, credentials, new DefaultGasProvider());
                publishProgress(debtFactory.isValid());;
                return debtFactory;
            }catch (Exception e){
                Log.e(TAG, "Error Loading Contract: ", e);
                isDeployed = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(DebtFactory debtFactory) {
            super.onPostExecute(debtFactory);
            Log.i(TAG, "onPostExecute: " + debtFactory + "\n" + debtFactory.getContractAddress());
                mDebtFactory = debtFactory;
                isDeployed = true;
        }
    }

    private class CreateDebtAsyncTask extends AsyncTask<Object, Boolean, String> {

        @Override
        protected String doInBackground(Object... objects) {
            DebtFactory tempDebtFactory = (DebtFactory) objects[0];
            int amount = (int) objects[1];
            String borrower = (String) objects[2];
            String description = (String) objects[3];
            String debtAddress = null;
            TransactionReceipt receipt;
            try {
                if (tempDebtFactory != null) {
                    BigInteger amountBigInt = BigInteger.valueOf(amount);
                    receipt = tempDebtFactory.createDebt(amountBigInt, borrower, description).sendAsync().get();
                    debtAddress = receipt.getContractAddress();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return debtAddress;
        }

        @Override
        protected void onPostExecute(String deployedAddress) {
            super.onPostExecute(deployedAddress);
            if (deployedAddress == null) {
                Log.i(TAG, "onPostExecute: Debt not created successfully");
            }
        }
    }

    private class LoadDebtAsyncTask extends AsyncTask<String, Boolean, Debt> {

        @Override
        protected Debt doInBackground(String... strings) {
            String deployedAddress = strings[0];
            Debt deployedDebt;
            if (deployedAddress.isEmpty()) {
                return null;
            }else{
                deployedDebt = Debt.load(deployedAddress, web3j, credentials, new DefaultGasProvider());
                try {
//                    TransactionReceipt receipt = deployedDebt.amount().send();
//                    Log.i(TAG, "doInBackground: " + receipt.getTransactionHash());
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: ", e);
                }
            }
            return deployedDebt;
        }
    }
}
