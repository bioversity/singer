#!/bin/sh

# This is just a script that will deploy my local war to the singer.cgiar.org live server.
# It can and will only run from Bioversity's network.

# Unpack my local war
mkdir livedeploy/singer
cd livedeploy/singer
jar -xvf ../../target/bioversity-singer.war 

# let's go back to our main folder where this sh script is
cd ../../

# copy the singer.properties.LIVE file to my local version
cp livedeploy/singer.properties.LIVE livedeploy/singer/WEB-INF/singer.properties

# pack the singer folder
cd livedeploy/singer
jar -cvf ../singer.war *

# let's go back to our main folder where this sh script is
cd ../../

# now we have a deploy/singer.war that we can send to the live site
# send it to the home directory on the live server
scp -r livedeploy/singer.war singer@172.19.0.206:/home/singer

# then move the file using ssh
#ssh singer@172.19.0.206 sudo sh /opt/tomcat/bin/shutdown.sh ; sudo rm -rf /opt/tomcat/webapps/singer ; sudo cp /home/singer/singer.war /opt/tomcat/webapps/ ; sudo sh /opt/tomcat/bin/startup.sh

# cleanup
rm -rf livedeploy/singer
rm -rf livedeploy/singer.war
