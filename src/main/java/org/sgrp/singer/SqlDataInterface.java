package org.sgrp.singer;
public interface SqlDataInterface {
    /**
     * The sql string to retrieve data
     */
    String getSql();  

    int getItemsPerPage();

    int getNumberOfPages();
}
