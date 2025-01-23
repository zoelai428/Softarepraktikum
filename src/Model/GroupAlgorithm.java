package Model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Model.SpinfoodEvent.findDistance;

public class GroupAlgorithm {

    /**
     * Calculates the number of times a specific kitchen is used by pairs.
     *
     * @param kitchen The kitchen to check usage for.
     * @return The number of times the specified kitchen is used.
     */
    protected static Integer getKitchenUsage(Kitchen kitchen, List<Pair> eventPairs) {
        Integer result = 0;
        for(Pair pair : eventPairs) {
            if(pair.hasCooked() && pair.getKitchen().equals(kitchen)) {
                result++;
            }
        }
        return result;
    }


    /**
     * Finds all pairs associated with the given course that are not part of any group.
     *
     * @return A list of pairs that are not in any group but are taking the specified course.
     */
    protected static List<Pair> findPairsWithoutGroups(List<Pair> eventPairs, List<Group> eventGroups) {
        List<Pair> pairsWithoutGroups = new ArrayList<>();
        for (Pair pair : eventPairs) {
            boolean found = false;
            for (Group group : eventGroups) {
                if (group.getGroupPairs() != null && group.getGroupPairs().contains(pair)){
                    found = true;
                }
            }
            if (!found) pairsWithoutGroups.add(pair);
        }
        return pairsWithoutGroups;
    }

    /**
     * Finds all pairs associated with the given course that are part a group.
     *
     * @return A list of pairs that are in a group and taking the specified course.
     */
    protected static List<Pair> findPairsWithGroups(List<Pair> eventPairs, List<Group> eventGroups) {
        List<Pair> pairsWithoutGroups = new ArrayList<>();
        for (Pair pair : eventPairs) {
            boolean found = false;
            for (Group group : eventGroups) {
                if ((group.getGroupPairs().contains(pair))){
                    found = true;
                }
            }
            if (found) pairsWithoutGroups.add(pair);
        }
        return pairsWithoutGroups;
    }

    /**
     *  * Finds valid candidates for forming groups based on several criteria, including food preferences and kitchen usage.
     *
     * @param pair The pair to find candidates for.
     * @param listOfPairs A list of other pairs to consider as candidates.
     * @return A list of pairs that are valid candidates to form a group with the specified pair.
     */
    protected static List<Pair> findValidCandidatesForGroups(Pair pair, List<Pair> listOfPairs, List<Pair> eventPairs) {
        List<Pair> result = new ArrayList<>();
        for (Pair p : listOfPairs) {
            if (pair.equals(p))
                continue;
            boolean valid = true;

            if (((pair.getFoodPreference().equals(FoodPreference.MEAT)) // criteria 6.1
                    && ((p.getFoodPreference().equals(FoodPreference.VEGAN)) || (p.getFoodPreference().equals(FoodPreference.VEGGIE))))
                    || ((p.getFoodPreference().equals(FoodPreference.MEAT))
                    && ((pair.getFoodPreference().equals(FoodPreference.VEGAN)) || (pair.getFoodPreference().equals(FoodPreference.VEGGIE)))))
                valid = false;
            if (pair.getKitchen().equals(p.getKitchen())) {
                valid = false;
            }
            if (pair.getKitchenLocation().equals(p.getKitchenLocation())) {
                valid = false;
            }
            if (getKitchenUsage(p.getKitchen(), eventPairs) > 2) {
                valid = false;
            }
            if (kitchenCount(p.getKitchen(), eventPairs) > 2) {
                valid = false;
            }
            if (valid)
                result.add(p);
        }
        return result;
    }


    /**
     * Calculates the number of pairs using a specific kitchen based on its location.
     * This method iterates through a list of pairs and increments a count for each pair
     * that uses a kitchen located at the same place as the specified kitchen.
     *
     * @param kitchen The kitchen for which the count is to be performed. This parameter
     *                should not be {@code null} and is expected to have a valid location.
     * @param eventPairs A list of pairs, each potentially using a kitchen. This list
     *                   should not be {@code null} but may be empty, in which case the
     *                   result will be zero.
     * @return The number of pairs that use the specified kitchen based on its location.
     *         Returns zero if no pairs match the specified kitchen's location or if the
     *         list of pairs is empty.
     */
    private static int kitchenCount(Kitchen kitchen, List<Pair> eventPairs) {
        int result = 0;
        for(Pair pair : eventPairs) {
            if(pair.getKitchen().getLocation().equals(kitchen.getLocation())) {
                result++;
            }
        }
        return result;
    }

    /**
     * Determines the collective food preference for a group based on the food preferences of individual pairs.
     * The method checks the food preferences of three pairs and determines the group's food preference.
     * The precedence for determining the group preference is as follows:
     * - If any pair prefers vegan food, the group preference is set to VEGAN.
     * - If no pair prefers vegan but any pair prefers vegetarian food, the group preference is set to VEGGIE.
     * - Otherwise, the group preference defaults to MEAT.
     *
     * @param pair1 The first pair in the group.
     * @param pair2 The second pair in the group.
     * @param pair3 The third pair in the group.
     * @return The determined food preference for the group (VEGAN, VEGGIE, or MEAT).
     */
    public static FoodPreference findGroupFoodPreference(Pair pair1, Pair pair2, Pair pair3) {
        if ((pair1.getFoodPreference().equals(FoodPreference.VEGAN)) || (pair2.getFoodPreference().equals(FoodPreference.VEGAN)) || (pair3.getFoodPreference().equals(FoodPreference.VEGAN))) {
            return FoodPreference.VEGAN;
        } else if ((pair1.getFoodPreference().equals(FoodPreference.VEGGIE)) || (pair2.getFoodPreference().equals(FoodPreference.VEGGIE)) || (pair3.getFoodPreference().equals(FoodPreference.VEGGIE)))  {
            return FoodPreference.VEGGIE;
        } else
            return FoodPreference.MEAT;
    }

    /**
     * Determines if a specific pair is eligible to cook for a given course based on multiple conditions.
     *
     * This method checks the following criteria to determine eligibility:
     * 1. The pair must not have cooked yet.
     * 2. The kitchen used by the pair must not have been used by three or more pairs already.
     * 3. The kitchen must be available for the specified course, ensuring no other group has booked the
     *    same kitchen for the same course.
     *
     * @param pair The pair to check for cooking eligibility.
     * @param course The course for which cooking eligibility needs to be checked.
     * @param eventPairs A list of all pairs participating in the event, used to check kitchen usage.
     * @param eventGroups A list of all groups formed in the event, used to determine if the kitchen
     *                    is available for the specified course.
     * @return true if the pair can cook for the specified course, false otherwise.
     */

    protected static boolean checkIfPairCanCook(Pair pair, Course course, List<Pair> eventPairs, List<Group> eventGroups) {
        if(!pair.hasCooked() && !(GroupAlgorithm.getKitchenUsage(pair.getKitchen(), eventPairs) >= 3)
                && checkIfPairsKitchenAvailable(pair, course, eventGroups)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if there is any available kitchen for the specified course.
     *
     * This method iterates over all pairs and groups to find a kitchen owner pair in a group
     * that matches a pair and has the same course. If such a pair is found, it returns false,
     * indicating that there is no available kitchen for the course. Otherwise, it returns true.
     *
     * @param course the course to check for available kitchen
     * @return true if an available kitchen exists, false otherwise
     */

    protected static boolean checkIfPairsKitchenAvailable(Pair pair, Course course, List<Group> eventGroups) {
        for(Group group : eventGroups) {
            if(group.getKitchenOwner() != null && group.getKitchenOwner().getKitchenLocation().equals(pair.getKitchenLocation()) && group.getCourse() == course) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds which course a specific pair is responsible for.
     *
     * This method iterates over all groups to find the kitchen owner pair in each group.
     * If the kitchen owner pair matches the specified pair, it returns the course they are
     * responsible for. The return value is 1 for APPETIZER, 2 for MAIN, and 3 for DESSERT.
     * If the pair is not found, it returns 0.
     *
     * @param pair the pair to check for course responsibility
     * @return 1 for APPETIZER, 2 for MAIN, 3 for DESSERT, or 0 if the pair is not found
     */
    protected static int findWhichCourse(Pair pair, List<Group> eventGroups) {
        for (Group group : eventGroups) {
            Pair kitchenOwner = group.getKitchenOwner();
            if (kitchenOwner != null) {
                if(kitchenOwner.equals(pair)) {
                    Course course = group.getCourse();
                    switch (course) {
                        case APPETIZER:
                            return 1;
                        case MAIN:
                            return 2;
                        case DESSERT:
                            return 3;
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Handles the creation of a new group with specified pairs and assigns a kitchen owner if eligible.
     * This method first creates a group with specified pairs and a course, then attempts to assign
     * one of the pairs as the kitchen owner based on their eligibility to cook (not having cooked before,
     * kitchen not overly used, and kitchen available for the course). It also updates the cooking status
     * for the pair assigned as kitchen owner.
     *
     * @param result The list where the newly created group will be added.
     * @param pairCourseMap A map linking pairs to courses, potentially useful for extended logic.
     * @param p1 The first pair to include in the group.
     * @param p2 The second pair to include in the group.
     * @param p3 The third pair to include in the group.
     * @param course The course that the group will be preparing.
     * @param eventPairs A list of all pairs at the event, used to check kitchen usage.
     * @param eventGroups A list of all existing groups, used to verify kitchen availability.
     */

    public static void handleCreateGroup(List<Group> result, Map<Pair, Course> pairCourseMap, Pair p1, Pair p2, Pair p3, Course course, List<Pair> eventPairs, List<Group> eventGroups) {
        Group group = new Group(null, null, course, null);
        group.getGroupPairs().add(p1);
        group.getGroupPairs().add(p2);
        group.getGroupPairs().add(p3);

        findGroupFoodPreference(p1, p2, p3);

        if (checkIfPairCanCook(p1, course, eventPairs, eventGroups)) {
            group.setKitchenOwner(p1);
            p1.setHasCooked(true);
        } else if (checkIfPairCanCook(p2, course, eventPairs, eventGroups)) {
            group.setKitchenOwner(p2);
            p2.setHasCooked(true);
        } else if (checkIfPairCanCook(p3, course, eventPairs, eventGroups)) {
            group.setKitchenOwner(p3);
            p3.setHasCooked(true);
        }

        result.add(group);
    }

    /**
     * Assigns courses to pairs based on their order in the list, assuming the list is already sorted
     * by some criteria like food preferences or age. The method is designed to work with at least nine
     * pairs, assigning them to three courses: Dessert, Main, and Appetizer, in that order.
     *
     * @param sortedPairs A list of pairs sorted according to some criteria.
     * @return A map linking each pair to a course, or null if there are fewer than nine pairs.
     */
    public static Map<Pair, Course> assignCourses(List<Pair> sortedPairs) {
        Map<Pair, Course> pairCourseMap = new HashMap<>();
        if(sortedPairs.size() >= 9) {
            pairCourseMap.put(sortedPairs.get(0), Course.DESSERT);
            pairCourseMap.put(sortedPairs.get(1), Course.DESSERT);
            pairCourseMap.put(sortedPairs.get(2), Course.DESSERT);
            pairCourseMap.put(sortedPairs.get(3), Course.MAIN);
            pairCourseMap.put(sortedPairs.get(4), Course.MAIN);
            pairCourseMap.put(sortedPairs.get(5), Course.MAIN);
            pairCourseMap.put(sortedPairs.get(6), Course.APPETIZER);
            pairCourseMap.put(sortedPairs.get(7), Course.APPETIZER);
            pairCourseMap.put(sortedPairs.get(8), Course.APPETIZER);
            return pairCourseMap;
        } else return null;
    }

    /**
     * Sorts a list of pairs by their food preferences. It assumes a method getFoodPreferenceRank is
     * available that assigns a numerical rank to each food preference, with lower numbers indicating
     * higher priority.
     *
     * @param pairs The list of pairs to be sorted.
     * @return A sorted list of pairs based on the food preference rank.
     */

    protected static List<Pair> sortByPairFoodPreference(List<Pair> pairs) {
        Comparator<Pair> foodPreferenceComparator = (pair1, pair2) -> {
            int rank1 = getFoodPreferenceRank(pair1.getFoodPreference());
            int rank2 = getFoodPreferenceRank(pair2.getFoodPreference());
            return Integer.compare(rank1, rank2);
        };

        Collections.sort(pairs, foodPreferenceComparator);
        return pairs;
    }

    /**
     * Converts a food preference into a numerical ranking.
     *
     * This method assigns a unique rank to each type of food preference. Lower numbers
     * represent a higher priority or ranking. The rankings are as follows:
     * - MEAT is ranked as 1.
     * - NONE is ranked as 2.
     * - VEGGIE is ranked as 3.
     * - VEGAN, being the default case, is ranked as 4.
     *
     * @param foodPreference The food preference to rank.
     * @return The integer rank associated with the given food preference.
     */

    protected static int getFoodPreferenceRank(FoodPreference foodPreference) {
        switch (foodPreference) {
            case MEAT:
                return 1;
            case NONE:
                return 2;
            case VEGGIE:
                return 3;
            default: // VEGAN
                return 4;
        }
    }

    /**
     * Sorts a list of pairs by the average age within each pair. This is useful for clustering or
     * organizing pairs by age similarity.
     *
     * @param pairs The list of pairs to be sorted by average age.
     * @return A list of pairs sorted by the average age of members within each pair.
     */

    protected static List<Pair> sortByAverageAgeGroup(List<Pair> pairs) {
        Comparator<Pair> ageGroupComparator = Comparator.comparingDouble(Pair::getAverageAgeGroup);
        Collections.sort(pairs, ageGroupComparator);
        return pairs;
    }


    /**
     * Calculates the average food preference value for a cluster of pairs.
     * This value is computed as the average of individual food preference values
     * for each pair in the cluster.
     *
     * @param cluster The list of pairs forming the cluster.
     * @return The average food preference value of the cluster.
     */

    protected static double clusterAverageFoodPreferenceValue(List<Pair> cluster) {
        double sum = 0.0;
        if(!cluster.isEmpty()) {
            for (Pair pair : cluster)
                sum += pair.getAverageFoodPreferenceValue();
            return sum / cluster.size();
        } else return 0.0;
    }


    /**
     * Computes the average age group for a cluster of pairs.
     * This average is determined by averaging the age groups of each pair within the cluster.
     *
     * @param cluster The list of pairs forming the cluster.
     * @return The average age group of the cluster.
     */

    protected static double clusterAverageAgeGroup(List<Pair> cluster) {
        double sum = 0.0;
        if(!cluster.isEmpty()) {
            for (Pair pair : cluster)
                sum += pair.getAverageAgeGroup();
            return sum / cluster.size();
        } else return 0.0;
    }

    /**
     * Calculates the proportion of female participants within a cluster of pairs.
     * This is determined by averaging the female proportion of each pair in the cluster.
     *
     * @param cluster The list of pairs forming the cluster.
     * @return The average proportion of females in the cluster.
     */

    protected static double clusterFemaleProportion(List<Pair> cluster) {
        double sum = 0.0;
        if(!cluster.isEmpty()) {
            for (Pair pair : cluster)
                sum += Metrics.measurePairFemaleProportion(pair);
            return sum / cluster.size();
        } else return 0.0;
    }

    /**
     * Determines the maximum distance of any pair's kitchen from a given party location within a cluster.
     * This method finds the pair with the kitchen located the farthest from the specified location.
     *
     * @param cluster The cluster of pairs to evaluate.
     * @param partyLocation The central party location to measure distance from.
     * @return The maximum distance from the party location to the farthest pair's kitchen in the cluster.
     */
    protected static double clusterMaxDistance(List<Pair> cluster, Location partyLocation) {
        double maxDistance = 0.0;
        for (Pair pair : cluster) {
            double pairDistance = findDistance(pair.getKitchenLocation(), partyLocation);
            if (pairDistance > maxDistance)
                maxDistance = pairDistance;
        }
        return maxDistance;
    }


    /**
     * Computes a score based on the path length change when adding new pairs to a cluster.
     * The score reflects how much the maximum distance to the party location increases
     * or remains stable when a new cluster configuration is compared to an old one.
     *
     * @param oldCluster The previous cluster configuration.
     * @param newCluster The new cluster configuration including additional pairs.
     * @param partyLocation The central location of the event.
     * @return A score indicating the effect on path length: 1.0 if no increase, 0.0 if more than doubled,
     *         or a calculated decrease in score if the distance increases but not doubled.
     */

    protected static double getPathLengthScore(List<Pair> oldCluster, List<Pair> newCluster, Location partyLocation) {
        double newDistance = clusterMaxDistance(newCluster, partyLocation);
        double oldDistance = clusterMaxDistance(oldCluster, partyLocation);
        if (newDistance <= oldDistance) // no increase in distance
            return 1.0;
        else if ((newDistance - oldDistance) > oldDistance) // more than double the old distance
            return 0.0;
        else return 1 - (newDistance - oldDistance) / oldDistance;
    }

    /**
     * Retrieves a weighting factor for scoring based on the given priority of criteria.
     * Criteria priorities are predefined with specific weight percentages.
     *
     * @param givenPriority The priority level of the criteria.
     * @return The weighting factor corresponding to the given priority.
     */

    protected static double getWeighting(int givenPriority) {
        //   1st criteria * 60%
        // + 2nd criteria * 25%
        // + 3rd criteria * 10%
        // + 4th criteria *  5% = overall score between 0 and 1
        switch (givenPriority) {
            case 1: return 0.6;
            case 2: return 0.25;
            case 3: return 0.1;
            case 4: return 0.05;
            default: return 0;
        }
    }

    /**
     * Calculates a composite score for a potential new pair candidate based on various criteria.
     * The score is computed using predefined weightings and the differences between cluster averages
     * and the new pair's values for food preference, age group, gender diversity, and path length.
     *
     * @param newPairCandidate The new pair being considered for addition to the cluster.
     * @param cluster The current cluster of pairs.
     * @param foodPreference The priority weighting for food preference.
     * @param ageDifference The priority weighting for age difference.
     * @param genderDiversity The priority weighting for gender diversity.
     * @param pathLength The priority weighting for path length.
     * @param numberOfElements The priority weighting for the number of elements in the cluster.
     * @param partyLocation The central location of the event, used for distance calculations.
     * @return A composite score representing the suitability of the new pair for addition to the cluster.
     */

    protected static double calculatePairScore(Pair newPairCandidate, List<Pair> cluster,
                                             int foodPreference, int ageDifference, int genderDiversity, int pathLength, int numberOfElements, Location partyLocation) {

        // calculate point between 0 and 1 for each criteria (We take 4 criteria: foodPreference, ageDifference, genderDiversity, pathLength)
        double foodPreferenceScore = 1 - Math.abs(clusterAverageFoodPreferenceValue(cluster) - newPairCandidate.getAverageFoodPreferenceValue()) / 3; // maximum deviation is 3
        double ageDifferenceScore = 1 - Math.abs(clusterAverageAgeGroup(cluster) - newPairCandidate.getAverageAgeGroup()) / 8; // maximum deviation is 8
        List<Pair> newCluster = Stream.concat(cluster.stream(), Stream.of(newPairCandidate)).collect(Collectors.toList());
        double genderDiversityScore = 1 - 2 * Math.abs(0.5 - clusterFemaleProportion(newCluster)); // maximum deviation is 0.5
        double pathLengthScore = getPathLengthScore(cluster, newCluster, partyLocation);
        double numberOfElementsScore = 1.0;

        return getWeighting(foodPreference) * foodPreferenceScore
                + getWeighting(ageDifference) * ageDifferenceScore
                + getWeighting(genderDiversity) * genderDiversityScore
                + getWeighting(pathLength) * pathLengthScore
                + getWeighting(numberOfElements) * numberOfElementsScore;
//        return 0.0; // to remove optimization from algorithm
    }


    /**
     * Iteratively finds and adds pairs to a cluster based on a composite score calculated from several criteria.
     * The method shuffles and sorts the valid candidate pairs to find the best match based on calculated
     * scores and existing constraints within the cluster.
     *
     * @param validCandidates A list of pairs that are valid candidates for adding to the cluster.
     * @param cluster The current cluster of pairs to which new pairs are being added.
     * @param availablePairs A list of pairs available for clustering, used in validation checks.
     * @param eventPairs A list of all pairs present at the event.
     * @param foodPreference Weighting factor for the food preference criteria.
     * @param ageDifference Weighting factor for the age difference criteria.
     * @param genderDiversity Weighting factor for the gender diversity criteria.
     * @param pathLength Weighting factor for the path length criteria.
     * @param numberOfElements Weighting factor for the number of elements criteria.
     * @param partyLocation The central location of the event, used in distance calculations.
     */

    protected static void findPairsForCluster(List<Pair> validCandidates, List<Pair> cluster, List<Pair> availablePairs, List<Pair> eventPairs,
                                              int foodPreference, int ageDifference, int genderDiversity, int pathLength, int numberOfElements,
                                              Location partyLocation) {
        while (cluster.size() < 9 && !validCandidates.isEmpty()) {
            // shuffle the list to allow more flexibility in finding possible pairs
            Collections.shuffle(validCandidates);

            // Sort validCandidates based on the scoring method
            validCandidates.sort(Comparator.comparingDouble((Pair pair)
                    -> calculatePairScore(pair, cluster, foodPreference, ageDifference, genderDiversity, pathLength, numberOfElements, partyLocation)).reversed());

            // Use an iterator to safely remove elements during iteration
            Iterator<Pair> iterator = validCandidates.iterator();

            while (cluster.size() < 9 && iterator.hasNext()) {
                Pair bestPair = iterator.next();
                boolean canAdd = true;

                // Check if the best pair can be added to the cluster
                for (Pair existingPair : cluster) {
                    if (!GroupAlgorithm.findValidCandidatesForGroups(existingPair, availablePairs, eventPairs).contains(bestPair)) {
                        canAdd = false;
                        break;
                    }
                }

                if (canAdd) {
                    cluster.add(bestPair);
                    iterator.remove();
                    break; // Exit the while loop to sort and iterate again
                } else {
                    // If the pair cannot be added, just remove it from the iterator
                    iterator.remove();
                }
            }
        }
    }

    public static List<Group> findGroupsCluster(Group group, List<Group> eventGroups) {
        List<Group> result = new ArrayList<>();
        List<Pair> list = group.getGroupPairs();
        List<Pair> list2 = new ArrayList<>();
        for(Pair p : list) {
            for(Group g : eventGroups) {
                if(g.getGroupPairs().contains(p)) {
                    if(!result.contains(g)) result.add(g);
                    list2.addAll(g.getGroupPairs());
                }
            }
        }
        for(Pair p : list2) {
            for(Group g : eventGroups) {
                if(g.getGroupPairs().contains(p)) {
                    if(!result.contains(g)) result.add(g);
                }
            }
        }

        return result;
    }

}
