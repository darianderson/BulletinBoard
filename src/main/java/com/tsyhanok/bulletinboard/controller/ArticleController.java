package com.tsyhanok.bulletinboard.controller;

import com.tsyhanok.bulletinboard.beans.Article;
import com.tsyhanok.bulletinboard.repository.ArticleRepository;
import com.tsyhanok.bulletinboard.service.HibernateSearchService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Collections;


@Controller
@AllArgsConstructor
public class ArticleController {

    final ArticleRepository articleRepository;
    final HibernateSearchService articleSearch;

    @PostMapping("/art")
    public String createArticle(@ModelAttribute("articleModel") Article article,
                                @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setAuthor(userDetails.getUsername());

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:art";
        }

        try {
            byte[] bytes = file.getBytes();
            String name = new Timestamp(System.currentTimeMillis()).getTime() + file.getOriginalFilename();
            Path path = Paths.get("C:\\Users\\win7-dfyh\\Desktop" +
                    "\\Project\\BulletinBoard\\src\\main\\resources\\static\\img\\" + file.getOriginalFilename());
            Files.write(path, bytes);
            article.setPicture(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        articleRepository.save(article);
        return "redirect:/art/page/0";
    }


    @GetMapping("/search")
    public ModelAndView search(@RequestParam(value = "search", required = false) String q) { // @RequestParam(value = "search", required = false) String q

        ModelAndView model = new ModelAndView("main-page");
        Page<Article> searchResults = null;
        try {
            searchResults = articleSearch.searchArticles(q);
        } catch (Exception ex) {

        }
        assert searchResults != null;

        if(!searchResults.isEmpty()){
            model.addObject("search", searchResults.getContent());
            model.addObject("numberOfPages", searchResults.getTotalPages());
        } else {
            model.addObject("message", "No article was found by your request...");
        }
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
}
