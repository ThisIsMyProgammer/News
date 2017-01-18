package com.news.common;

import java.util.List;

public class SiteConfig {

	String homePageURL;
	List<String> hometags;
	boolean homeDeepLook;
	
	public SiteConfig(){
		
	}
	
	public SiteConfig(String bUrl,List<String> inTags, boolean lookDeep){
		homePageURL = bUrl;
		hometags = inTags;
		homeDeepLook = lookDeep;
	}
	
	public SiteConfig(String bUrl,List<String> inTags){
		this(bUrl,inTags,false);
	}

	public boolean isHomeDeepLook() {
		return homeDeepLook;
	}

	public void setHomeDeepLook(boolean homeDeepLook) {
		this.homeDeepLook = homeDeepLook;
	}

	public String getHomePageURL() {
		return homePageURL;
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
