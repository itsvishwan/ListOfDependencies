package com.example;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;



public class XmlFileReader {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        // Specify the directory containing the XML files
        String directoryPath = "C:\\Users\\viswa\\OneDrive\\Documents\\Projects\\ReadXMLCreateReport\\demo\\src\\main\\resources\\pomfiles";
        
        // Create a File object for the directory
        File directory = new File(directoryPath);
        
        // Get all the files in the directory
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));
        Map<String, Integer> fileNameCount = new HashMap();

        // Check if the directory contains XML files
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println("Processing file: " + file.getName());
                    try {
                        // Parse the XML file
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(file);
                        doc.getDocumentElement().normalize();
                        
                        // Process the XML content (example: print root element)
                        System.out.println("Root element: " + "dependencies");
                        
                        // Add further processing of XML content as needed

                        NodeList dependencyList = doc.getElementsByTagName("dependencies");

                        // Iterate through the book elements
                        for (int i = 0; i < dependencyList.getLength(); i++) {
                            Node bookNode = dependencyList.item(i);

                            if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element bookElement = (Element) bookNode;

                                for(int j=0;j < bookElement.getChildNodes().getLength();j++){
                                    NodeList dependencyNodes= bookElement.getChildNodes().item(j).getChildNodes();
                                    for(int k=0;k<dependencyNodes.getLength();k++){
                                        if(dependencyNodes.item(k).getNodeName().equals("artifactId")){
                                            String dependencyName = dependencyNodes.item(k).getTextContent();
                                            //System.out.println("dependencyName->"+dependencyName); 
                                            if(fileNameCount.containsKey(dependencyName)){
                                                Integer count = (Integer) fileNameCount.get(dependencyName);
                                                fileNameCount.put(dependencyName, count+1);
                                            }else{
                                                fileNameCount.put(dependencyName, 1);  
                                            }
                                        }                                    
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("fileNameCount-->"+fileNameCount);
            String csvFilePath = "output.csv";
             // Write the HashMap content to the CSV file
            try (FileWriter writer = new FileWriter(csvFilePath)) {
                // Write CSV header
                writer.append("DependencyName,Count\n");

                // Iterate through the HashMap and write each key-value pair to the CSV file
                for (Map.Entry<String, Integer> entry : fileNameCount.entrySet()) {
                    writer.append(entry.getKey())
                        .append(",")
                        .append(entry.getValue().toString())
                        .append("\n");
                }

                System.out.println("CSV file created successfully: " + csvFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No XML files found in the directory.");
        }
    }
}
