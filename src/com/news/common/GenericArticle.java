package com.news.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.rowset.CachedRowSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class GenericArticle {

	Document dom;
	String article_url;
	boolean readArticle = false;
	List<String> AticleContent;
	Set<String> ArticleUniqLinks;
	String parentSite;
	int parentID;

	public GenericArticle(String bUrl, int pID) {
		article_url = bUrl;
		parentID = pID;

	}

	public ArticleContent readAndDigestArticle() {
		readArticle();

		ArticleContent readArticleContent = new ArticleContent(article_url,
				ArticleUniqLinks, AticleContent);

		DigestArticle digestor = new DigestArticle(readArticleContent);
		readArticleContent.setProperNouns(digestor.digestSentences());

		return readArticleContent;

	}

	public int getParentID() {
		return parentID;
	}

	public void readArticle() {
		ArticleUniqLinks = new HashSet<String>();
		AticleContent = new ArrayList<String>();
		try {
			dom = Jsoup.connect(article_url).userAgent("Mozilla").get();
			Document.OutputSettings settings = dom.outputSettings();

			settings.prettyPrint(false);
			settings.escapeMode(Entities.EscapeMode.extended);
			settings.charset("ASCII");

			Elements getText = dom.getElementsByTag("p");

			DataBaseConnector dbconnect = new DataBaseConnector();

			CachedRowSet rsCleanBase = dbconnect
					.queryNewsDB("Select clean_tag from CLEAN_FOOTER where website_id = "
							+ parentID);
			List<String> cleaner = new ArrayList<String>();
			while (rsCleanBase.next()) {
				cleaner.add(rsCleanBase.getString(1));
			}

			for (int para = 0; para < getText.size(); para++) {
				if (!cleaner.contains(getText.get(para).attr("class"))) {
					AticleContent.add(getText.get(para).text());
					System.out.println(getText.get(para).text());
					Elements articleLinks = getText.get(para).getElementsByTag(
							"a");
					if (articleLinks.attr("href") != "") {
						ArticleUniqLinks.add(articleLinks.attr("href"));
					}
				}
			}

			readArticle = true;

		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
