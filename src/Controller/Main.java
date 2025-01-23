package Controller;

import Model.*;
import View.GUI;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The entry point for the SpinFood event application.
 * This class handles reading participant data from a CSV file, initializing SpinFood event,
 * and printing summary information about participants and pairs.
 */
public class Main {

    /**
     * Reads a CSV file from the specified file path and returns the data as a list of lists of strings.
     * @param filepath the path to the CSV file.
     * @return a list of lists of strings containing the data from the CSV file.
     */
    public static List<List<String>> readCsv(String filepath) {
        List<List<String>> output = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();  // first line contains column headings, not the participant information
            while ((line = br.readLine()) != null) {
                String[] participant = line.split(",");
                output.add(Arrays.asList(participant));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File Not Found.");
        } catch (IOException e) {
            throw new RuntimeException("IO Exception.");
        }
        return output;
    }
    public static List<Group> getGroupsCluster(Group group, List<Group> eventGroups) {
        return GroupAlgorithm.findGroupsCluster(group, eventGroups);
    }

    /**
     * The main method initializes the SpinFood event by reading participant data from a CSV file.
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createAndShowGUI();

//         // read the input files of participants list and after party location
//         String filepath_inputList = "Daten/teilnehmerliste.csv";
//         String filepath_partyLocation = "Daten/partyLocation.csv";
//         List<List<String>> inputList = readCsv(filepath_inputList);
//         List<String> partyLocationList = readCsv(filepath_partyLocation).get(0);
//
//        // initialize SpinfoodEvent with the given input list
//         Location partyLocation = new Location(
//                 Double.parseDouble(partyLocationList.get(0)),
//                 Double.parseDouble(partyLocationList.get(1)));
//         SpinfoodEvent event1 = new SpinfoodEvent(partyLocation);
//         event1.createInitialParticipantsAndPairs(inputList);
//         System.out.println("Number of participants: " + event1.getParticipants().size());
//         System.out.println("Number of pairs: " + event1.getPairs().size());
//         System.out.println("Number of kitchens: " + event1.getKitchens().size());
//
//         // there are 3 criteria which the user can determine their importance:
//         // criteria 6.6: minimal food preference deviation
//         // criteria 6.7: minimal age difference
//         // criteria 6.8: maximal gender diversity
//         // 1 = most important, 2 = next important, 3 = least important; no repetitions allowed (must be 1x 1, 1x 2 & 1x 3)
//         int foodPreference = 1;
//         int ageDifference = 2;
//         int genderDiversity = 3;
//         event1.createPairs(foodPreference, ageDifference, genderDiversity);
//         event1.showMetricsOfPairs();
//         event1.numberThePairs();
//
//         int pathLength = 0;
//         int numberOfElements = 0;
//         char criteriaSet = 'A'; // choose a predefined set here or define a new set below
//
//         if (criteriaSet=='A') {
//             foodPreference = 1;
//             ageDifference = 3;
//             genderDiversity = 4;
//             pathLength = 2;
//             numberOfElements = 5;
//         } else if (criteriaSet=='B') {
//             foodPreference = 2;
//             ageDifference = 5;
//             genderDiversity = 4;
//             pathLength = 3;
//             numberOfElements = 1;
//         } else if (criteriaSet=='C') {
//             foodPreference = 3;
//             ageDifference = 2;
//             genderDiversity = 1;
//             pathLength = 4;
//             numberOfElements = 5;
//         } else { // define own new set here.
//             foodPreference = 1;
//             ageDifference = 2;
//             genderDiversity = 3;
//             pathLength = 4;
//             numberOfElements = 5;
//         }
//
//         event1.createGroups(foodPreference, ageDifference, genderDiversity, pathLength, numberOfElements);
////         event1.numberTheGroups();
////         for (Model.Group group : event1.getGroups()) {
////             System.out.print(group.getCourse() + " Group " + group.getNumber() + " has kitchen owner: ");
////             System.out.println(group.getKitchenOwner().getKitchenOwner().getName());
////         }
//         event1.showMetricsOfGroups();
//         event1.numberTheGroups();
//
//
//         event1.writeCsvMilestone2("Daten/output.csv");
    }


}
