package org.sgrp.singer.datadictionary;

import java.io.IOException;
import java.io.PrintWriter;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this has some DB api
import org.sgrp.singer.AccessionServlet;

public class Vote extends HttpServlet {

    private String getVoteInfo(String descriptor, String voteQuestion) throws SQLException{
        // create db connection
        Connection conn = AccessionServlet.openConnection();
        String query = "select count(*) as count, vote_value from data_dict_votes where descriptor = ? and vote_question = ? group by vote_value";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        prepStmt.setString(1, descriptor);
        prepStmt.setString(2, voteQuestion);

        ResultSet rs = prepStmt.executeQuery();

        String ret = voteQuestion +" <br>";
        while(rs.next()){
            String p = "people";
            if(rs.getString("count").equals("1"))
                p = "person"; 
            ret += rs.getString("count") + " "+p+" voted <b>"+ rs.getString("vote_value") + "</b>; "; 
        }

        ret += " <br>Please comment your vote";

        conn.close();

        return ret;
    }
    private String genStats() throws SQLException{
        Connection conn = AccessionServlet.openConnection();
        String query = "select *, count(vote_value) as num_votes from data_dict_votes group by descriptor, vote_question, vote_value";
        PreparedStatement prepStmt = conn.prepareStatement(query);
        ResultSet rs = prepStmt.executeQuery();

        String ret = "";
        while(rs.next()){
            ret += rs.getString("descriptor") + " - Question: "+rs.getString("vote_question")+"; "+rs.getString("num_votes")+" voted "+ rs.getString("vote_value") + "\n"; 
        }

        conn.close();

        return ret;
    }
    /**
     * display vote information for specific descriptor and voteQuestion
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descriptor = request.getParameter("descriptor");
        String voteQuestion = request.getParameter("voteQuestion");

        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.

        if(descriptor == null || voteQuestion == null) { // show stats
            String stats = "";
            try {
                stats = genStats();
            } catch (SQLException e){
            }
            response.getWriter().println(stats);
            return;
        }

        String voteInfo = "";
        try {
            voteInfo = getVoteInfo(descriptor, voteQuestion); 
        } catch (SQLException e) {
        }

        response.getWriter().println(voteInfo);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descriptor = request.getParameter("descriptor");
        String voteQuestion = request.getParameter("vote_question");
        String voteValue = request.getParameter("vote_value");

        if(descriptor == null || voteQuestion == null || voteValue == null)
            return;

        try {
            // create db connection
            Connection conn = AccessionServlet.openConnection();
            String query = "INSERT INTO data_dict_votes VALUES (null, ?, ?, ?, null, null)";
            PreparedStatement prepStmt = conn.prepareStatement(query);
            prepStmt.setString(1, descriptor);
            prepStmt.setString(2, voteQuestion);
            prepStmt.setString(3, voteValue);

            prepStmt.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
