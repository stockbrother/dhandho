
.Configuration and running
==========================

..3rd Part Software

...JDK-v1.8

...OrientDB-v3
....	Download the package:
		
....	Unzip package:
    
...Jetty Server


.Buiding By Maven
===============
..Build Dhandho Core
	
	cd dhandho-core
	mvn install
	
..Build Dhandho Web App
	
	cd dhandho-web
	mvn package -DskipTests
	
.Eclipse Dev
============
..Enable GWT
	Properties>GWT>Use GWT
..Change GWT Lib to Top (Fix GWT Not Found issue)
	Poperties>Java Build Path>Order>Select GWT Lib>Top	

..Add GWT Lib
	Properties>Java Build Path>Libraries>Add Library>GWT
..Running Web App In Eclipse
	Right click on src/main/webapp/Dhandho.html
	Run as: GWT Dev Mode With Jetty
	Waiting the start complete
	Right click the http://127.0.0.1:8888/Dhandho.html in Development Mode tab.


.Deploy and Running
===================
..Prepare Data Folder
---------------------

	Data Folder Structure:
		
	+   C:\dhandho								--Home Folder
	+   C:\\dhandho\client\\init				--Folder contains data to be load for client init. 
		C:\\dhandho\client\\init\\metric-define-group-table.csv		--
		C:\\dhandho\client\\init\\metric-define-table.csv			--
	+	C:\dhandho\data\xueqiu\washed				--Folder contains washed data from xueqiu.com.
	+	C:\dhandho\server\init						--Load(If not do so before) at server start
		C:\dhandho\server\init\sse.corplist2.csv	--CorpList1
		C:\dhandho\server\init\sse.corplist.csv     --CorpList2
		C:\dhandho\server\init\szse.corplist.csv    --CorpList3
	
	
	
..Start orient Db
-----------------
...Start DB Server: 
	bin/server.bat
		    
...Create DB:
	http://localhost:2480
	DB:dhandho
	user:admin
	pass:admin
	
..Start Jetty Server
--------------------
	cd <jetty-home>/demo-base
	java -jar ../start.jar
	
..Deploy war
------------
	cp dhandho-web-<version>.war <jetty-home>/demo-base/webapps
	
..Access Web Page
-----------------
	http://localhost:8080/dhandho-web-<version>/
	
.Collect Data from xueqiu.com
=============================

-------------------------------------------------------------
.Wash Data
-------------------------------------------------------------

-------------------------------------------------------------
.Load Washed Data Into DB
-------------------------------------------------------------
..Put the washed data to folder:
	C:\dhandho\data\xueqiu\washed
..Click Load Data button in web page.

-------------------------------------------------------------
.Finally
-------------------------------------------------------------
	