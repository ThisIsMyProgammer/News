package com.news.common;
import java.sql.*;  

//import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.*;

import com.sun.rowset.CachedRowSetImpl;

import org.apache.log4j.*;

public class DataBaseConnector {
	
	
	Logger DBlogger;
	
	public DataBaseConnector(){
		DBlogger = Logger.getLogger("com.news.common.DataBaseConnector");
	}
	
	public CachedRowSet queryNewsDB(String sqlQuery){
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://192.168.1.21:3306/NEWS_DB","newsUser","Rewer011491");  
		//System.out.println(sqlQuery);
		DBlogger.info(sqlQuery);
		Statement stmt=con.createStatement();  
		ResultSet rs=stmt.executeQuery(sqlQuery); 
		ResultSetMetaData rsmd = rs.getMetaData();
		CachedRowSet rowset = new CachedRowSetImpl();
		rowset.populate(rs);
		
		con.close();
		return rowset;
		}catch(Exception e){ 
			DBlogger.error(e);	
			return null;
		}   
	}
	
	
	
	public void updateNewsDB(String sqlQuery){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			//System.out.println(sqlQuery);
			DBlogger.info(sqlQuery);
			Connection con=DriverManager.getConnection(  
			"jdbc:mysql://192.168.1.21:3306/NEWS_DB","newsUser","Rewer011491");  
			Statement stmt=con.createStatement();  
			stmt.executeUpdate(sqlQuery);
			con.close();
			}catch(Exception e){ 
				DBlogger.error(e);	
			}   
		}
	
	
}
