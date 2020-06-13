package com.wang.service.impl;

import com.imooc.enums.YesOrNo;
import com.wang.bo.AddressBo;
import com.wang.mapper.UserAddressMapper;
import com.wang.pojo.UserAddress;
import com.wang.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    /**
     * 查询所有该用户的收货地址
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress ua=new UserAddress();
        ua.setUserId(userId);

        return userAddressMapper.select(ua);
    }

    /**
     * 新增收货地址
     * @param addressBo
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBo addressBo) {
        //1.判断当前用户是否存在地址 如果没有则新增的为默认地址
        Integer isDefaulf=0;
        List<UserAddress> addressList = this.queryAll(addressBo.getUserId());
        if(addressList ==null || addressList.isEmpty() || addressList.size() == 0){
            isDefaulf=1;
        }

        //2.保存到数据库
        String aShort = sid.nextShort();

        UserAddress userAddress=new UserAddress();

        BeanUtils.copyProperties(addressBo,userAddress);

        userAddress.setId(aShort);
        userAddress.setIsDefault(isDefaulf);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(userAddress);
    }

    /**
     * 修改地址
     * @param addressBo
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBo addressBo) {
        String addressId = addressBo.getAddressId();
        UserAddress userAddress=new UserAddress();
        BeanUtils.copyProperties(addressBo,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        int i = userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    /**
     * 删除用户地址
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress deleteAddress=new UserAddress();
        deleteAddress.setId(addressId);
        deleteAddress.setUserId(userId);
        userAddressMapper.delete(deleteAddress);
    }
    /**
     * 根据addressId 设置为默认地址
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        //1.先把默认地址查询出来 设置为不默认
        UserAddress queryAddress=new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        UserAddress ua = userAddressMapper.selectOne(queryAddress);
        ua.setIsDefault(YesOrNo.NO.type);
        userAddressMapper.updateByPrimaryKeySelective(ua);
        //2.根据addressId 设置为默认地址
        UserAddress defaultAddress=new UserAddress();
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress ua=new UserAddress();
        ua.setId(addressId);
        ua.setUserId(userId);
        UserAddress userAddress = userAddressMapper.selectOne(ua);
        return userAddress;
    }
}
