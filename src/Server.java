import org.json.JSONArray;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.util.List;

public class Server {
    public static final String SERVER = "BSV";
    public static final int PORT = 3000;

    private static int port = PORT;
    private static DatabaseController databaseController;


    public static void main(String[] args) {
        if (parseArgs(args)) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server is running...");
                while (true) {
                    new Client(serverSocket.accept());
                    System.out.println("\nNew connection accepted.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("The server is offline.");
        }
    }

    public static String[] getResponse(String method, String url, String content) {
        if (url.equals("/students")) {
            switch (method) {
                case "GET":
                    return getStudents();
                default:
                    return new String[]{"403 Forbidden", ""};
            }
        } else if (url.equals("/student")) {
            switch (method) {
                case "POST":
                    return addStudent(content);
                default:
                    return new String[]{"403 Forbidden", ""};
            }
        } else if (url.matches("^/student/[a-zA-Z0-9]+$")) {
            switch (method) {
                case "DELETE":
                    return deleteStudent(url.substring("/student/".length()));
                default:
                    return new String[]{"403 Forbidden", ""};
            }
        } else {
            return new String[]{"404 Not found", ""};
        }
    }

    public static boolean isAllowedMethod(String method, String url) {
        if (url.equals("/students")) {
            switch (method) {
                case "GET":
                    return true;
                default:
                    return false;
            }
        } else if (url.equals("/student")) {
            switch (method) {
                case "POST":
                    return true;
                default:
                    return false;
            }
        } else if (url.matches("^/student/[a-zA-Z0-9]+$")) {
            switch (method) {
                case "DELETE":
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private static String[] getStudents() {
        List<Student> students = databaseController.getAllStudents();
        if (students != null) {
            JSONArray studentsArray = new JSONArray();
            for (Student student : students) {
                studentsArray.put(student.serializeToJSON());
            }
            return new String[]{"200 OK", studentsArray.toString()};
        } else {
            return new String[]{"500 Internal Server Error", ""};
        }
    }

    private static String[] deleteStudent(String pathVariable) {
        try {
            boolean deleted = databaseController.deleteStudent(Long.parseLong(pathVariable));
            return deleted ? new String[]{"200 OK", ""} : new String[]{"404 Not Found", ""};
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return new String[]{"400 Bad Request", ""};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"500 Internal Server Error", ""};
        }
    }

    private static String[] addStudent(String body) {
        try {
            Student student = Student.deserializeFromJSON(new JSONObject(body));
            boolean added = databaseController.addStudent(student);
            return added ? new String[]{"201 OK", ""} : new String[]{"500 Internal Server Error", ""};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"500 Internal Server Error", ""};
        }
    }

    private static boolean parseArgs(String[] args) {
        String databaseHost = null;
        String databasePort = null;
        String databaseUser = null;
        String databaseUserPassword = null;
        String databaseName = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    return false;
                case "-P":
                    port = Integer.parseInt(args[++i]);
                    break;
                case "-dbh":
                    databaseHost = args[++i];
                    break;
                case "-dbp":
                    databasePort = args[++i];
                    break;
                case "-dbu":
                    databaseUser = args[++i];
                    break;
                case "-dbup":
                    databaseUserPassword = args[++i];
                    break;
                case "-dbn":
                    databaseName = args[++i];
                    break;
                default:
                    return false;
            }
        }
        databaseController = new DatabaseController(databaseHost, databasePort,
                databaseUser, databaseUserPassword, databaseName);
        return true;
    }

    private static void printHelp() {
        System.out.println("Available parameters are:" +
                "\n[-h] - print help;" +
                "\n[-P <port number>] - the port the server will listen on(DEFAULT - 3000);" +
                "\n[-dbh <hostname>] - the hostname of the PostgreSQL server to connect to(DEFAULT - localhost);" +
                "\n[-dbp <port number>] - the port of the PostgreSQL server to connect to(DEFAULT - 5432);" +
                "\n[-dbu <user name>] - a database user name(DEFAULT - postgres);" +
                "\n[-dbup <user password>] - a database user password(DEFAULT - admin);" +
                "\n[-dbn <database name>] - a database name(DEFAULT - students);");
    }
}
