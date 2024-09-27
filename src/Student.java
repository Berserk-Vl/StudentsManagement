import org.json.JSONObject;

public class Student {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private String dateOfBirth;
    private String group;

    public Student(Long id, String surname, String name, String patronymic, String dateOfBirth, String group) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGroup() {
        return group;
    }

    public JSONObject serializeToJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("surname", surname);
        jsonObject.put("name", name);
        jsonObject.put("patronymic", patronymic);
        jsonObject.put("dateOfBirth", dateOfBirth);
        jsonObject.put("group", group);
        return jsonObject;
    }

    public static Student deserializeFromJSON(JSONObject jsonObject) {
        return new Student((jsonObject.has("id") ? jsonObject.getLong("id") : null), jsonObject.getString("surname"),
                jsonObject.getString("name"), jsonObject.getString("patronymic"),
                jsonObject.getString("dateOfBirth"), jsonObject.getString("group"));
    }
}
