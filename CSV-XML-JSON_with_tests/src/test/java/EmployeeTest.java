import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EmployeeTest {
    @org.junit.jupiter.api.Test
    public void testToString() {
        Employee employee = new Employee(1, "John", "Smith", "USA", 25);
        assertThat(employee, hasToString("Employee{id=1, firstName='John', lastName='Smith', " +
                "country='USA', age=25}"));

    }
}
