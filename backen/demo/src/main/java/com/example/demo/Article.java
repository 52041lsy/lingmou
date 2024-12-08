package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Article implements Serializable {
    @Id
    //private int id;
    private String title;
    private String content;

    public Article() {}
    public Article( String title, String content) {
        //this.id = id;
        this.title = title;
        this.content = content;
    }

/*    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article user = (Article) o;
        return Objects.equals(id, user.id) ;
    }
*/

    @Override
    public int hashCode() {
        return Objects.hash(title, content);
    }
}
