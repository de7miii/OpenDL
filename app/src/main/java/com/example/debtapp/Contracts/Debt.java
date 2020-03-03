package com.example.debtapp.Contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class Debt extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516106173803806106178339818101604052608081101561003357600080fd5b81516020830151604080850151606086018051925194969395919493918201928464010000000082111561006657600080fd5b90830190602082018581111561007b57600080fd5b825164010000000081118282018810171561009557600080fd5b82525081516020918201929091019080838360005b838110156100c25781810151838201526020016100aa565b50505050905090810190601f1680156100ef5780820380516001836020036101000a031916815260200191505b506040525050600280546001600160a01b038087166001600160a01b0319928316179092556000879055600180549286169290911691909117905550805161013e906003906020840190610153565b50506004805460ff19169055506101ee915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061019457805160ff19168380011785556101c1565b828001600101855582156101c1579182015b828111156101c15782518255916020019190600101906101a6565b506101cd9291506101d1565b5090565b6101eb91905b808211156101cd57600081556001016101d7565b90565b61041a806101fd6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063aa8c217c1161005b578063aa8c217c1461013f578063aafb96b614610159578063bcead63e14610163578063fbbf93a01461016b5761007d565b80637284e416146100825780637df1f1b9146100ff578063a7b5c52b14610123575b600080fd5b61008a61022f565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100c45781810151838201526020016100ac565b50505050905090810190601f1680156100f15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101076102bd565b604080516001600160a01b039092168252519081900360200190f35b61012b6102cc565b604080519115158252519081900360200190f35b6101476102d5565b60408051918252519081900360200190f35b6101616102db565b005b610107610301565b610173610310565b60405180866001600160a01b03166001600160a01b03168152602001856001600160a01b03166001600160a01b031681526020018481526020018060200183151515158152602001828103825284818151815260200191508051906020019080838360005b838110156101f05781810151838201526020016101d8565b50505050905090810190601f16801561021d5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b6003805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102b55780601f1061028a576101008083540402835291602001916102b5565b820191906000526020600020905b81548152906001019060200180831161029857829003601f168201915b505050505081565b6001546001600160a01b031681565b60045460ff1681565b60005481565b6001546001600160a01b031633146102f257600080fd5b6004805460ff19166001179055565b6002546001600160a01b031681565b60028054600180546000805460045460038054604080516020601f6000199a851615610100029a909a019093169a909a0497880182900482028a018201905286895293978897889760609789976001600160a01b0393841697919093169590949360ff90911692909184918301828280156103cc5780601f106103a1576101008083540402835291602001916103cc565b820191906000526020600020905b8154815290600101906020018083116103af57829003601f168201915b5050505050915094509450945094509450909192939456fea264697066735822122010dd8124a214a8473b9b3cedbd61dbcc0d5f5ad28c6f57ecb87f72149d0bc57564736f6c63430006020033";

    public static final String FUNC_AMOUNT = "amount";

    public static final String FUNC_BORROWER = "borrower";

    public static final String FUNC_DESCRIPTION = "description";

    public static final String FUNC_GETDETAILS = "getDetails";

    public static final String FUNC_IS_SETTLED = "is_settled";

    public static final String FUNC_LENDER = "lender";

    public static final String FUNC_SETTLEDEBT = "settleDebt";

    @Deprecated
    protected Debt(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Debt(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Debt(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Debt(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> amount() {
        final Function function = new Function(
                FUNC_AMOUNT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> borrower() {
        final Function function = new Function(
                FUNC_BORROWER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> description() {
        final Function function = new Function(
                FUNC_DESCRIPTION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getDetails() {
        final Function function = new Function(
                FUNC_GETDETAILS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> is_settled() {
        final Function function = new Function(
                FUNC_IS_SETTLED, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> lender() {
        final Function function = new Function(
                FUNC_LENDER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> settleDebt() {
        final Function function = new Function(
                FUNC_SETTLEDEBT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Debt load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Debt(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Debt load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Debt(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Debt load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Debt(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Debt load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Debt(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Debt> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, BigInteger _amount, String _lender, String _borrower, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _lender), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(Debt.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Debt> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, BigInteger _amount, String _lender, String _borrower, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _lender), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(Debt.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Debt> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _amount, String _lender, String _borrower, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _lender), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(Debt.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Debt> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _amount, String _lender, String _borrower, String _description) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _lender), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)));
        return deployRemoteCall(Debt.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
