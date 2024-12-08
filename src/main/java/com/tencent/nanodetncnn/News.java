package com.tencent.nanodetncnn;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    /*资讯文章*/
    private String title;
    private String content;
    public News(String a,String b) {
        title = a;
        content = b;
    }
    protected News(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

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
    public int describeContents() {
        // 返回0，除非你有文件描述符要返回
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}