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
    private static final String BINARY = "0x608060405234801561001057600080fd5b50611191806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063183d7e081461004657806359f02595146100a557806385af9b3f14610261575b600080fd5b61004e6102cf565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b83811015610091578082015181840152602081019050610076565b505050509050019250505060405180910390f35b61021f600480360360808110156100bb57600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561010257600080fd5b82018360208201111561011457600080fd5b8035906020019184600183028401116401000000008311171561013657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561019957600080fd5b8201836020820111156101ab57600080fd5b803590602001918460018302840111640100000000831117156101cd57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061035d565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61028d6004803603602081101561027757600080fd5b81019080803590602001909291905050506105e7565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6060600080548060200260200160405190810160405280929190818152602001828054801561035357602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610309575b5050505050905090565b60008373ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141561039857600080fd5b600085338686866040516103ab90610623565b808681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561045657808201518184015260208101905061043b565b50505050905090810190601f1680156104835780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156104bc5780820151818401526020810190506104a1565b50505050905090810190601f1680156104e95780820380516001836020036101000a031916815260200191505b50975050505050505050604051809103906000f08015801561050f573d6000803e3d6000fd5b50905060008190806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507fcf78cf0d6f3d8371e1075c69c492ab4ec5d8cf23a1a239b6a51a1d00be7ca31281604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390a180915050949350505050565b600081815481106105f457fe5b906000526020600020016000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b610b2c806106318339019056fe608060405234801561001057600080fd5b50604051610b2c380380610b2c833981810160405260a081101561003357600080fd5b810190808051906020019092919080519060200190929190805190602001909291908051604051939291908464010000000082111561007157600080fd5b8382019150602082018581111561008757600080fd5b82518660018202830111640100000000821117156100a457600080fd5b8083526020830192505050908051906020019080838360005b838110156100d85780820151818401526020810190506100bd565b50505050905090810190601f1680156101055780820380516001836020036101000a031916815260200191505b506040526020018051604051939291908464010000000082111561012857600080fd5b8382019150602082018581111561013e57600080fd5b825186600182028301116401000000008211171561015b57600080fd5b8083526020830192505050908051906020019080838360005b8381101561018f578082015181840152602081019050610174565b50505050905090810190601f1680156101bc5780820380516001836020036101000a031916815260200191505b5060405250505083600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508460008190555082600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550816003908051906020019061026292919061029f565b506000600460006101000a81548160ff021916908315150217905550806005908051906020019061029492919061029f565b505050505050610344565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102e057805160ff191683800117855561030e565b8280016001018555821561030e579182015b8281111561030d5782518255916020019190600101906102f2565b5b50905061031b919061031f565b5090565b61034191905b8082111561033d576000816000905550600101610325565b5090565b90565b6107d9806103536000396000f3fe608060405234801561001057600080fd5b50600436106100885760003560e01c8063aafb96b61161005b578063aafb96b61461019a578063bcead63e146101a4578063eb2c89a3146101ee578063fbbf93a01461027157610088565b80637284e4161461008d5780637df1f1b914610110578063a7b5c52b1461015a578063aa8c217c1461017c575b600080fd5b6100956103d8565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d55780820151818401526020810190506100ba565b50505050905090810190601f1680156101025780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b610118610476565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61016261049c565b604051808215151515815260200191505060405180910390f35b6101846104af565b6040518082815260200191505060405180910390f35b6101a26104b5565b005b6101ac61052c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6101f6610552565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561023657808201518184015260208101905061021b565b50505050905090810190601f1680156102635780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6102796105f0565b604051808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001858152602001806020018415151515815260200180602001838103835286818151815260200191508051906020019080838360005b83811015610331578082015181840152602081019050610316565b50505050905090810190601f16801561035e5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561039757808201518184015260208101905061037c565b50505050905090810190601f1680156103c45780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b60038054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561046e5780601f106104435761010080835404028352916020019161046e565b820191906000526020600020905b81548152906001019060200180831161045157829003601f168201915b505050505081565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600460009054906101000a900460ff1681565b60005481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161461050f57600080fd5b6001600460006101000a81548160ff021916908315150217905550565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60058054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105e85780601f106105bd576101008083540402835291602001916105e8565b820191906000526020600020905b8154815290600101906020018083116105cb57829003601f168201915b505050505081565b6000806000606060006060600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff166000546003600460009054906101000a900460ff166005828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106ed5780601f106106c2576101008083540402835291602001916106ed565b820191906000526020600020905b8154815290600101906020018083116106d057829003601f168201915b50505050509250808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107895780601f1061075e57610100808354040283529160200191610789565b820191906000526020600020905b81548152906001019060200180831161076c57829003601f168201915b5050505050905095509550955095509550955090919293949556fea265627a7a723158205cc51c8d7db82fb39247e7141ed98220ed8d90a9b1b0a41460e3e325eb551f4264736f6c63430005100032a265627a7a72315820fc93d2f5754c160fef77b11a65b1fe46f69010a38614bc1143464d2bf9be067e64736f6c63430005100032";

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

    public RemoteFunctionCall<TransactionReceipt> createDebt(BigInteger _amount, String _borrower, String _description, String _txHash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEDEBT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(_borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description), 
                new org.web3j.abi.datatypes.Utf8String(_txHash)), 
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
