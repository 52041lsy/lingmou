package com.tencent.nanodetncnn;
import java.io.Serializable;
import java.util.Objects;

public class Article implements Serializable {
    private int id;
    private String title;
    private String content;

    public Article(){}
    public Article(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


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
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article user = (Article) o;
        return Objects.equals(id, user.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content);
    }
}
