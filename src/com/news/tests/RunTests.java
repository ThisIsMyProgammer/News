package com.news.tests;

import java.util.List;

import com.news.common.ArticleContent;
import com.news.common.DigestArticle;
import com.news.huffpost.reader.ReadHuffArticles;

public class RunTests {

	public static void main(String[] args) {
		runSingleArticle("http://www.huffingtonpost.com/entry/obamacare-repeal-delay_us_5862a42be4b0eb586487380d?4dbwz64j6k4lyds4i");

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
	

}
