package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Integer> {


    @Query(value = "select * from user where userid = ?1;",nativeQuery = true)
    public Optional<User> findById(String id);

    @Query(value = "SELECT COUNT(*) FROM user WHERE type = 1", nativeQuery = true)
    public int Vol_cnt();

    @Query(value = "SELECT COUNT(*) FROM user WHERE type = 0", nativeQuery = true)
    public int Pat_cnt();

    @Query("SELECT u.phonenumber FROM User u WHERE u.type = 0 ORDER BY RAND() LIMIT 1")
    public String findRandomPhonenumberByType();


    @Transactional
    @Modifying
    @Query(value = "delete from user where userid = ?1;",nativeQuery = true)
    public int deleteById(String id);

    @Transactional
    @Modifying
    @Query(value = "update user set phonenumber=?2 where userid = ?1;",nativeQuery = true)
    public int modifynum(String id,String phonenumber);



}