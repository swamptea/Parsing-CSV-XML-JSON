import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString("data.json", json);
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString("data2.json", json2);
        String json3 = readString("data.json");
        List<Employee> list3 = jsonToList(json3);
    }

    public static List<Employee> parseCSV(String[] arr, String filename) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy strategy = new ColumnPositionMappingStrategy();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(arr);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Employee> parseXML(String filename) {
        List<Employee> list2 = new ArrayList<>();
        int id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            NodeList employeeElements = doc.getDocumentElement().getElementsByTagName("employee");
            for (int i = 0; i < employeeElements.getLength(); i++) {
                Node employee = employeeElements.item(i);
                Element e = (Element) employee;
                NodeList childList = e.getChildNodes();
                for (int j = 0; j < childList.getLength(); j++) {
                    Node child = childList.item(j);
                    if (Node.ELEMENT_NODE == child.getNodeType()) {
                        Element e2 = (Element) child;
                        switch (e2.getNodeName()) {
                            case "id":
                                id = Integer.parseInt(e2.getTextContent());
                                break;
                            case "firstName":
                                firstName = e2.getTextContent();
                                break;
                            case "lastName":
                                lastName = e2.getTextContent();
                                break;
                            case "country":
                                country = e2.getTextContent();
                                break;
                            case "age":
                                age = Integer.parseInt(e2.getTextContent());
                                break;
                        }
                    }
                }
                list2.add(new Employee(id, firstName, lastName, country, age));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return list2;
    }

    public static <T> String listToJson(List<T> list) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static <T> List jsonToList(String json) {
        List<Employee> list3 = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        JSONParser parser = new JSONParser();
        JSONArray empArray = null;
        try {
            empArray = (JSONArray) parser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (Object item : empArray) {
            JSONObject empJSONObject = (JSONObject) item;
            Employee employee = gson.fromJson(String.valueOf(empJSONObject), Employee.class);
            list3.add(employee);
        }
        for(Employee emp : list3){
            System.out.println(emp);
        }
        return list3;
    }

    public static void writeString(String filename, String json) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String filename) {
        String jsonString = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            jsonString = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
