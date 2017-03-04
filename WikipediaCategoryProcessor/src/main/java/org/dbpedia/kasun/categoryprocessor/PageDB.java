/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/** 
 *      Date             Author          Changes 
 *      Aug 3, 2013     Kasun Perera    Created   
 * 
 */ 

package org.dbpedia.kasun.categoryprocessor;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

/**
 * TODO- describe the  purpose  of  the  class
 * 
 */
public class PageDB {

	public static boolean isArticlePage(int pageId){
		boolean state= false;

		DB_connection con = new DB_connection();
		Connection connection = con.dbConnect();

		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT page_namespace FROM `page` WHERE `page_id` = " + pageId ;
		try{
			ps = connection.prepareStatement( query );
			rs = ps.executeQuery();
			while( rs.next() ){
				if(rs.getInt( "page_namespace" )== 0 ){
					state=true;
					break;
				}
			}
			connection.close();
		} catch ( SQLException e ){
			e.printStackTrace();
		}
		return state;
	}

	public static int getPageId(String catPageTitle){
		int resultId = 0;

		DB_connection con = new DB_connection();
		Connection connection = con.dbConnect();

		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT page_id FROM `category_only_page` WHERE `page_title` = '" + catPageTitle+"'" ;
		try{
			ps = connection.prepareStatement( query );
			rs = ps.executeQuery();
			while( rs.next() ){
				resultId= rs.getInt("page_id");
			}
			connection.close();
		} catch ( SQLException e ){
			e.printStackTrace();
		}
		return resultId;
	}

	public static HashMap<String,Integer> getAllPages() throws IOException{

		DB_connection con = new DB_connection();
		Connection connection = con.dbConnect();
		HashMap<String,Integer> pagesMap= new HashMap<String,Integer>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT page_id, page_title FROM `page` where page_namespace=0" ;
		
		try{
/*			String pathToIndex = "C:\\Users\\Dell\\Documents\\DBpedia\\categoty_page_candidate_index";
			IndexWriter iW;

			NIOFSDirectory dir = new NIOFSDirectory( new File( pathToIndex ) );
			iW = new IndexWriter( dir, new IndexWriterConfig( Version.LUCENE_43, new WhitespaceAnalyzer( Version.LUCENE_43 ) ) );
*/			
			ps = connection.prepareStatement( query );	
			rs = ps.executeQuery();
			
			while( rs.next() ){
//				Document doc = new Document();
//				doc.add( new TextField( "page_title",  rs.getString( "page_title" ), Field.Store.YES ) );
//				doc.add( new IntField( "page_id", rs.getInt("page_id"), Field.Store.YES ) );
				pagesMap.put(rs.getString("page_title"), rs.getInt("page_id"));
//				iW.addDocument( doc );
			}
			System.out.println("PageDB.getAllPages: index done");
			
//			iW.close();
//			dir.close();
			connection.close();
		} catch ( SQLException e ){
			e.printStackTrace();        
		}

		return pagesMap;
	}

	public static void insertCategoryPage( String data){
		DB_connection con = new DB_connection();
		Connection connection = con.dbConnect();
		PreparedStatement ps = null;

		String query = "INSERT IGNORE INTO category_only_page(page_id,page_namespace,page_title,page_restrictions, page_counter,page_is_redirect, page_is_new, page_random, page_touched,page_latest,page_len) VALUES ("+data+")";

		try{
			ps = connection.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			System.out.println(data);
		}

	}

	public static Page getPagebyID( int pageId )
	{

		DB_connection con = new DB_connection();
		Connection connection = con.dbConnect();

		PreparedStatement ps = null;
		ResultSet rs = null;
		Page page= new Page();

		String query = "SELECT page_id,page_namespace,page_title FROM page WHERE page_id = ?" ;

		try
		{
			ps = connection.prepareStatement( query );
			ps.setInt( 1, pageId );
			rs = ps.executeQuery();

			while( rs.next() ){
				page.setPageID(rs.getInt("page_id"));
				page.setPageNameSapce( rs.getInt("page_namespace"));
				page.setPageName( rs.getString("page_title") );
			}
			connection.close();
		} catch ( SQLException e ){
			e.printStackTrace();
		}
		return page;
	}
}