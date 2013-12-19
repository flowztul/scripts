legacyapi = burp/IBurpExtenderCallbacks.java burp/IBurpExtender.java burp/IHttpRequestResponse.java burp/IMenuItemHandler.java burp/IScanIssue.java burp/IScanQueueItem.java
legacyapihtml = burp/IBurpExtenderCallbacks.html burp/IBurpExtender.html burp/IHttpRequestResponse.html burp/IMenuItemHandler.html burp/IScanIssue.html burp/IScanQueueItem.html


default: run
  
legacyapi.zip:	
	wget http://portswigger.net/burp/extender/legacyapi.zip

burp/%.java: legacyapi.zip
	@unzip -n $< $@

burpsuite_free_v1.5.jar:
	wget http://portswigger.net/burp/burpsuite_free_v1.5.jar

burp/BurpExtender.class: burp/BurpExtender.java $(legacyapi)
	javac burp/BurpExtender.java

BurpExtender.jar: burp/BurpExtender.class
	jar cvf BurpExtender.jar burp/*.class

run: burpsuite_free_v1.5.jar BurpExtender.jar
	java -classpath burpsuite_free_v1.5.jar:BurpExtender.jar burp.StartBurp

clean:
	rm -f $(legacyapi) $(legacyapihtml) burp/*.class *.zip *.jar burp/BurpExtender.class
