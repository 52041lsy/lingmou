package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //查询所有用户，返回所有用户信息
    public Iterable<User> getAllUsers()
    {
        return userRepository.findAll();
    }
    //根据id检索用户信息
    public Optional<User> findById(String id)
    {
        return userRepository.findById(id);
    }
    //新增用户
    public Optional<Boolean> add(String id, String password, String phonenumber, int type)
    {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        user.setUsernumber(phonenumber);
        user.setType(type);
        userRepository.save(user);
        return Optional.of(true);
    }
    //统计志愿者数量
    public Optional<Integer> getVolsNum(){
        return Optional.of(userRepository.Vol_cnt());
    }

    public Optional<Integer> getPatsNum(){
        return Optional.of(userRepository.Pat_cnt());
    }

    public Optional<String> getRandomNum(){
        return Optional.of(userRepository.findRandomPhonenumberByType());
    }
    //更改绑定的号码
    public Optional<Boolean> modifynum(String id, String phonenumber)
    {
        userRepository.modifynum(id,phonenumber);
        return Optional.of(true);
    }
    //用户注销
    public boolean deleteById(String id)
    {
        return userRepository.deleteById(id) != 0;
    }
}
