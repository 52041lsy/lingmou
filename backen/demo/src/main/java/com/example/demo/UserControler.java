package com.example.demo;

import com.example.demo.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserControler {
        @Autowired
        private UserService userService;

        @CrossOrigin
        @GetMapping(path = "/getAll")
        public @ResponseBody Iterable<User> getAllUsers()
        {
            return userService.getAllUsers();
        }

        @GetMapping(path = "/get")
        public @ResponseBody Optional<User> findById(String id)
        {
            return userService.findById(id);
        }

        @PostMapping(path = "/add")
        public @ResponseBody Optional<Boolean> add(@RequestParam String id, @RequestParam String password, @RequestParam String phonenumber, @RequestParam int type)
        {
            return userService.add(id,password,phonenumber,type);
        }

        //志愿者人数统计
        @GetMapping(path="/Volnum")
        public  @ResponseBody Optional<Integer> getVolsNum(){return userService.getVolsNum();}

        @GetMapping(path="/Patnum")
        public  @ResponseBody Optional<Integer> getPatsNum(){return userService.getPatsNum();}

        //获取手机号码
        @GetMapping(path="/Phone")
        public  @ResponseBody Optional<String> GetRandomPhone(){return userService.getRandomNum();}

        //修改电话号码
        @PostMapping(path = "/modifynum")
        public @ResponseBody Optional<Boolean> modifynum(@RequestParam String id, @RequestParam String phonenumber)
        {
            return userService.modifynum(id,phonenumber);
        }

        @PostMapping(path = "/delete")
        public @ResponseBody boolean deleteById(@RequestParam String id)
        {
            return userService.deleteById(id);
        }
}

