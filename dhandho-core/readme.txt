>.Requisition
>>.jdk1.8
	export JAVA_HOME=<path-to-jdk1.8>
>>.maven

>.Compile
	mvn compile
>.Runing 
	mvn exec:java -Dexec.mainClass=cc.dhandho.Main
>.Usage:
	cat   Print file content!
	chart Show metric value as SVG chart for corpId, years and metrics!
	cls   Clear screen!
	exit  Exit!
	help  Print this message!
	load  Load input data from folder!
	show  Show some thing. 'help show' for detail!
	sina  Collect/wash/load all-quotes data from sina.
>.TODO

>>.close DB properly when console exit.




	