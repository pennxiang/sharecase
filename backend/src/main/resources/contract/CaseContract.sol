// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/// @title 去中心化病例共享合约
/// @author Penn
contract CaseContract {

    /// @notice 病例记录结构体
    struct CaseRecord {
        string caseId;       // 业务标识，用于区分病例（如 UUID）
        string icdCode;      // 病种编码（ICD-10）
        string ipfsHash;     // IPFS 地址，指向病例PDF
        address patient;     // 患者地址（数据归属人）
        address doctor;      // 填写人（医生地址）
        uint256 visitTime;   // 就诊时间（Unix时间戳）
    }

    /// @notice 存储每位患者的所有病例记录
    mapping(address => CaseRecord[]) private records;

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
        CaseRecord memory record = CaseRecord({
            caseId: caseId,
            icdCode: icdCode,
            ipfsHash: ipfsHash,
            patient: patient,
            doctor: msg.sender,
            visitTime: block.timestamp
        });

        records[patient].push(record);

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
        require(index < records[patient].length, "Index out of range");

        CaseRecord memory r = records[patient][index];
        return (r.caseId, r.icdCode, r.ipfsHash, r.doctor, r.visitTime);
    }

    /// @notice 获取某个患者的所有 IPFS 地址（简化版本）
    function getIpfsHashes(address patient) public view returns (string[] memory) {
        uint256 count = records[patient].length;
        string[] memory hashes = new string[](count);
        for (uint i = 0; i < count; i++) {
            hashes[i] = records[patient][i].ipfsHash;
        }
        return hashes;
    }
}
