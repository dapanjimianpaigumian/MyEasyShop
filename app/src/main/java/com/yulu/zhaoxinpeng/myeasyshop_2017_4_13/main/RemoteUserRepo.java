package com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.main;

import com.feicuiedu.apphx.model.repository.IRemoteUsersRepo;
import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.commons.CurrentUser;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.GetUsersResult;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.model.User;
import com.yulu.zhaoxinpeng.myeasyshop_2017_4_13.network.NetClient;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/27.
 * 实现环信模块定义的远程用户仓库
 */

public class RemoteUserRepo implements IRemoteUsersRepo{
    @Override
    public List<EaseUser> queryByName(String username) throws Exception {
        Call call = NetClient.getInstance().getSearchUser(username);
        //同步执行，拿到返回结果
        Response response = call.execute();

        //如果失败
        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }

        String json=response.body().string();
        GetUsersResult result = new Gson().fromJson(json, GetUsersResult.class);

        //本地用户的类转换成环信的用户类
        List<User> users = result.getDatas();
        List<EaseUser> easeUsers = CurrentUser.convertAll(users);
        return easeUsers;
    }

    //通过环信Id查询用户信息
    @Override
    public List<EaseUser> getUsers(List<String> ids) throws Exception {
        //将好友列表存起来
        CurrentUser.setList(ids);

        Call call = NetClient.getInstance().getUsers(ids);
        Response response = call.execute();

        //如果失败
        if (!response.isSuccessful()) {
            throw new Exception(response.body().string());
        }

        String json=response.body().string();
        GetUsersResult result = new Gson().fromJson(json, GetUsersResult.class);

        if (result.getCode()==2) {
            throw new Exception(result.getMessage());
        }

        //本地用户的类转换成环信的用户类
        List<User> users = result.getDatas();
        List<EaseUser> easeUsers = CurrentUser.convertAll(users);
        return easeUsers;
    }
}
