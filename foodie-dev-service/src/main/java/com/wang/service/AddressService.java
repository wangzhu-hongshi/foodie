package com.wang.service;

import com.wang.bo.AddressBo;
import com.wang.pojo.UserAddress;

import java.util.List;

/**
 * 购物车接口
 */
public interface AddressService {
    /**
     * 查询该用户所有地址
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 新增收货地址
     * @param addressBo
     */
    void addNewUserAddress(AddressBo addressBo);


    /**
     * 根据addressId 修改 该用户地址
     * @param addressBo
     */
    void updateUserAddress(AddressBo addressBo);

    /**
     * 删除地址
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId,String addressId);

    /**
     * 根据addressId 设置为默认地址
     * @param userId
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId,String addressId);

    /**
     * 查询地址 根据userId 喝 addressId
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId,String addressId);
}
