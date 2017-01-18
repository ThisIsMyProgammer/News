package com.news.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.news.common.ArticleContent;
import com.news.common.DigestArticle;
import com.news.common.GenericArticle;
import com.news.common.GenericHomepage;
import com.news.common.SiteConfig;
import com.news.huffpost.reader.ReadHuffArticles;
import com.news.marketwatch.reader.MarketWatchHomeReader;
import com.news.marketwatch.reader.ReadMarketWatchArticle;
import com.news.politico.reader.PoliticoHomepageReader;

public class RunTests {

	public static void main(String[] args) {
		// runSingleArticle("http://www.huffingtonpost.com/entry/obamacare-repeal-delay_us_5862a42be4b0eb586487380d?4dbwz64j6k4lyds4i");
		// runMarketHomePage();
		// runMarketArticle("http://www.marketwatch.com/story/these-stock-market-sectors-will-be-the-most-volatile-in-2017-goldman-sachs-2017-01-05");
		// runPoliticoHomePage();
		//testGenericMultipleHompage();
		testSingleGenericHomepage();
		//readSingleGenericArticle();
	}

	public static void runSingleArticle(String url) {
		ReadHuffArticles testRead = new ReadHuffArticles(url);
		ArticleContent testArticleContent = new ArticleContent();
		testArticleContent = testRead.readArticle();
		testArticleContent.printArticleLines();

		DigestArticle testDigest = new DigestArticle(testArticleContent);

		testDigest.digestSentences();

		// System.out.println(testDigest.cleanArticle());

	}

	public static void runMarketHomePage() {
		MarketWatchHomeReader MWHR = new MarketWatchHomeReader();
		MWHR.readMarketWatchHomepage();
	}

	public static void runMarketArticle(String burl) {
		ReadMarketWatchArticle RMWA = new ReadMarketWatchArticle(burl);
		RMWA.readArticle();
	}

	public static void runPoliticoHomePage() {
		PoliticoHomepageReader PHR = new PoliticoHomepageReader();
		PHR.readPoliticoHomepage();
	}
	
	public static void testSingleGenericHomepage(){
		Set<String> gatheredLinks = null;
		String testHomeUrl = "https://www.bloomberg.com/";
		List<String> testLinkList = new ArrayList<String>();
		testLinkList.add("highlights-v6-story__headline-link");
		testLinkList.add("hero-v6-story__headline-link");
		GenericHomepage genHome = new GenericHomepage(testHomeUrl);
		gatheredLinks = genHome.ReadHomepageLinks(testLinkList,true);
		
		for (String oneLink : gatheredLinks) {
			System.out.println(oneLink);
		}
		
	}
	public static void readSingleGenericArticle(){
		GenericArticle oneArt = new GenericArticle("http://www.reuters.com/article/us-usa-trump-sessions-marijuana-idUSKBN14T2A2");
		oneArt.readArticle();
	}

	public static void testGenericMultipleHompage() {

		List<SiteConfig> sites = new ArrayList<SiteConfig>();
		Set<String> gatheredLinks = null;

		//Huffington Post
		String huffUrl = "http://www.huffingtonpost.com";
		List<String> huffLinkList = new ArrayList<String>();
		huffLinkList.add("splash__link");
		huffLinkList.add("card__link");
		SiteConfig huffSite = new SiteConfig(huffUrl, huffLinkList);
		sites.add(huffSite);
		//Market Watch
		String marketUrl = "http://www.marketwatch.com";
		List<String> martketLinkList = new ArrayList<String>();
		martketLinkList.add("article__content");
		SiteConfig marketSite = new SiteConfig(marketUrl, martketLinkList,true);
		sites.add(marketSite);
		//BBC
		String bbcUrl= "http://www.bbc.com";
		List<String> bccLinkList = new ArrayList<String>();
		bccLinkList.add("media__link");
		SiteConfig bbcSite = new SiteConfig(bbcUrl, bccLinkList,true);
		sites.add(bbcSite);
		//Reuters
		
		//Bloomberg
		String bloomUrl = "https://www.bloomberg.com/";
		List<String> bloomLinkList = new ArrayList<String>();
		bloomLinkList.add("highlights-v6-story__headline-link");
		bloomLinkList.add("hero-v6-story__headline-link");
		SiteConfig bloomSite = new SiteConfig(bloomUrl, bloomLinkList,true);
		sites.add(bloomSite);
			
	
		
		
		int countSites = 0;

		for (SiteConfig singleSite : sites) {
			System.out.println("========================" + singleSite.getHomePageURL() + "======================");
			

			GenericHomepage genHome = new GenericHomepage(singleSite.getHomePageURL());
			if(gatheredLinks !=null)
				gatheredLinks.addAll(genHome.ReadHomepageLinks(singleSite.getHomeTags(),singleSite.isHomeDeepLook()));
				//gatheredLinks.addAll(gatheredLinks = genHome.ReadTryHomepageLinks());
			else{
				System.out.println("working correctly");
				gatheredLinks = genHome.ReadHomepageLinks(singleSite.getHomeTags(),singleSite.isHomeDeepLook());
				
				//gatheredLinks = genHome.ReadTryHomepageLinks();
			}
			
			countSites++;

		}
		System.out.println("========================Printing all unique links======================");
		for (String oneLink : gatheredLinks) {
			System.out.println(oneLink);
		}

	}

}
