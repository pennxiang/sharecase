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
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a96806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80631a5d7f3e14610051578063a49ef4461461007a578063c241dba91461008f578063c277aaf5146100c6575b600080fd5b61006461005f36600461070d565b6100ea565b604051610071919061077c565b60405180910390f35b61008d610088366004610881565b61025c565b005b6100b861009d36600461070d565b6001600160a01b031660009081526020819052604090205490565b604051908152602001610071565b6100d96100d436600461091a565b610394565b604051610071959493929190610944565b6001600160a01b0381166000908152602081905260408120546060918167ffffffffffffffff81111561011f5761011f6107de565b60405190808252806020026020018201604052801561015257816020015b606081526020019060019003908161013d5790505b50905060005b82811015610254576001600160a01b038516600090815260208190526040902080548290811061018a5761018a61099b565b906000526020600020906006020160020180546101a6906109b1565b80601f01602080910402602001604051908101604052809291908181526020018280546101d2906109b1565b801561021f5780601f106101f45761010080835404028352916020019161021f565b820191906000526020600020905b81548152906001019060200180831161020257829003601f168201915b50505050508282815181106102365761023661099b565b6020026020010181905250808061024c906109ec565b915050610158565b509392505050565b6040805160c08101825285815260208082018690528183018590526001600160a01b038416606083018190523360808401524260a0840152600090815280825292832080546001810182559084529281902082518051939485946006909102909201926102ce92849290910190610658565b5060208281015180516102e79260018501920190610658565b5060408201518051610303916002840191602090910190610658565b5060608201516003820180546001600160a01b03199081166001600160a01b0393841617909155608084015160048401805490921690831617905560a09092015160059091015560405133918416907fa696ea2bd4f5d1a726325d1ef704eb07d006c16c9adcc981d8129870aed316fd90610385908990899089904290610a15565b60405180910390a35050505050565b6001600160a01b0382166000908152602081905260408120546060918291829190819086106103fe5760405162461bcd60e51b8152602060048201526012602482015271496e646578206f7574206f662072616e676560701b604482015260640160405180910390fd5b6001600160a01b03871660009081526020819052604081208054889081106104285761042861099b565b90600052602060002090600602016040518060c0016040529081600082018054610451906109b1565b80601f016020809104026020016040519081016040528092919081815260200182805461047d906109b1565b80156104ca5780601f1061049f576101008083540402835291602001916104ca565b820191906000526020600020905b8154815290600101906020018083116104ad57829003601f168201915b505050505081526020016001820180546104e3906109b1565b80601f016020809104026020016040519081016040528092919081815260200182805461050f906109b1565b801561055c5780601f106105315761010080835404028352916020019161055c565b820191906000526020600020905b81548152906001019060200180831161053f57829003601f168201915b50505050508152602001600282018054610575906109b1565b80601f01602080910402602001604051908101604052809291908181526020018280546105a1906109b1565b80156105ee5780601f106105c3576101008083540402835291602001916105ee565b820191906000526020600020905b8154815290600101906020018083116105d157829003601f168201915b505050918352505060038201546001600160a01b03908116602080840191909152600484015490911660408084019190915260059093015460609092019190915282519083015191830151608084015160a090940151919c929b5099509197509095509350505050565b828054610664906109b1565b90600052602060002090601f01602090048101928261068657600085556106cc565b82601f1061069f57805160ff19168380011785556106cc565b828001600101855582156106cc579182015b828111156106cc5782518255916020019190600101906106b1565b506106d89291506106dc565b5090565b5b808211156106d857600081556001016106dd565b80356001600160a01b038116811461070857600080fd5b919050565b60006020828403121561071f57600080fd5b610728826106f1565b9392505050565b6000815180845260005b8181101561075557602081850181015186830182015201610739565b81811115610767576000602083870101525b50601f01601f19169290920160200192915050565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b828110156107d157603f198886030184526107bf85835161072f565b945092850192908501906001016107a3565b5092979650505050505050565b634e487b7160e01b600052604160045260246000fd5b600082601f83011261080557600080fd5b813567ffffffffffffffff80821115610820576108206107de565b604051601f8301601f19908116603f01168101908282118183101715610848576108486107de565b8160405283815286602085880101111561086157600080fd5b836020870160208301376000602085830101528094505050505092915050565b6000806000806080858703121561089757600080fd5b843567ffffffffffffffff808211156108af57600080fd5b6108bb888389016107f4565b955060208701359150808211156108d157600080fd5b6108dd888389016107f4565b945060408701359150808211156108f357600080fd5b50610900878288016107f4565b92505061090f606086016106f1565b905092959194509250565b6000806040838503121561092d57600080fd5b610936836106f1565b946020939093013593505050565b60a08152600061095760a083018861072f565b8281036020840152610969818861072f565b9050828103604084015261097d818761072f565b6001600160a01b039590951660608401525050608001529392505050565b634e487b7160e01b600052603260045260246000fd5b600181811c908216806109c557607f821691505b602082108114156109e657634e487b7160e01b600052602260045260246000fd5b50919050565b6000600019821415610a0e57634e487b7160e01b600052601160045260246000fd5b5060010190565b608081526000610a28608083018761072f565b8281036020840152610a3a818761072f565b90508281036040840152610a4e818661072f565b9150508260608301529594505050505056fea2646970667358221220da7e126477c186be2001db052854c8bff508278c1e8157a7632aba8e55b7ee4564736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a97806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c80633612df7e1461005157806347216fa51461007e578063a0bc903e146100b5578063e559202d146100ca575b600080fd5b61006461005f36600461070e565b6100ea565b604051610075959493929190610785565b60405180910390f35b6100a761008c3660046107dc565b6001600160a01b031660009081526020819052604090205490565b604051908152602001610075565b6100c86100c33660046108a1565b6103af565b005b6100dd6100d83660046107dc565b6104e7565b604051610075919061093a565b6001600160a01b03821660009081526020819052604081205460609182918291908190861061015557604051636381e58960e11b8152602060048201526012602482015271496e646578206f7574206f662072616e676560701b604482015260640160405180910390fd5b6001600160a01b038716600090815260208190526040812080548890811061017f5761017f61099c565b90600052602060002090600602016040518060c00160405290816000820180546101a8906109b2565b80601f01602080910402602001604051908101604052809291908181526020018280546101d4906109b2565b80156102215780601f106101f657610100808354040283529160200191610221565b820191906000526020600020905b81548152906001019060200180831161020457829003601f168201915b5050505050815260200160018201805461023a906109b2565b80601f0160208091040260200160405190810160405280929190818152602001828054610266906109b2565b80156102b35780601f10610288576101008083540402835291602001916102b3565b820191906000526020600020905b81548152906001019060200180831161029657829003601f168201915b505050505081526020016002820180546102cc906109b2565b80601f01602080910402602001604051908101604052809291908181526020018280546102f8906109b2565b80156103455780601f1061031a57610100808354040283529160200191610345565b820191906000526020600020905b81548152906001019060200180831161032857829003601f168201915b505050918352505060038201546001600160a01b03908116602080840191909152600484015490911660408084019190915260059093015460609092019190915282519083015191830151608084015160a090940151919c929b5099509197509095509350505050565b6040805160c08101825285815260208082018690528183018590526001600160a01b038416606083018190523360808401524260a08401526000908152808252928320805460018101825590845292819020825180519394859460069091029092019261042192849290910190610659565b50602082810151805161043a9260018501920190610659565b5060408201518051610456916002840191602090910190610659565b5060608201516003820180546001600160a01b03199081166001600160a01b0393841617909155608084015160048401805490921690831617905560a09092015160059091015560405133918416907ff2cfffb9246917389e8e605a1c0150fcdc9b8dab78ea4f9de37917ddefa5e9e8906104d89089908990899042906109ed565b60405180910390a35050505050565b6001600160a01b0381166000908152602081905260408120546060918167ffffffffffffffff81111561051c5761051c6107fe565b60405190808252806020026020018201604052801561054f57816020015b606081526020019060019003908161053a5790505b50905060005b82811015610651576001600160a01b03851660009081526020819052604090208054829081106105875761058761099c565b906000526020600020906006020160020180546105a3906109b2565b80601f01602080910402602001604051908101604052809291908181526020018280546105cf906109b2565b801561061c5780601f106105f15761010080835404028352916020019161061c565b820191906000526020600020905b8154815290600101906020018083116105ff57829003601f168201915b50505050508282815181106106335761063361099c565b6020026020010181905250808061064990610a38565b915050610555565b509392505050565b828054610665906109b2565b90600052602060002090601f01602090048101928261068757600085556106cd565b82601f106106a057805160ff19168380011785556106cd565b828001600101855582156106cd579182015b828111156106cd5782518255916020019190600101906106b2565b506106d99291506106dd565b5090565b5b808211156106d957600081556001016106de565b80356001600160a01b038116811461070957600080fd5b919050565b6000806040838503121561072157600080fd5b61072a836106f2565b946020939093013593505050565b6000815180845260005b8181101561075e57602081850181015186830182015201610742565b81811115610770576000602083870101525b50601f01601f19169290920160200192915050565b60a08152600061079860a0830188610738565b82810360208401526107aa8188610738565b905082810360408401526107be8187610738565b6001600160a01b039590951660608401525050608001529392505050565b6000602082840312156107ee57600080fd5b6107f7826106f2565b9392505050565b63b95aa35560e01b600052604160045260246000fd5b600082601f83011261082557600080fd5b813567ffffffffffffffff80821115610840576108406107fe565b604051601f8301601f19908116603f01168101908282118183101715610868576108686107fe565b8160405283815286602085880101111561088157600080fd5b836020870160208301376000602085830101528094505050505092915050565b600080600080608085870312156108b757600080fd5b843567ffffffffffffffff808211156108cf57600080fd5b6108db88838901610814565b955060208701359150808211156108f157600080fd5b6108fd88838901610814565b9450604087013591508082111561091357600080fd5b5061092087828801610814565b92505061092f606086016106f2565b905092959194509250565b6000602080830181845280855180835260408601915060408160051b870101925083870160005b8281101561098f57603f1988860301845261097d858351610738565b94509285019290850190600101610961565b5092979650505050505050565b63b95aa35560e01b600052603260045260246000fd5b600181811c908216806109c657607f821691505b602082108114156109e75763b95aa35560e01b600052602260045260246000fd5b50919050565b608081526000610a006080830187610738565b8281036020840152610a128187610738565b90508281036040840152610a268186610738565b91505082606083015295945050505050565b6000600019821415610a5a5763b95aa35560e01b600052601160045260246000fd5b506001019056fea26469706673582212200f5f1c1b47d1813da50df096b4c6c921adadc699002f986efd4c96667192556864736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"icdCode\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"patient\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"visitTime\",\"type\":\"uint256\"}],\"name\":\"CaseAdded\",\"type\":\"event\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"icdCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"patient\",\"type\":\"address\"}],\"name\":\"addCase\",\"outputs\":[],\"selector\":[2761880646,2696712254],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":0,\"value\":[1]}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"patient\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"getCase\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"caseId\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"icdCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"ipfsHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"doctor\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"visitTime\",\"type\":\"uint256\"}],\"selector\":[3262622453,907206526],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"patient\",\"type\":\"address\"}],\"name\":\"getCaseCount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"selector\":[3259095977,1193373605],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0}],\"inputs\":[{\"internalType\":\"address\",\"name\":\"patient\",\"type\":\"address\"}],\"name\":\"getIpfsHashes\",\"outputs\":[{\"internalType\":\"string[]\",\"name\":\"\",\"type\":\"string[]\"}],\"selector\":[442335038,3847823405],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDCASE = "addCase";

    public static final String FUNC_GETCASE = "getCase";

    public static final String FUNC_GETCASECOUNT = "getCaseCount";

    public static final String FUNC_GETIPFSHASHES = "getIpfsHashes";

    public static final Event CASEADDED_EVENT = new Event("CaseAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
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

    public List<CaseAddedEventResponse> getCaseAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CASEADDED_EVENT, transactionReceipt);
        ArrayList<CaseAddedEventResponse> responses = new ArrayList<CaseAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CaseAddedEventResponse typedResponse = new CaseAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.patient = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.doctor = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.caseId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.icdCode = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.visitTime = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeCaseAddedEvent(BigInteger fromBlock, BigInteger toBlock,
            List<String> otherTopics, EventSubCallback callback) {
        String topic0 = eventEncoder.encode(CASEADDED_EVENT);
//        subscribeEvent(topic0,otherTopics,fromBlock,toBlock,callback);
    }

    public void subscribeCaseAddedEvent(EventSubCallback callback) {
        String topic0 = eventEncoder.encode(CASEADDED_EVENT);
//        subscribeEvent(topic0,callback);
    }

    public TransactionReceipt addCase(String caseId, String icdCode, String ipfsHash,
            String patient) {
        final Function function = new Function(
                FUNC_ADDCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(icdCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public Function getMethodAddCaseRawFunction(String caseId, String icdCode, String ipfsHash,
            String patient) throws ContractException {
        final Function function = new Function(FUNC_ADDCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(icdCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForAddCase(String caseId, String icdCode, String ipfsHash,
            String patient) {
        final Function function = new Function(
                FUNC_ADDCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(icdCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String addCase(String caseId, String icdCode, String ipfsHash, String patient,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(caseId), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(icdCode), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(ipfsHash), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple4<String, String, String, String> getAddCaseInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDCASE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue()
                );
    }

    public Tuple5<String, String, String, String, BigInteger> getCase(String patient,
            BigInteger index) throws ContractException {
        final Function function = new Function(FUNC_GETCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple5<String, String, String, String, BigInteger>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue(), 
                (BigInteger) results.get(4).getValue());
    }

    public Function getMethodGetCaseRawFunction(String patient, BigInteger index) throws
            ContractException {
        final Function function = new Function(FUNC_GETCASE, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient), 
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return function;
    }

    public BigInteger getCaseCount(String patient) throws ContractException {
        final Function function = new Function(FUNC_GETCASECOUNT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodGetCaseCountRawFunction(String patient) throws ContractException {
        final Function function = new Function(FUNC_GETCASECOUNT, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return function;
    }

    public List getIpfsHashes(String patient) throws ContractException {
        final Function function = new Function(FUNC_GETIPFSHASHES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public Function getMethodGetIpfsHashesRawFunction(String patient) throws ContractException {
        final Function function = new Function(FUNC_GETIPFSHASHES, 
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(patient)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
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

    public static class CaseAddedEventResponse {
        public TransactionReceipt.Logs log;

        public String patient;

        public String doctor;

        public String caseId;

        public String icdCode;

        public String ipfsHash;

        public BigInteger visitTime;
    }
}
