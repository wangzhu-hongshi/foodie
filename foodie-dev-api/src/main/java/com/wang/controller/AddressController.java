package com.wang.controller;


import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import com.wang.bo.AddressBo;
import com.wang.pojo.UserAddress;
import com.wang.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "收货地址接口",tags = "地址相关接口")
@RestController
@RequestMapping("address")
public class AddressController {
    /**
     * 地址相关功能
     * 1.查看所有收货地址
     * 2.新增地址
     * 3.修改地址
     * 4.删除地址
     * 5.设置默认地址
     *
     */
    @Autowired
    private AddressService addressService;


    @ApiOperation(value = "根据用户id查询收货地址列表",notes = "根据用户id查询收货地址列表",httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult queryAll(@RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return  IMOOCJSONResult.errorMsg("");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddresses);
    }

    @ApiOperation(value = "新增收货地址",notes = "新增收货地址",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBo addressBo){
        IMOOCJSONResult result = this.checkAddress(addressBo);
        if(result.getStatus()!=200){
            return result;
        }
        addressService.addNewUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "修改收货地址",notes = "修改收货地址",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBo addressBo){
        String addressId = addressBo.getAddressId();
        if(StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("addressId 不能为空");
        }
        IMOOCJSONResult result = this.checkAddress(addressBo);
        if(result.getStatus()!=200){
            return result;
        }
        addressService.updateUserAddress(addressBo);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "删除用户收货地址",notes = "删除用户收货地址",httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(@RequestParam String userId,@RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id不能为空");
        }
        if(StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("地址Id不能为空");
        }
        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("/setDefault")
    public IMOOCJSONResult setDefault(@RequestParam String userId,@RequestParam String addressId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id不能为空");
        }
        if(StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("地址Id不能为空");
        }
        addressService.updateUserAddressToBeDefault(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    /**
     * 检验address
     * @param addressBo
     * @return
     */
    private IMOOCJSONResult checkAddress(AddressBo addressBo){
        String receiver = addressBo.getReceiver();
        if(StringUtils.isBlank(receiver)){
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if(receiver.length()>12){
            return IMOOCJSONResult.errorMsg("收货姓名不能太长");
        }
        String mobile = addressBo.getMobile();
        if(StringUtils.isBlank(mobile)){
            return IMOOCJSONResult.errorMsg("收货人电话不能为空");
        }
        if(mobile.length()!=11){
            return IMOOCJSONResult.errorMsg("收货人电话长度不正确");
        }
        boolean b = MobileEmailUtils.checkMobileIsOk(mobile);
        if(!b){
            return IMOOCJSONResult.errorMsg("收货人电话格式不正确");
        }
        String province = addressBo.getProvince();
        String city = addressBo.getCity();
        String district = addressBo.getDistrict();
        String detail = addressBo.getDetail();
        if(StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)
        ){
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }
        return IMOOCJSONResult.ok();
    }

}
