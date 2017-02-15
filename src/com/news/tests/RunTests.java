package com.news.tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;


import com.news.common.ArticleContent;
import com.news.common.DataBaseConnector;
import com.news.common.DigestArticle;
import com.news.common.GenericArticle;
import com.news.common.GenericHomepage;
import com.news.common.SiteConfig;
import com.news.huffpost.reader.ReadHuffArticles;
import com.news.marketwatch.reader.MarketWatchHomeReader;
import com.news.marketwatch.reader.ReadMarketWatchArticle;
import com.news.politico.reader.PoliticoHomepageReader;

public class RunTests {

	public static void main(String[] args) {
		// runSingleArticle("http://www.huffingtonpost.com/entry/obamacare-repeal-delay_us_5862a42be4b0eb586487380d?4dbwz64j6k4lyds4i");
		// runMarketHomePage();
		// runMarketArticle("http://www.marketwatch.com/story/these-stock-market-sectors-will-be-the-most-volatile-in-2017-goldman-sachs-2017-01-05");
		// runPoliticoHomePage();
	    //testGenericMultipleHompage();
	    //testSingleGenericHomepage();
	    //readSingleGenericArticle();
		// testDatabase();

		// createBatch();
		// crateArtcleEntry();

		// @TODO still need to work on this function
		// crateProperEntry();

		// testJsonParser();

		// getDailyBatches();

		////testquery();
		//siteTest();
		testThing();
	}
	
	
	public static void testThing(){
    	 StringBuilder full_page = new StringBuilder();
 		String mainDisplayHeader = "<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"CSS/MainDisplay.css\">";
 		full_page.append(mainDisplayHeader);

 		try {
 			DataBaseConnector dbConnect = new DataBaseConnector();

 			CachedRowSet rsAllPropers = dbConnect
 					.queryNewsDB("Select * from ALL_PROPER");

 			full_page
 					.append("<h2>Daily Proper Noun Counts</h2><table style=\"width:100%\">");
 			full_page.append("<tr><th>Full Proper</th><th>Type</th></tr>");

 			while (rsAllPropers.next()) {
 				String properString = rsAllPropers.getString(2);
 				full_page.append("<tr><td>"+ properString +"</td><td>1</td></tr>");
 				
 			}

 			full_page.append("</table>");
 		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		full_page.append("</html>");
		
		System.out.println(full_page.toString());
	}
	
	public static void siteTest(){
		 StringBuilder full_page =new StringBuilder();
	      String mainDisplayHeader = "<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"CSS/MainDisplay.css\">";
         full_page.append(mainDisplayHeader);
         String message = "Welcome to News Reader Main page";
         
         
         String googleChartsHeader = " <script type=\"text/javascript\""
       		  + "src=\"https://www.gstatic.com/charts/loader.js\"></script>"
       		  + "<script type=\"text/javascript\">"
       		  +"google.charts.load('current', {'packages':['corechart']});"
       		  +"google.charts.setOnLoadCallback(drawChart);"
       		  +"function drawChart() {"
       		  +"var data = new google.visualization.DataTable();"
       	      +"data.addColumn('string', 'Topping');"
       	      +"data.addColumn('number', 'Slices');"
       	      +"data.addRows(["
       	      +"['Mushrooms', 3],"
       	      +"['Onions', 1],"
       	      +"['Olives', 1],"
       	      +"['Zucchini', 1],"
       	      +"['Pepperoni', 2]"
       	      +"]);"
       	      +"var options = {'title':'How Much Pizza I Ate Last Night',"
                 +"'width':400,"
                 +"'height':300};"
                 +"var chart = new google.visualization.PieChart(document.getElementById('chart_div'));"
                 +"chart.draw(data, options);}"
                 +"</script>";
         
         full_page.append(googleChartsHeader);
         full_page.append("</head>");
	      full_page.append("<h1>" + message + "</h1>");
	     
	      
	      try {
				DataBaseConnector dbConnect = new DataBaseConnector();

				CachedRowSet rsbatch = dbConnect
						.queryNewsDB("select * from BATCHES where batch_id = (select max(batch_id) from BATCHES);");

				if (rsbatch.next()) {
					int batch_id = rsbatch.getInt(1);
					Timestamp start_time = rsbatch.getTimestamp(2);
					Timestamp end_time = rsbatch.getTimestamp(3);

					full_page.append("<h3>" + "The lastest batch is " + batch_id + " it started at " + start_time 
							 + " and ended at " +end_time + "</h3>" + 
							"<h2>Total Proper Noun Counts </h2>");
				}
				
				CachedRowSet rsMaxTenProper = dbConnect
						.queryNewsDB("select * from ALL_PROPER order by count desc limit 10;");
				
				
				full_page.append("<table style=\"width:100%\">");
				String startProperMax = "<tr><th>Name</th><th>Count</th></tr>";
				full_page.append(startProperMax);
				
				
				while (rsMaxTenProper.next()){
					
					String proper = rsMaxTenProper.getString(2);
					int count = rsMaxTenProper.getInt(3);
					
					String singleLine = "<tr><td>"+proper +"</td><td>" + count + "</td></tr>";
					full_page.append(singleLine);
				}
				full_page.append("</table>");
				
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dateobj = new Date();
				
				
				CachedRowSet rsbatchdaily = dbConnect
						.queryNewsDB("select * from BATCHES where START_TIME > '" + df.format(dateobj) + "';");
				
				String inBatches = "(";
				
				int batchStart = 1;
				while(rsbatchdaily.next()){
						int batch_id = rsbatchdaily.getInt(1);
						if(batchStart > 1){
							inBatches = inBatches + ",";
						}
						inBatches = inBatches + batch_id;
						batchStart++;
				}
				inBatches = inBatches + ")";

				
				String query = "select ALLP.full_proper, sum(ARTP.count) full_count from ARTICLE_PROPER ARTP join ALL_PROPER ALLP"
								+ " on ARTP.proper_id = ALLP.proper_id join ARTICLES ART on ART.article_id = ARTP.article_id"
								+ " join BATCHES bat on bat.batch_id = ART.batch_id where bat.batch_id in " + inBatches 
								+ " group by ARTP.proper_id" 
								+ " order by full_count desc limit 10;";
				
				
				System.out.println(query);
				
				CachedRowSet rsMaxTenProperDaily = dbConnect
						.queryNewsDB(query);

				full_page.append("<br><br><h2>Daily Proper Noun Counts</h2><table style=\"width:100%\">");
				String startProperMaxDaily = "<tr><th>Name</th><th>Count</th></tr>";
				full_page.append(startProperMaxDaily);

				while (rsMaxTenProperDaily.next()) {

					String proper = rsMaxTenProperDaily.getString(1);
					int count = rsMaxTenProperDaily.getInt(2);

					String singleLine = "<tr><td>" + proper + "</td><td>" + count
							+ "</td></tr>";
					full_page.append(singleLine);
				}
				full_page.append("</table>");	
				
				
				
				String allProperArticleCountQuery = "Select AP.full_proper, AP.count," 
						+" count(artProp.proper_id) articleCount from ALL_PROPER AP join ARTICLE_PROPER "
						+ "artProp on AP.proper_id = artProp.proper_id group by artProp.proper_id order by "
						+ "articleCount desc limit 20;";
				
				System.out.println(allProperArticleCountQuery);
						
						
						CachedRowSet rsArticleProperCountAll = dbConnect
								.queryNewsDB(allProperArticleCountQuery);
						
						full_page.append("<br><br><h2>Total Articles Used In</h2><table style=\"width:100%\">");
						String allProperArticleCountStart = "<tr><th>Name</th><th>Total Count</th><th>Article Count</th></tr>";
						full_page.append(allProperArticleCountStart);
						
						
						while(rsArticleProperCountAll.next()){
							
							String full_proper = rsArticleProperCountAll.getString(1);
							int total_count = rsArticleProperCountAll.getInt(2);
							int article_count = rsArticleProperCountAll.getInt(3);
							
							String singleLine = "<tr><td>" + full_proper + "</td><td>" + total_count
									+ "</td><td>" + article_count +"</td></tr>";
							full_page.append(singleLine);
							
						}
						
						
						full_page.append("</table>");	
						
						
						
						String dailyProperArticleCountQuery = "Select AP.full_proper, AP.count," 
								+" count(artProp.proper_id) articleCount from ALL_PROPER AP join ARTICLE_PROPER "
								+ "artProp on AP.proper_id = artProp.proper_id "
								+ "join ARTICLES ART on ART.article_id = artProp.article_id "
								+ "join BATCHES bat on bat.batch_id = ART.batch_id where bat.batch_id in " + inBatches 
								+ " group by artProp.proper_id order by "
								+ "articleCount desc limit 20;";
								
								
								CachedRowSet rsArticleProperCountDaily = dbConnect
										.queryNewsDB(dailyProperArticleCountQuery);
								
								full_page.append("<br><br><h2>Total Articles Used In Daily</h2><table style=\"width:100%\">");
								String dailyProperArticleCountStart = "<tr><th>Name</th><th>Total Count</th><th>Article Count</th></tr>";
								full_page.append(dailyProperArticleCountStart);
								
								
								while(rsArticleProperCountDaily.next()){
									
									String full_proper = rsArticleProperCountDaily.getString(1);
									int total_count = rsArticleProperCountDaily.getInt(2);
									int article_count = rsArticleProperCountDaily.getInt(3);
									
									String singleLine = "<tr><td>" + full_proper + "</td><td>" + total_count
											+ "</td><td>" + article_count +"</td></tr>";
									full_page.append(singleLine);
									
								}
								
								
								full_page.append("</table>");	
						
				
				//This must be the last thing added
				full_page.append("<div id=\"chart_div\"></div></html>");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	      System.out.println(full_page);
	}

	public static void testquery() {
		String message = "Welcome to News Reader Main page";
		StringBuilder full_page = new StringBuilder();
		full_page.append("<h1>" + message + "</h1>");

		try {
			DataBaseConnector dbConnect = new DataBaseConnector();

			CachedRowSet rsbatchdaily = dbConnect
					.queryNewsDB("select * from BATCHES where START_TIME > '2017-02-05';");
			
			String inBatches = "(";
			
			int batchStart = 1;
			while(rsbatchdaily.next()){
				System.out.println(rsbatchdaily.getInt(1));
					int batch_id = rsbatchdaily.getInt(1);
					if(batchStart > 1){
						inBatches = inBatches + ",";
					}
					inBatches = inBatches + batch_id;
					batchStart++;
			}
			inBatches = inBatches + ")";

			
			String query = "select ALLP.full_proper, sum(ARTP.count) full_count from ARTICLE_PROPER ARTP join ALL_PROPER ALLP"
							+ " on ARTP.proper_id = ALLP.proper_id join ARTICLES ART on ART.article_id = ARTP.article_id"
							+ " join BATCHES bat on bat.batch_id = ART.batch_id where bat.batch_id in " + inBatches 
							+ " group by ARTP.proper_id" 
							+ " order by full_count desc limit 10;";
			
			
			System.out.println(query);
			
			CachedRowSet rsMaxTenProperDaily = dbConnect
					.queryNewsDB(query);

			full_page.append("<table style=\"width:100%\">");
			String startProperMax = "<tr><th>Name</th><th>Count</th></tr>";
			full_page.append(startProperMax);

			while (rsMaxTenProperDaily.next()) {

				String proper = rsMaxTenProperDaily.getString(1);
				int count = rsMaxTenProperDaily.getInt(2);

				String singleLine = "<tr><td>" + proper + "</td><td>" + count
						+ "</td></tr>";
				full_page.append(singleLine);
			}
			full_page.append("</table");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(full_page);

	}

	public static void getDailyBatches() {

		try {
			DataBaseConnector dbConnect = new DataBaseConnector();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date dateobj = new Date();
			
			
			CachedRowSet rs = dbConnect
					.queryNewsDB("select * from BATCHES where START_TIME > '" + df.format(dateobj) + "';");

			while (rs.next()) {
				int batch_id = rs.getInt(1);
				Timestamp start_time = rs.getTimestamp(2);
				Timestamp end_time = rs.getTimestamp(3);

				System.out.println(batch_id);
				System.out.println(start_time.toString());
				System.out.println(end_time.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void testJsonParser() {
		GenericHomepage genHome = new GenericHomepage(
				"http://www.bbc.com/news", 3);
		// genHome.ReadHomepageJsonHomeLinks();
	}

	public static void createBatch() {

		try {
			DataBaseConnector dbConnect = new DataBaseConnector();

			dbConnect.updateNewsDB("insert into BATCHES() values ();");
			CachedRowSet rs = dbConnect
					.queryNewsDB("select max(batch_id) from BATCHES;");

			if (rs.next()) {
				int batch_id = rs.getInt(1);

				System.out.println(batch_id);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void crateArtcleEntry() {

		DataBaseConnector dbConnect = new DataBaseConnector();

		dbConnect
				.updateNewsDB("insert into ARTICLES(article_link,parent_id) values ('http://www.marketwatch.com/story/justice-department-wont-defend-trumps-travel-ban-for-now-2017-01-30',2);");

	}

	// Still writing this one
	public static void crateProperEntry() {

		DataBaseConnector dbConnect = new DataBaseConnector();

		GenericArticle oneArt = new GenericArticle(
				"http://www.marketwatch.com/story/justice-department-wont-defend-trumps-travel-ban-for-now-2017-01-30",
				2);
		oneArt.readArticle();

		ArticleContent TestArticle = oneArt.readAndDigestArticle();

		crateArtcleEntry();

		try {

			CachedRowSet rsArticle = dbConnect
					.queryNewsDB("Select article_id from ARTICLES where article_link = '"
							+ TestArticle.articleUrl + "';");
			if (rsArticle.next()) {
				int article_id = rsArticle.getInt(1);
				for (Map.Entry<String, Integer> entry : TestArticle.properNouns
						.entrySet()) {

					int proper_id = -999;
					int count = -999;

					String oneProper = entry.getKey();

					CachedRowSet rs = dbConnect
							.queryNewsDB("select proper_id, count from ALL_PROPER where full_proper = '"
									+ oneProper + "';");

					if (rs.next()) {
						proper_id = rs.getInt(1);
						count = rs.getInt(2);
					} else {
						count = entry.getValue();
						dbConnect
								.updateNewsDB("insert into ALL_PROPER(full_proper,count) values ('"
										+ oneProper + "'," + count + ");");

						CachedRowSet newRS = dbConnect
								.queryNewsDB("select proper_id, count from ALL_PROPER where full_proper = '"
										+ oneProper + "';");

						if (newRS.next()) {
							proper_id = newRS.getInt(1);
						} else {
							proper_id = -999;
							count = -999;
						}

					}

					if (proper_id != -999 && count != -999) {
						dbConnect
								.updateNewsDB("insert into ARTICLE_PROPER (proper_id,article_id,count) values ("
										+ proper_id
										+ ","
										+ article_id
										+ ","
										+ count + ");");
					}

				}
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public static void testDatabase() {
		/*DataBaseConnector dbConnect = new DataBaseConnector();
		try {
			CachedRowSet rs = dbConnect
					.queryNewsDB("SELECT * FROM WEBSITE wb JOIN HOME_PAGE_SEARCH_KEYS hpsk on wb.id = hpsk.site_id order by wb.id;");
			int webID = -1;
			List<String> searchingList = null;
			String searchHomeUrl = null;
			List<SiteConfig> sites = new ArrayList<SiteConfig>();
			while (rs.next()) {

				if (rs.getInt(1) == webID) {
					searchingList.add(rs.getString(4));
				} else {
					if (searchHomeUrl != null) {
						SiteConfig newSite = new SiteConfig(searchHomeUrl,
								webID, searchingList);
						sites.add(newSite);
					}
					webID = rs.getInt(1);
					searchHomeUrl = rs.getString(2);
					System.out.println(searchHomeUrl);
					searchingList = new ArrayList<String>();
					searchingList.add(rs.getString(4));
					System.out.println(rs.getString(4));

				}
			}
			SiteConfig newSite = new SiteConfig(searchHomeUrl, webID,
					searchingList);
			sites.add(newSite);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */

		/*
		 * List<String> huffLinkList = new ArrayList<String>();
		 * huffLinkList.add("splash__link"); huffLinkList.add("card__link");
		 * SiteConfig huffSite = new SiteConfig(huffUrl, huffLinkList);
		 * sites.add(huffSite);
		 */

	}

	public static void testSingleGenericHomepage() {
		Set<String> gatheredLinks = null;
		String testHomeUrl = "http://www.reuters.com";
		List<String> testLinkList = new ArrayList<String>();
		List<String> testattrList = new ArrayList<String>();
		/*testLinkList.add("_top");
		testattrList.add("target");*/
		testLinkList.add("story-title");
		testattrList.add("class");
		
		
		//testLinkList.add("hero-v6-story__headline-link");
		GenericHomepage genHome = new GenericHomepage(testHomeUrl, 5);
		gatheredLinks = genHome.ReadHomepageLinks(testattrList,testLinkList);

		for (String oneLink : gatheredLinks) {
			System.out.println(oneLink);
		}

	}

	public static void readSingleGenericArticle() {
		GenericArticle oneArt = new GenericArticle(				
				"http://www.politico.com/story/2017/02/elliott-abrams-no-deputy-secretary-of-state-234908",
				6);
		ArticleContent testContent = oneArt.readAndDigestArticle();

	}

	public static void testGenericMultipleHompage() {

		Set<String> gatheredLinks = null;

		DataBaseConnector dbConnect = new DataBaseConnector();
		try {
			ResultSet rs = dbConnect
					.queryNewsDB("SELECT * FROM WEBSITE wb JOIN HOME_PAGE_SEARCH_KEYS hpsk on wb.id = hpsk.site_id order by wb.id;");
			int webID = -1;
			List<String> searchingList = null;
			List<String> attrList = null;
			String searchHomeUrl = null;
			List<SiteConfig> sites = new ArrayList<SiteConfig>();
			while (rs.next()) {

				if (rs.getInt(1) == webID) {
					attrList.add(rs.getString(4));
					searchingList.add(rs.getString(5));
				} else {
					if (searchHomeUrl != null) {
						SiteConfig newSite = new SiteConfig(searchHomeUrl,
								webID,attrList, searchingList);
						sites.add(newSite);
					}
					webID = rs.getInt(1);
					searchHomeUrl = rs.getString(2);
					System.out.println(searchHomeUrl);
					searchingList = new ArrayList<String>();
					attrList = new ArrayList<String>();
					attrList.add(rs.getString(4));
					searchingList.add(rs.getString(5));

				}
			}
			SiteConfig newSite = new SiteConfig(searchHomeUrl, webID,
					attrList,searchingList);
			sites.add(newSite);

			int countSites = 0;

			for (SiteConfig singleSite : sites) {
				System.out.println("========================"
						+ singleSite.getHomePageURL()
						+ "======================");

				GenericHomepage genHome = new GenericHomepage(
						singleSite.getHomePageURL(), singleSite.getID());
				if (gatheredLinks != null){
					gatheredLinks.addAll(genHome.ReadHomepageLinks(singleSite.getHomeattr(),singleSite
							.getHomeTags()));
				// gatheredLinks.addAll(gatheredLinks =
				// genHome.ReadTryHomepageLinks());
				} else {
					System.out.println(countSites);
					System.out.println(singleSite.getHomePageURL());
					
					gatheredLinks = genHome.ReadHomepageLinks(singleSite.getHomeattr(),singleSite
							.getHomeTags());

					// gatheredLinks = genHome.ReadTryHomepageLinks();
				}

				countSites++;

			}
			System.out
					.println("========================Printing all unique links======================   ");
			for (String oneLink : gatheredLinks) {
				System.out.println(oneLink);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
