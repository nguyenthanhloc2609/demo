package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {

  public @Bean MongoClient mongoClient() {
      return MongoClients.create("mongodb+srv://juvia:locnt2609@juvia0.bgzyzm9.mongodb.net");
  }

  public @Bean MongoTemplate mongoTemplate() {
      return new MongoTemplate(mongoClient(), "clinic");
  }
}