package org.sgrp.singer.datadictionary;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataDictionary extends HttpServlet {

    String parseUri(String uri) {
        if(uri == null) return "";
        String arr[] = uri.split("/");
        if(arr.length > 2)
            return arr[2];
        else
            return "";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acc = parseUri(request.getRequestURI());
        request.setAttribute("acc", acc);

        request.getRequestDispatcher("/data-dictionary.jsp").forward(request, response);
    }
}
