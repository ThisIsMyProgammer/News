package com.news.marketwatch.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.news.common.ArticleContent;

public class ReadMarketWatchArticle {
	Document dom;
	String article_url;
	boolean readArticle = false;
	List<String> AticleContent;
	Set<String> ArticleUniqLinks;
	
	
	public ReadMarketWatchArticle(String bUrl){
		
		article_url = bUrl;
	}
	
	public ArticleContent readArticle(){
		ArticleUniqLinks = new HashSet<String>();
		AticleContent = new ArrayList<String>();
		try {
		 dom = Jsoup.connect(article_url).userAgent("Mozilla").get();
				 
		 Elements getText = dom.getElementsByTag("p");
		 
		 for(int para = 0; para <getText.size(); para++){
			 AticleContent.add(getText.get(para).text());
			 System.out.println(getText.get(para).text());
			 
			 Elements articleLinks = getText.get(para).getElementsByTag("a");
			 if(articleLinks.attr("href")!= ""){
				 //System.out.println(articleLinks.attr("href"));
				 ArticleUniqLinks.add(articleLinks.attr("href"));
				 
			 }	 			 
			 
		 }
		 
		 
		 ArticleContent articleLinksContent = new ArticleContent(article_url,ArticleUniqLinks,AticleContent);
		 
		 readArticle = true;
		 return articleLinksContent;
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
}
