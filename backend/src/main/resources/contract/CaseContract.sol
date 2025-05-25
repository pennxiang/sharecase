// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/// @title 去中心化病例共享合约（最终版）
/// @author Penn
contract CaseContract {

    struct CaseRecord {
        string caseId;        // 业务标识，用于区分病例（如 UUID）
        string icdCode;       // 病种编码（ICD-10）
        string ipfsHash;      // IPFS 地址，指向病例PDF
        address patient;      // 患者地址（数据归属人）
        address doctor;       // 填写人（医生地址）
        uint256 visitTime;    // 就诊时间（时间戳）
    }

    // 每位患者的病例记录
    mapping(address => CaseRecord[]) private records;

    // 所有患者地址（用于全局查询）
    address[] private allPatients;
    mapping(address => bool) private patientExists;

    /// @notice 添加病例成功的事件
    event CaseAdded(
        string caseId,
        string icdCode,
        string ipfsHash,
        address indexed patient,
        address indexed doctor,
        uint256 visitTime
    );

    /// @notice 医生为患者添加病例（记录写链）
    /// @param caseId 业务上的病例ID（可由后端生成 UUID）
    /// @param icdCode ICD编码，如 I10、E11
    /// @param ipfsHash 病历文件的 IPFS 地址
    /// @param patient 患者地址
    function addCase(
        string memory caseId,
        string memory icdCode,
        string memory ipfsHash,
        address patient
    ) public {
        CaseRecord memory r = CaseRecord({
            caseId: caseId,
            icdCode: icdCode,
            ipfsHash: ipfsHash,
            patient: patient,
            doctor: msg.sender,
            visitTime: block.timestamp
        });

        if (!patientExists[patient]) {
            allPatients.push(patient);
            patientExists[patient] = true;
        }

        records[patient].push(r);
        emit CaseAdded(caseId, icdCode, ipfsHash, patient, msg.sender, block.timestamp);
    }

    /// @notice 获取某个患者的病例总数
    function getCaseCount(address patient) public view returns (uint256) {
        return records[patient].length;
    }

    /// @notice 获取某个患者的第 index 条病例
    function getCase(address patient, uint index) public view returns (
        string memory caseId,
        string memory icdCode,
        string memory ipfsHash,
        address doctor,
        uint256 visitTime
    ) {
        CaseRecord memory r = records[patient][index];
        return (r.caseId, r.icdCode, r.ipfsHash, r.doctor, r.visitTime);
    }

    /// @notice 获取某个患者的所有 IPFS 地址
    function getIpfsHashes(address patient) public view returns (string[] memory) {
        uint len = records[patient].length;
        string[] memory hashes = new string[](len);
        for (uint i = 0; i < len; i++) {
            hashes[i] = records[patient][i].ipfsHash;
        }
        return hashes;
    }

    /// @notice 获取所有患者地址
    function getAllPatients() public view returns (address[] memory) {
        return allPatients;
    }

    /// @notice 获取所有病例数（全平台）
    function getAllCaseCount() public view returns (uint total) {
        for (uint i = 0; i < allPatients.length; i++) {
            total += records[allPatients[i]].length;
        }
    }

    /// @notice 获取所有病例（字段展开形式）
    function getAllCases() public view returns (
        string[] memory caseIds,
        string[] memory icdCodes,
        string[] memory ipfsHashes,
        address[] memory patients,
        address[] memory doctors,
        uint256[] memory visitTimes
    ) {
        uint total = getAllCaseCount();
        caseIds = new string[](total);
        icdCodes = new string[](total);
        ipfsHashes = new string[](total);
        patients = new address[](total);
        doctors = new address[](total);
        visitTimes = new uint256[](total);

        uint k = 0;
        for (uint i = 0; i < allPatients.length; i++) {
            address addr = allPatients[i];
            for (uint j = 0; j < records[addr].length; j++) {
                CaseRecord memory r = records[addr][j];
                caseIds[k] = r.caseId;
                icdCodes[k] = r.icdCode;
                ipfsHashes[k] = r.ipfsHash;
                patients[k] = r.patient;
                doctors[k] = r.doctor;
                visitTimes[k] = r.visitTime;
                k++;
            }
        }
    }
}
