#-i "acc|coop|dist|donor|inst|misscoll|misscoop|missdist|olinks|rec|miss|keywords" \
java \
-Xms500M -Xmx500M \
-classpath "/Users/lucamatteis/tomcat/lib/*:\
/Users/lucamatteis/tomcat/webapps/ROOT/WEB-INF/lib/*:\
/Users/lucamatteis/tomcat/webapps/ROOT/WEB-INF/classes/" \
org.sgrp.singer.Main \
-u \
-i "acc|coop|dist|donor|inst|misscoll|misscoop|missdist|olinks|rec|miss|keywords" \
-properties "/Users/lucamatteis/bioversity-singer/trunk/src/main/webapp/WEB-INF/singer.properties"
