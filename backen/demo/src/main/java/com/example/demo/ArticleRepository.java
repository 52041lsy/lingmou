package com.example.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article,Integer> {


    @Query(value = "select title,content from article;",nativeQuery = true)
    public Iterable<Article> findById();


}