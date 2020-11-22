package com.tsyhanok.bulletinboard.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Indexed
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field
    private String title;

    @Field
    @Column(columnDefinition = "TEXT")
    private String text;

    @Field
    private String author;

    private String picture;

    public Article(String title, String text, String author) {
        this.title = title;
        this.text = text;
        this.author = author;
    }

}
