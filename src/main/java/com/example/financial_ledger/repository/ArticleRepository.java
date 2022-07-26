package com.example.financial_ledger.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.example.financial_ledger.entity.Article;

public interface ArticleRepository extends CrudRepository<Article, Long> {
	@Override
	ArrayList<Article> findAll(); // 덮어쓰기 사용해서 findAll()의 반환타입을 ArrayList로 변경해줌.

}
