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
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.eventsub.EventSubCallback;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class DoctorRegistryContract extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610312806100326000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c8063239cd31614610067578063699ca9981461007c578063996440c61461008f5780639f8a13d7146100c7578063dcf6a382146100ea578063f851a440146100fd575b600080fd5b61007a6100753660046102ac565b610128565b005b61007a61008a3660046102ac565b6101bd565b6100b261009d3660046102ac565b60016020526000908152604090205460ff1681565b60405190151581526020015b60405180910390f35b6100b26100d53660046102ac565b60026020526000908152604090205460ff1681565b6100b26100f83660046102ac565b610266565b600054610110906001600160a01b031681565b6040516001600160a01b0390911681526020016100be565b6000546001600160a01b031633146101745760405162461bcd60e51b815260206004820152600a60248201526927b7363c9030b236b4b760b11b60448201526064015b60405180910390fd5b6001600160a01b038116600081815260026020526040808220805460ff19169055517f65bf6bb33967d6228c69c7ca9166023f0d61abf7acba883b3b127edae2d038009190a250565b6000546001600160a01b031633146102045760405162461bcd60e51b815260206004820152600a60248201526927b7363c9030b236b4b760b11b604482015260640161016b565b6001600160a01b0381166000818152600160208181526040808420805460ff199081168517909155600290925280842080549092169092179055517f2dc8105a7545248d0e354c4c6b02fb583071cc719fb4bef1ca74d72d475570bc9190a250565b6001600160a01b03811660009081526001602052604081205460ff1680156102a657506001600160a01b03821660009081526002602052604090205460ff165b92915050565b6000602082840312156102be57600080fd5b81356001600160a01b03811681146102d557600080fd5b939250505056fea26469706673582212204f00189fa683490808cc694a73a415c1b8ab287ad76cfd2018d82eda7786b08064736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610314806100326000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c806314957b4c146100675780635578bdcb1461007c5780635c3bcc4c146100b4578063c2e32599146100c7578063dc48e579146100da578063f1522800146100fd575b600080fd5b61007a6100753660046102ae565b610128565b005b61009f61008a3660046102ae565b60026020526000908152604090205460ff1681565b60405190151581526020015b60405180910390f35b61009f6100c23660046102ae565b6101be565b61007a6100d53660046102ae565b610204565b61009f6100e83660046102ae565b60016020526000908152604090205460ff1681565b600054610110906001600160a01b031681565b6040516001600160a01b0390911681526020016100ab565b6000546001600160a01b0316331461017557604051636381e58960e11b815260206004820152600a60248201526927b7363c9030b236b4b760b11b60448201526064015b60405180910390fd5b6001600160a01b038116600081815260026020526040808220805460ff19169055517fd7c49862989a2de1bdbee02a3a92b62ab4ef9352c9ca77eaab2d8b212d6af09c9190a250565b6001600160a01b03811660009081526001602052604081205460ff1680156101fe57506001600160a01b03821660009081526002602052604090205460ff165b92915050565b6000546001600160a01b0316331461024c57604051636381e58960e11b815260206004820152600a60248201526927b7363c9030b236b4b760b11b604482015260640161016c565b6001600160a01b0381166000818152600160208181526040808420805460ff199081168517909155600290925280842080549092169092179055517f187a81bce6df894fced652c52f9a3802fe293063abc34a3dd890ce3c3d32f3899190a250565b6000602082840312156102c057600080fd5b81356001600160a01b03811681146102d757600080fd5b939250505056fea26469706673582212206557d5d0fc1eca3c4e23e026b369ba73177f8854229be4ba439922a81e6fd7c464736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"}],\"name\":\"DoctorDisabled\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"}],\"name\":\"DoctorRegistered\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":4,\"value\":[0]}],\"inputs\":[],\"name\":\"admin\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"selector\":[4166100032,4048693248],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":4,\"value\":[0]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"}],\"name\":\"disableDoctor\",\"outputs\":[],\"selector\":[597480214,345340748],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":2,\"value\":[0]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"name\":\"isActive\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[2676626391,1433976267],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"}],\"name\":\"isAuthorizedDoctor\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[3707151234,1547422796],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":1,\"value\":[0]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"name\":\"isDoctor\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"selector\":[2573484230,3695764857],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":4,\"value\":[0]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"}],\"name\":\"registerDoctor\",\"outputs\":[],\"selector\":[1771874712,3269666201],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_DISABLEDOCTOR = "disableDoctor";

    public static final String FUNC_ISACTIVE = "isActive";

    public static final String FUNC_ISAUTHORIZEDDOCTOR = "isAuthorizedDoctor";

    public static final String FUNC_ISDOCTOR = "isDoctor";

    public static final String FUNC_REGISTERDOCTOR = "registerDoctor";

    public static final Event DOCTORDISABLED_EVENT = new Event("DoctorDisabled", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event DOCTORREGISTERED_EVENT = new Event("DoctorRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    protected DoctorRegistryContract(String contractAddress, Client client,
            CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<DoctorDisabledEventResponse> getDoctorDisabledEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DOCTORDISABLED_EVENT, transactionReceipt);
        ArrayList<DoctorDisabledEventResponse> responses = new ArrayList<DoctorDisabledEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DoctorDisabledEventResponse typedResponse = new DoctorDisabledEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.doctor = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

/*
    public void subscribeDoctorDisabledEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(DOCTORDISABLED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeDoctorDisabledEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(DOCTORDISABLED_EVENT);
        subscribeEvent(topic0,callback);
    }
*/

    public List<DoctorRegisteredEventResponse> getDoctorRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DOCTORREGISTERED_EVENT, transactionReceipt);
        ArrayList<DoctorRegisteredEventResponse> responses = new ArrayList<DoctorRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DoctorRegisteredEventResponse typedResponse = new DoctorRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.doctor = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

/*
    public void subscribeDoctorRegisteredEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(DOCTORREGISTERED_EVENT);
        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeDoctorRegisteredEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(DOCTORREGISTERED_EVENT);
        subscribeEvent(topic0,callback);
    }
*/

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

    public TransactionReceipt disableDoctor(String doctor) {
        final Function function = new Function(
                FUNC_DISABLEDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public Function getMethodDisableDoctorRawFunction(String doctor) throws ContractException {
        final Function function = new Function(FUNC_DISABLEDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForDisableDoctor(String doctor) {
        final Function function = new Function(
                FUNC_DISABLEDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String disableDoctor(String doctor, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DISABLEDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple1<String> getDisableDoctorInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_DISABLEDOCTOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Boolean isActive(String param0) throws ContractException {
        final Function function = new Function(FUNC_ISACTIVE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Function getMethodIsActiveRawFunction(String param0) throws ContractException {
        final Function function = new Function(FUNC_ISACTIVE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public Boolean isAuthorizedDoctor(String doctor) throws ContractException {
        final Function function = new Function(FUNC_ISAUTHORIZEDDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Function getMethodIsAuthorizedDoctorRawFunction(String doctor) throws ContractException {
        final Function function = new Function(FUNC_ISAUTHORIZEDDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public Boolean isDoctor(String param0) throws ContractException {
        final Function function = new Function(FUNC_ISDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallWithSingleValueReturn(function, Boolean.class);
    }

    public Function getMethodIsDoctorRawFunction(String param0) throws ContractException {
        final Function function = new Function(FUNC_ISDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return function;
    }

    public TransactionReceipt registerDoctor(String doctor) {
        final Function function = new Function(
                FUNC_REGISTERDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public Function getMethodRegisterDoctorRawFunction(String doctor) throws ContractException {
        final Function function = new Function(FUNC_REGISTERDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForRegisterDoctor(String doctor) {
        final Function function = new Function(
                FUNC_REGISTERDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String registerDoctor(String doctor, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTERDOCTOR, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(doctor)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple1<String> getRegisterDoctorInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTERDOCTOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public static DoctorRegistryContract load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new DoctorRegistryContract(contractAddress, client, credential);
    }

    public static DoctorRegistryContract deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(DoctorRegistryContract.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class DoctorDisabledEventResponse {
        public TransactionReceipt.Logs log;

        public String doctor;
    }

    public static class DoctorRegisteredEventResponse {
        public TransactionReceipt.Logs log;

        public String doctor;
    }
}
