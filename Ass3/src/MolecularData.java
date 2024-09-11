import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures (simply finds the connected components)
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {

        ArrayList<MolecularStructure> structures = new ArrayList<>();

        if(molecules.isEmpty()) {return structures;}

        ensureReciprocalBonds(molecules);

        Map<String, Molecule> localMoleculesHashMap = new HashMap<>();
        for (Molecule molecule : getMolecules()) {
            localMoleculesHashMap.put(molecule.getId(), molecule);
        }

        Map<String, Boolean> visited = new HashMap<>();
        for (Molecule molecule : getMolecules()) {
            visited.put(molecule.getId(), false); // Initialize all molecules as not visited
        }

        Map<String, MolecularStructure> moleculeToStructureMap = new HashMap<>();

        for (String id : visited.keySet()){
            if (!visited.get(id)){
                MolecularStructure molecularStructure = new MolecularStructure();
                structures.add(molecularStructure);
                DFSUtil(id, visited, molecularStructure, localMoleculesHashMap, moleculeToStructureMap); // pass the local map to DFSUtil
            }
        }

        return structures;
    }

    // Depth-first search
    private void DFSUtil(String id, Map<String, Boolean> visited,
                     MolecularStructure molecularStructure, Map<String, Molecule> localMoleculesHashMap,
                     Map<String, MolecularStructure> moleculeToStructureMap)
    {
        visited.put(id, true);
        if (!moleculeToStructureMap.containsKey(id)) {
            molecularStructure.addMolecule(localMoleculesHashMap.get(id));
            moleculeToStructureMap.put(id, molecularStructure);
        }
        for (String adjacent : localMoleculesHashMap.get(id).getBonds()) {
            if (!visited.get(adjacent)) {
                DFSUtil(adjacent, visited, molecularStructure, localMoleculesHashMap, moleculeToStructureMap);
            }
        }
    }

    // helper method to ensure reciprocal bonds
    private void ensureReciprocalBonds(List<Molecule> molecules) {
        Map<String, Molecule> moleculesHashMap = new HashMap<>();
        for (Molecule molecule : molecules) {
            moleculesHashMap.put(molecule.getId(), molecule);
        }

        for (Molecule molecule : molecules) {
            for (String bond : new ArrayList<>(molecule.getBonds())) {
                if (!bond.equals(molecule.getId())) { // Check if the bond ID is not the same as the molecule's ID
                    if (moleculesHashMap.containsKey(bond)) {
                        Molecule bondedMolecule = moleculesHashMap.get(bond);
                        if (bondedMolecule != null && !bondedMolecule.getBonds().contains(molecule.getId())) {
                            bondedMolecule.getBonds().add(molecule.getId());
                        }
                    }
                }
            }
        }
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        
        /* YOUR CODE HERE */
        int numberOfMolecularStructures = molecularStructures.size();
        System.out.println(numberOfMolecularStructures +
                " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++){
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecularStructures.get(i));
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures,
                                                                  List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        
        /* YOUR CODE HERE */

        for (MolecularStructure targetStructure : targetStructures) {
            if (!sourceStructures.contains(targetStructure)) {
                anomalyList.add(targetStructure);
            }
        }
        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */

        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure anomaly : molecularStructures) {
            System.out.println(anomaly);
        }
    }
}
