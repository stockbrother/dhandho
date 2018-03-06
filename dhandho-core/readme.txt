
>.Requisitions
>>.jdk1.8
	export JAVA_HOME=<path-to-jdk1.8>
>>.maven

>.Compile
>>.Compile client side:
    Cd ../jsclient, npm build --base-href=/web/    
>>.Compile server(this) side:
	mvn compile
>.Runing
>>.Start the server:
	mvn exec:java -Dexec.mainClass=cc.dhandho.Main
>>.Access through browser:
	http://localhost:8080/web/index.html
			
>.Usage:
>>.In console:
	
>.TODO

>>




	