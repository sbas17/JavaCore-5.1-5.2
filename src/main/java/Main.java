//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.opencsv.CSVReader;
//import com.opencsv.bean.ColumnPositionMappingStrategy;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.swing.text.Element;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.*;
//import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");

        List<Employee> listXML = parseXML("data.xml");
        String jsonXML = listToJson(listXML);
        writeString(jsonXML, "data2.json");
    }

    public static List<Employee> parseCSV (String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML (String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileName);

        List<Employee> list = new ArrayList<>();

        NodeList root = document.getElementsByTagName("employee");
        for (int i = 0; i < root.getLength(); i++) {
            Node node = root.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                Employee employee = new Employee(id, firstName, lastName, country, age);
                list.add(employee);
            }

        }


//        Node root = document.getDocumentElement();
//        NodeList nodeList = root.getChildNodes();
//        List<Employee> list = new ArrayList<>();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    Element element = (Element) node;
//                    NamedNodeMap map = element.getAttributes();
//                    Employee person = new Employee();
//                    for (int a = 0; a < map.getLength(); a++) {
//                        String attrName = map.item(a).getNodeName();
//                        String attrValue = map.item(a).getNodeValue();
//                        if (attrName.equals("id")) {
//                            person.id = Long.valueOf(attrValue);
//                        } else if (attrName.equals("firstName")) {
//                            person.firstName = attrValue;
//                        } else if (attrName.equals("lastName")) {
//                            person.lastName = attrValue;
//                        } else if (attrName.equals("country")) {
//                            person.country = attrValue;
//                        } else {
//                            person.age = Integer.valueOf(attrValue);
//                        }
//                    }
//                    list.add(person);
//            }
//
//        }

        return list;
    }
}
