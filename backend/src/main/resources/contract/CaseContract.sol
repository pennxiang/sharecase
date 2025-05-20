// SPDX-License-Identifier: MIT
pragma solidity >=0.6.10 <0.8.20;

/// @title 病例版本管理合约
/// @author Penn
/// @notice 管理病例的版本记录，支持版本新增与版本查询
/// @dev 病例版本通过 IPFS 哈希存储内容，通过链上进行版本不可篡改追踪
contract CaseContract {

    /// @notice 病例版本结构体
    /// @param versionCode 版本号（如 J18.9:1:240519124522）
    /// @param ipfsHash IPFS 存储地址
    /// @param editor 编辑医生链上地址
    /// @param timestamp 区块时间戳
    struct CaseVersion {
        string versionCode;
        string ipfsHash;
        address editor;
        uint256 timestamp;
    }

    /// @notice 病例版本映射表
    /// @dev caseId => 所有版本数组
    mapping(string => CaseVersion[]) private caseVersions;

    /// @notice 新增版本事件
    event VersionAdded(string caseId, string versionCode, address indexed editor);

    /// @notice 添加病例的新版本
    /// @param caseId 病例 ID
    /// @param versionCode 版本号（建议包含 ICD + 序号 + 时间戳）
    /// @param ipfsHash IPFS 哈希（加密后上传）
    function addVersion(
        string memory caseId,
        string memory versionCode,
        string memory ipfsHash
    ) public {
        CaseVersion memory version = CaseVersion(versionCode, ipfsHash, msg.sender, block.timestamp);
        caseVersions[caseId].push(version);
        emit VersionAdded(caseId, versionCode, msg.sender);
    }

    /// @notice 获取某病例的版本数量
    /// @param caseId 病例 ID
    /// @return count 当前已有版本数量
    function getVersionCount(string memory caseId) public view returns (uint256 count) {
        return caseVersions[caseId].length;
    }

    /// @notice 获取指定病例某版本的信息
    /// @param caseId 病例 ID
    /// @param index 第 index 个版本（从 0 开始）
    /// @return versionCode 版本号
    /// @return ipfsHash IPFS 哈希
    /// @return editor 编辑医生地址
    /// @return timestamp 上链时间戳
    function getVersion(string memory caseId, uint index) public view returns (
        string memory versionCode,
        string memory ipfsHash,
        address editor,
        uint256 timestamp
    ) {
        CaseVersion memory v = caseVersions[caseId][index];
        return (v.versionCode, v.ipfsHash, v.editor, v.timestamp);
    }
}
