package com.news.runner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.news.huffpost.reader.*;
import com.news.common.*;
public class RunReaders {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Read Home Page*/
		ReaderHomepage reader = new ReaderHomepage();
		Set<String> mainLinks = reader.huffFrontPageReader();
		Set<String> huffLinks = new HashSet<String>();
		Map<String, Integer> countSource = new HashMap<String,Integer>();
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
		
	}

}
