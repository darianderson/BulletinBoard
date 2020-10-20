package com.tsyhanok.bulletinboard.service;

import com.tsyhanok.bulletinboard.beans.Article;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class HibernateSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Article> searchArticles(String searchText) {
        FullTextQuery jpaQuery = searchArticlesQuery(searchText);
        List<Article> result =  jpaQuery.getResultList();
        Page<Article> page = new PageImpl<>(result);
        return page;
    }

    private FullTextQuery searchArticlesQuery(String searchText) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Article.class)
                .get();

        Query luceneQuery = queryBuilder
                .keyword()
                .fuzzy()
                .onFields("title")
                .andField("author")
                .andField("text")
                .matching(searchText + "*")
                .createQuery();

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);
    }
}
