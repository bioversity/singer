# This deployment script generates the appropriate 
# war container, that contains the entire SINGER site.
# It uses Maven to create such package.
export CATALINA_OPTS="-Xms1g -Xmx1g"
sh ~/tomcat/bin/shutdown.sh
#mvn -Dmaven.test.skip=true package
mvn package
rm -r ~/tomcat/webapps/ROOT
cp target/bioversity-singer.war ~/tomcat/webapps/ROOT.war
sh ~/tomcat/bin/startup.sh

# copy a .properties files, FIXME
#cp ~/singer-webapp/src/main/java/org/sgrp/singer/ApplicationResources.properties ~/tomcat/webapps/ROOT/WEB-INF/classes/org/sgrp/singer/
