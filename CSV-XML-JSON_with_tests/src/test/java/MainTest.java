import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

public class MainTest {

    List<Employee> list;
    String json = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25}," +
            "{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";


    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        list.add(new Employee(1, "John", "Smith", "USA", 25));
        list.add(new Employee(2, "Inav", "Petrov", "RU", 23));
    }

    @org.junit.jupiter.api.Test
    public void testParseCSV() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> actual = Main.parseCSV(columnMapping, "data.csv");
        assertThat(actual, containsInAnyOrder(list.toArray()));

    }

    @org.junit.jupiter.api.Test
    public void testParseXML() {
        List<Employee> actual = Main.parseXML("data.xml");
        assertThat(actual, containsInAnyOrder(list.toArray()));
    }

    @org.junit.jupiter.api.Test
    public void testlistToJson() {
        assertThat(Main.listToJson(list), equalTo(json));
    }

    @org.junit.jupiter.api.Test
    public void testJsonToList() {
        List<Employee> actual = Main.jsonToList(json);
        assertThat(actual, containsInAnyOrder(list.toArray()));
    }


}
