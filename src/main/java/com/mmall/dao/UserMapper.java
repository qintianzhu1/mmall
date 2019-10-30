package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String userName);

    int checkEmail(String email);

    User selectLogin(@Param("userName") String userName, @Param("password") String password);

    String selectQuestionByUserName(String userName);

    int checkAnswer(@Param("userName")String userName,@Param("question")String question,@Param("answer")String answer);

    int updatePasswordByUserName(@Param("userName")String userName,@Param("newPassword")String newPassword);

    int checkPassword(@Param("password") String password,  @Param("userId") Integer userId);

    int checkEmailByUserId(@Param("email") String email,@Param("userId") Integer userId);
}