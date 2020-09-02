package com.you.rabbitmq;

import com.you.rabbitmq.UserInfo;
import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    List<UserInfo> selectAll();

    int updateByPrimaryKey(UserInfo record);
}