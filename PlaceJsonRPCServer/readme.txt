Author: Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, CIDSE, SE
Version: March 2017

See http://pooh.poly.asu.edu/Mobile

Purpose: Json-RPC server for places

This program is executable on MacOS, linux, and probably Windows.
The project consists of an executable jar file which is contained in the lib directory.
lib/placeserver.jar

This jar is a JsonRPC server for place (waypoint) management. It provides
a server for a library of place descriptions. Clients may call
the following methods, which are implemented by the server:

   public boolean resetFromJsonFile();  //reset the library to its original contents
   public boolean saveToJsonFile();     //save the library to a json file
   public boolean add(Item anItem);     //add a new place to the library
   public boolean remove(String aName); //remove the place named
   public String[] getNames();          //get a string array of titles kwown to the server
   public PlaceDescription get(String aName);       //get the named place 
   public String[] getCategoryNames();  //get category names for library places
   public String[] getNamesInCategory(String aCat); //get place names within a category

The shell script (.sh files) in the project directory show how to call these
methods demonstrating what attributes (see the placeAdd.sh script) are stored and
returned for Places.

See below for information on how to start, stop and send requests to the server.

The sample shell scripts that use the curl command to invoke and demonstrate
the calls to server functions.
There is also included a sample build.xml Ant build file that will
execute the server. Communication between a client and the server is done using
JSON-RPC over http. Don't be confused by the back slashes in the scripts. A literal
string (whether in a source program or command line) that includes a double quote
must escape (\) that double quote to indicate its an embedded double quote and not
the end of the string.

The purpose of this server is to provide a platform to
use to complete course exercises. The following are references for background on
these technologies (Json and JsonRPC):

JSON (JavaScript Object Notation):
 http://en.wikipedia.org/wiki/JSON
 The JSON web site: http://json.org/

JSON-RPC (JSON Remote Procedure Call):
 http://www.jsonrpc.org
 http://en.wikipedia.org/wiki/JSON-RPC

This example depends on the following frameworks:
1. Ant (not needed if you execute directly from the command line)
   see: http://ant.apache.org/
2. Json for the jdk (SE) as implemented by Doug Crockford.
   See: https://github.com/stleary/JSON-java
   Doug Crockford's reference implementation for the package org.json
   is included in the executable jar files, and are in the lib/json.jar file.
3. The instructor supplied server is provided in compiled form only.
   The server classes were compiled with jdk 1.8.0_112 javac. The org.json
   package was compiled with an older version of the jdk.

The place server is a stand alone Java app. Run it from a command line.
In doing so, you must specify a port number.
It uses the following default: place server -- 8080.

If you decide to write your own server, then it may be easiest to base your
implementation(s) on the source code provided in the student collection JsonRPC
example, which is linked from the schedule and assignment pages.

The server assumes that the Json objects they receive have the keys/attributes as
show in the initialization file: places.json. The place server is
constructed so that if you pass it a place Json object that does not contain all of the
information it expects then it will provide default values. You must provide it with an
object containing a "name", as it will not default the name's value.
If the server receives an object containing keys(attributes) that it does not expect,
then they will be ignored (not stored and not returned on a subsequent get request).
If you send the server a request that it does not expect (unknown method name,
or unexpected or arguments missing or of the wrong type), then it may respond with
an error, or it may not provide any response.

There are several ways to run the server:

With Ant:
ant execute.place.server

From the command line as an executable jar:
java -jar lib/placeserver.jar 8080

The included shell scripts which demonstrate server methods available will likely not
run on Windows, but should be fine on MacOS or Linux.
The scripts use the curl command which is commonly available if its
not already on your system. Use Brew, MacPort, or apt-get to install. I don't know
whether curl is installed on the ASU Lab Macs. To run the scripts:

(1) You will need to provide exection permission to the scripts (jar strips it) execute:
chmod u+x *.sh
(2) After starting one or both of the servers, move to another terminal window and run
(while the server is still running) any of the following to demonstrate calls and returns:
./placeAdd.sh
./placeGet.sh
./placeGetCategoryNames.sh
./placeGetNames.sh
./placeGetNamesInCategory.sh
./placeLoadJson.sh
./placeRemove.sh
./placeSave.sh

All assume that the default ports are used to start the servers and that they are
running on localhost.

(3) To stop the servers, on MacOS you can type Control-C (CNTRL-C) in the terminal.
On linux (and if you background the servers), you will probably need to us the ps
(process status) command to find the process id (pid -- a number) of the server and
then kill it with: kill -9 pid.

end

