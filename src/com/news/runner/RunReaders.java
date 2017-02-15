package com.news.runner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.rowset.CachedRowSet;
import com.news.common.*;

public class RunReaders {

	public static void main(String[] args) {

		/*
		 * need to implement change to remove proper clean as well as getting
		 * rid of end of line 's
		 */

		List<String> coveredWebsites = new ArrayList<String>();
		coveredWebsites.add("huffingtonpost");
		coveredWebsites.add("marketwatch");
		coveredWebsites.add("bbc");
		coveredWebsites.add("reuters");
		coveredWebsites.add("politico");
		List<String> cleanProper = new ArrayList<String>();

		Set<String> siteLinks = null;

		DataBaseConnector dbConnect = new DataBaseConnector();
		try {

			CachedRowSet rsCleanProper = dbConnect
					.queryNewsDB("select * from PROPER_CLEANER;");

			while (rsCleanProper.next()) {
				cleanProper.add(rsCleanProper.getString(1));
			}

			int batch_id = -999;

			dbConnect.updateNewsDB("insert into BATCHES() values ();");
			CachedRowSet rsBatch = dbConnect
					.queryNewsDB("select max(batch_id) from BATCHES;");

			if (rsBatch.next()) {
				batch_id = rsBatch.getInt(1);
				System.out.println(batch_id);
			}
			if (batch_id != -999) {

				ResultSet rsHomeKeys = dbConnect
						.queryNewsDB("SELECT * FROM WEBSITE wb JOIN HOME_PAGE_SEARCH_KEYS hpsk on wb.id = hpsk.site_id order by wb.id;");
				int webID = -1;
				List<String> attrList = null;
				List<String> searchingList = null;
				String searchHomeUrl = null;
				
				List<SiteConfig> sites = new ArrayList<SiteConfig>();
				String sType = null;
				while (rsHomeKeys.next()) {

					if (rsHomeKeys.getInt(1) == webID) {
						attrList.add(rsHomeKeys.getString(4));
						searchingList.add(rsHomeKeys.getString(5));
					} else {
						if (searchHomeUrl != null) {
							SiteConfig newSite = new SiteConfig(searchHomeUrl,
									webID,attrList ,searchingList, sType);
							sites.add(newSite);
						}
						webID = rsHomeKeys.getInt(1);
						searchHomeUrl = rsHomeKeys.getString(2);
						System.out.println(searchHomeUrl);
						searchingList = new ArrayList<String>();
						attrList = new ArrayList<String>();
						attrList.add(rsHomeKeys.getString(4));
						searchingList.add(rsHomeKeys.getString(5));
						sType = rsHomeKeys.getString(6);

					}
				}
				SiteConfig newSite = new SiteConfig(searchHomeUrl, webID,
						attrList,searchingList, sType);
				sites.add(newSite);

				int countSites = 0;

				for (SiteConfig singleSite : sites) {
					System.out.println("========================"
							+ singleSite.getHomePageURL()
							+ "======================");

					GenericHomepage genHome = new GenericHomepage(
							singleSite.getHomePageURL(), singleSite.getID());
					siteLinks = new HashSet<String>();
					if (singleSite.getSearchType().equals("html")) {
						siteLinks.addAll(genHome.ReadHomepageLinks(singleSite.getHomeattr(),singleSite
								.getHomeTags()));
					} else {
						siteLinks.addAll(genHome
								.ReadHomepageJsonHomeLinks(singleSite
										.getHomeTags()));
					}

					for (String oneLink : siteLinks) {
						String parentSite;
						if (oneLink.length() > 0) {
							System.out.println(oneLink);
							String[] splitArticle = oneLink.split("\\.");
							System.out.println(splitArticle[1]);
							parentSite = splitArticle[1];
						} else {
							parentSite = "";
						}
						// System.out.println(splitArticle);
						if (coveredWebsites.contains(parentSite)) {

							CachedRowSet rsIsRead = dbConnect
									.queryNewsDB("Select article_id from ARTICLES where article_link = '"
											+ oneLink + "';");

							if (!rsIsRead.next()) {

								GenericArticle oneArticle = new GenericArticle(
										oneLink, singleSite.getID());
								ArticleContent newArt = oneArticle
										.readAndDigestArticle();
								if (newArt.articleLines.size() > 1) {
									dbConnect
											.updateNewsDB("insert into ARTICLES(article_link,parent_id,batch_id) values ('"
													+ newArt.articleUrl
													+ "',"
													+ singleSite.getID()
													+ ","
													+ batch_id + ");");

									CachedRowSet rsArticle = dbConnect
											.queryNewsDB("Select article_id from ARTICLES where article_link = '"
													+ newArt.articleUrl + "';");
									if (rsArticle.next()) {
										int article_id = rsArticle.getInt(1);
										for (Map.Entry<String, Integer> entry : newArt.properNouns
												.entrySet()) {

											String oneProper = entry.getKey();
											if (!cleanProper.contains(oneProper)) {

												int proper_id = -999;
												int count = -999;
												int art_count = -999;

												CachedRowSet rsProper = dbConnect
														.queryNewsDB("select proper_id, count from ALL_PROPER where full_proper = '"
																+ oneProper
																+ "';");

												if (rsProper.next()) {
													proper_id = rsProper
															.getInt(1);
													art_count = entry
															.getValue();
													count = rsProper.getInt(2)
															+ entry.getValue();
													dbConnect
															.updateNewsDB("update ALL_PROPER set count ="
																	+ count
																	+ " where proper_id ="
																	+ proper_id
																	+ ";");
												} else {
													count = entry.getValue();
													art_count = count;
													dbConnect
															.updateNewsDB("insert into ALL_PROPER(full_proper,count) values ('"
																	+ oneProper
																	+ "',"
																	+ count
																	+ ");");

													CachedRowSet newRS = dbConnect
															.queryNewsDB("select proper_id, count from ALL_PROPER where full_proper = '"
																	+ oneProper
																	+ "';");

													if (newRS.next()) {
														proper_id = newRS
																.getInt(1);
													} else {
														proper_id = -999;
														count = -999;
													}

												}

												if (proper_id != -999
														&& art_count != -999) {
													dbConnect
															.updateNewsDB("insert into ARTICLE_PROPER (proper_id,article_id,count) values ("
																	+ proper_id
																	+ ","
																	+ article_id
																	+ ","
																	+ art_count
																	+ ");");
												}

											}
										}
									}
								}
							}
						}
					}
				}
			}

			dbConnect
					.updateNewsDB("UPDATE BATCHES set END_TIME = CURRENT_TIMESTAMP where batch_id ="
							+ batch_id + ";");

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}
