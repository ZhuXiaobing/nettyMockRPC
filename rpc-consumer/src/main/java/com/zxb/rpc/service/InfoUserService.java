package com.zxb.rpc.service;

import com.zxb.rpc.annotation.RpcServiceConsumer;
import com.zxb.rpc.entity.InfoUser;

import java.util.List;
import java.util.Map;


@RpcServiceConsumer
public interface InfoUserService {

    List<InfoUser> insertInfoUser(InfoUser infoUser);

    InfoUser getInfoUserById(String id);

    void deleteInfoUserById(String id);

    String getNameById(String id);

    Map<String,InfoUser> getAllUser();
}
