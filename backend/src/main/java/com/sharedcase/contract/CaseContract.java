package com;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610731806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063223c3bb914610046578063246194331461006c578063762d8bd81461008f575b600080fd5b6100596100543660046104b8565b6100a4565b6040519081526020015b60405180910390f35b61007f61007a3660046104f5565b6100cb565b6040516100639493929190610596565b6100a261009d3660046105d8565b610281565b005b600080826040516100b59190610660565b9081526040519081900360200190205492915050565b606080600080600080876040516100e29190610660565b908152602001604051809103902086815481106101015761010161067c565b906000526020600020906004020160405180608001604052908160008201805461012a90610692565b80601f016020809104026020016040519081016040528092919081815260200182805461015690610692565b80156101a35780601f10610178576101008083540402835291602001916101a3565b820191906000526020600020905b81548152906001019060200180831161018657829003601f168201915b505050505081526020016001820180546101bc90610692565b80601f01602080910402602001604051908101604052809291908181526020018280546101e890610692565b80156102355780601f1061020a57610100808354040283529160200191610235565b820191906000526020600020905b81548152906001019060200180831161021857829003601f168201915b505050918352505060028201546001600160a01b031660208083019190915260039092015460409182015282519183015190830151606090930151919a90995091975095509350505050565b6040805160808101825283815260208101839052338183015242606082015290516000906102b0908690610660565b9081526040516020918190038201902080546001810182556000918252908290208351805185946004909402909201926102ef9284929091019061037c565b506020828101518051610308926001850192019061037c565b506040828101516002830180546001600160a01b0319166001600160a01b039092169190911790556060909201516003909101555133907f337b109e3f497728f2bdd27545c9ed1cb52ed4a4103cc94da88b868879c982e29061036e90879087906106cd565b60405180910390a250505050565b82805461038890610692565b90600052602060002090601f0160209004810192826103aa57600085556103f0565b82601f106103c357805160ff19168380011785556103f0565b828001600101855582156103f0579182015b828111156103f05782518255916020019190600101906103d5565b506103fc929150610400565b5090565b5b808211156103fc5760008155600101610401565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261043c57600080fd5b813567ffffffffffffffff8082111561045757610457610415565b604051601f8301601f19908116603f0116810190828211818310171561047f5761047f610415565b8160405283815286602085880101111561049857600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000602082840312156104ca57600080fd5b813567ffffffffffffffff8111156104e157600080fd5b6104ed8482850161042b565b949350505050565b6000806040838503121561050857600080fd5b823567ffffffffffffffff81111561051f57600080fd5b61052b8582860161042b565b95602094909401359450505050565b60005b8381101561055557818101518382015260200161053d565b83811115610564576000848401525b50505050565b6000815180845261058281602086016020860161053a565b601f01601f19169290920160200192915050565b6080815260006105a9608083018761056a565b82810360208401526105bb818761056a565b6001600160a01b0395909516604084015250506060015292915050565b6000806000606084860312156105ed57600080fd5b833567ffffffffffffffff8082111561060557600080fd5b6106118783880161042b565b9450602086013591508082111561062757600080fd5b6106338783880161042b565b9350604086013591508082111561064957600080fd5b506106568682870161042b565b9150509250925092565b6000825161067281846020870161053a565b9190910192915050565b634e487b7160e01b600052603260045260246000fd5b600181811c908216806106a657607f821691505b602082108114156106c757634e487b7160e01b600052602260045260246000fd5b50919050565b6040815260006106e0604083018561056a565b82810360208401526106f2818561056a565b9594505050505056fea264697066735822122045158076c88e3c764698d1816b4a6c132588771c2e5279c6700bddfcf57e8ba464736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610735806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80630767f60b14610046578063b79b164a14610072578063d0a5a5f814610087575b600080fd5b6100596100543660046104bc565b6100a8565b604051610069949392919061055d565b60405180910390f35b61008561008036600461059f565b61025e565b005b61009a610095366004610627565b610359565b604051908152602001610069565b606080600080600080876040516100bf9190610664565b908152602001604051809103902086815481106100de576100de610680565b906000526020600020906004020160405180608001604052908160008201805461010790610696565b80601f016020809104026020016040519081016040528092919081815260200182805461013390610696565b80156101805780601f1061015557610100808354040283529160200191610180565b820191906000526020600020905b81548152906001019060200180831161016357829003601f168201915b5050505050815260200160018201805461019990610696565b80601f01602080910402602001604051908101604052809291908181526020018280546101c590610696565b80156102125780601f106101e757610100808354040283529160200191610212565b820191906000526020600020905b8154815290600101906020018083116101f557829003601f168201915b505050918352505060028201546001600160a01b031660208083019190915260039092015460409182015282519183015190830151606090930151919a90995091975095509350505050565b60408051608081018252838152602081018390523381830152426060820152905160009061028d908690610664565b9081526040516020918190038201902080546001810182556000918252908290208351805185946004909402909201926102cc92849290910190610380565b5060208281015180516102e59260018501920190610380565b506040828101516002830180546001600160a01b0319166001600160a01b039092169190911790556060909201516003909101555133907f73990bd45293cd77668343e07b72411773a8d98e3fbad386873dcf44c2e2a1139061034b90879087906106d1565b60405180910390a250505050565b6000808260405161036a9190610664565b9081526040519081900360200190205492915050565b82805461038c90610696565b90600052602060002090601f0160209004810192826103ae57600085556103f4565b82601f106103c757805160ff19168380011785556103f4565b828001600101855582156103f4579182015b828111156103f45782518255916020019190600101906103d9565b50610400929150610404565b5090565b5b808211156104005760008155600101610405565b63b95aa35560e01b600052604160045260246000fd5b600082601f83011261044057600080fd5b813567ffffffffffffffff8082111561045b5761045b610419565b604051601f8301601f19908116603f0116810190828211818310171561048357610483610419565b8160405283815286602085880101111561049c57600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080604083850312156104cf57600080fd5b823567ffffffffffffffff8111156104e657600080fd5b6104f28582860161042f565b95602094909401359450505050565b60005b8381101561051c578181015183820152602001610504565b8381111561052b576000848401525b50505050565b60008151808452610549816020860160208601610501565b601f01601f19169290920160200192915050565b6080815260006105706080830187610531565b82810360208401526105828187610531565b6001600160a01b0395909516604084015250506060015292915050565b6000806000606084860312156105b457600080fd5b833567ffffffffffffffff808211156105cc57600080fd5b6105d88783880161042f565b945060208601359150808211156105ee57600080fd5b6105fa8783880161042f565b9350604086013591508082111561061057600080fd5b5061061d8682870161042f565b9150509250925092565b60006020828403121561063957600080fd5b813567ffffffffffffffff81111561065057600080fd5b61065c8482850161042f565b949350505050565b60008251610676818460208701610501565b9190910192915050565b63b95aa35560e01b600052603260045260246000fd5b600181811c908216806106aa57607f821691505b602082108114156106cb5763b95aa35560e01b600052602260045260246000fd5b50919050565b6040815260006106e46040830185610531565b82810360208401526106f68185610531565b9594505050505056fea26469706673582212209d9730909aa7cc9a4d5547c04666a43898cc6ed889d51ea94ca8cbb89da7502f64736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"editor\",\"type\":\"address\"}],\"name\":\"VersionAdded\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"}],\"name\":\"addVersion\",\"outputs\":[],\"selector\":[1982696408,3080394314],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getVersion\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"versionCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"editor\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"}],\"selector\":[610374707,124253707],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"}],\"name\":\"getVersionCount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"count\",\"type\":\"uint256\"}],\"selector\":[574372793,3500516856],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDVERSION = "addVersion";

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
/*
    public void subscribeVersionAddedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(VERSIONADDED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeVersionAddedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(VERSIONADDED_EVENT);
        subscribeEvent(topic0,callback);
    }
    */

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
