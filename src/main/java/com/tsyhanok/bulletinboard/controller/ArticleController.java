package com.tsyhanok.bulletinboard.controller;

import com.tsyhanok.bulletinboard.beans.Article;
import com.tsyhanok.bulletinboard.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;


@Controller
@AllArgsConstructor
public class ArticleController {

    final ArticleRepository articleRepository;

    @GetMapping("/art/page/{pageno}")
    public ModelAndView pagination(@PathVariable Integer pageno){
        ModelAndView model = new ModelAndView("main-page");
        Page<Article> articlesPage =  articleRepository.findAll(PageRequest.of(pageno, 10));
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
