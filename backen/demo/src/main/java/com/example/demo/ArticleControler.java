package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/article")
public class ArticleControler {
        @Autowired
        private ArticleService articleService;

        @GetMapping(path = "/get")
        public @ResponseBody Iterable<Article> findTitleById()
        {
            return articleService.findById();
        }

}

