package com.news.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DigestArticle {
	
	public ArticleContent articleContent;
	
	public DigestArticle(ArticleContent inArticle){
		articleContent = inArticle;
	}
	
	public String cleanArticle(){
		StringBuilder cleanArt = new StringBuilder();
		for(String line : articleContent.articleLines){
			cleanArt.append(line.replace("<p>", "\n"));
		}
		return cleanArt.toString();
	
	}
	
	//extract quotes and digest sentences
	public Map<String, Integer> digestSentences(){
		List<String> allSent = getSentences(articleContent.articleLines);
		
		//unComment to see sentence breakdown
		/*for(String foundSent : allSent){
			System.out.println(foundSent);
		}*/
		
		Map<String, Integer> artProper = findProperNouns(allSent);	
		
		return artProper;
		
	}
	
	//TODO create quote extracting function
	public void extractQuotes(){
		List<String> quotes = new ArrayList<String>();
		List<String> noQuotesLines = new ArrayList<String>();
		for(String line : articleContent.articleLines){
			int startQuote =  line.indexOf("\"");
			if(startQuote != -1){
				int endQuote = line.indexOf("\"", startQuote + 1);
				if(endQuote != -1){
					String fullQuote = line.substring(startQuote, endQuote);
					System.out.println("======Found Quote=====");
					System.out.println(fullQuote);
				}
				else{
					System.out.println("Quote found without a closing quote");
				}
			}
		}
		
		articleContent.quotes = quotes;
		articleContent.noQuotedText = noQuotesLines;
	}
	
	//Break sentences based on punctuation 
	public List<String> getSentences(List<String> noQuotesLines){
		List<String> artSentences = new ArrayList<String>();
		for(String line : noQuotesLines){
			List<String> sentences =  Arrays.asList(line.split("[.?!]"));
			artSentences.addAll(sentences);
		}
		return artSentences;
	}
	
	//TODO needs to handle multiple word proper nouns
	public Map<String, Integer> findProperNouns(List<String> sentences){
		
		Map<String, Integer> uniqueProper = new HashMap<String,Integer>();
		
		Pattern caps = Pattern.compile("[A-Z]");
		Pattern endOfWord = Pattern.compile("[,\\s]");
		
		for(String sent : sentences){
			int sentMax = sent.length() - 1;
			int previousStart = 0;
			if(sent.length()> 0){
				if(sent.charAt(0) == ' '){
					sent = sent.substring(1);
				}
			}
			else continue;
			Matcher matcher = caps.matcher(sent);
			Matcher endMatcher = endOfWord.matcher(sent);
			boolean endProper = false;
								
			
			int startNoun = matcher.find(0) ? matcher.start() : -1;
			int endNoun = 0;
			while(startNoun != -1){
				endProper = false;
				StringBuilder properString = new StringBuilder(); 
				while(!endProper){
					if(startNoun != -1){
						
						endNoun = endMatcher.find(startNoun) ? endMatcher.start() : -1;
						if(endNoun == -1)endNoun = sentMax;
						properString.append(sent.substring(startNoun, endNoun));
					}
					if(sentMax > endNoun + 1){
						previousStart = startNoun;
						startNoun = matcher.find(endNoun + 1) ? matcher.start() : -1;
					
						if((startNoun == endNoun + 1) && endNoun != -1){
							endProper = false;
							properString.append(" ");
						}
						else{
							endProper = true;
							//System.out.println("------------Double String ----------------------");
							if(previousStart != 0){
								String properDone = properString.toString(); 
								System.out.println(properDone);
								Integer count = uniqueProper.get(properDone);
						        if (count != null) {
						        	uniqueProper.put(properDone, count + 1);
						        } else {
						        	uniqueProper.put(properDone, 1);
						        }
							}
							properString = new StringBuilder();
							//startNoun = -1;
						}
					}
					else {
						startNoun = -1;
						String properDone = properString.toString(); 
						System.out.println(properDone);
						Integer count = uniqueProper.get(properDone);
				        if (count != null) {
				        	uniqueProper.put(properDone, count + 1);
				        } else {
				        	uniqueProper.put(properDone, 1);
				        }
						endProper = true;
					}
				}
			}
		}
		
	    System.out.println("------ Unique Propers for " + articleContent.articleUrl + "------");
		
		for (Map.Entry<String, Integer> entry : uniqueProper.entrySet())
		{
		    System.out.println(entry.getKey() + " used " + entry.getValue() + " times ");
		}
		return uniqueProper;
		
		
	}

}
