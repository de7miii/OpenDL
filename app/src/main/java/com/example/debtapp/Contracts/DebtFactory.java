package com.example.debtapp.Contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class DebtFactory extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610d02806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063183d7e081461004657806359f025951461009e57806385af9b3f146101fc575b600080fd5b61004e610219565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561008a578181015183820152602001610072565b505050509050019250505060405180910390f35b6101e0600480360360808110156100b457600080fd5b8135916001600160a01b03602082013516918101906060810160408201356401000000008111156100e457600080fd5b8201836020820111156100f657600080fd5b8035906020019184600183028401116401000000008311171561011857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561016b57600080fd5b82018360208201111561017d57600080fd5b8035906020019184600183028401116401000000008311171561019f57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061027b945050505050565b604080516001600160a01b039092168252519081900360200190f35b6101e06004803603602081101561021257600080fd5b5035610459565b6060600080548060200260200160405190810160405280929190818152602001828054801561027157602002820191906000526020600020905b81546001600160a01b03168152600190910190602001808311610253575b5050505050905090565b6000336001600160a01b038516141561029357600080fd5b600085338686866040516102a690610480565b80868152602001856001600160a01b03166001600160a01b03168152602001846001600160a01b03166001600160a01b031681526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561031a578181015183820152602001610302565b50505050905090810190601f1680156103475780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561037a578181015183820152602001610362565b50505050905090810190601f1680156103a75780820380516001836020036101000a031916815260200191505b50975050505050505050604051809103906000f0801580156103cd573d6000803e3d6000fd5b50600080546001810182559080527f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5630180546001600160a01b0383166001600160a01b0319909116811790915560408051918252519192507fcf78cf0d6f3d8371e1075c69c492ab4ec5d8cf23a1a239b6a51a1d00be7ca312919081900360200190a195945050505050565b6000818154811061046657fe5b6000918252602090912001546001600160a01b0316905081565b61083f8061048e8339019056fe608060405234801561001057600080fd5b5060405161083f38038061083f833981810160405260a081101561003357600080fd5b81516020830151604080850151606086018051925194969395919493918201928464010000000082111561006657600080fd5b90830190602082018581111561007b57600080fd5b825164010000000081118282018810171561009557600080fd5b82525081516020918201929091019080838360005b838110156100c25781810151838201526020016100aa565b50505050905090810190601f1680156100ef5780820380516001836020036101000a031916815260200191505b506040526020018051604051939291908464010000000082111561011257600080fd5b90830190602082018581111561012757600080fd5b825164010000000081118282018810171561014157600080fd5b82525081516020918201929091019080838360005b8381101561016e578181015183820152602001610156565b50505050905090810190601f16801561019b5780820380516001836020036101000a031916815260200191505b506040525050600280546001600160a01b038088166001600160a01b031992831617909255600088905560018054928716929091169190911790555081516101ea906003906020850190610214565b5080516101fe906005906020840190610214565b50506004805460ff19169055506102af92505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061025557805160ff1916838001178555610282565b82800160010185558215610282579182015b82811115610282578251825591602001919060010190610267565b5061028e929150610292565b5090565b6102ac91905b8082111561028e5760008155600101610298565b90565b610581806102be6000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063aafb96b61161005b578063aafb96b614610164578063bcead63e1461016e578063eb2c89a314610176578063fbbf93a01461017e57610088565b80637284e4161461008d5780637df1f1b91461010a578063a7b5c52b1461012e578063aa8c217c1461014a575b600080fd5b6100956102a8565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100cf5781810151838201526020016100b7565b50505050905090810190601f1680156100fc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610112610336565b604080516001600160a01b039092168252519081900360200190f35b610136610345565b604080519115158252519081900360200190f35b61015261034e565b60408051918252519081900360200190f35b61016c610354565b005b61011261037a565b610095610389565b6101866103e4565b60405180876001600160a01b03166001600160a01b03168152602001866001600160a01b03166001600160a01b03168152602001858152602001806020018415151515815260200180602001838103835286818151815260200191508051906020019080838360005b838110156102075781810151838201526020016101ef565b50505050905090810190601f1680156102345780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561026757818101518382015260200161024f565b50505050905090810190601f1680156102945780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b6003805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561032e5780601f106103035761010080835404028352916020019161032e565b820191906000526020600020905b81548152906001019060200180831161031157829003601f168201915b505050505081565b6001546001600160a01b031681565b60045460ff1681565b60005481565b6001546001600160a01b0316331461036b57600080fd5b6004805460ff19166001179055565b6002546001600160a01b031681565b6005805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561032e5780601f106103035761010080835404028352916020019161032e565b6002805460018054600080546004546003805460408051602061010099841615999099026000190190921699909904601f8101889004880282018801909952888152939788978897606097899789976001600160a01b039485169794909216959194919360ff169260059285918301828280156104a25780601f10610477576101008083540402835291602001916104a2565b820191906000526020600020905b81548152906001019060200180831161048557829003601f168201915b5050845460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959850869450925084019050828280156105305780601f1061050557610100808354040283529160200191610530565b820191906000526020600020905b81548152906001019060200180831161051357829003601f168201915b5050505050905095509550955095509550955090919293949556fea264697066735822122058c98a60e5e0b0fff9d4916cdd19a6422e9948b0a307ad565eb1c078d51f7b6864736f6c63430006020033a2646970667358221220008b61352b4648e504e0e2d918e01dcd509df376cda700fb219516f475ab604864736f6c63430006020033";

    public static final String FUNC_CREATEDEBT = "createDebt";

    public static final String FUNC_DEBTS = "debts";

    public static final String FUNC_GETDEPLOYEDDEBTS = "getDeployedDebts";

    public static final Event CONTRACTCREATED_EVENT = new Event("ContractCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected DebtFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DebtFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DebtFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DebtFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ContractCreatedEventResponse> getContractCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRACTCREATED_EVENT, transactionReceipt);
        ArrayList<ContractCreatedEventResponse> responses = new ArrayList<ContractCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContractCreatedEventResponse typedResponse = new ContractCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContractCreatedEventResponse> contractCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ContractCreatedEventResponse>() {
            @Override
            public ContractCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRACTCREATED_EVENT, log);
                ContractCreatedEventResponse typedResponse = new ContractCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.newAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContractCreatedEventResponse> contractCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTCREATED_EVENT));
        return contractCreatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createDebt(BigInteger _amount, String _borrower, String _description, String _txHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEDEBT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description), 
                new org.web3j.abi.datatypes.Utf8String(_txHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> debts(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEBTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getDeployedDebts() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GETDEPLOYEDDEBTS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static DebtFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DebtFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DebtFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DebtFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DebtFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DebtFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DebtFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DebtFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DebtFactory> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DebtFactory.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DebtFactory> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DebtFactory.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DebtFactory> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DebtFactory.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DebtFactory> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DebtFactory.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ContractCreatedEventResponse extends BaseEventResponse {
        public String newAddress;
    }
}
