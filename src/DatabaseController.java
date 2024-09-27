import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseController {
    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_PORT = "5432";
    public static final String DEFAULT_USER = "postgres";
    public static final String DEFAULT_USER_PASSWORD = "admin";
    public static final String DEFAULT_DATABASE_NAME = "students";

    private String host;
    private String port;
    private String user;
    private String userPassword;
    private String databaseName;

    private String url;

    public DatabaseController() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        this.user = DEFAULT_USER;
        this.userPassword = DEFAULT_USER_PASSWORD;
        this.databaseName = DEFAULT_DATABASE_NAME;
        constructURL();
    }

    public DatabaseController(String host, String port, String user, String userPassword, String databaseName) {
        this();
        setHost(host);
        setPort(port);
        setUser(user);
        setUserPassword(userPassword);
        setDatabaseName(databaseName);
        constructURL();
    }

    public void setHost(String host) {
        if (host != null) {
            this.host = host;
        }
    }

    public void setPort(String port) {
        if (port != null) {
            this.port = port;
        }
    }

    public void setUser(String user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void setUserPassword(String userPassword) {
        if (userPassword != null) {
            this.userPassword = userPassword;
        }
    }

    public void setDatabaseName(String databaseName) {
        if (databaseName != null) {
            this.databaseName = databaseName;
        }
    }

    public List<Student> getAllStudents() {
        try (Connection connection = DriverManager.getConnection(url)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");
            List<Student> students = new LinkedList<>();
            while (resultSet.next()) {
                students.add(new Student(resultSet.getLong("id"), resultSet.getString("surname"),
                        resultSet.getString("name"), resultSet.getString("patronymic"),
                        resultSet.getString("date_of_birth"), resultSet.getString("group_id")));
            }
            resultSet.close();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student student) {
        try (Connection connection = DriverManager.getConnection(url)) {
            String sqlQuery = "INSERT INTO students(surname, name, patronymic, date_of_birth, group_id) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, student.getSurname());
            statement.setString(2, student.getName());
            statement.setString(3, student.getPatronymic());
            statement.setString(4, student.getDateOfBirth());
            statement.setString(5, student.getGroup());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(long id) {
        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE id = ?;");
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void constructURL() {
        url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName + "?user=" + user + "&password=" + userPassword;
    }
}
