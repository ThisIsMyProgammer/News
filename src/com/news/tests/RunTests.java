package com.news.tests;

import java.util.List;

import com.news.common.ArticleContent;
import com.news.common.DigestArticle;
import com.news.huffpost.reader.ReadHuffArticles;
import com.news.marketwatch.reader.MarketWatchHomeReader;
import com.news.marketwatch.reader.ReadMarketWatchArticle;
import com.news.politico.reader.PoliticoHomepageReader;

public class RunTests {

	public static void main(String[] args) {
		//runSingleArticle("http://www.huffingtonpost.com/entry/obamacare-repeal-delay_us_5862a42be4b0eb586487380d?4dbwz64j6k4lyds4i");
		//runMarketHomePage();
		//runMarketArticle("http://www.marketwatch.com/story/these-stock-market-sectors-will-be-the-most-volatile-in-2017-goldman-sachs-2017-01-05");
		runPoliticoHomePage();
	}
	
	
	public static void runSingleArticle(String url){
		ReadHuffArticles testRead = new ReadHuffArticles(url);
		ArticleContent testArticleContent = new ArticleContent();
		testArticleContent = testRead.readArticle();
		testArticleContent.printArticleLines();
		
		DigestArticle testDigest = new DigestArticle(testArticleContent);
		
		testDigest.digestSentences(); 
		
		
		//System.out.println(testDigest.cleanArticle());
		
		}
	
	public static void runMarketHomePage(){
		MarketWatchHomeReader MWHR = new MarketWatchHomeReader();
		MWHR.readMarketWatchHomepage();
	}
	
	
	public static void runMarketArticle(String burl){
		ReadMarketWatchArticle RMWA = new ReadMarketWatchArticle(burl);
		RMWA.readArticle();
	}
	
	public static void runPoliticoHomePage(){
		PoliticoHomepageReader PHR = new PoliticoHomepageReader();
		PHR.readPoliticoHomepage();
	}
	

}
