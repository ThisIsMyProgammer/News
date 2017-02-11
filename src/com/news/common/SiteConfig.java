package com.news.common;

import java.util.List;

public class SiteConfig {

	String homePageURL;
	List<String> hometags;
	List<String> homeattr;
	boolean homeDeepLook;
	int id;
	String searchType;
	
	public SiteConfig(){
		
	}
	
	public SiteConfig(String bUrl,int webID,List<String> inAttr,List<String> inTags, String sType){
		homePageURL = bUrl;
		hometags = inTags;
		id = webID;
		searchType = sType;
		homeattr = inAttr;
	}
	
	public SiteConfig(String bUrl,int webID,List<String> inAttr,List<String> inTags){
		this(bUrl,webID,inAttr,inTags, "html");
	}

	public List<String> getHomeattr() {
		return homeattr;
	}

	public void setHomeattr(List<String> homeattr) {
		this.homeattr = homeattr;
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
