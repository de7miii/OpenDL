package com.example.debtapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.debtapp.Contracts.DebtFactory;
import com.example.debtapp.Utils.SaveSharedPreference;

import org.web3j.abi.datatypes.Bool;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;


public class Deploy {
    private final static String TAG = Deploy.class.getSimpleName();

    private final String PRIVATE_KEY = "3f30d2588cad03f8557c936db0e3af06afab6557a8af27f34a0c5f0993be09e3";
    private DebtFactory mDebtFactory;
    private Credentials credentials = Credentials.create(PRIVATE_KEY);
    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.182:7545"));

    public DebtFactory deployNewContract(Context context){
        DeployAsyncTask deployAsyncTask = new DeployAsyncTask(context);
        deployAsyncTask.onProgressUpdate(false);
        deployAsyncTask.execute();
        return mDebtFactory;
    }

    public DebtFactory loadDeployedContract(Context context){
        LoadAsyncTask loadAsyncTask = new LoadAsyncTask(context);
        loadAsyncTask.execute();
        return mDebtFactory;
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
            if (debtFactory != null && debtFactory.getContractAddress() != null){
                mDebtFactory = debtFactory;
            }
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
                publishProgress(debtFactory.isValid());
                return debtFactory;
            }catch (Exception e){
                Log.e(TAG, "Error Loading Contract: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(DebtFactory debtFactory) {
            super.onPostExecute(debtFactory);
            Log.i(TAG, "onPostExecute: " + debtFactory + "\n" + debtFactory.getContractAddress());
            if (debtFactory != null && debtFactory.getContractAddress() != null){
                mDebtFactory = debtFactory;
            }
        }
    }
}
