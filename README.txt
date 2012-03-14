Creating a WAR package
======================

To build a WAR package out of the entire SINGER site you need to
install Maven (http://maven.apache.org) and of course Java (JDK).

The root of the project is where the 'pom.xml' file is located. This
file defines all of the project settings and dependencies. From this
folder you need to type this in the command-line:

    mvn clean package

It will create a 'target' directory with a WAR file in it. You can 
now use this WAR file to deploy SINGER.



Using the WAR package
====================

Now that you have the entire SINGER site in a single WAR package you
must deploy it to your web server. With Tomcat you can simply copy the
WAR package to Tomcats "webapps" directory, and it will automatically 
deploy. 

Be sure to rename the directory it creates to "ROOT" otherwise the
site will only be accessible through 'http://site.com/singer-webapp'.
Renaming the folder to ROOT will make it so that SINGER can be
accessed through http://site.com/, and not by the sub-folder.



Configuring SINGER before starting the web server
================================================

SINGER's configuration files are located under
'src/main/webapp/WEB-INF/'

    * singer.properties
    * statements.properties

You must ONLY edit singer.properties with your local settings.
The important fields to edit are:

    * FT_INDEX_ROOT  - The full path of the Lucene indexes root folder
                       (we will discuss this in the next paragraph)

    * SQL_STATEMENTS - The full path of the statements.properties file
    
    * WEB_ROOT       - The full path to where the ROOT directory is
                       located under your Web Server

    * CACHE_ROOT     - This is a folder where cached Lucene indexes
                       are stored
    
    * JDBC_CONNECT_STRING - This hold information for accessing the
                            MySQL database



Database setup
==============

Setting up the database is very easy. Simply import the data from the
live servers and edit the JDBC_CONNECT_STRING attribute in
singer.properties.



Configuring the Web Server for SINGER
=====================================

SINGER needs lots of Java memory to run. This is because of Lucene.
The JVM needs to be configured to run with at least 1gb of memory. To
do this you must set the CATALINA_OPTS environment variable before
starting Tomcats web server (other webservers like JBOSS have other
methods of increasing the JVM memory).

Type this in a command line to set the environment variable:

    export CATALINA_OPTS="-Xms1g -Xmx1g"

Figure out your Web Servers setting and how to increase Java's memory
with that particular server.



Indexing data with Lucene
=========================

The site gets most of the its data from Lucene indexes which are
located in the FT_INDEX_ROOT path.

To create these indexes you need to run a .sh script I've created.
This script is called 'index.sh' and it's located at the root of the
project.

You need to edit this file with the appropriate paths and then you can
run it doing:

    sh index.sh

This usually takes a couple of hours to run.

Be sure to delete the CACHE_ROOT directory when indexing new data, 
it might show old data which is cached.
