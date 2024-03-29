package com.zxb.rpc.configurer.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zxb.rpc.entity.Request;
import com.zxb.rpc.entity.Response;
import com.zxb.rpc.netty.client.NettyClient;
import com.zxb.rpc.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;


@Component
public class RpcFactory<T> implements InvocationHandler {

    @Autowired
    NettyClient client;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setId(IdUtil.getId());

        Object result = client.send(request);
        Class<?> returnType = method.getReturnType();

        Response response = JSON.parseObject(result.toString(), Response.class);
        if (response.getCode()==1){
            throw new Exception(response.getError_msg());
        } else {
            Object data = response.getData();
            return JSONObject.parseObject(data.toString(), returnType);
        }
    }
}
