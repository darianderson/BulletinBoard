package com.tsyhanok.bulletinboard.controller;

import com.tsyhanok.bulletinboard.beans.Article;
import com.tsyhanok.bulletinboard.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
@AllArgsConstructor
public class ArticleController {

    final ArticleRepository articleRepository;

    @GetMapping("/search/{search}")
    public ModelAndView findByPagingCriteria(@PathVariable String search){
        ModelAndView model = new ModelAndView("main-page");
        Page<Article> page = articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(search!=null) {
                    predicates.add(criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("title"), search),
                            criteriaBuilder.equal(root.get("author"), search)
                    ));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, PageRequest.of(0, 3));
        model.addObject("searchResult", page.getContent());
        model.addObject("pages", page.getTotalPages());
        return model;
    }

    @GetMapping("/art/page/{pageno}")
    public ModelAndView pagination(@PathVariable Integer pageno){
        ModelAndView model = new ModelAndView("main-page");
        Page<Article> articlesPage =  articleRepository.findAll(PageRequest.of(pageno, 3));
        if(articlesPage != null && articlesPage.getContent() != null){
            model.addObject("articles", articlesPage.getContent());
            model.addObject("numberOfPages", articlesPage.getTotalPages());
        } else {
            model.addObject("articles", Collections.emptyList());
            model.addObject("numberOfPages", 0);
        }
        return model;
    }

    @GetMapping("/art")
    public ModelAndView createArticle(){
        ModelAndView model = new ModelAndView("add-article");
        model.addObject("articleModel", new Article());

        return model;
    }

    @PostMapping("/art")
    public String createArticle(@ModelAttribute("articleModel") Article article) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setAuthor(userDetails.getUsername());
        articleRepository.save(article);
        return "redirect:/art/page/0";
    }

}
