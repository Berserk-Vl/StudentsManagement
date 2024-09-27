<b>Prerequisites:</b><br>
Project uses two libraries so you need to download both of them(.jar files).<br><br>
PostgreSQL database driver: https://mvnrepository.com/artifact/org.postgresql/postgresql <br>
JSON: https://mvnrepository.com/artifact/org.json/json <br><br>
Click on the latest version number in the list of released versions and then in the <b>Files</b></br>
section select <b>.jar</b> or click <b>View All</b> and then select the <b>.jar</b> file to download<br>
(to run the program you only need the library, not javadoc or source code .jar files).
<hr>
To run the program using CLI:

1. You need to have/create a database with a table students (to create the table you can
<br> use the provided script <i>createStudentsTable.sql</i>, and to fill in the data <i>insertStudents.sql</i> script).<br>
Default values for database:<br> 
Port - 5432<br>
Host - localhost<br>
Name - students<br>
User - postgres<br>
Password - admin<br>
You can change these values when starting the Server.
2. Go to the directory where the .java files are located (<i>/src</i>).
3. Compile the .java files using the command: <code>javac -cp \<pathToPostgreSQLJDBC\>;\<pathToJSON\>;. *.java</code>,<br>
where <i>\<pathToPostgreSQLJDBC\></i> and <i>\<pathToJSON\></i> are paths to .jar files which you download previously,<br>
put each path in quotes when you will run the command and if you use UNIX change semicolons to colons.
4. Run the program using the command: <code>java -cp \<pathToPostgreSQLJDBC\>;\<pathToJSON\>;. Server</code>
to get a help message use <i>-h</i> parameter.
5. Now that the Server is running, you can go to the <i>/web</i> directory and open the <i>index.html</i> file in your browser.<br>
If you are running the Server with not default values for a server address, then go to the <i>/web/scripts</i> directory,<br>
open the <i>main.js</i> file and change in the first line the server address to an address which you are using.