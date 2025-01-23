package Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the SpinFood Event where participants can be registered and organized into pairs and groups.
 * This class can read input data, process it to create Participant objects,
 * and organize them into pairs and groups.
 */

public class SpinfoodEvent {
    private List<Participant> participants;
    private List<Kitchen> kitchens;
    private List<Pair> pairs;
    private List<Group> groups;
    private final Location afterDinnerPartyLocation;
    private List<Pair> successorPairs;
    private List<Participant> successorParticipants;

    public SpinfoodEvent(Location location) {
        this.participants = new ArrayList<>();
        this.kitchens = new ArrayList<>();
        this.pairs = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.afterDinnerPartyLocation = location;
        this.successorPairs = new ArrayList<>();
        this.successorParticipants = new ArrayList<>();
    }


    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Kitchen> getKitchens() {
        return kitchens;
    }

    public void setKitchens(List<Kitchen> kitchens) {
        this.kitchens = kitchens;
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair> pairs) {
        this.pairs = pairs;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Location getAfterDinnerPartyLocation() {
        return afterDinnerPartyLocation;
    }

    public List<Participant> getSuccessorParticipants() { return PairAlgorithm.findParticipantsWithoutPartner(participants, pairs); }
    public void setSuccessorParticipants(List<Participant> successorParticipants) { this.successorParticipants = successorParticipants; }

    public List<Pair> getSuccessorPairs() { return GroupAlgorithm.findPairsWithoutGroups(this.pairs, this.groups); }
    public void setSuccessorPairs(List<Pair> successorPairs) { this.successorPairs = successorPairs; }

    /**
     * This function takes in parameters of a potential new kitchen and compares with the existing kitchens list.
     * If a kitchen with the same story, longitude and latitude exists already, the function returns this kitchen object.
     * @param story story of the potential new kitchen
     * @param longitude longitude of the potential new kitchen
     * @param latitude latitude of the potential new kitchen
     * @return the kitchen object if the same kitchen already exists; null if no kitchen is found
     */
    public Kitchen findExistingKitchen(Integer story, Double longitude, Double latitude) {
        Kitchen tempKitchen = null;
        for (Kitchen kitchen : kitchens) {
            if (kitchen.getStory().equals(story)
                    && kitchen.getLocation().getLongitude().equals(longitude)
                    && kitchen.getLocation().getLatitude().equals(latitude)) {
                tempKitchen = kitchen;
            }
        }
        return tempKitchen;
    }

    /**
     * This function reads strings from List<List<String>>, parse them into desired data objects
     * and create appropriate 'Participant' objects
     * as well as 'Pair' objects (if 2 participants are registered as a pair).
     * @param inputList is designated to be the output from function 'readCsv(String filepath)'.
     *                  The outer list should contain entries of all registrations.
     *                  The inner list should contain attribute values (stored as Strings) of each registration.
     */
    public void createInitialParticipantsAndPairs(List<List<String>> inputList) {
        for (List<String> sublist : inputList) {
            // each registration should at least contain the first 7 columns up to Kitchen (yes/no/maybe)
            // first column (index 0) contains only entry number, and is not needed
            String id = sublist.get(1);
            String name = sublist.get(2);
            FoodPreference foodPreference = FoodPreference.valueOf(sublist.get(3).toUpperCase());
            Integer age = (int) Double.parseDouble(sublist.get(4));
            Gender gender = Gender.valueOf(sublist.get(5).toUpperCase());
            KitchenExists kitchenExists = KitchenExists.valueOf(sublist.get(6).toUpperCase());
            Integer kitchenStory = null;
            Double kitchenLongitude = null;
            Double kitchenLatitude = null;

            // if the registration only contains the first 7 columns, create participant with the provided attributes only
            // if there are more attributs to read, continue reading more columns
            if (sublist.size() > 7) {
                if (sublist.get(7).isEmpty())
                    kitchenStory = 0;
                else
                    kitchenStory = (int) Double.parseDouble(sublist.get(7));
            }
            if (sublist.size() > 8) {
                kitchenLongitude = Double.parseDouble(sublist.get(8));
                kitchenLatitude = Double.parseDouble(sublist.get(9));
            }

            // create the first participant object with all the available attributes
            Participant participant1 = new Participant(id, name, foodPreference, age, gender, null);

            // if the participant defines the registered kitchen as unavailable ("NO"), the kitchen object of the participant stays null.
            if (!kitchenExists.equals(KitchenExists.NO)) {
                // if the kitchen is available ("YES"/"MAYBE"), find if a duplicated kitchen already exists.
                // use the same kitchen object if duplication is found
                Kitchen kitchen = findExistingKitchen(kitchenStory, kitchenLongitude, kitchenLatitude);
                if (kitchen == null){
                    kitchen = new Kitchen(kitchenExists, kitchenStory, kitchenLongitude, kitchenLatitude);
                    this.kitchens.add(kitchen);
                }
                participant1.setKitchen(kitchen);
            }
            this.participants.add(participant1);

            // read the attributes of the 2nd participant, if available
            if (sublist.size() == 14) {
                String id2 = sublist.get(10);
                String name2 = sublist.get(11);
                Integer age2 = (int) Double.parseDouble(sublist.get(12));
                Gender gender2 = Gender.valueOf(sublist.get(13).toUpperCase());
                Participant participant2 = new Participant(id2, name2, foodPreference, age2, gender2, null); // registered kitchen will be only linked to the first participant
                this.participants.add(participant2);
                // the 2 participants form a pair upon registration
                // & participant 1 owns the kitchen (Spezifikation Seite 5: die Küche nur der ersten Person zugeordnet)
                Pair pair = new Pair(participant1, participant2, foodPreference, false);
                pair.setRegisteredAsPair(true);
                this.pairs.add(pair);
            }
        }
    }


    /**
     * Calculates the air distance between two locations.
     * @param location1 first given location
     * @param location2 second given location
     * @return euclidian distance between location1 and location2
     */
    public static Double findDistance(Location location1, Location location2) {
        return Math.sqrt((location2.getLatitude() - location1.getLatitude()) * (location2.getLatitude() - location1.getLatitude())
                + (location2.getLongitude() - location1.getLongitude()) * (location2.getLongitude() - location1.getLongitude()));
    }

    /**
     * The dominant method to start creating pairs for the event, which extracts all participants without partner
     * and classifies the participants into groups depending on the existence of kitchen.
     * There should be only 1 parameter for each priority (1/2/3).
     * @param foodPreference the priority for food preference
     * @param ageDifference the priority for age difference
     * @param genderDiversity the priority for gender diversity
     */
    public void  createPairs(int foodPreference, int ageDifference, int genderDiversity) {
        System.out.println("\n** start createPairs **");
        final List<Participant> participantsWithoutPartner = PairAlgorithm.findParticipantsWithoutPartner(this.participants, this.pairs);
        System.out.println("BEFORE Number of participants without partner: " + participantsWithoutPartner.size());

        // since a kitchen is essential for a pair, we start with classifying participants with/has maybe/without kitchen
        List<Participant> participantsWithYesKitchen = PairAlgorithm.findParticipantsWithSpecificKitchenExists(participantsWithoutPartner, KitchenExists.YES);
        List<Participant> participantsWithMaybeKitchen = PairAlgorithm.findParticipantsWithSpecificKitchenExists(participantsWithoutPartner, KitchenExists.MAYBE);
        List<Participant> participantsWithNoKitchen = PairAlgorithm.findParticipantsWithSpecificKitchenExists(participantsWithoutPartner, KitchenExists.NO);

        // criteria 6.3: avoid waste of kitchens => we match those who have kitchen with those who do not have kitchen, if possible
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithNoKitchen, foodPreference, ageDifference, genderDiversity, true, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithMaybeKitchen, foodPreference, ageDifference, genderDiversity, true, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithYesKitchen, foodPreference, ageDifference, genderDiversity, true, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithMaybeKitchen, participantsWithNoKitchen, foodPreference, ageDifference, genderDiversity, true, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithMaybeKitchen, participantsWithMaybeKitchen, foodPreference, ageDifference, genderDiversity, true, this);

        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithNoKitchen, foodPreference, ageDifference, genderDiversity, false, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithMaybeKitchen, foodPreference, ageDifference, genderDiversity, false, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithYesKitchen, participantsWithYesKitchen, foodPreference, ageDifference, genderDiversity, false, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithMaybeKitchen, participantsWithNoKitchen, foodPreference, ageDifference, genderDiversity, false, this);
        PairAlgorithm.handlePairMatchingWithPreference(participantsWithMaybeKitchen, participantsWithMaybeKitchen, foodPreference, ageDifference, genderDiversity, false, this);

        System.out.println("AFTER Number of participants without partner: " + PairAlgorithm.findParticipantsWithoutPartner(this.participants, this.pairs).size());
        System.out.println("** end createPairs **");
        System.out.println();
    }


    /**
     * Displays the metrics of all the pairs (Pärchenkennzahlen).
     */
    public void showMetricsOfPairs() {
        System.out.println("====================Metrics of Pairs=====================");
        System.out.println(" Number of Pairs: " + this.pairs.size());
        System.out.println(" Number of Successors(Nachrückende): " + PairAlgorithm.findParticipantsWithoutPartner(this.participants, this.pairs).size());
        double sumFemaleProportion = 0.0;
        int sumAgeDifference = 0;
        int sumFoodPreferenceDifference = 0;
        for (Pair pair : this.pairs) {
            sumFemaleProportion += Metrics.measurePairFemaleProportion(pair);
            sumAgeDifference += Metrics.measurePairAgeGroupDifference(pair);
            sumFoodPreferenceDifference += Metrics.measurePairPreferenceDeviation(pair);
        }
        System.out.println(" Average Gender Diversity: " + (double) sumFemaleProportion/this.pairs.size());
        System.out.println(" Average Age Difference: " + (double) sumAgeDifference/this.pairs.size());
        System.out.println(" Average Food Preference deviation: " + (double) sumFoodPreferenceDifference/this.pairs.size());

        System.out.println("\n ---Only those created by algorithm---");
        List<Pair> pairList = new ArrayList<>();
        for (Pair pair : this.pairs)
            if (!pair.isRegisteredAsPair())
                pairList.add(pair);
        System.out.println(" Number of Pairs created by algorithm: " + pairList.size());

        sumFemaleProportion = 0.0;
        sumAgeDifference = 0;
        sumFoodPreferenceDifference = 0;
        for (Pair pair : pairList) {
            sumFemaleProportion += pair.getFemaleProportion();
            sumAgeDifference += pair.getAgeDifference();
            sumFoodPreferenceDifference += pair.getPreferenceDeviation();
        }
        System.out.println(" Average Gender Diversity: " + (double) sumFemaleProportion/pairList.size());
        System.out.println(" Average Age Difference: " + (double) sumAgeDifference/pairList.size());
        System.out.println(" Average Food Preference deviation: " + (double) sumFoodPreferenceDifference/pairList.size());
        System.out.println("=========================================================");
    }

    /**
     * Assigns numbers for the pairs.
     */
    public void numberThePairs() {
        for (int i = 0; i < this.pairs.size(); i++) {
            pairs.get(i).setNumber(i+1);
        }
    }

    /**
     * Writes the results to a csv file for milestone 2.
     * @param filepath the given filepath to write the file.
     */
    public void writeCsvMilestone2(String filepath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            StringBuilder content = new StringBuilder();
            for (Pair pair:this.pairs) {
                Integer appetizerGroupNumber = null;
                Integer mainGroupNumber = null;
                Integer dessertGroupNumber = null;
                for (Group group : this.groups) {
                    if (group.getGroupPairs().contains(pair)) {
                        if (group.getCourse() == Course.APPETIZER) {
                            appetizerGroupNumber = group.getNumber();
                        } else if (group.getCourse() == Course.MAIN) {
                            mainGroupNumber = group.getNumber();
                        } else if (group.getCourse() == Course.DESSERT) {
                            dessertGroupNumber = group.getNumber();
                        }
                    }
                }
                if (appetizerGroupNumber != null){
                    content.append(pair.getParticipant1().getName()).append(";");
                    content.append(pair.getParticipant2().getName()).append(";");
                    content.append(pair.isRegisteredAsPair()).append(";");
                    content.append(pair.getKitchenLocation().getLongitude()).append(";");
                    content.append(pair.getKitchenLocation().getLatitude()).append(";");
                    content.append(pair.getFoodPreference() == FoodPreference.NONE ? "ANY" : pair.getFoodPreference()).append(";");
                    content.append(pair.getNumber()).append(";");
                    content.append(appetizerGroupNumber != null ? appetizerGroupNumber : "").append(";");
                    content.append(mainGroupNumber != null ? mainGroupNumber : "").append(";");
                    content.append(dessertGroupNumber != null ? dessertGroupNumber : "").append(";");
                    content.append(pair.getPariticipant2IsKitchenOwner()).append(";");

                    for (Group group : groups) {
                        if (group.getKitchenOwner() != null && group.getKitchenOwner().equals(pair))
                            content.append(GroupAlgorithm.findWhichCourse(pair, this.groups));
                    }
                    content.append("\n");
                }
            }
            bw.write(content.toString());
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException("IO Exception.");
        }
    }

    /**
     * Numbers each group sequentially starting from 1.
     * This method assigns a unique number to each group in the main group list,
     * starting at 1 and incrementing by 1 for each subsequent group.
     */
    public void numberTheGroups() {
        for (int i = 0; i < this.groups.size(); i++) {
            this.groups.get(i).setNumber(i + 1);
        }
    }

    /**
     * Organizes pairs without existing groups into new groups based on specified criteria.
     * The method evaluates pairs based on their kitchen assignment to limit the selection,
     * then attempts to form groups by evaluating their compatibility in terms of food preference,
     * age difference, gender diversity, and path length to an after-dinner location.
     *
     * Groups are formed to maximize the number of elements while maintaining the set criteria.
     * The grouping process continues until it either runs out of viable pairs or exceeds
     * a set number of unsuccessful attempts to form a new group.
     *
     * Parameters:
     * @param foodPreference        Weight or priority given to matching food preferences within the group.
     * @param ageDifference         Weight or priority given to minimizing age differences within the group.
     * @param genderDiversity       Weight or priority given to achieving gender diversity within the group.
     * @param pathLength            Weight or priority given to minimizing the path length to a common destination.
     * @param numberOfElements      The desired number of elements (pairs) in each group to be formed.
     */
    public void createGroups(int foodPreference, int ageDifference, int genderDiversity, int pathLength, int numberOfElements) {
        List<Pair> pairsWithoutGroups = GroupAlgorithm.findPairsWithoutGroups(this.pairs, this.groups);
        List<Pair> availablePairs = new ArrayList<>();
        Map<Kitchen, Integer> kitchenCounter = new HashMap<>();

        for (Pair p : pairsWithoutGroups) {
            Kitchen kitchen = p.getKitchen();
            kitchenCounter.putIfAbsent(kitchen, 0);
            if (kitchenCounter.get(kitchen) < 2) {
                availablePairs.add(p);
                kitchenCounter.put(kitchen, kitchenCounter.get(kitchen) + 1);
            }
        }



        int countUnsuccessful = 0;
        while ((availablePairs.size() >= 9) && countUnsuccessful <= 1000) { // we always maximize the numberOfElements
            List<Pair> cluster = new ArrayList<>();
            Pair p = availablePairs.remove(0);
            cluster.add(p);

            List<Pair> validCandidates = GroupAlgorithm.findValidCandidatesForGroups(p, availablePairs, this.pairs);

            // find pairs for the cluster
            GroupAlgorithm.findPairsForCluster(validCandidates, cluster, availablePairs, this.pairs, foodPreference, ageDifference, genderDiversity, pathLength, numberOfElements, this.afterDinnerPartyLocation);

            List<Group> result = new ArrayList<>();
            boolean successful = true;
            if (cluster.size() != 9)
                successful = false;
            else {
                List<Pair> sortedPairs = cluster.stream()
                        .sorted(Comparator.comparing(pair -> findDistance(pair.getKitchenLocation(), afterDinnerPartyLocation)))
                        .collect(Collectors.toList());
                Map<Pair, Course> pairCourseMap = GroupAlgorithm.assignCourses(sortedPairs);

                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(6), sortedPairs.get(0), sortedPairs.get(3), Course.APPETIZER, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(7), sortedPairs.get(1), sortedPairs.get(4), Course.APPETIZER, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(8), sortedPairs.get(2), sortedPairs.get(5), Course.APPETIZER, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(3), sortedPairs.get(1), sortedPairs.get(8), Course.MAIN, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(4), sortedPairs.get(2), sortedPairs.get(6), Course.MAIN, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(5), sortedPairs.get(0), sortedPairs.get(7), Course.MAIN, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(0), sortedPairs.get(4), sortedPairs.get(8), Course.DESSERT, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(1), sortedPairs.get(6), sortedPairs.get(5), Course.DESSERT, this.pairs, this.groups);
                GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(2), sortedPairs.get(3), sortedPairs.get(7), Course.DESSERT, this.pairs, this.groups);

                for (Group group : result)
                    if (group.getKitchenOwner() == null)
                        successful = false;
            }

            if (successful) {
                availablePairs.removeAll(cluster);
                this.groups.addAll(result);
                countUnsuccessful = 0;
            } else {
                countUnsuccessful++;
            }
        }
    }


    /**
     * Prints metrics related to groups and their characteristics. Metrics include:
     * - Count of groups per course type (Appetizer, Main, Dessert).
     * - Total and average path lengths for pairs within groups.
     * - Standard deviation of path lengths to measure dispersion.
     * - Average age difference, food preference deviation, and gender diversity across all groups.
     *
     * The method iterates over all groups to calculate the counts of different course types,
     * and uses helper methods from the Metrics class to compute age differences, preference deviations,
     * and gender diversity values. It also calculates path length metrics for pairs within groups,
     * including total path length, average path length, and the standard deviation of path lengths.
     *
     * Outputs are printed directly to the console.
     */
    public void showMetricsOfGroups() {
        System.out.println("====================Metrics of Groups=====================");

        int appetizerCount = 0;
        int mainCount = 0;
        int dessertCount = 0;
        double totalPathLength = 0.0;
        double totalAgeDifference = 0.0;
        double totalPreferenceDeviation = 0.0;
        double totalGenderDiversity = 0.0;

        for (Group group : this.groups) {
            if (group.getCourse().equals(Course.APPETIZER)) {
                appetizerCount++;
            } else if (group.getCourse().equals(Course.MAIN)) {
                mainCount++;
            } else if (group.getCourse().equals(Course.DESSERT)) {
                dessertCount++;
            }
            totalAgeDifference += Metrics.measureGroupAgeGroupDifference(group);
            totalPreferenceDeviation += Metrics.measureGroupPreferenceDeviation(group);
            totalGenderDiversity += Metrics.measureGenderDiversityInGroup(group);
        }

        System.out.println("Number of groups for appetizer: " + appetizerCount);
        System.out.println("Number of groups for main: " + mainCount);
        System.out.println("Number of groups for dessert: " + dessertCount);
        System.out.println("Number of groups: " + this.groups.size());
        System.out.println("Number of pairs without groups: " + GroupAlgorithm.findPairsWithoutGroups(this.pairs, this.groups).size());

        List<Pair> pairsWithGroups = GroupAlgorithm.findPairsWithGroups(this.pairs, this.groups);
        double sumOfSquares = 0;
        for (Pair pair : pairsWithGroups) {
            Metrics.measurePathLength(pair, this.groups, this.afterDinnerPartyLocation);
            totalPathLength += pair.getPathLength();
        }
        int totalPairsWithGroups = pairsWithGroups.size();
        double avgPathLength = totalPathLength / totalPairsWithGroups;
        System.out.println("Path length of all groups: " + totalPathLength);
        System.out.println("Average Path Length: " + avgPathLength);

        for (Pair pair : pairsWithGroups) {
            double deviation = pair.getPathLength() - avgPathLength;
            sumOfSquares += deviation * deviation;
        }
        double variance = sumOfSquares / totalPairsWithGroups;
        double standardDeviation = Math.sqrt(variance);
        System.out.println("Standard Deviation Of Path Length: " + standardDeviation);

//        int totalAgeDifferencePairs = 0;
//        int totalPreferenceDeviationPairs = 0;
//        for (Pair pair : this.pairs) {
//            totalAgeDifferencePairs += pair.getAgeDifference();
//            totalPreferenceDeviationPairs += pair.getPreferenceDeviation();
//        }
        System.out.println("Average Age Difference: " + totalAgeDifference / this.groups.size());
        System.out.println("Average Food Preference Deviation: " + totalPreferenceDeviation / this.groups.size());
        System.out.println("Average Gender Diversity: " + totalGenderDiversity / this.groups.size());
        System.out.println("==========================================================");
    }

    /**
     * Handles the effects on pairs when certain participants are cancelling.
     * This method checks each pair to determine if it contains a cancelling participant.
     * Depending on the registration status of the pair and whether other participants
     * in the pair are also cancelling, it either schedules the pair for removal
     * or schedules the participant in the pair to be replaced.
     *
     * @param cancelling The list of participants that are cancelling.
     */
    public void handleCancellingParticipant(List<Participant> cancelling) {
        List<Pair> pairsToRemove = new ArrayList<>();
        Map<Pair, Participant> participantsToReplace = new HashMap<>();

        for (Participant par : cancelling) {
            for (Pair pair : pairs) {
                if (pair.getParticipants().contains(par)) {
                    if (cancelling.containsAll(pair.getParticipants())) {
                        if(!pairsToRemove.contains(pair)) pairsToRemove.add(pair);
                    } else {
                        if (cancelling.contains(pair.getParticipant1()) || cancelling.contains(pair.getParticipant2())) {
                            participantsToReplace.put(pair, par);
                        }
                    }
                }
            }
        }

        // Remove pairs
        for (Pair pair : pairsToRemove) {
            replacePair(pair);
        }

        // Update participants in pairs
        for (Map.Entry<Pair, Participant> entry : participantsToReplace.entrySet()) {
            replaceParticipant(entry.getKey(), entry.getValue());
        }
    }


    /**
     * Replaces a specified pair with another pair from the successor pairs list if possible.
     * If no suitable replacement is found, the pair is removed from the group it belongs to,
     * and all related pairs are added to the successor pairs list.
     * This method modifies the group and pair structure based on the availability of successor pairs.
     *
     * @param cancellingPair The pair that needs to be replaced due to cancellation.
     */
    protected void replacePair(Pair cancellingPair) {
        boolean solved = false;
        List<Pair> toRemovePair = new ArrayList<>();
        List<Group> toRemoveGroup = new ArrayList<>();
        List<Pair> remainingPairs = new ArrayList<>();
        List<Group> remainingGroups = new ArrayList<>();
        Pair newPair;
        for (Group group : groups) {
            if (group.getGroupPairs().contains(cancellingPair)) {
                group.getGroupPairs().remove(cancellingPair);
                remainingPairs.addAll(group.getGroupPairs());
                remainingGroups.add(group);
            }
        }

        if(!successorPairs.isEmpty()) {
            List<Pair> candidatePairs = GroupAlgorithm.findValidCandidatesForGroups(remainingPairs.getFirst(), successorPairs, this.pairs);
            for (Pair pair : remainingPairs) {
                candidatePairs.retainAll(GroupAlgorithm.findValidCandidatesForGroups(pair, successorPairs, this.pairs));
            }
            if (!candidatePairs.isEmpty()) {
                newPair = candidatePairs.getFirst();
                for (Group group : remainingGroups) {
                    group.getGroupPairs().add(newPair);
                    if(group.getKitchenOwner().equals(cancellingPair)) group.setKitchenOwner(newPair);
                }
                successorPairs.remove(newPair);
                solved = true;
            } else {
                toRemovePair.addAll(remainingPairs);
                toRemoveGroup.addAll(remainingGroups);
            }
        }else {
            toRemovePair.addAll(remainingPairs);
            toRemoveGroup.addAll(remainingGroups);
        }
        if(!solved) {
            for (Pair pair : toRemovePair) {
                for (Group group : groups) {
                    if (group.getGroupPairs().contains(pair)) {
                        toRemoveGroup.add(group);
                        group.getGroupPairs().addAll(remainingPairs);
                    }
                }
            }
            groups.removeAll(toRemoveGroup);
            successorPairs.addAll(remainingPairs);
        }
    }

    /**
     * Replaces a cancelling participant in a pair with a suitable new participant
     * from the list of successor participants if available. If no replacement is available,
     * the pair is removed entirely. The method adjusts the participants within the pair
     * based on which participant is cancelling and updates the list of successor participants.
     *
     * @param pair The pair containing the participant to be replaced.
     * @param cancellingPar The participant within the pair that is cancelling.
     */
    public void replaceParticipant(Pair pair, Participant cancellingPar) {
        boolean remainingParIsFirstPar = false;
        Participant remainingPar;
        Participant newParticipant;
        if (!pair.getParticipant1().equals(cancellingPar)) {
            remainingPar = pair.getParticipant1();
            pair.setParticipant2(null);
            remainingParIsFirstPar = true;
        } else {
            remainingPar = pair.getParticipant2();
            pair.setParticipant1(null);
        }


        List<Participant> availableSuccPar = PairAlgorithm.findValidCandidates(remainingPar, successorParticipants);

        if (!availableSuccPar.isEmpty()) {
            newParticipant = availableSuccPar.getFirst();
            if(remainingParIsFirstPar) {
                pair.setParticipant2(newParticipant);
            } else {
                pair.setParticipant1(newParticipant);
            }
            successorParticipants.remove(newParticipant);
        } else {
            pairs.remove(pair);
            successorParticipants.add(remainingPar);
            replacePair(pair);
        }

    }



}
