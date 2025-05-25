// 用户合约，存放每个用户的链上地址，和哈希加密后的身份证
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract UserRegistry {
    struct User {
        string idCardEnc;
        address registrant;
    }

    // 用户哈希 => 用户结构体
    mapping(string => User) private users;

    // 用户注册事件
    event UserRegistered(string userHash, string idCardEnc, address registrant);

    /**
     * 注册用户（传入 userHash 和 加密后的 idCard）
     */
    function register(string memory userHash, string memory idCardEnc) public {
        require(users[userHash].registrant == address(0), "User already registered");

        users[userHash] = User({
            idCardEnc: idCardEnc,
            registrant: msg.sender
        });

        emit UserRegistered(userHash, idCardEnc, msg.sender);
    }

    /**
     * 是否已注册
     */
    function isRegistered(string memory userHash) public view returns (bool) {
        return users[userHash].registrant != address(0);
    }

    /**
     * 获取加密身份证号
     */
    function getIdCardEnc(string memory userHash) public view returns (string memory) {
        return users[userHash].idCardEnc;
    }

    /**
     * 获取注册地址
     */
    function getRegistrant(string memory userHash) public view returns (address) {
        return users[userHash].registrant;
    }
}

