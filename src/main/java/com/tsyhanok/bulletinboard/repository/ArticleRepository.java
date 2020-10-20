package com.tsyhanok.bulletinboard.repository;

import com.tsyhanok.bulletinboard.beans.Article;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {


}
