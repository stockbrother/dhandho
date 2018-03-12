
>.Requisitions
>>.jdk1.8
	export JAVA_HOME=<path-to-jdk1.8>
>>.maven

>.You may need to Compile
>>.Compile client side:
    Cd ../jsclient and  
    npm build --base-href=/web/    
>>.Compile server(this) side:
	mvn compile
	
>.Or Runing
>>.Start the server:
	mvn exec:java -Dexec.mainClass=cc.dhandho.Main
>>.Exit the server:
	exit		
>>.Access through browser:
	http://localhost:8080/web/index.html
			
>.Usage:
>>.In console:
	
>.TODO

>>




	