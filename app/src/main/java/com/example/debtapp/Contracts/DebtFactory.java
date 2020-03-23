package com.example.debtapp.Contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
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
    private static final String BINARY = "0x608060405234801561001057600080fd5b50610d82806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063183d7e081461004657806342e2bbba146100a557806385af9b3f146101ca575b600080fd5b61004e610238565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610091578082015181840152602081019050610076565b505050509050019250505060405180910390f35b610188600480360360608110156100bb57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561010257600080fd5b82018360208201111561011457600080fd5b8035906020019184600183028401116401000000008311171561013657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506102c6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101f6600480360360208110156101e057600080fd5b81019080803590602001909291905050506104e2565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b606060008054806020026020016040519081016040528092919081815260200182805480156102bc57602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610272575b5050505050905090565b60008273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141561030157600080fd5b6000843385856040516103139061051e565b808581526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156103ba57808201518184015260208101905061039f565b50505050905090810190601f1680156103e75780820380516001836020036101000a031916815260200191505b5095505050505050604051809103906000f08015801561040b573d6000803e3d6000fd5b50905060008190806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507fcf78cf0d6f3d8371e1075c69c492ab4ec5d8cf23a1a239b6a51a1d00be7ca31281604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a1809150509392505050565b600081815481106104ef57fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6108228061052c8339019056fe608060405234801561001057600080fd5b506040516108223803806108228339818101604052608081101561003357600080fd5b810190808051906020019092919080519060200190929190805190602001909291908051604051939291908464010000000082111561007157600080fd5b8382019150602082018581111561008757600080fd5b82518660018202830111640100000000821117156100a457600080fd5b8083526020830192505050908051906020019080838360005b838110156100d85780820151818401526020810190506100bd565b50505050905090810190601f1680156101055780820380516001836020036101000a031916815260200191505b5060405250505082600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508360008190555081600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600390805190602001906101ab9291906101d0565b506000600460006101000a81548160ff02191690831515021790555050505050610275565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061021157805160ff191683800117855561023f565b8280016001018555821561023f579182015b8281111561023e578251825591602001919060010190610223565b5b50905061024c9190610250565b5090565b61027291905b8082111561026e576000816000905550600101610256565b5090565b90565b61059e806102846000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063aa8c217c1161005b578063aa8c217c14610171578063aafb96b61461018f578063bcead63e14610199578063fbbf93a0146101e35761007d565b80637284e416146100825780637df1f1b914610105578063a7b5c52b1461014f575b600080fd5b61008a6102de565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100ca5780820151818401526020810190506100af565b50505050905090810190601f1680156100f75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61010d61037c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101576103a2565b604051808215151515815260200191505060405180910390f35b6101796103b5565b6040518082815260200191505060405180910390f35b6101976103bb565b005b6101a1610432565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101eb610458565b604051808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018060200183151515158152602001828103825284818151815260200191508051906020019080838360005b8381101561029f578082015181840152602081019050610284565b50505050905090810190601f1680156102cc5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b60038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103745780601f1061034957610100808354040283529160200191610374565b820191906000526020600020905b81548152906001019060200180831161035757829003601f168201915b505050505081565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600460009054906101000a900460ff1681565b60005481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461041557600080fd5b6001600460006101000a81548160ff021916908315150217905550565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080600060606000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166000546003600460009054906101000a900460ff16818054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105515780601f1061052657610100808354040283529160200191610551565b820191906000526020600020905b81548152906001019060200180831161053457829003601f168201915b5050505050915094509450945094509450909192939456fea265627a7a72315820b2dc1172de96b7fe66d04ebf015696063c572e053933d2385dcdff1d645d3bb364736f6c63430005100032a265627a7a72315820116ed1ec74119de786bb5220150d7318c8e793fc241078782289fa833d701f8c64736f6c63430005100032";

    public static final String FUNC_DEBTS = "debts";

    public static final String FUNC_CREATEDEBT = "createDebt";

    public static final String FUNC_GETDEPLOYEDDEBTS = "getDeployedDebts";

    public static final Event CONTRACTCREATED_EVENT = new Event("ContractCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

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

    public RemoteFunctionCall<String> debts(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEBTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createDebt(BigInteger _amount, String _borrower, String _description) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEDEBT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(_borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getDeployedDebts() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETDEPLOYEDDEBTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
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

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class ContractCreatedEventResponse extends BaseEventResponse {
        public String newAddress;
    }
}
