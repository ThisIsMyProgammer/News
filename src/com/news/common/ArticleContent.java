package com.news.common;

import java.util.Set;

public class ArticleContent {
	public Set<String> articleLinks;
	public Set<String> articleContent;
	public String mainArticle;
	
	public ArticleContent(String articleLink, Set<String> links, Set<String> content){
		articleLinks = links;
		articleContent = content;
		mainArticle = articleLink;
	}
	public ArticleContent(){
		articleLinks = null;
		articleContent = null;
		mainArticle = null;
	}
	
	
}
