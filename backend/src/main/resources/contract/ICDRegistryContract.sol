// SPDX-License-Identifier: MIT
pragma solidity >=0.6.10 <0.8.20;

/// @title ICD 疾病编码注册合约（链上版本号规范支持）
/// @notice 管理支持的 ICD 编码及其中文描述，支持前端选择版本前验证
/// @dev 可扩展为支持多语言、失效标记等
contract ICDRegistryContract {
    address public admin;

    /// @notice 编码对应的描述，如 "J18.9" => "未明确病原体的肺炎"
    mapping(string => string) public icdDescriptions;

    /// @notice 所有注册过的编码列表
    string[] public allCodes;

    event ICDAdded(string code, string description);

    /// @dev 仅允许管理员调用的 modifier
    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin");
        _;
    }

    /// @notice 构造函数，初始化合约管理员
    constructor() public {
        admin = msg.sender;
    }

    /// @notice 添加 ICD 编码条目
    /// @param code ICD 编码（如 J18.9）
    /// @param description 中文描述（如 "未明确病原体的肺炎"）
    function addICD(string memory code, string memory description) public onlyAdmin {
        require(bytes(icdDescriptions[code]).length == 0, "Code exists");
        icdDescriptions[code] = description;
        allCodes.push(code);
        emit ICDAdded(code, description);
    }

    /// @notice 获取 ICD 描述
    /// @param code ICD 编码
    /// @return description 中文描述
    function getICD(string memory code) public view returns (string memory) {
        return icdDescriptions[code];
    }

    /// @notice 获取所有 ICD 编码
    /// @return 所有已添加的编码列表
    function getAllCodes() public view returns (string[] memory) {
        return allCodes;
    }
}
