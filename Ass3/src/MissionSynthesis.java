import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        /* YOUR CODE HERE */ 

        List<Molecule> humanMoleculesForSynthesis = findTheSynthesisMolecules(humanStructures);
        List<Molecule> vitalesMoleculesSynthesis = findTheSynthesisMolecules(diffStructures);

        List<Bond> bondList = createBondList(humanMoleculesForSynthesis, vitalesMoleculesSynthesis);

        serum = findMST(bondList);
        return serum;
    }

    public List<Bond> findMST(List<Bond> bondList) {
        // Create a map from each molecule to its bonds
        Map<Molecule, List<Bond>> moleculeToBonds = new HashMap<>();
        for (Bond bond : bondList) {
            moleculeToBonds.computeIfAbsent(bond.getFrom(), k -> new ArrayList<>()).add(bond);
            moleculeToBonds.computeIfAbsent(bond.getTo(), k -> new ArrayList<>()).add(bond);
        }
        // Create a priority queue to hold the bonds, sorted by weight
        PriorityQueue<Bond> bondQueue = new PriorityQueue<>(Comparator.comparing(Bond::getWeight));

        // Create a set to hold the molecules that have been added to the MST
        Set<Molecule> addedMolecules = new HashSet<>();

        // Create a list to hold the bonds in the MST
        List<Bond> mstBonds = new ArrayList<>();

        // Start with an arbitrary molecule
        Molecule startMolecule = bondList.get(0).getFrom();
        addedMolecules.add(startMolecule);
        bondQueue.addAll(moleculeToBonds.get(startMolecule));

        // While there are still bonds that can be added to the MST
        while (!bondQueue.isEmpty()) {
            Bond bond = bondQueue.poll(); // smallest bond

            // If the bond connects a molecule in the MST to a molecule outside the MST
            if (addedMolecules.contains(bond.getFrom()) != addedMolecules.contains(bond.getTo())) {
                mstBonds.add(bond);

                // Add the new molecule to the MST and add all its bonds to the priority queue
                Molecule newMolecule = addedMolecules.contains(bond.getFrom()) ? bond.getTo() : bond.getFrom();
                addedMolecules.add(newMolecule);
                bondQueue.addAll(moleculeToBonds.get(newMolecule));
            }
        }
        return mstBonds;
    }

    private double findMinimumCost(List<Bond> serum) {
        double minCost = 0.0;
        for (int i = 0; i < serum.size(); i++) {
            minCost += serum.get(i).getWeight();
        }
        return minCost;
    }

    private List<Molecule> findTheSynthesisMolecules(List<MolecularStructure> molecularStructures) {
    List<Molecule> synthesisBonds = new ArrayList<>();

    for (MolecularStructure structure : molecularStructures) {
        Molecule weakestMolecule = structure.getMoleculeWithWeakestBondStrength();
        if (weakestMolecule != null) {
            synthesisBonds.add(weakestMolecule);
        }
    }
    return synthesisBonds;
}

    private List<Bond> createBondList(List<Molecule> humanMolecules, List<Molecule> vitalesMolecules) {
        List<Molecule> allMolecules = Stream.concat(humanMolecules.stream(),
                vitalesMolecules.stream()).collect(Collectors.toList()); // list of all molecules
        List<Bond> bondList = new ArrayList<>();

        for (int i = 0; i < allMolecules.size() - 1; i++) {
            Molecule molecule1 = allMolecules.get(i);
            for (int j = i + 1; j < allMolecules.size(); j++) {
                Molecule molecule2 = allMolecules.get(j);
                double weight = (double) (molecule1.getBondStrength() + molecule2.getBondStrength()) / 2;
                Bond bond1 = new Bond(molecule1, molecule2, weight);
                Bond bond2 = new Bond(molecule2, molecule1, weight);
                bondList.add(bond1);
                bondList.add(bond2);
            }
        }
        return bondList;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */

        List<Molecule> humanMoleculesForSynthesis = findTheSynthesisMolecules(humanStructures);
        System.out.println("Typical human molecules selected for synthesis: " + humanMoleculesForSynthesis);
        List<Molecule> vitalesMoleculesSynthesis = findTheSynthesisMolecules(diffStructures);
        System.out.println("Vitales molecules selected for synthesis: " + vitalesMoleculesSynthesis);

        System.out.println("Synthesizing the serum...");
        for (Bond b : serum) {
            int from = Integer.parseInt(b.getFrom().getId().substring(1));
            int to = Integer.parseInt(b.getTo().getId().substring(1));
            if (to > from){
                System.out.println("Forming a bond between " + b.getFrom().getId() + " - " + b.getTo().getId() +
                        " with strength " + String.format("%.2f", b.getWeight()));
            }
            else {
                System.out.println("Forming a bond between " +  b.getTo().getId() + " - " + b.getFrom().getId() +
                        " with strength " + String.format("%.2f", b.getWeight()));
            }
        }
        double minCost = findMinimumCost(serum);
        System.out.println("The total serum bond strength is " + String.format("%.2f", minCost));
    }
}
