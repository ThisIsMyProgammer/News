package com.news.common;

import java.util.List;

public class SiteConfig {

	String homePageURL;
	List<String> hometags;
	boolean homeDeepLook;
	int id;
	String searchType;
	
	public SiteConfig(){
		
	}
	
	public SiteConfig(String bUrl,int webID,List<String> inTags, String sType){
		homePageURL = bUrl;
		hometags = inTags;
		id = webID;
		searchType = sType;
	}
	
	public SiteConfig(String bUrl,int webID,List<String> inTags){
		this(bUrl,webID,inTags, "html");
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getHomePageURL() {
		return homePageURL;
	}
	public int getID(){
		return id;
	}

	public void setHomePageURL(String homePageURL) {
		this.homePageURL = homePageURL;
	}

	public List<String> getHomeTags() {
		return hometags;
	}

	public void setTags(List<String> tags) {
		this.hometags = tags;
	}
	
	
}
