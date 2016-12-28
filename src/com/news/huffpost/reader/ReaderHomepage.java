package com.news.huffpost.reader;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ReaderHomepage {

	Document dom;
	String base_url;
	String rEnd;
	String base_search_url;
	
	public Set<String> huffFrontPageReader(){
		base_url = "http://www.huffingtonpost.com/";
		Set<String> UniqLinks = new HashSet<String>();
		try {
			dom = Jsoup.connect(base_url).get();
			Elements splashlinks = dom.getElementsByClass("splash__link");
			
			for( Element splashlink :  splashlinks){
				/*add all splash links to unqiue*/
				UniqLinks.add(splashlink.attr("href"));
			}
			/*This will get all links for all articles not just the middle ones*/
			Elements allMidleClassLinks = dom.getElementsByClass("card__link");
			//System.out.println(allMidleClassLinks.size());
			
			for( Element mLink : allMidleClassLinks){
				//add all mLinks to unqiue link set
				UniqLinks.add(mLink.attr("href"));
			}
			
			return UniqLinks;
			 			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
