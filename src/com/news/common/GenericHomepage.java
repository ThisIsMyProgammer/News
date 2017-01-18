package com.news.common;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GenericHomepage {

	Document dom;
	String base_url;
	String rEnd;
	String base_start_url;

	public GenericHomepage(String HomeUrl) {
		base_url = HomeUrl;
	}

	public Set<String> ReadHomepageLinks(List<String> tags, boolean deepLook) {
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
}
