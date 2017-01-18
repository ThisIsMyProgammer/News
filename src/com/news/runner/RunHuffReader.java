package com.news.runner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.news.common.ArticleContent;
import com.news.common.DigestArticle;
import com.news.huffpost.reader.ReadHuffArticles;
import com.news.huffpost.reader.ReaderHomepage;

public class RunHuffReader {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Read Home Page*/
		ReaderHomepage reader = new ReaderHomepage();
		Set<String> mainLinks = reader.huffFrontPageReader();
		Set<String> huffLinks = new HashSet<String>();
		Map<String, Integer> countSource = new HashMap<String,Integer>();
		Map<String, Integer> properMaster = new HashMap<String,Integer>();
		huffLinks.addAll(mainLinks);
		
		System.out.println("----- ALL UNIQUE MAIN LINKS -----");
		for(String link : huffLinks ){
			System.out.println(link);
		}
		
		System.out.println("----- Start Reading article -----");
		
		
		for(String articlelink : mainLinks){
		/*Read Single Article*/
									
			System.out.println("----- Reading article: "+ articlelink + "-----");
			
			ReadHuffArticles huffArticle = new ReadHuffArticles(articlelink);
			
			ArticleContent oneArticleContent = huffArticle.readArticle();
			
			DigestArticle digestArt = new DigestArticle(oneArticleContent);
			
			Map<String, Integer> artProper = digestArt.digestSentences();
			
			for(Map.Entry<String, Integer> entry : artProper.entrySet()){
				
				Integer count = artProper.get(entry.getKey());
		        if (count != null) {
		        	properMaster.put(entry.getKey(), count + entry.getValue());
		        } else {
		        	properMaster.put(entry.getKey(), entry.getValue());
		        }
			}
			
			if(oneArticleContent != null){
				if(!oneArticleContent.articleLinks.isEmpty())
				huffLinks.addAll(oneArticleContent.articleLinks);
			}
			
		}
		
		System.out.println("----- ALL UNIQUE LINKS -----");
		for(String link : huffLinks ){
			System.out.println(link);
			int splitCount = 0;
			for(String linkSplit : link.split("/")){
				splitCount++;
				if(splitCount == 3){
					Integer count = countSource.get(linkSplit);
			        if (count != null) {
			        	countSource.put(linkSplit, count + 1);
			        } else {
			        	countSource.put(linkSplit, 1);
			        }
				}
			}
		}
		
		
		System.out.println("------ Website References ------");
		
		for (Map.Entry<String, Integer> entry : countSource.entrySet())
		{
		    System.out.println(entry.getKey() + " used " + entry.getValue() + " times ");
		}
		
		
		System.out.println("------All Proper Nouns and count ------");
		
		for (Map.Entry<String, Integer> entry : properMaster.entrySet())
		{
		    System.out.println(entry.getKey() + " used " + entry.getValue() + " times ");
		}
		
		
		
	}

}
