SQOOP_VERSION="sqoop-1.4.6.bin__hadoop-2.0.4-alpha"

wget -q http://apache.rediris.es/sqoop/1.4.6/$SQOOP_VERSION.tar.gz -O /tmp/$SQOOP_VERSION.tar.gz
tar zxf /tmp/$SQOOP_VERSION.tar.gz -C /home/hadoop

hdfs dfs -rm /user/oozie/share/lib/sqoop/sqoop-1.4.3-hadoop100.jar

sudo yum -y install git

echo "" >> ~/.bashrc
echo "export PATH=$PATH:/home/hadoop/hive/hcatalog/bin:/home/hadoop/$SQOOP_VERSION/bin" >> ~/.bashrc
echo "export HCAT_HOME=/home/hadoop/hive/hcatalog/" >> ~/.bashrc
echo "export OOZIE_URL='http://localhost:11000/oozie'" >> ~/.bashrc