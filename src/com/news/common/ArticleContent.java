package com.news.common;

import java.util.List;
import java.util.Set;

public class ArticleContent {
	public Set<String> articleLinks;
	public List<String> articleLines;
	public List<String> quotes;
	public List<String> noQuotedText;
	public String articleUrl;
	
	public ArticleContent(String articleLink, Set<String> links, List<String> content){
		articleLinks = links;
		articleLines = content;
		articleUrl = articleLink;
		quotes = null;
		noQuotedText = null;
	}
	public ArticleContent(){
		articleLinks = null;
		articleLines = null;
		articleUrl = null;
	}
	public void printArticleLinks(){
		for(String link : articleLinks ){
			System.out.println(link);
		}
	}
	public void printArticleLines(){
		for(String line : articleLines){
			System.out.println(line);
		}
	}
	
	
}
