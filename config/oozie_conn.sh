#!/bin/sh

OOZIE_SITE="/etc/oozie/conf/oozie-site.xml"

sudo yum -y install xmlstarlet

sudo hdfs dfs -get s3://big-deploy/emr/sqoop/* /var/lib/oozie/

sudo xmlstarlet ed -u "//property[name='oozie.service.JPAService.jdbc.driver']/child::value" -v "$1" $OOZIE_SITE > t && sudo mv t $OOZIE_SITE
sudo xmlstarlet ed -u "//property[name='oozie.service.JPAService.jdbc.url']/child::value" -v "$2" $OOZIE_SITE > t && sudo mv t $OOZIE_SITE
sudo xmlstarlet ed -u "//property[name='oozie.service.JPAService.jdbc.username']/child::value" -v "$3" $OOZIE_SITE > t && sudo mv t $OOZIE_SITE
sudo xmlstarlet ed -u "//property[name='oozie.service.JPAService.jdbc.password']/child::value" -v "$4" $OOZIE_SITE > t && sudo mv t $OOZIE_SITE

sudo initctl restart oozie