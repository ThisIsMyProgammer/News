package com.news.politico.reader;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PoliticoHomepageReader {
	Document dom;
	String base_url;
	String rEnd;
	String base_search_url;
	
	public Set<String> readPoliticoHomepage(){
		base_url = "http://www.politico.com";
		Set<String> UniqLinks = new HashSet<String>();
		try {
			dom = Jsoup.connect(base_url).get();
			
			/*This will get all links for all articles not just the middle ones*/
			Elements headlineContentLinks = dom.getElementsByClass("headline-content");
			//System.out.println(allMidleClassLinks.size());
			
			for( Element mLink : headlineContentLinks){
				//add all mLinks to unqiue link set
				System.out.println(mLink.text());
				System.out.println(mLink.attr("href"));
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
