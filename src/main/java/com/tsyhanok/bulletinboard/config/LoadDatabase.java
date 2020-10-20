package com.tsyhanok.bulletinboard.config;

import com.tsyhanok.bulletinboard.beans.Article;
import com.tsyhanok.bulletinboard.beans.User;
import com.tsyhanok.bulletinboard.repository.ArticleRepository;
import com.tsyhanok.bulletinboard.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LoadDatabase {


    @Bean
    CommandLineRunner initDatabase(ArticleRepository repository, UserRepository userRepository) {

        return args -> {

            log.info("Preloading " + userRepository.save(new User("daria@mail.com", "Daria", "Tsyhanok",
                    "$2a$10$iLOIuBgqOt2kXXpawWOKNu5hGJAnw47kYfeXIUuybBQOfJwM/eEM2")));

            log.info("Preloading " + repository.save(new Article("Google", "Google was founded by Larry Page and Sergey Brin while they were Ph.D. students at Stanford University.",
                    "daria@mail.com")));

            log.info("Preloading " + repository.save(new Article("Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
                    + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco ","daria@mail.com")));

           log.info("Preloading " + repository.save(new Article("Tiger", "Content of tiger article.", "daria@mail.com")));

            log.info("Preloading " + repository.save(new Article("Title", "Content of article.", "daria@mail.com")));

            log.info("Preloading " + repository.save(new Article("Tiger", "Content of tiger article and Larry Page.", "daria@mail.com")));

        };
    }

}

