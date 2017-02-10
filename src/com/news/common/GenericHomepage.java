package com.news.common;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GenericHomepage {

	Document dom;
	String base_url;
	String rEnd;
	String base_start_url;
	int id;

	public GenericHomepage(String HomeUrl, int webID) {
		base_url = HomeUrl;
		id = webID;
	}

	public int getID() {
		return id;
	}

	public Set<String> ReadHomepageLinks(List<String> tags) {
		Set<String> UniqLinks = new HashSet<String>();
		if (tags.size() > 0) {
			try {
				dom = Jsoup.connect(base_url).userAgent("Mozilla").get();
				Elements selectedElements = null;
				for (String tag : tags) {
					System.out.println(dom.text());

					selectedElements = dom.getElementsByClass(tag);

					System.out.println(tag);
					System.out.println(selectedElements.size());
					System.out.println(selectedElements.text());

					if (!selectedElements.isEmpty()) {
						for (Element oneElement : selectedElements) {
							System.out.println(oneElement.text());
							String homeLink;
							// if(!deepLook)
							// homeLink = oneElement.attr("href");
							// else
							homeLink = oneElement.getElementsByIndexEquals(0)
									.attr("href");

							if (!homeLink.startsWith(base_url)) {
								homeLink = base_url + homeLink;
							}
							UniqLinks.add(homeLink);
						}
					} else {
						System.out.println("No elements were selected");
					}

				}

				return UniqLinks;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		System.out.println("No tags were passed");
		return null;
	}
	
	public Set<String> ReadHomepageJsonHomeLinks(List<String> tags) {
		if (tags.size() == 1) {
			Set<String> UniqLinks = new HashSet<String>();
			try {
				
				dom = Jsoup.connect(base_url).userAgent("Mozilla").get();
				Elements allEl = dom.getAllElements();
				Elements scripts = dom.getElementsByTag("script");
			for (String tag : tags) {
				for (Element oneElement : scripts) {
					if (oneElement.hasAttr("type")) {
						// System.out.println(oneElement.attr("type"));
						if (oneElement.attr("type").equals(
								"application/ld+json")) {

							System.out.println(oneElement.data());

							String scriptText = oneElement.data();
							JSONObject obj = new JSONObject(scriptText);

							if (obj.has("itemListElement")) {
								JSONArray arr = obj
										.getJSONArray(tag);

								for (int i = 0; i < arr.length(); i++) {
									String outUrl = arr.getJSONObject(i)
											.getString("url");

									UniqLinks.add(outUrl);
								}

							}
						}
					}
				}
			}
				return UniqLinks;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public Set<String> ReadHomepageSingleLink(List<String> tags) {
		Set<String> UniqLinks = new HashSet<String>();
		if (tags.size() == 1) {
			try {
				dom = Jsoup.connect(base_url).userAgent("Mozilla").get();
				Elements selectedElements = null;
				for (String tag : tags) {
					System.out.println(dom.text());

					//selectedElements = dom.getElementsByClass(tag);
					
					selectedElements = dom.getElementsByAttribute("target");

							//dom.getElementsByAttributeValue("target", tag);
					//dom.getElementsByAttribute("target");

					System.out.println(tag);
					System.out.println(selectedElements.size());
					System.out.println(selectedElements.text());

					if (!selectedElements.isEmpty()) {
						for (Element oneElement : selectedElements) {
							System.out.println(oneElement.text());
							String homeLink;
							// if(!deepLook)
							// homeLink = oneElement.attr("href");
							// else
							homeLink = oneElement.getElementsByIndexEquals(0)
									.attr("href");

							if (!homeLink.startsWith(base_url)) {
								homeLink = base_url + homeLink;
							}
							UniqLinks.add(homeLink);
						}
					} else {
						System.out.println("No elements were selected");
					}

				}

				return UniqLinks;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			System.out.println("No tags were passed");
			return null;
		}
	}

}
