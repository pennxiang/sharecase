// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/// @title 病例版本管理合约
/// @author Penn
/// @notice 管理病例的版本记录，支持版本新增与版本查询
contract CaseContract {

    struct CaseVersion {
        string versionCode;
        string ipfsHash;
        address editor;
        uint256 timestamp;
    }

    // 病例ID => 所有版本数组
    mapping(string => CaseVersion[]) private caseVersions;

    // 所有病例ID（去重记录）
    string[] private allCaseIds;

    // 记录是否已存在某个病例ID
    mapping(string => bool) private caseIdExists;

    event VersionAdded(string caseId, string versionCode, address indexed editor);

    /// 添加新版本
    function addVersion(
        string memory caseId,
        string memory versionCode,
        string memory ipfsHash
    ) public {
        if (!caseIdExists[caseId]) {
            allCaseIds.push(caseId);
            caseIdExists[caseId] = true;
        }

        CaseVersion memory version = CaseVersion(versionCode, ipfsHash, msg.sender, block.timestamp);
        caseVersions[caseId].push(version);
        emit VersionAdded(caseId, versionCode, msg.sender);
    }

    /// 获取版本数量
    function getVersionCount(string memory caseId) public view returns (uint256) {
        return caseVersions[caseId].length;
    }

    /// 获取指定版本
    function getVersion(string memory caseId, uint index) public view returns (
        string memory versionCode,
        string memory ipfsHash,
        address editor,
        uint256 timestamp
    ) {
        CaseVersion memory v = caseVersions[caseId][index];
        return (v.versionCode, v.ipfsHash, v.editor, v.timestamp);
    }

    /// 获取指定病例的所有版本号
    function getVersionCodesByCaseId(string memory caseId) public view returns (string[] memory) {
        uint len = caseVersions[caseId].length;
        string[] memory result = new string[](len);
        for (uint i = 0; i < len; i++) {
            result[i] = caseVersions[caseId][i].versionCode;
        }
        return result;
    }

    /// 获取所有病例的所有版本号（不需要传 caseId）
    function getAllVersionCodes() public view returns (string[] memory) {
        uint total = 0;
        for (uint i = 0; i < allCaseIds.length; i++) {
            total += caseVersions[allCaseIds[i]].length;
        }

        string[] memory allCodes = new string[](total);
        uint k = 0;
        for (uint i = 0; i < allCaseIds.length; i++) {
            string memory id = allCaseIds[i];
            for (uint j = 0; j < caseVersions[id].length; j++) {
                allCodes[k++] = caseVersions[id][j].versionCode;
            }
        }

        return allCodes;
    }
}
