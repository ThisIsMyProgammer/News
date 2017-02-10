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
	    testSingleGenericHomepage();
	    //readSingleGenericArticle();
		// testDatabase();

		// createBatch();
		// crateArtcleEntry();

		// @TODO still need to work on this function
		// crateProperEntry();

		// testJsonParser();

		// getDailyBatches();

		//testquery();
		//siteTest();
		//testThing();
	}
	
	
	public static void testThing(){
     try {
		DataBaseConnector dbConnect = new DataBaseConnector();
		Map<Integer, String> websiteMap = new HashMap<Integer, String>();
		CachedRowSet rsAllWebsites = dbConnect
				.queryNewsDB("Select * from WEBSITE");
		
		Vector<Vector<Integer>> batchCounts = new Vector<Vector<Integer>>();
		Vector<Integer> identifierRow = new Vector<Integer>();
		identifierRow.add(0);
		Vector<Integer> initializer = new Vector<Integer>();
		while (rsAllWebsites.next()){
			websiteMap.put( rsAllWebsites.getInt(1), rsAllWebsites.getString(2));
			identifierRow.add(rsAllWebsites.getInt(1));
			initializer.add(0);
		}
		CachedRowSet rsChartBatchBySite = dbConnect
				.queryNewsDB("Select ART.batch_id,ART.parent_id,web.home_link,count(ART.article_link)" + 
		" from ARTICLES ART join WEBSITE web where ART.parent_id = web.id group by batch_id, parent_id " +
						"order by ART.batch_id asc;");
		
		
		int numberOfSites = rsAllWebsites.size() + 2;
		initializer.add(0);
		Vector<Integer> newRow = null;
		System.out.println(initializer.size());
		
		
		//List<Integer> list = new ArrayList<>(Arrays.asList(intInializer));
		
		int currentBatch = 0;
		int batchCountTotal = 0;
		
		while(rsChartBatchBySite.next()){
			int batchID = rsChartBatchBySite.getInt(1);
			if(batchID != currentBatch){
				if(currentBatch != 0){
					newRow.add(numberOfSites - 1,batchCountTotal);
					batchCounts.add(newRow);
					
				}
				newRow =  new Vector<Integer>(initializer);
				currentBatch = batchID;
				newRow.set(0, batchID);
				batchCountTotal = 0;
			}
			
			int webID =  rsChartBatchBySite.getInt(2);
			int count =  rsChartBatchBySite.getInt(4);
			System.out.println(webID);
			batchCountTotal = batchCountTotal + count;
			newRow.set(webID, count);
		}
				
		StringBuilder dataBuild = new StringBuilder();
		dataBuild.append("['ID',");
		  for (int x = 1 ; x < numberOfSites - 1; x ++){
			  dataBuild.append("'");
			  String websiteFullLink = websiteMap.get(x);
			  System.out.println(websiteFullLink);
			  int trim_location = websiteFullLink.indexOf("www.");
			  trim_location = trim_location + 4;
			  dataBuild.append(websiteFullLink.substring(trim_location));
			  dataBuild.append("',");
		  }
		  dataBuild.append("'Total' ");
		  //build.replace(build.length(), build.length(), "");
		for(int i = 0; i < batchCounts.size(); i++){
			dataBuild.deleteCharAt(dataBuild.length() - 1);
			dataBuild.append("],[");
			for(int j = 0; j < batchCounts.get(i).size();j++){
				dataBuild.append(batchCounts.get(i).get(j));
				dataBuild.append(",");
			}
			System.out.println();
		}
		dataBuild.deleteCharAt(dataBuild.length() - 1);
		 System.out.println(dataBuild.toString());
	   
       }catch(SQLException e){
			   
		   }
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
		DataBaseConnector dbConnect = new DataBaseConnector();
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
		}

		/*
		 * List<String> huffLinkList = new ArrayList<String>();
		 * huffLinkList.add("splash__link"); huffLinkList.add("card__link");
		 * SiteConfig huffSite = new SiteConfig(huffUrl, huffLinkList);
		 * sites.add(huffSite);
		 */

	}

	public static void testSingleGenericHomepage() {
		Set<String> gatheredLinks = null;
		String testHomeUrl = "http://www.politico.com/";
		List<String> testLinkList = new ArrayList<String>();
		testLinkList.add("_top");
		//testLinkList.add("hero-v6-story__headline-link");
		GenericHomepage genHome = new GenericHomepage(testHomeUrl, -999);
		gatheredLinks = genHome.ReadHomepageLinks(testLinkList);

		for (String oneLink : gatheredLinks) {
			System.out.println(oneLink);
		}

	}

	public static void readSingleGenericArticle() {
		GenericArticle oneArt = new GenericArticle(				
				"http://www.huffingtonpost.com/entry/trump-administration-leaks_us_589a45f1e4b04061313a1fbb?ncid=inblnkushpmg00000009",
				1);
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

				}
			}
			SiteConfig newSite = new SiteConfig(searchHomeUrl, webID,
					searchingList);
			sites.add(newSite);

			int countSites = 0;

			for (SiteConfig singleSite : sites) {
				System.out.println("========================"
						+ singleSite.getHomePageURL()
						+ "======================");

				GenericHomepage genHome = new GenericHomepage(
						singleSite.getHomePageURL(), singleSite.getID());
				if (gatheredLinks != null)
					gatheredLinks.addAll(genHome.ReadHomepageLinks(singleSite
							.getHomeTags()));
				// gatheredLinks.addAll(gatheredLinks =
				// genHome.ReadTryHomepageLinks());
				else {
					System.out.println("working correctly");
					gatheredLinks = genHome.ReadHomepageLinks(singleSite
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