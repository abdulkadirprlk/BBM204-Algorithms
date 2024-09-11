import java.io.File;
import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        /* YOUR CODE HERE */
        try{
            File file  = new File(filename);
            if (!file.exists() || !file.canRead()) {
                throw new IllegalArgumentException("File does not exist or cannot be read: " + filename);
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file); // Parse the XML file
            Element root = doc.getDocumentElement(); // Get the root element

            // Process Human Molecular Data
            if (root.getElementsByTagName("HumanMolecularData").getLength() != 0) {
                List<Molecule> humanMoleculesList = processMolecularData(root, "HumanMolecularData");
                molecularDataHuman = new MolecularData(humanMoleculesList);
            }

            // Process Vitales Molecular Data
            if (root.getElementsByTagName("VitalesMolecularData").getLength() != 0) {
                List<Molecule> vitalesMoleculesList = processMolecularData(root, "VitalesMolecularData");
                molecularDataVitales = new MolecularData(vitalesMoleculesList);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static List<Molecule> processMolecularData(Element root, String molecularDataType) throws NullPointerException {
        List<Molecule> molecules = new ArrayList<>();
        Element molecularData = (Element) root.getElementsByTagName(molecularDataType).item(0);
        NodeList moleculeList = molecularData.getElementsByTagName("Molecule");

        for (int i = 0; i < moleculeList.getLength(); i++) { // iterates over molecules
            Element moleculeElement = (Element) moleculeList.item(i);

            // check condition
            if (moleculeElement.getElementsByTagName("ID").getLength() == 0 ||
                    moleculeElement.getElementsByTagName("BondStrength").getLength() == 0) {
                throw new IllegalArgumentException("Missing necessary XML elements in molecule data");
            }
            String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
            int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
            Set<String> bonds = new HashSet<>();
            NodeList bondsList = moleculeElement.getElementsByTagName("Bonds");

            for (int j = 0; j < bondsList.getLength(); j++) { // iterates over bonds
                Element bondsElement = (Element) bondsList.item(j);
                NodeList moleculeIds = bondsElement.getElementsByTagName("MoleculeID");

                for (int k = 0; k < moleculeIds.getLength(); k++) { // iterates over bond names
                    String moleculeId = moleculeIds.item(k).getTextContent();
                    bonds.add(moleculeId);
                }
            }
            molecules.add(new Molecule(id, bondStrength, new ArrayList<>(bonds)));
        }
        return molecules;
    }
}
