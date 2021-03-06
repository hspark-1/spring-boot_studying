package com.example.financial_ledger.dto;

import com.example.financial_ledger.entity.Article;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleForm {
	
	private Long id; // id νλ μΆκ°!!
	private String title;
	private String content;
	
	public Article toEntity() {
		return new Article(id, title, content);
	}
	
}
