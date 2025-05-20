// SPDX-License-Identifier: MIT
pragma solidity >=0.6.10 <0.8.20;

/// @title 医生注册与状态控制合约
/// @author Penn
/// @notice 允许管理员添加/禁用医生账号，控制医生是否可上链写入病例
/// @dev 所有医生权限逻辑通过 isDoctor + isActive 控制
contract DoctorRegistryContract {
    address public admin;

    /// @notice 地址是否为注册医生
    mapping(address => bool) public isDoctor;

    /// @notice 医生是否处于启用状态
    mapping(address => bool) public isActive;

    event DoctorRegistered(address indexed doctor);
    event DoctorDisabled(address indexed doctor);

    /// @dev 仅允许管理员调用的 modifier
    modifier onlyAdmin() {
        require(msg.sender == admin, "Only admin");
        _;
    }

    /// @notice 初始化合约时设置管理员
    constructor() public {
        admin = msg.sender;
    }

    /// @notice 注册新的医生地址
    /// @param doctor 医生链上地址
    function registerDoctor(address doctor) public onlyAdmin {
        isDoctor[doctor] = true;
        isActive[doctor] = true;
        emit DoctorRegistered(doctor);
    }

    /// @notice 禁用某个医生（禁止其写入病例）
    /// @param doctor 医生链上地址
    function disableDoctor(address doctor) public onlyAdmin {
        isActive[doctor] = false;
        emit DoctorDisabled(doctor);
    }

    /// @notice 检查医生是否注册且启用
    /// @param doctor 医生链上地址
    /// @return 是否为合法活跃医生
    function isAuthorizedDoctor(address doctor) public view returns (bool) {
        return isDoctor[doctor] && isActive[doctor];
    }
}
