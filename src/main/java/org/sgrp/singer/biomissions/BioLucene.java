package org.sgrp.singer.biomissions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap; 

import java.io.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.CorruptIndexException;


public class BioLucene {

    public String lucenePath;

    public BioLucene(String lFile) {
        lucenePath = lFile;
    }

    public boolean indexExists() {
        File f;
        if((f = new File(lucenePath)).exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * Creates an index file with the data from a ResultSet
     * only if the lucenePath doesn't exist
     */
    public void write(ResultSet data) throws IOException, SQLException {
        IndexWriter writer;
        File f;

        if(indexExists()) {
            // path exists and it's a directory, don't do anything
            System.out.println("Lucene index already exists");
            return;
        }
        try {
            writer = new IndexWriter(lucenePath, new StandardAnalyzer(),
                                true, IndexWriter.MaxFieldLength.UNLIMITED); 
        } catch (Exception ex) {
            throw new IOException("Cannot create index..." + ex.getMessage());
        }

        while(data.next()){
            addDoc(writer, data);
        }

        writer.optimize();
        writer.close();
    }

    private static void addDoc(IndexWriter w, ResultSet data) throws IOException, SQLException {
        Document doc = new Document();
        doc.add(new Field("all", data.getString("ID_SUB_MISSION")+" "+data.getString("TAXON"), Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }


    /**
     * Reads the lucene index file and returns an array of Lucene Documents
     */
    public ArrayList<Document> read(String queryStr) throws CorruptIndexException, ParseException, IOException {
        ArrayList<Document> arr = new ArrayList<Document>();
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Now search the index:
        IndexSearcher isearcher = new IndexSearcher(lucenePath);
        QueryParser parser = new QueryParser("all", analyzer);
        Query query = parser.parse(queryStr);
        Hits hits = isearcher.search(query);
        // Iterate through the results:
        for (int i = 0; i < hits.length(); i++) {
            Document hitDoc = hits.doc(i);
            arr.add(hitDoc);
        }

        return arr;
    }

}
