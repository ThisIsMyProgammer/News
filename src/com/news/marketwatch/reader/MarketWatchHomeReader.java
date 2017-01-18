package com.news.marketwatch.reader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MarketWatchHomeReader {
		
	Document dom;
	String base_url;
	String rEnd;
	String base_search_url;
	
	public Set<String> readMarketWatchHomepage(){
	
	base_url = "http://www.marketwatch.com";
	Set<String> UniqLinks = new HashSet<String>();
	try {
		dom = Jsoup.connect(base_url).get();
		Elements articleLinks = dom.getElementsByClass("article__content");
		
		for( Element articleLink :  articleLinks){
			/*add all splash links to unqiue*/
			System.out.println(articleLink.text());
			UniqLinks.add(articleLink.getElementsByIndexEquals(0).attr("href"));
			
		}
		
			
		return UniqLinks;
		 			
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	}
	
}

