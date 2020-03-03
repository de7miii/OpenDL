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
    private static final String BINARY = "608060405234801561001057600080fd5b506109eb806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063183d7e081461004657806342e2bbba1461009e57806385af9b3f14610175575b600080fd5b61004e610192565b60408051602080825283518183015283519192839290830191858101910280838360005b8381101561008a578181015183820152602001610072565b505050509050019250505060405180910390f35b610159600480360360608110156100b457600080fd5b8135916001600160a01b03602082013516918101906060810160408201356401000000008111156100e457600080fd5b8201836020820111156100f657600080fd5b8035906020019184600183028401116401000000008311171561011857600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506101f4945050505050565b604080516001600160a01b039092168252519081900360200190f35b6101596004803603602081101561018b57600080fd5b503561036a565b606060008054806020026020016040519081016040528092919081815260200182805480156101ea57602002820191906000526020600020905b81546001600160a01b031681526001909101906020018083116101cc575b5050505050905090565b6000336001600160a01b038416141561020c57600080fd5b60008433858560405161021e90610391565b80858152602001846001600160a01b03166001600160a01b03168152602001836001600160a01b03166001600160a01b0316815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561028e578181015183820152602001610276565b50505050905090810190601f1680156102bb5780820380516001836020036101000a031916815260200191505b5095505050505050604051809103906000f0801580156102df573d6000803e3d6000fd5b50600080546001810182559080527f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e5630180546001600160a01b0383166001600160a01b0319909116811790915560408051918252519192507fcf78cf0d6f3d8371e1075c69c492ab4ec5d8cf23a1a239b6a51a1d00be7ca312919081900360200190a1949350505050565b6000818154811061037757fe5b6000918252602090912001546001600160a01b0316905081565b6106178061039f8339019056fe608060405234801561001057600080fd5b506040516106173803806106178339818101604052608081101561003357600080fd5b81516020830151604080850151606086018051925194969395919493918201928464010000000082111561006657600080fd5b90830190602082018581111561007b57600080fd5b825164010000000081118282018810171561009557600080fd5b82525081516020918201929091019080838360005b838110156100c25781810151838201526020016100aa565b50505050905090810190601f1680156100ef5780820380516001836020036101000a031916815260200191505b506040525050600280546001600160a01b038087166001600160a01b0319928316179092556000879055600180549286169290911691909117905550805161013e906003906020840190610153565b50506004805460ff19169055506101ee915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061019457805160ff19168380011785556101c1565b828001600101855582156101c1579182015b828111156101c15782518255916020019190600101906101a6565b506101cd9291506101d1565b5090565b6101eb91905b808211156101cd57600081556001016101d7565b90565b61041a806101fd6000396000f3fe608060405234801561001057600080fd5b506004361061007d5760003560e01c8063aa8c217c1161005b578063aa8c217c1461013f578063aafb96b614610159578063bcead63e14610163578063fbbf93a01461016b5761007d565b80637284e416146100825780637df1f1b9146100ff578063a7b5c52b14610123575b600080fd5b61008a61022f565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100c45781810151838201526020016100ac565b50505050905090810190601f1680156100f15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101076102bd565b604080516001600160a01b039092168252519081900360200190f35b61012b6102cc565b604080519115158252519081900360200190f35b6101476102d5565b60408051918252519081900360200190f35b6101616102db565b005b610107610301565b610173610310565b60405180866001600160a01b03166001600160a01b03168152602001856001600160a01b03166001600160a01b031681526020018481526020018060200183151515158152602001828103825284818151815260200191508051906020019080838360005b838110156101f05781810151838201526020016101d8565b50505050905090810190601f16801561021d5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b6003805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156102b55780601f1061028a576101008083540402835291602001916102b5565b820191906000526020600020905b81548152906001019060200180831161029857829003601f168201915b505050505081565b6001546001600160a01b031681565b60045460ff1681565b60005481565b6001546001600160a01b031633146102f257600080fd5b6004805460ff19166001179055565b6002546001600160a01b031681565b60028054600180546000805460045460038054604080516020601f6000199a851615610100029a909a019093169a909a0497880182900482028a018201905286895293978897889760609789976001600160a01b0393841697919093169590949360ff90911692909184918301828280156103cc5780601f106103a1576101008083540402835291602001916103cc565b820191906000526020600020905b8154815290600101906020018083116103af57829003601f168201915b5050505050915094509450945094509450909192939456fea264697066735822122010dd8124a214a8473b9b3cedbd61dbcc0d5f5ad28c6f57ecb87f72149d0bc57564736f6c63430006020033a2646970667358221220d82b0485f33128fc7e9a3f8533e22a5ddfaeb4dc1db6a8ae95d195bee9a9b4d664736f6c63430006020033";

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

    public RemoteFunctionCall<TransactionReceipt> createDebt(BigInteger _amount, String _borrower, String _description) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEDEBT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.Address(160, _borrower), 
                new org.web3j.abi.datatypes.Utf8String(_description)), 
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
