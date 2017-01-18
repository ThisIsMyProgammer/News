package com.news.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.news.huffpost.reader.*;
import com.news.common.*;

public class RunReaders {

	public static void main(String[] args) {
				
		List<String> coveredWebsites = new ArrayList<String>();
		coveredWebsites.add("huffingtonpost");
		coveredWebsites.add("marketwatch");
		coveredWebsites.add("politico");

		List<SiteConfig> sites = new ArrayList<SiteConfig>();
		Set<String> gatheredLinks = null;
		List<ArticleContent> readArticles = new ArrayList<ArticleContent>();
		Map<String, Integer> allProperNouns = new HashMap<String, Integer>();

		String huffUrl = "http://www.huffingtonpost.com/";
		List<String> huffLinkList = new ArrayList<String>();
		huffLinkList.add("splash__link");
		huffLinkList.add("card__link");
		SiteConfig huffSite = new SiteConfig(huffUrl, huffLinkList);
		sites.add(huffSite);
		
		String politicoUrl = "http://www.politico.com/";
		List<String> politicoLinkList = new ArrayList<String>();
		politicoLinkList.add("headline-content");
		SiteConfig politicoSite = new SiteConfig(politicoUrl, politicoLinkList);
		sites.add(politicoSite);
				
		String marketUrl = "http://www.marketwatch.com";
		List<String> martketLinkList = new ArrayList<String>();
		martketLinkList.add("article__content");
		SiteConfig marketSite = new SiteConfig(marketUrl, martketLinkList, true);
		sites.add(marketSite);
		
		int countSites = 0;

		for (SiteConfig singleSite : sites) {
			System.out.println("========================"
					+ singleSite.getHomePageURL() + "======================");

			GenericHomepage genHome = new GenericHomepage(
					singleSite.getHomePageURL());
			if (gatheredLinks != null)
				gatheredLinks.addAll(genHome.ReadHomepageLinks(
						singleSite.getHomeTags(), singleSite.isHomeDeepLook()));
			else {
				System.out.println("working correctly");
				gatheredLinks = genHome.ReadHomepageLinks(
						singleSite.getHomeTags(), singleSite.isHomeDeepLook());
			}

			countSites++;

		}
		System.out
				.println("========================Running Articles======================");
		for (String oneLink : gatheredLinks) {
			String parentSite;
			if(oneLink.length() > 0 ){
				System.out.println(oneLink);
				String[] splitArticle = oneLink.split("\\.");
				System.out.println(splitArticle[0]);
				System.out.println(splitArticle[0]);
				parentSite = splitArticle[1];
			}
			else{
				parentSite = "";
			}
			//System.out.println(splitArticle);
			if (coveredWebsites.contains(parentSite)) {
				GenericArticle oneArticle = new GenericArticle(oneLink);
				ArticleContent newArt = oneArticle.readAndDigestArticle();
				readArticles.add(newArt);
				for (Map.Entry<String, Integer> entry : newArt.properNouns
						.entrySet()) {
					Integer count = allProperNouns.get(entry.getKey());
					if (count != null) {
						allProperNouns.put(entry.getKey(),
								count + entry.getValue());
					} else {
						allProperNouns.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}

		System.out.println("------ Unique Propers totals ------");

		for (Map.Entry<String, Integer> entry : allProperNouns.entrySet()) {
			System.out.println(entry.getKey() + " used " + entry.getValue()
					+ " times ");
		}

	}

}
