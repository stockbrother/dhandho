-------------------------------------------------------------
.Configuration and running
-------------------------------------------------------------

..3rd Part Software

...JDK-v1.8

...OrientDB-v3
....	Download the package:
		
....	Unzip package:
    
...Jetty Server

-------------------------------------------------------------
.Buiding By Mvn
-------------------------------------------------------------

-------------------------------------------------------------
.Eclipse Dev
-------------------------------------------------------------
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

-------------------------------------------------------------
.Deploy and Running
-------------------------------------------------------------
..Start orient Db
....	Start DB Server: 
		bin/server.bat
		    
....    Create DB:
		http://localhost:2480
		DB:dhandho
		user:admin
		pass:admin
		
..Start Jetty Server

..Deploy war

-------------------------------------------------------------
.Collect Data from xueqiu.com
-------------------------------------------------------------

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
	