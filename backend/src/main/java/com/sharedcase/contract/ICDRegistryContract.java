package com;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class ICDRegistryContract extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610853806100326000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80630bd8c0c61461006757806326d22da314610085578063af7747b7146100a5578063c304e916146100b8578063d3460464146100cd578063f851a440146100e0575b600080fd5b61006f61010b565b60405161007c91906105bf565b60405180910390f35b610098610093366004610621565b6101e4565b60405161007c919061063a565b6100986100b33660046106f7565b610290565b6100cb6100c6366004610734565b6102b4565b005b6100986100db3660046106f7565b61041a565b6000546100f3906001600160a01b031681565b6040516001600160a01b03909116815260200161007c565b60606002805480602002602001604051908101604052809291908181526020016000905b828210156101db57838290600052602060002001805461014e90610798565b80601f016020809104026020016040519081016040528092919081815260200182805461017a90610798565b80156101c75780601f1061019c576101008083540402835291602001916101c7565b820191906000526020600020905b8154815290600101906020018083116101aa57829003601f168201915b50505050508152602001906001019061012f565b50505050905090565b600281815481106101f457600080fd5b90600052602060002001600091509050805461020f90610798565b80601f016020809104026020016040519081016040528092919081815260200182805461023b90610798565b80156102885780601f1061025d57610100808354040283529160200191610288565b820191906000526020600020905b81548152906001019060200180831161026b57829003601f168201915b505050505081565b80516020818301810180516001825292820191909301209152805461020f90610798565b6000546001600160a01b031633146103005760405162461bcd60e51b815260206004820152600a60248201526927b7363c9030b236b4b760b11b60448201526064015b60405180910390fd5b60018260405161031091906107d3565b9081526020016040518091039020805461032990610798565b1590506103665760405162461bcd60e51b815260206004820152600b60248201526a436f64652065786973747360a81b60448201526064016102f7565b8060018360405161037791906107d3565b908152602001604051809103902090805190602001906103989291906104ca565b506002805460018101825560009190915282516103dc917f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ace019060208501906104ca565b507f630370e5c9382153f7196fd362b5d48f730516c7415ebf0981345661da3f02e1828260405161040e9291906107ef565b60405180910390a15050565b606060018260405161042c91906107d3565b9081526020016040518091039020805461044590610798565b80601f016020809104026020016040519081016040528092919081815260200182805461047190610798565b80156104be5780601f10610493576101008083540402835291602001916104be565b820191906000526020600020905b8154815290600101906020018083116104a157829003601f168201915b50505050509050919050565b8280546104d690610798565b90600052602060002090601f0160209004810192826104f8576000855561053e565b82601f1061051157805160ff191683800117855561053e565b8280016001018555821561053e579182015b8281111561053e578251825591602001919060010190610523565b5061054a92915061054e565b5090565b5b8082111561054a576000815560010161054f565b60005b8381101561057e578181015183820152602001610566565b8381111561058d576000848401525b50505050565b600081518084526105ab816020860160208601610563565b601f01601f19169290920160200192915050565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b8281101561061457603f19888603018452610602858351610593565b945092850192908501906001016105e6565b5092979650505050505050565b60006020828403121561063357600080fd5b5035919050565b60208152600061064d6020830184610593565b9392505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261067b57600080fd5b813567ffffffffffffffff8082111561069657610696610654565b604051601f8301601f19908116603f011681019082821181831017156106be576106be610654565b816040528381528660208588010111156106d757600080fd5b836020870160208301376000602085830101528094505050505092915050565b60006020828403121561070957600080fd5b813567ffffffffffffffff81111561072057600080fd5b61072c8482850161066a565b949350505050565b6000806040838503121561074757600080fd5b823567ffffffffffffffff8082111561075f57600080fd5b61076b8683870161066a565b9350602085013591508082111561078157600080fd5b5061078e8582860161066a565b9150509250929050565b600181811c908216806107ac57607f821691505b602082108114156107cd57634e487b7160e01b600052602260045260246000fd5b50919050565b600082516107e5818460208701610563565b9190910192915050565b6040815260006108026040830185610593565b82810360208401526108148185610593565b9594505050505056fea2646970667358221220121727c10dc9fcade60ae0884f829559e4d445b134966ab3df19c7ae5d4b885164736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610855806100326000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80631294941e146100675780637761dda01461007c578063af3b90b5146100a5578063dfa07736146100b8578063ef05386b146100cd578063f1522800146100e0575b600080fd5b61007a610075366004610608565b61010b565b005b61008f61008a36600461066c565b610273565b60405161009c91906106e1565b60405180910390f35b61008f6100b33660046106fb565b61031f565b6100c0610343565b60405161009c9190610738565b61008f6100db3660046106fb565b61041c565b6000546100f3906001600160a01b031681565b6040516001600160a01b03909116815260200161009c565b6000546001600160a01b0316331461015857604051636381e58960e11b815260206004820152600a60248201526927b7363c9030b236b4b760b11b60448201526064015b60405180910390fd5b600182604051610168919061079a565b90815260200160405180910390208054610181906107b6565b1590506101bf57604051636381e58960e11b815260206004820152600b60248201526a436f64652065786973747360a81b604482015260640161014f565b806001836040516101d0919061079a565b908152602001604051809103902090805190602001906101f19291906104cc565b50600280546001810182556000919091528251610235917fd35f1a30cf220fbb02dc852aaf21cae20338ac7d10d548ea52adafe8261a7873019060208501906104cc565b507fe5a22f576e1400370747cec88a55dbc0871b7009bd1383b59d32871af8581e9f82826040516102679291906107f1565b60405180910390a15050565b6002818154811061028357600080fd5b90600052602060002001600091509050805461029e906107b6565b80601f01602080910402602001604051908101604052809291908181526020018280546102ca906107b6565b80156103175780601f106102ec57610100808354040283529160200191610317565b820191906000526020600020905b8154815290600101906020018083116102fa57829003601f168201915b505050505081565b80516020818301810180516001825292820191909301209152805461029e906107b6565b60606002805480602002602001604051908101604052809291908181526020016000905b82821015610413578382906000526020600020018054610386906107b6565b80601f01602080910402602001604051908101604052809291908181526020018280546103b2906107b6565b80156103ff5780601f106103d4576101008083540402835291602001916103ff565b820191906000526020600020905b8154815290600101906020018083116103e257829003601f168201915b505050505081526020019060010190610367565b50505050905090565b606060018260405161042e919061079a565b90815260200160405180910390208054610447906107b6565b80601f0160208091040260200160405190810160405280929190818152602001828054610473906107b6565b80156104c05780601f10610495576101008083540402835291602001916104c0565b820191906000526020600020905b8154815290600101906020018083116104a357829003601f168201915b50505050509050919050565b8280546104d8906107b6565b90600052602060002090601f0160209004810192826104fa5760008555610540565b82601f1061051357805160ff1916838001178555610540565b82800160010185558215610540579182015b82811115610540578251825591602001919060010190610525565b5061054c929150610550565b5090565b5b8082111561054c5760008155600101610551565b63b95aa35560e01b600052604160045260246000fd5b600082601f83011261058c57600080fd5b813567ffffffffffffffff808211156105a7576105a7610565565b604051601f8301601f19908116603f011681019082821181831017156105cf576105cf610565565b816040528381528660208588010111156105e857600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806040838503121561061b57600080fd5b823567ffffffffffffffff8082111561063357600080fd5b61063f8683870161057b565b9350602085013591508082111561065557600080fd5b506106628582860161057b565b9150509250929050565b60006020828403121561067e57600080fd5b5035919050565b60005b838110156106a0578181015183820152602001610688565b838111156106af576000848401525b50505050565b600081518084526106cd816020860160208601610685565b601f01601f19169290920160200192915050565b6020815260006106f460208301846106b5565b9392505050565b60006020828403121561070d57600080fd5b813567ffffffffffffffff81111561072457600080fd5b6107308482850161057b565b949350505050565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b8281101561078d57603f1988860301845261077b8583516106b5565b9450928501929085019060010161075f565b5092979650505050505050565b600082516107ac818460208701610685565b9190910192915050565b600181811c908216806107ca57607f821691505b602082108114156107eb5763b95aa35560e01b600052602260045260246000fd5b50919050565b60408152600061080460408301856106b5565b828103602084015261081681856106b5565b9594505050505056fea2646970667358221220a3760ee1e1c2223aa75dc2b63a3b496bb4d073984cc39280595b33a2dbe7865764736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"code\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"}],\"name\":\"ICDAdded\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":1,\"value\":[0]},{\"kind\":4,\"value\":[0]},{\"kind\":4,\"value\":[2]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"code\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"description\",\"type\":\"string\"}],\"name\":\"addICD\",\"outputs\":[],\"selector\":[3271878934,311727134],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"inputs\":[],\"name\":\"admin\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"selector\":[4166100032,4048693248],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":2,\"value\":[0]},{\"kind\":4,\"value\":[2]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"name\":\"allCodes\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[651308451,2002902432],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":4,\"value\":[2]}],\"inputs\":[],\"name\":\"getAllCodes\",\"outputs\":[{\"internalType\":\"string[]\",\"name\":\"\",\"type\":\"string[]\"}],\"selector\":[198754502,3751835446],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":1,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"code\",\"type\":\"string\"}],\"name\":\"getICD\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[3544581220,4010096747],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"name\":\"icdDescriptions\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[2943829943,2939916469],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDICD = "addICD";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_ALLCODES = "allCodes";

    public static final String FUNC_GETALLCODES = "getAllCodes";

    public static final String FUNC_GETICD = "getICD";

    public static final String FUNC_ICDDESCRIPTIONS = "icdDescriptions";

    public static final Event ICDADDED_EVENT = new Event("ICDAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected ICDRegistryContract(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<ICDAddedEventResponse> getICDAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ICDADDED_EVENT, transactionReceipt);
        ArrayList<ICDAddedEventResponse> responses = new ArrayList<ICDAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ICDAddedEventResponse typedResponse = new ICDAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.code = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }
/*

    public void subscribeICDAddedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(ICDADDED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeICDAddedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(ICDADDED_EVENT);
        subscribeEvent(topic0,callback);
    }
*/

    public TransactionReceipt addICD(String code, String description) {
        final Function function = new Function(
                FUNC_ADDICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public Function getMethodAddICDRawFunction(String code, String description) throws
            ContractException {
        final Function function = new Function(FUNC_ADDICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(description)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForAddICD(String code, String description) {
        final Function function = new Function(
                FUNC_ADDICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String addICD(String code, String description, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(description)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, String> getAddICDInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDICD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public String admin() throws ContractException {
        final Function function = new Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodAdminRawFunction() throws ContractException {
        final Function function = new Function(FUNC_ADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return function;
    }

    public String allCodes(BigInteger param0) throws ContractException {
        final Function function = new Function(FUNC_ALLCODES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodAllCodesRawFunction(BigInteger param0) throws ContractException {
        final Function function = new Function(FUNC_ALLCODES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public List getAllCodes() throws ContractException {
        final Function function = new Function(FUNC_GETALLCODES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public Function getMethodGetAllCodesRawFunction() throws ContractException {
        final Function function = new Function(FUNC_GETALLCODES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return function;
    }

    public String getICD(String code) throws ContractException {
        final Function function = new Function(FUNC_GETICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodGetICDRawFunction(String code) throws ContractException {
        final Function function = new Function(FUNC_GETICD, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(code)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public String icdDescriptions(String param0) throws ContractException {
        final Function function = new Function(FUNC_ICDDESCRIPTIONS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodIcdDescriptionsRawFunction(String param0) throws ContractException {
        final Function function = new Function(FUNC_ICDDESCRIPTIONS, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public static ICDRegistryContract load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new ICDRegistryContract(contractAddress, client, credential);
    }

    public static ICDRegistryContract deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(ICDRegistryContract.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class ICDAddedEventResponse {
        public TransactionReceipt.Logs log;

        public String code;

        public String description;
    }
}
