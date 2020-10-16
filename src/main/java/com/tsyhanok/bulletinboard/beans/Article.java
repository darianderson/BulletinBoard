package com.tsyhanok.bulletinboard.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String text;

    private String author;

    public Article(String title, String text, String author) {
        this.title = title;
        this.text = text;
        this.author = author;
    }

}
