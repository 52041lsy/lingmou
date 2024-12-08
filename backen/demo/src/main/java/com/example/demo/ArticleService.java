package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ArticleService {
    @Autowired
    private ArticleRepository ArticleRepository;

    public Iterable<Article> findById()
    {
        return ArticleRepository.findById();
    }
}
