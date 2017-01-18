package com.news.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ArticleContent {
	public Set<String> articleLinks;
	public List<String> articleLines;
	public List<String> quotes;
	public List<String> noQuotedText;
	public Map<String, Integer> properNouns;
	public String articleUrl;
	
	
	public ArticleContent(String articleLink, Set<String> links, List<String> content, Map<String,Integer> inProp){
		articleLinks = links;
		articleLines = content;
		articleUrl = articleLink;
		properNouns = inProp;
		quotes = null;
		noQuotedText = null;
	}
	
	
	public Set<String> getArticleLinks() {
		return articleLinks;
	}


	public void setArticleLinks(Set<String> articleLinks) {
		this.articleLinks = articleLinks;
	}


	public List<String> getArticleLines() {
		return articleLines;
	}


	public void setArticleLines(List<String> articleLines) {
		this.articleLines = articleLines;
	}


	public List<String> getQuotes() {
		return quotes;
	}


	public void setQuotes(List<String> quotes) {
		this.quotes = quotes;
	}


	public List<String> getNoQuotedText() {
		return noQuotedText;
	}


	public void setNoQuotedText(List<String> noQuotedText) {
		this.noQuotedText = noQuotedText;
	}


	public Map<String, Integer> getProperNouns() {
		return properNouns;
	}


	public void setProperNouns(Map<String, Integer> properNouns) {
		this.properNouns = properNouns;
	}


	public ArticleContent(String articleLink, Set<String> links, List<String> content){
		this(articleLink,links,content,null);
	}
	
	public ArticleContent(){
		this(null,null,null,null);
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
