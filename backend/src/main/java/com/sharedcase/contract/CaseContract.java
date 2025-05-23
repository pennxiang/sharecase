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
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CaseContract extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610969806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c8063223c3bb9146100515780632461943314610077578063762d8bd81461009a578063bf455448146100af575b600080fd5b61006461005f366004610665565b6100cf565b6040519081526020015b60405180910390f35b61008a6100853660046106a2565b6100f6565b60405161006e9493929190610743565b6100ad6100a8366004610785565b6102ac565b005b6100c26100bd366004610665565b6103a7565b60405161006e919061080d565b600080826040516100e0919061086f565b9081526040519081900360200190205492915050565b6060806000806000808760405161010d919061086f565b9081526020016040518091039020868154811061012c5761012c61088b565b9060005260206000209060040201604051806080016040529081600082018054610155906108a1565b80601f0160208091040260200160405190810160405280929190818152602001828054610181906108a1565b80156101ce5780601f106101a3576101008083540402835291602001916101ce565b820191906000526020600020905b8154815290600101906020018083116101b157829003601f168201915b505050505081526020016001820180546101e7906108a1565b80601f0160208091040260200160405190810160405280929190818152602001828054610213906108a1565b80156102605780601f1061023557610100808354040283529160200191610260565b820191906000526020600020905b81548152906001019060200180831161024357829003601f168201915b505050918352505060028201546001600160a01b031660208083019190915260039092015460409182015282519183015190830151606090930151919a90995091975095509350505050565b6040805160808101825283815260208101839052338183015242606082015290516000906102db90869061086f565b90815260405160209181900382019020805460018101825560009182529082902083518051859460049094029092019261031a92849290910190610529565b5060208281015180516103339260018501920190610529565b506040828101516002830180546001600160a01b0319166001600160a01b039092169190911790556060909201516003909101555133907f337b109e3f497728f2bdd27545c9ed1cb52ed4a4103cc94da88b868879c982e29061039990879087906108dc565b60405180910390a250505050565b6060600080836040516103ba919061086f565b90815260405190819003602001902054905060008167ffffffffffffffff8111156103e7576103e76105c2565b60405190808252806020026020018201604052801561041a57816020015b60608152602001906001900390816104055790505b50905060005b8281101561052157600085604051610438919061086f565b908152602001604051809103902081815481106104575761045761088b565b90600052602060002090600402016000018054610473906108a1565b80601f016020809104026020016040519081016040528092919081815260200182805461049f906108a1565b80156104ec5780601f106104c1576101008083540402835291602001916104ec565b820191906000526020600020905b8154815290600101906020018083116104cf57829003601f168201915b50505050508282815181106105035761050361088b565b602002602001018190525080806105199061090a565b915050610420565b509392505050565b828054610535906108a1565b90600052602060002090601f016020900481019282610557576000855561059d565b82601f1061057057805160ff191683800117855561059d565b8280016001018555821561059d579182015b8281111561059d578251825591602001919060010190610582565b506105a99291506105ad565b5090565b5b808211156105a957600081556001016105ae565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126105e957600080fd5b813567ffffffffffffffff80821115610604576106046105c2565b604051601f8301601f19908116603f0116810190828211818310171561062c5761062c6105c2565b8160405283815286602085880101111561064557600080fd5b836020870160208301376000602085830101528094505050505092915050565b60006020828403121561067757600080fd5b813567ffffffffffffffff81111561068e57600080fd5b61069a848285016105d8565b949350505050565b600080604083850312156106b557600080fd5b823567ffffffffffffffff8111156106cc57600080fd5b6106d8858286016105d8565b95602094909401359450505050565b60005b838110156107025781810151838201526020016106ea565b83811115610711576000848401525b50505050565b6000815180845261072f8160208601602086016106e7565b601f01601f19169290920160200192915050565b6080815260006107566080830187610717565b82810360208401526107688187610717565b6001600160a01b0395909516604084015250506060015292915050565b60008060006060848603121561079a57600080fd5b833567ffffffffffffffff808211156107b257600080fd5b6107be878388016105d8565b945060208601359150808211156107d457600080fd5b6107e0878388016105d8565b935060408601359150808211156107f657600080fd5b50610803868287016105d8565b9150509250925092565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b8281101561086257603f19888603018452610850858351610717565b94509285019290850190600101610834565b5092979650505050505050565b600082516108818184602087016106e7565b9190910192915050565b634e487b7160e01b600052603260045260246000fd5b600181811c908216806108b557607f821691505b602082108114156108d657634e487b7160e01b600052602260045260246000fd5b50919050565b6040815260006108ef6040830185610717565b82810360208401526109018185610717565b95945050505050565b600060001982141561092c57634e487b7160e01b600052601160045260246000fd5b506001019056fea26469706673582212201e7d9cca738061bbcd8d3367feb0fa97326158094bbd734acdea71400fe69be864736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061096d806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80630767f60b1461005157806386c7661f1461007d578063b79b164a1461009d578063d0a5a5f8146100b2575b600080fd5b61006461005f366004610669565b6100d3565b604051610074949392919061070a565b60405180910390f35b61009061008b36600461074c565b610289565b6040516100749190610789565b6100b06100ab3660046107eb565b61040b565b005b6100c56100c036600461074c565b610506565b604051908152602001610074565b606080600080600080876040516100ea9190610873565b908152602001604051809103902086815481106101095761010961088f565b9060005260206000209060040201604051806080016040529081600082018054610132906108a5565b80601f016020809104026020016040519081016040528092919081815260200182805461015e906108a5565b80156101ab5780601f10610180576101008083540402835291602001916101ab565b820191906000526020600020905b81548152906001019060200180831161018e57829003601f168201915b505050505081526020016001820180546101c4906108a5565b80601f01602080910402602001604051908101604052809291908181526020018280546101f0906108a5565b801561023d5780601f106102125761010080835404028352916020019161023d565b820191906000526020600020905b81548152906001019060200180831161022057829003601f168201915b505050918352505060028201546001600160a01b031660208083019190915260039092015460409182015282519183015190830151606090930151919a90995091975095509350505050565b60606000808360405161029c9190610873565b90815260405190819003602001902054905060008167ffffffffffffffff8111156102c9576102c96105c6565b6040519080825280602002602001820160405280156102fc57816020015b60608152602001906001900390816102e75790505b50905060005b828110156104035760008560405161031a9190610873565b908152602001604051809103902081815481106103395761033961088f565b90600052602060002090600402016000018054610355906108a5565b80601f0160208091040260200160405190810160405280929190818152602001828054610381906108a5565b80156103ce5780601f106103a3576101008083540402835291602001916103ce565b820191906000526020600020905b8154815290600101906020018083116103b157829003601f168201915b50505050508282815181106103e5576103e561088f565b602002602001018190525080806103fb906108e0565b915050610302565b509392505050565b60408051608081018252838152602081018390523381830152426060820152905160009061043a908690610873565b9081526040516020918190038201902080546001810182556000918252908290208351805185946004909402909201926104799284929091019061052d565b506020828101518051610492926001850192019061052d565b506040828101516002830180546001600160a01b0319166001600160a01b039092169190911790556060909201516003909101555133907f73990bd45293cd77668343e07b72411773a8d98e3fbad386873dcf44c2e2a113906104f89087908790610909565b60405180910390a250505050565b600080826040516105179190610873565b9081526040519081900360200190205492915050565b828054610539906108a5565b90600052602060002090601f01602090048101928261055b57600085556105a1565b82601f1061057457805160ff19168380011785556105a1565b828001600101855582156105a1579182015b828111156105a1578251825591602001919060010190610586565b506105ad9291506105b1565b5090565b5b808211156105ad57600081556001016105b2565b63b95aa35560e01b600052604160045260246000fd5b600082601f8301126105ed57600080fd5b813567ffffffffffffffff80821115610608576106086105c6565b604051601f8301601f19908116603f01168101908282118183101715610630576106306105c6565b8160405283815286602085880101111561064957600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806040838503121561067c57600080fd5b823567ffffffffffffffff81111561069357600080fd5b61069f858286016105dc565b95602094909401359450505050565b60005b838110156106c95781810151838201526020016106b1565b838111156106d8576000848401525b50505050565b600081518084526106f68160208601602086016106ae565b601f01601f19169290920160200192915050565b60808152600061071d60808301876106de565b828103602084015261072f81876106de565b6001600160a01b0395909516604084015250506060015292915050565b60006020828403121561075e57600080fd5b813567ffffffffffffffff81111561077557600080fd5b610781848285016105dc565b949350505050565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b828110156107de57603f198886030184526107cc8583516106de565b945092850192908501906001016107b0565b5092979650505050505050565b60008060006060848603121561080057600080fd5b833567ffffffffffffffff8082111561081857600080fd5b610824878388016105dc565b9450602086013591508082111561083a57600080fd5b610846878388016105dc565b9350604086013591508082111561085c57600080fd5b50610869868287016105dc565b9150509250925092565b600082516108858184602087016106ae565b9190910192915050565b63b95aa35560e01b600052603260045260246000fd5b600181811c908216806108b957607f821691505b602082108114156108da5763b95aa35560e01b600052602260045260246000fd5b50919050565b60006000198214156109025763b95aa35560e01b600052601160045260246000fd5b5060010190565b60408152600061091c60408301856106de565b828103602084015261092e81856106de565b9594505050505056fea2646970667358221220c4b4f194032d62eaaecc4ffcb6fccacfb6a12615ef6fb7fa9f91c4ba555d898764736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"editor\",\"type\":\"address\"}],\"name\":\"VersionAdded\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"}],\"name\":\"addVersion\",\"outputs\":[],\"selector\":[1982696408,3080394314],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"}],\"name\":\"getAllVersionCodes\",\"outputs\":[{\"internalType\":\"string[]\",\"name\":\"\",\"type\":\"string[]\"}],\"selector\":[3208991816,2261214751],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getVersion\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"editor\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"}],\"selector\":[610374707,124253707],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"}],\"name\":\"getVersionCount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[574372793,3500516856],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDVERSION = "addVersion";

    public static final String FUNC_GETALLVERSIONCODES = "getAllVersionCodes";

    public static final String FUNC_GETVERSION = "getVersion";

    public static final String FUNC_GETVERSIONCOUNT = "getVersionCount";

    public static final Event VERSIONADDED_EVENT = new Event("VersionAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    protected CaseContract(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<VersionAddedEventResponse> getVersionAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VERSIONADDED_EVENT, transactionReceipt);
        ArrayList<VersionAddedEventResponse> responses = new ArrayList<VersionAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VersionAddedEventResponse typedResponse = new VersionAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.editor = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.caseId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.versionCode = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeVersionAddedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(VERSIONADDED_EVENT);
//        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeVersionAddedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(VERSIONADDED_EVENT);
//        subscribeEvent(topic0,callback);
    }

    public TransactionReceipt addVersion(String caseId, String versionCode, String ipfsHash) {
        final Function function = new Function(
                FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(versionCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodAddVersionRawFunction(String caseId, String versionCode,
            String ipfsHash) throws ContractException {
        final Function function = new Function(FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(versionCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForAddVersion(String caseId, String versionCode,
            String ipfsHash) {
        final Function function = new Function(
                FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(versionCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String addVersion(String caseId, String versionCode, String ipfsHash,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(versionCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple3<String, String, String> getAddVersionInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDVERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue()
                );
    }

    public List getAllVersionCodes(String caseId) throws ContractException {
        final Function function = new Function(FUNC_GETALLVERSIONCODES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public Function getMethodGetAllVersionCodesRawFunction(String caseId) throws ContractException {
        final Function function = new Function(FUNC_GETALLVERSIONCODES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return function;
    }

    public Tuple4<String, String, String, BigInteger> getVersion(String caseId, BigInteger index)
            throws ContractException {
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<String, String, String, BigInteger>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue());
    }

    public Function getMethodGetVersionRawFunction(String caseId, BigInteger index) throws
            ContractException {
        final Function function = new Function(FUNC_GETVERSION, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return function;
    }

    public BigInteger getVersionCount(String caseId) throws ContractException {
        final Function function = new Function(FUNC_GETVERSIONCOUNT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodGetVersionCountRawFunction(String caseId) throws ContractException {
        final Function function = new Function(FUNC_GETVERSIONCOUNT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return function;
    }

    public static CaseContract load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new CaseContract(contractAddress, client, credential);
    }

    public static CaseContract deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(CaseContract.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class VersionAddedEventResponse {
        public TransactionReceipt.Logs log;

        public String editor;

        public String caseId;

        public String versionCode;
    }
}
