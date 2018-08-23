package com.merabreak.models.challenge;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Article implements Parcelable{

    private String article_name;
    private String content_type;
    private String content;
    private String article_key;
    private int content_id;
    private int astro_id;

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticle_key() {
        return article_key;
    }

    public void setArticle_key(String article_key) {
        this.article_key = article_key;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getAstro_id() {
        return astro_id;
    }

    public void setAstro_id(int astro_id) {
        this.astro_id = astro_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.article_name);
        dest.writeString(this.content_type);
        dest.writeString(this.content);
        dest.writeString(this.article_key);
        dest.writeInt(this.content_id);
        dest.writeInt(this.astro_id);
    }

    protected Article(Parcel in) {
        this.article_name = in.readString();
        this.content_type = in.readString();
        this.content = in.readString();
        this.article_key = in.readString();
        this.content_id = in.readInt();
        this.astro_id = in.readInt();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
