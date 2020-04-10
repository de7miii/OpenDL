package com.example.debtapp;

import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.web3j.crypto.ContractUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.ens.Contracts;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.183:7545"));
    List<String> mAccounts = new ArrayList<>();

    public ExampleUnitTest() {
        try{
            EthAccounts accounts = web3j.ethAccounts().send();
            mAccounts = accounts.getAccounts();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void addressValid_test() {
        assertTrue("Address Validity Test Failed", WalletUtils.isValidAddress(mAccounts.get(0)));
    }

    @Test
    public void debtFactoryDeployment_test(){
        TransactionManager manager = new ClientTransactionManager(web3j, mAccounts.get(0));
        try{
            DebtFactory factory = DebtFactory.deploy(web3j, manager, new DefaultGasProvider()).send();
            assertTrue("DebtFactory Contract Deployment Failed", factory.isValid());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void debtDeployment_test(){
        TransactionManager manager = new ClientTransactionManager(web3j, mAccounts.get(0));
        try{
            Debt debt = Debt.deploy(web3j, manager,
                    new DefaultGasProvider(),
                    BigInteger.valueOf(1000000),
                    mAccounts.get(0),
                    mAccounts.get(1),
                    "testing").send();
            assertTrue("Debt Contract Deployment Failed", debt.isValid());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void createDebt_test(){
        TransactionManager manager = new ClientTransactionManager(web3j, mAccounts.get(0));
        try{
            DebtFactory factory = DebtFactory.deploy(web3j, manager, new DefaultGasProvider()).send();
            assertTrue("DebtFactory Contract Deployment Failed", factory.isValid());
            TransactionReceipt receipt = factory.createDebt(Convert.toWei("2", Convert.Unit.ETHER).toBigInteger(),
                    mAccounts.get(1), "testing").send();
            assertTrue("Debt Contract Creation From DebtFactory Failed", receipt.isStatusOK());

            List<DebtFactory.ContractCreatedEventResponse> events = factory.getContractCreatedEvents(receipt);
            String deployedDebtFromEventsLog = events.get(0).newAddress;
            String deployedDebtFromFactory = (String) factory.getDeployedDebts().send().get(0);

            assertSame("Deployed Debt address is invalid", deployedDebtFromFactory, deployedDebtFromEventsLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}