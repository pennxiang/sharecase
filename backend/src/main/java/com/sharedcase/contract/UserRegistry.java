package com;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
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
public class UserRegistry extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061065d806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806307b359f9146100515780633ffbd47f1461007a5780638d3e3fd01461008f578063c822d7f0146100ba575b600080fd5b61006461005f36600461047b565b6100dd565b6040516100719190610514565b60405180910390f35b61008d61008836600461052e565b61018e565b005b6100a261009d36600461047b565b6102cc565b6040516001600160a01b039091168152602001610071565b6100cd6100c836600461047b565b6102ff565b6040519015158152602001610071565b60606000826040516100ef9190610592565b9081526040519081900360200190208054610109906105ae565b80601f0160208091040260200160405190810160405280929190818152602001828054610135906105ae565b80156101825780601f1061015757610100808354040283529160200191610182565b820191906000526020600020905b81548152906001019060200180831161016557829003601f168201915b50505050509050919050565b60006001600160a01b03166000836040516101a99190610592565b908152604051908190036020019020600101546001600160a01b0316146102165760405162461bcd60e51b815260206004820152601760248201527f5573657220616c72656164792072656769737465726564000000000000000000604482015260640160405180910390fd5b6040805180820182528281523360208201529051600090610238908590610592565b9081526020016040518091039020600082015181600001908051906020019061026292919061033f565b5060209190910151600190910180546001600160a01b0319166001600160a01b039092169190911790556040517f7d18b42f8b54b38db3858d85addd4845b244c7ab57e5b3246475b88815c0c5cf906102c0908490849033906105e9565b60405180910390a15050565b600080826040516102dd9190610592565b908152604051908190036020019020600101546001600160a01b031692915050565b6000806001600160a01b031660008360405161031b9190610592565b908152604051908190036020019020600101546001600160a01b0316141592915050565b82805461034b906105ae565b90600052602060002090601f01602090048101928261036d57600085556103b3565b82601f1061038657805160ff19168380011785556103b3565b828001600101855582156103b3579182015b828111156103b3578251825591602001919060010190610398565b506103bf9291506103c3565b5090565b5b808211156103bf57600081556001016103c4565b634e487b7160e01b600052604160045260246000fd5b600082601f8301126103ff57600080fd5b813567ffffffffffffffff8082111561041a5761041a6103d8565b604051601f8301601f19908116603f01168101908282118183101715610442576104426103d8565b8160405283815286602085880101111561045b57600080fd5b836020870160208301376000602085830101528094505050505092915050565b60006020828403121561048d57600080fd5b813567ffffffffffffffff8111156104a457600080fd5b6104b0848285016103ee565b949350505050565b60005b838110156104d35781810151838201526020016104bb565b838111156104e2576000848401525b50505050565b600081518084526105008160208601602086016104b8565b601f01601f19169290920160200192915050565b60208152600061052760208301846104e8565b9392505050565b6000806040838503121561054157600080fd5b823567ffffffffffffffff8082111561055957600080fd5b610565868387016103ee565b9350602085013591508082111561057b57600080fd5b50610588858286016103ee565b9150509250929050565b600082516105a48184602087016104b8565b9190910192915050565b600181811c908216806105c257607f821691505b602082108114156105e357634e487b7160e01b600052602260045260246000fd5b50919050565b6060815260006105fc60608301866104e8565b828103602084015261060e81866104e8565b91505060018060a01b038316604083015294935050505056fea26469706673582212208bb7fd9043d68f983f14dbe186403765d0911419c3fba019729b6abfb880943964736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5061065e806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80631de50dd2146100515780637abdec841461007a578063ded723ae146100a5578063f0297836146100c8575b600080fd5b61006461005f36600461047c565b6100dd565b6040516100719190610515565b60405180910390f35b61008d61008836600461047c565b61018e565b6040516001600160a01b039091168152602001610071565b6100b86100b336600461047c565b6101c1565b6040519015158152602001610071565b6100db6100d636600461052f565b610201565b005b60606000826040516100ef9190610593565b9081526040519081900360200190208054610109906105af565b80601f0160208091040260200160405190810160405280929190818152602001828054610135906105af565b80156101825780601f1061015757610100808354040283529160200191610182565b820191906000526020600020905b81548152906001019060200180831161016557829003601f168201915b50505050509050919050565b6000808260405161019f9190610593565b908152604051908190036020019020600101546001600160a01b031692915050565b6000806001600160a01b03166000836040516101dd9190610593565b908152604051908190036020019020600101546001600160a01b0316141592915050565b60006001600160a01b031660008360405161021c9190610593565b908152604051908190036020019020600101546001600160a01b03161461028a57604051636381e58960e11b815260206004820152601760248201527f5573657220616c72656164792072656769737465726564000000000000000000604482015260640160405180910390fd5b60408051808201825282815233602082015290516000906102ac908590610593565b908152602001604051809103902060008201518160000190805190602001906102d6929190610340565b5060209190910151600190910180546001600160a01b0319166001600160a01b039092169190911790556040517fdd565376cab74d5403b06b03cfc4d89be3ebaefceca788a3745c71f8798cb17190610334908490849033906105ea565b60405180910390a15050565b82805461034c906105af565b90600052602060002090601f01602090048101928261036e57600085556103b4565b82601f1061038757805160ff19168380011785556103b4565b828001600101855582156103b4579182015b828111156103b4578251825591602001919060010190610399565b506103c09291506103c4565b5090565b5b808211156103c057600081556001016103c5565b63b95aa35560e01b600052604160045260246000fd5b600082601f83011261040057600080fd5b813567ffffffffffffffff8082111561041b5761041b6103d9565b604051601f8301601f19908116603f01168101908282118183101715610443576104436103d9565b8160405283815286602085880101111561045c57600080fd5b836020870160208301376000602085830101528094505050505092915050565b60006020828403121561048e57600080fd5b813567ffffffffffffffff8111156104a557600080fd5b6104b1848285016103ef565b949350505050565b60005b838110156104d45781810151838201526020016104bc565b838111156104e3576000848401525b50505050565b600081518084526105018160208601602086016104b9565b601f01601f19169290920160200192915050565b60208152600061052860208301846104e9565b9392505050565b6000806040838503121561054257600080fd5b823567ffffffffffffffff8082111561055a57600080fd5b610566868387016103ef565b9350602085013591508082111561057c57600080fd5b50610589858286016103ef565b9150509250929050565b600082516105a58184602087016104b9565b9190910192915050565b600181811c908216806105c357607f821691505b602082108114156105e45763b95aa35560e01b600052602260045260246000fd5b50919050565b6060815260006105fd60608301866104e9565b828103602084015261060f81866104e9565b91505060018060a01b038316604083015294935050505056fea2646970667358221220b636b5c0318fd6120e23ef7b8f193580d46f02a44b706f6c3940130b9b56f06b64736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"userHash\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"idCardEnc\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"registrant\",\"type\":\"address\"}],\"name\":\"UserRegistered\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"userHash\",\"type\":\"string\"}],\"name\":\"getIdCardEnc\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"selector\":[129194489,501550546],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"userHash\",\"type\":\"string\"}],\"name\":\"getRegistrant\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"selector\":[2369667024,2059267204],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"userHash\",\"type\":\"string\"}],\"name\":\"isRegistered\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[3357726704,3738641326],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"userHash\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"idCardEnc\",\"type\":\"string\"}],\"name\":\"register\",\"outputs\":[],\"selector\":[1073468543,4029249590],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETIDCARDENC = "getIdCardEnc";

    public static final String FUNC_GETREGISTRANT = "getRegistrant";

    public static final String FUNC_ISREGISTERED = "isRegistered";

    public static final String FUNC_REGISTER = "register";

    public static final Event USERREGISTERED_EVENT = new Event("UserRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    protected UserRegistry(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<UserRegisteredEventResponse> getUserRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(USERREGISTERED_EVENT, transactionReceipt);
        ArrayList<UserRegisteredEventResponse> responses = new ArrayList<UserRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UserRegisteredEventResponse typedResponse = new UserRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.userHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.idCardEnc = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.registrant = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeUserRegisteredEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(USERREGISTERED_EVENT);
//        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeUserRegisteredEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(USERREGISTERED_EVENT);
//        subscribeEvent(topic0,callback);
    }

    public String getIdCardEnc(String userHash) throws ContractException {
        final Function function = new Function(FUNC_GETIDCARDENC, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodGetIdCardEncRawFunction(String userHash) throws ContractException {
        final Function function = new Function(FUNC_GETIDCARDENC, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return function;
    }

    public String getRegistrant(String userHash) throws ContractException {
        final Function function = new Function(FUNC_GETREGISTRANT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodGetRegistrantRawFunction(String userHash) throws ContractException {
        final Function function = new Function(FUNC_GETREGISTRANT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return function;
    }

    public Boolean isRegistered(String userHash) throws ContractException {
        final Function function = new Function(FUNC_ISREGISTERED, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Function getMethodIsRegisteredRawFunction(String userHash) throws ContractException {
        final Function function = new Function(FUNC_ISREGISTERED, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public TransactionReceipt register(String userHash, String idCardEnc) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(idCardEnc)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodRegisterRawFunction(String userHash, String idCardEnc) throws
            ContractException {
        final Function function = new Function(FUNC_REGISTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(idCardEnc)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForRegister(String userHash, String idCardEnc) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(idCardEnc)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String register(String userHash, String idCardEnc, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(userHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(idCardEnc)), 
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, String> getRegisterInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public static UserRegistry load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new UserRegistry(contractAddress, client, credential);
    }

    public static UserRegistry deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(UserRegistry.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class UserRegisteredEventResponse {
        public TransactionReceipt.Logs log;

        public String userHash;

        public String idCardEnc;

        public String registrant;
    }
}
