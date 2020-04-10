package com.example.debtapp;

import com.example.debtapp.Contracts.Debt;
import com.example.debtapp.Contracts.DebtFactory;

import org.junit.Test;

import static org.junit.Assert.*;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
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


/**
 * local unit test, which will execute on the development machine (host).
 */
public class UnitTests {

    private Web3j web3j = Web3j.build(new HttpService("HTTP://192.168.43.183:7545"));
    private List<String> mAccounts = new ArrayList<>();

    public UnitTests() {
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
    public void endToEnd_test(){
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

            assertEquals("Deployed Debt address is invalid", deployedDebtFromFactory, deployedDebtFromEventsLog);

            Debt debt = Debt.load(deployedDebtFromFactory, web3j, manager, new DefaultGasProvider());
            Tuple5 debtInfo = debt.getDetails().send();

            assertNotNull("Failed to get debt contract info", debtInfo);
            assertNotNull("Failed to get lender address", debtInfo.component1());
            assertNotNull("Failed to get borrower address", debtInfo.component2());
            assertNotNull("Failed to get debt amount", debtInfo.component3());
            assertNotNull("Failed to get debt description", debtInfo.component4());
            assertNotNull("Failed to get debt status", debtInfo.component5());

            assertEquals("Expected Lender Address is Wrong", mAccounts.get(0), debtInfo.component1());
            assertEquals("Expected Borrower Address is Wrong", mAccounts.get(1), debtInfo.component2());
            assertEquals("Expected Amount is Wrong", Convert.toWei("2", Convert.Unit.ETHER).toBigInteger(), debtInfo.component3());
            assertEquals("Expected Description is Wrong", "testing", debtInfo.component4());
            assertEquals("Expected Debt Status is Wrong", false, debtInfo.component5());

            TransactionManager borrowerManager = new ClientTransactionManager(web3j, mAccounts.get(1));

            debt = Debt.load(deployedDebtFromFactory, web3j, borrowerManager, new DefaultGasProvider());
            debt.settleDebt().send();
            debtInfo = debt.getDetails().send();
            assertEquals("Expected Debt Status After Settling is Wrong", true, debtInfo.component5());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}