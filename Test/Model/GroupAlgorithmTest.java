package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GroupAlgorithmTest {
    GroupAlgorithm groupAlgorithm = new GroupAlgorithm();
    private SpinfoodEvent spinfoodEvent;
    private Participant Alice, Bob, Jane, Joe, Zuhal, Numan, Dilek, Ali, Ziyan, Zoe, Eman;
    private Pair pair1, pair2, pair3, pair4, pair5, pair6, pair7, pair8, pair9, pair10;
    private Group group1, group2;
    List<Pair> pairs = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    private Map<Pair, Course> pairCourseMap;
    private Map<Pair, Course> courseAssignments;
    private Location partyLocation;
    private List<Pair> validCandidates, cluster, availablePairs, eventPairs;
    private List<Group> eventGroups;
    private int foodPreference, ageDifference, genderDiversity, pathLength, numberOfElements;


    @BeforeEach
    public void setUp() {
        spinfoodEvent = new SpinfoodEvent(new Location(0.0, 0.0));
        Alice = new Participant("1", "Alice", FoodPreference.MEAT, 20, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));
        Bob = new Participant("2", "Bob", FoodPreference.MEAT, 20, Gender.MALE, new Kitchen(KitchenExists.YES, 1, 8.718914539788807, 50.590899839788804));
        Jane = new Participant("3", "Jane", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));
        Joe = new Participant("4", "Joe", FoodPreference.VEGAN, 25, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555808, 50.5941282715559));
        Zuhal = new Participant("5", "Zuhal", FoodPreference.MEAT, 30, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555831, 50.5941282715531));
        Numan = new Participant("6", "Numan", FoodPreference.MEAT, 30, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555862, 50.5941282715562));
        Dilek = new Participant("7", "Dilek", FoodPreference.MEAT, 35, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.123456789, 8.123456789));
        Ali = new Participant("8", "Ali", FoodPreference.MEAT, 35, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.123456780, 8.123456780));
        Ziyan = new Participant("9", "Ziyan", FoodPreference.MEAT, 35, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 1.23456, 1.23456));
        Zoe = new Participant("10", "Zoe", FoodPreference.MEAT, 35, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 6.54321, 6.54321));
        Eman = new Participant("11", "Eman", FoodPreference.MEAT, 35, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 5.5, 5.5));


        pair1 = new Pair(Alice, Bob, FoodPreference.MEAT, false);
        pair2 = new Pair(Jane, Joe, FoodPreference.VEGAN, false);
        pair3 = new Pair(Numan, Zuhal, FoodPreference.MEAT, false);
        pair4 = new Pair(Dilek, Ali, FoodPreference.MEAT, false);
        pair5 = new Pair(Bob, Joe, FoodPreference.MEAT, false);
        pair6 = new Pair(Joe, Bob, FoodPreference.MEAT, false);
        pair7 = new Pair(Eman, Ziyan, FoodPreference.MEAT, false);
        pair8 = new Pair(Zuhal, Eman, FoodPreference.MEAT, false);
        pair9 = new Pair(Ziyan, Zuhal, FoodPreference.MEAT, false);
        pair10 = new Pair(Zoe, Eman, FoodPreference.MEAT, false);

        group1 = new Group(Arrays.asList(pair1, pair2, pair3), FoodPreference.MEAT, Course.APPETIZER, pair2);
        group2 = new Group(Arrays.asList(pair1, pair2, pair4), FoodPreference.MEAT, Course.APPETIZER, pair4);

        group1.setGroupPairs(Arrays.asList(pair1, pair2, pair3));
        group2.setGroupPairs(Arrays.asList(pair1, pair2, pair4));

        pairs = spinfoodEvent.getPairs();
        groups = spinfoodEvent.getGroups();

        pairCourseMap = new HashMap<>();

        partyLocation = new Location(0.0, 0.0);

        validCandidates = new ArrayList<>();
        cluster = new ArrayList<>();
        availablePairs = new ArrayList<>();
        eventPairs = new ArrayList<>();
        eventPairs.addAll(Arrays.asList(pair1,pair2,pair3,pair4,pair5,pair6,pair7,pair8,pair9,pair10));
        eventGroups = new ArrayList<>();
        eventGroups.addAll(Arrays.asList(group1, group2));

        foodPreference = 5;
        ageDifference = 1;
        genderDiversity = 2;
        pathLength = 3;
        numberOfElements = 4;

    }

    /**
     * Tests the getKitchenUsage method.
     * Verifies that the kitchen usage count is correct after pairs have cooked.
     */
    @Test
    public void testGetKitchenUsage() {
        Kitchen kitchen1 = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
        Kitchen kitchen2 = new Kitchen(KitchenExists.YES, 2, 1.0, 1.0);

        Participant participant1 = new Participant("1", "Person1", FoodPreference.MEAT, 25, Gender.MALE, kitchen1);
        Participant participant2 = new Participant("2", "Person2", FoodPreference.VEGAN, 30, Gender.FEMALE, kitchen1);
        Participant participant3 = new Participant("3", "Person3", FoodPreference.VEGGIE, 28, Gender.MALE, kitchen2);

        Pair pair1 = new Pair(participant1, participant2, FoodPreference.VEGAN, false);
        Pair pair2 = new Pair(participant3, participant2, FoodPreference.VEGGIE, false);//
        pair1.setHasCooked(true);
        pair2.setHasCooked(true);

        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);

        assertEquals(1, groupAlgorithm.getKitchenUsage(kitchen1, spinfoodEvent.getPairs()));
        assertEquals(1, groupAlgorithm.getKitchenUsage(kitchen2, spinfoodEvent.getPairs()));
    }

    /**
     * Tests the findPairsWithoutGroups method with no pairs and no groups.
     * Verifies that the method returns an empty list under these conditions.
     */
    @Test
    public void testFindPairsWithoutGroups_NoPairsNoGroups() {
        assertTrue(groupAlgorithm.findPairsWithoutGroups(new ArrayList<>(), new ArrayList<>()).isEmpty());
    }

    /**
     * Tests the findPairsWithoutGroups method with pairs but no groups.
     * Verifies that the method returns all pairs, as none are in groups.
     */
    @Test
    public void testFindPairsWithoutGroups_PairsNoGroups() {
        pairs.addAll(Arrays.asList(pair1, pair2, pair3));
        List<Pair> result = groupAlgorithm.findPairsWithoutGroups(pairs, new ArrayList<>());
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(pair1, pair2, pair3)));
    }

    /**
     * Tests the findPairsWithoutGroups method with no pairs but some groups exist.
     * Verifies that the method returns an empty list since there are no pairs to evaluate.
     */
    @Test
    public void testFindPairsWithoutGroups_NoPairsSomeGroups() {
        assertTrue(groupAlgorithm.findPairsWithoutGroups(new ArrayList<>(), Arrays.asList(group1, group2)).isEmpty());
    }

    /**
     * Tests the findPairsWithoutGroups method with some pairs in groups.
     * Verifies that the method returns pairs that are not part of any group.
     */
    @Test
    public void testFindPairsWithoutGroups_PairsInGroups() {
        pairs.addAll(Arrays.asList(pair1, pair2, pair3, pair4));
        groups.add(group1);
        List<Pair> result = groupAlgorithm.findPairsWithoutGroups(pairs, groups);
        assertEquals(1, result.size());
        assertTrue(result.contains(pair4));
    }

    /**
     * Tests the findPairsWithoutGroups method where all pairs are included in groups.
     * Verifies that the method returns an empty list, as all pairs are part of groups.
     */
    @Test
    public void testFindPairsWithoutGroups_AllPairsInGroups() {
        pairs.addAll(Arrays.asList(pair1, pair2, pair3));
        groups.addAll(Arrays.asList(group1));
        List<Pair> result = groupAlgorithm.findPairsWithoutGroups(pairs, groups);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the findPairsWithGroups method with no pairs.
     * Verifies that the method returns an empty list as there are no pairs to check.
     */
    @Test
    public void testFindPairsWithGroups_NoPairs() {
        List<Pair> result = GroupAlgorithm.findPairsWithGroups(new ArrayList<>(), groups);
        assertTrue(result.isEmpty(), "No pairs should result in an empty return");
    }

    /**
     * Tests the findPairsWithGroups method with no groups.
     * Verifies that the method returns an empty list as there are no groups to check pair membership.
     */
    @Test
    public void testFindPairsWithGroups_NoGroups() {
        List<Pair> result = GroupAlgorithm.findPairsWithGroups(pairs, new ArrayList<>());
        assertTrue(result.isEmpty(), "No groups should result in an empty return");
    }

    /**
     * Tests the findPairsWithGroups method where all pairs are part of at least one group.
     * Verifies that all pairs are returned since they meet the criteria of being in a group.
     */
    @Test
    public void testFindPairsWithGroups_AllPairsInGroups() {
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        groups.add(group1);
        List<Pair> result = GroupAlgorithm.findPairsWithGroups(pairs, groups);
        assertEquals(3, result.size(), "All pairs should be returned");
    }

    /**
     * Tests the findPairsWithGroups method with some pairs not in any groups.
     * Verifies that only the pairs which are part of groups are returned.
     */
    @Test
    public void testFindPairsWithGroups_PartiallyInGroups() {
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        pairs.add(pair4);
        groups.add(group1);
        List<Pair> result = GroupAlgorithm.findPairsWithGroups(pairs, groups);
        assertEquals(3, result.size(), "Only pairs in groups should be returned");
        assertTrue(result.containsAll(Arrays.asList(pair1, pair2, pair3)), "Pairs in groups should be returned");
        assertFalse(result.contains(pair4));
    }

    /**
     * Tests the findPairsWithGroups method where none of the pairs are in groups.
     * Verifies that an empty list is returned since no pairs meet the criteria of being in a group.
     */
    @Test
    public void testFindPairsWithGroups_NoneInGroups() {
        pairs.add(pair4);
        groups.add(group1);
        List<Pair> result = GroupAlgorithm.findPairsWithGroups(pairs, groups);
        assertTrue(result.isEmpty(), "No pairs in groups should result in an empty return");
    }


    /**
     * Tests the exclusion of the same pair being considered as a candidate for grouping.
     * Ensures that a pair does not include itself as a valid candidate.
     */
    @Test
    public void testFindValidCandidatesForGroups_SelfComparisonExclusion() {
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair1, pair2, pair3, pair4), pairs);
        assertFalse(candidates.contains(pair1), "The method should exclude the same pair from the results.");
    }

    /**
     * Tests the exclusion of pairs with conflicting food preferences.
     * Verifies that pairs with incompatible food preferences are not considered valid candidates.
     */
    @Test
    public void testFindValidCandidatesForGroups_FoodPreferenceIncompatibility() {
        pair2.setFoodPreference(FoodPreference.VEGAN);
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair2), pairs);
        assertTrue(candidates.isEmpty(), "Pairs with conflicting food preferences should be excluded.");
    }

    /**
     * Tests the exclusion of pairs that share the same kitchen.
     * Ensures that pairs using the same kitchen are not considered valid candidates to avoid resource conflicts.
     */
    @Test
    public void testFindValidCandidatesForGroups_SameKitchenExclusion() {
        pair2.getParticipants().get(0).setKitchen(pair1.getParticipants().get(0).getKitchen());
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair2), pairs);
        assertTrue(candidates.isEmpty(), "Pairs with the same kitchen should be excluded.");
    }

    /**
     * Tests the exclusion of pairs that are located in the same kitchen location.
     * Verifies that pairs from the same kitchen location are not considered valid candidates to ensure diversity and resource distribution.
     */
    @Test
    public void testFindValidCandidatesForGroups_SameKitchenLocationExclusion() {
        pair2.getParticipants().get(0).getKitchen().setLocation(pair1.getParticipants().get(0).getKitchen().getLocation());
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair2), pairs);
        assertTrue(candidates.isEmpty(), "Pairs with the same kitchen location should be excluded.");
    }

    /**
     * Tests the exclusion of pairs from kitchens that have been used beyond a certain limit.
     * Ensures that pairs from overly utilized kitchens are not considered to maintain fair resource usage.
     */
    @Test
    public void testFindValidCandidatesForGroups_KitchenUsageLimitExclusion() {
        // Simulate the scenario where pair2's kitchen has been used 3 times
        for (int i = 0; i < 3; i++) pairs.add(pair2);
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair2), pairs);
        assertTrue(candidates.isEmpty(), "Pairs from an overused kitchen should be excluded.");
    }

    /**
     * Tests the exclusion of pairs from kitchens that have already exceeded a count limit within the event.
     * Ensures that resource constraints are adhered to by not considering pairs from frequently counted kitchens.
     */
    @Test
    public void testFindValidCandidatesForGroups_KitchenCountLimitExclusion() {
        // Simulate the scenario where the kitchen count for pair2's kitchen exceeds the limit
        for (int i = 0; i < 3; i++) pairs.add(pair2);
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair2), pairs);
        assertTrue(candidates.isEmpty(), "Pairs from a kitchen counted too many times should be excluded.");
    }

    /**
     * Tests the inclusion of valid candidates that meet all criteria for forming a group.
     * Verifies that pairs meeting all specified criteria are correctly identified as valid candidates.
     */
    @Test
    public void testFindValidCandidatesForGroups_ValidCandidateIncluded() {
        // Assuming all conditions are met for pair4 to be a valid candidate
        List<Pair> candidates = GroupAlgorithm.findValidCandidatesForGroups(pair1, Arrays.asList(pair4), pairs);
        assertTrue(candidates.contains(pair4), "Valid pairs meeting all criteria should be included.");
    }


    /**
     * Tests the numberTheGroups method.
     * Verifies that groups are correctly numbered.
     */
    @Test
    public void testNumberTheGroups() {
        Kitchen kitchen1 = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
        Kitchen kitchen2 = new Kitchen(KitchenExists.YES, 2, 1.0, 1.0);
        Kitchen kitchen3 = new Kitchen(KitchenExists.YES, 3, 2.0, 2.0);

        Participant participant1 = new Participant("1", "Person1", FoodPreference.MEAT, 25, Gender.MALE, kitchen1);
        Participant participant2 = new Participant("2", "Person2", FoodPreference.VEGAN, 30, Gender.FEMALE, kitchen1);
        Participant participant3 = new Participant("3", "Person3", FoodPreference.VEGGIE, 28, Gender.MALE, kitchen2);
        Participant participant4 = new Participant("4", "Person4", FoodPreference.NONE, 26, Gender.MALE, kitchen3);
        Participant participant5 = new Participant("5", "Person5", FoodPreference.NONE, 29, Gender.MALE, kitchen3);
        Participant participant6 = new Participant("6", "Person6", FoodPreference.NONE, 27, Gender.FEMALE, kitchen3);

        Pair pair1 = new Pair(participant1, participant2, FoodPreference.VEGAN, false);
        Pair pair2 = new Pair(participant3, participant4, FoodPreference.NONE, false);
        Pair pair3 = new Pair(participant5, participant6, FoodPreference.NONE, false);

        Group group1 = new Group(Arrays.asList(pair1, pair2, pair3), FoodPreference.NONE, Course.APPETIZER, pair1);
        Group group2 = new Group(Arrays.asList(pair1, pair2, pair3), FoodPreference.NONE, Course.MAIN, pair2);
        Group group3 = new Group(Arrays.asList(pair1, pair2, pair3), FoodPreference.NONE, Course.DESSERT, pair3);

        spinfoodEvent.getGroups().add(group1);
        spinfoodEvent.getGroups().add(group2);
        spinfoodEvent.getGroups().add(group3);

        spinfoodEvent.numberTheGroups();
        assertEquals(1, spinfoodEvent.getGroups().get(0).getNumber());
        assertEquals(2, spinfoodEvent.getGroups().get(1).getNumber());
        assertEquals(3, spinfoodEvent.getGroups().get(2).getNumber());
    }


    /**
     * Tests the findGroupFoodPreference method when at least one pair prefers VEGAN.
     * Verifies that VEGAN is returned if any pair in the group prefers it.
     */
    @Test
    public void testFindGroupFoodPreference_WithVeganPreference() {
        Pair veganPair = new Pair(Alice, Joe, FoodPreference.VEGAN, false);
        Pair meatPair1 = new Pair(Bob, Jane, FoodPreference.MEAT, false);
        Pair meatPair2 = new Pair(Zuhal, Numan, FoodPreference.MEAT, false);
        assertEquals(FoodPreference.VEGAN, GroupAlgorithm.findGroupFoodPreference(veganPair, meatPair1, meatPair2),
                "VEGAN should be returned when at least one pair prefers VEGAN.");
    }

    /**
     * Tests the findGroupFoodPreference method when no pairs prefer VEGAN but at least one prefers VEGGIE.
     * Verifies that VEGGIE is returned if any pair in the group prefers it and none prefer VEGAN.
     */
    @Test
    public void testFindGroupFoodPreference_WithVeggiePreference() {
        Pair veggiePair = new Pair(Alice, Jane, FoodPreference.VEGGIE, false);
        Pair meatPair1 = new Pair(Bob, Zuhal, FoodPreference.MEAT, false);
        Pair meatPair2 = new Pair(Numan, Dilek, FoodPreference.MEAT, false);
        assertEquals(FoodPreference.VEGGIE, GroupAlgorithm.findGroupFoodPreference(meatPair1, veggiePair, meatPair2),
                "VEGGIE should be returned when no pair prefers VEGAN but at least one prefers VEGGIE.");
    }

    /**
     * Tests the findGroupFoodPreference method when all pairs prefer MEAT.
     * Verifies that MEAT is returned when no pairs prefer VEGAN or VEGGIE.
     */
    @Test
    public void testFindGroupFoodPreference_WithAllMeatPreference() {
        Pair meatPair1 = new Pair(Alice, Bob, FoodPreference.MEAT, false);
        Pair meatPair2 = new Pair(Jane, Joe, FoodPreference.MEAT, false);
        Pair meatPair3 = new Pair(Zuhal, Numan, FoodPreference.MEAT, false);
        assertEquals(FoodPreference.MEAT, GroupAlgorithm.findGroupFoodPreference(meatPair1, meatPair2, meatPair3),
                "MEAT should be returned when all pairs prefer MEAT.");
    }

    /**
     * Tests if a pair can cook when the pair has not cooked, kitchen usage is below threshold,
     * and the kitchen is available.
     */
    @Test
    public void testPairCanCook_AllConditionsMet() {
        pair1.setHasCooked(false);
        // Assume getKitchenUsage returns less than 3 and kitchen is available
        assertTrue(GroupAlgorithm.checkIfPairCanCook(pair1, Course.APPETIZER, pairs, groups));
    }

    /**
     * Tests if a pair cannot cook because the pair has already cooked.
     */
    @Test
    public void testPairCannotCook_AlreadyCooked() {
        pair1.setHasCooked(true);
        assertFalse(GroupAlgorithm.checkIfPairCanCook(pair1, Course.APPETIZER, pairs, groups));
    }

    /**
     * Tests if a pair cannot cook because the kitchen usage exceeds the allowed limit.
     */
    @Test
    public void testPairCannotCook_KitchenOverLimit() {
        pair1.setHasCooked(false);
        pair2.getParticipant1().setKitchen(pair1.getKitchen());
        pair2.setHasCooked(true);
        for (int i = 0; i < 3; i++) pairs.add(pair2);
        assertFalse(GroupAlgorithm.checkIfPairCanCook(pair1, Course.APPETIZER, pairs, groups));
    }

    /**
     * Tests if a pair cannot cook because their kitchen is not available for the course.
     */
    @Test
    public void testPairCannotCook_KitchenUnavailable() {
        pair4.setHasCooked(false);
        pair2.getParticipant1().setKitchen(pair4.getKitchen());
        pair2.setHasCooked(true);
        group1.setCourse(Course.APPETIZER);
        group1.setKitchenOwner(pair2);
        groups.add(group1);

        assertFalse(GroupAlgorithm.checkIfPairCanCook(pair4, Course.APPETIZER, pairs, groups));
    }

    /**
     * Tests that a kitchen is not available if another group has already booked it for the same course at the same location.
     */
    @Test
    public void testKitchenNotAvailable_SameCourseAndLocation() {
        pair4.getParticipant1().setKitchen(pair2.getKitchen());
        groups.addAll(Arrays.asList(group1, group2));
        assertFalse(GroupAlgorithm.checkIfPairsKitchenAvailable(pair2, Course.APPETIZER, groups),
                "Kitchen should not be available as it is booked for the same course and location.");
    }

    /**
     * Tests that a kitchen is available if no group has booked it for the same course at the same location.
     */
    @Test
    public void testKitchenAvailable_DifferentLocation() {
        Participant newParticipant = new Participant("9", "New Person", FoodPreference.MEAT, 26, Gender.FEMALE, new Kitchen(KitchenExists.YES, 4, 10.0, 10.0));
        Pair newPair = new Pair(newParticipant, Ali, FoodPreference.MEAT, false);
        assertTrue(GroupAlgorithm.checkIfPairsKitchenAvailable(newPair, Course.APPETIZER, groups),
                "Kitchen should be available as no group has booked it at this new location.");
    }

    /**
     * Tests that a kitchen is available if it is booked for a different course even though the location is the same.
     */
    @Test
    public void testKitchenAvailable_DifferentCourse() {
        assertTrue(GroupAlgorithm.checkIfPairsKitchenAvailable(pair1, Course.MAIN, groups),
                "Kitchen should be available as it is booked for a different course.");
    }

    /**
     * Tests that a kitchen is available if there are no groups with a kitchen owner.
     */
    @Test
    public void testKitchenAvailable_NoKitchenOwner() {
        groups.clear();
        assertTrue(GroupAlgorithm.checkIfPairsKitchenAvailable(pair1, Course.APPETIZER, groups),
                "Kitchen should be available as there are no kitchen owners in any group.");
    }


    /**
     * Test to ensure the correct course number is returned when the pair is the kitchen owner for an appetizer.
     */
    @Test
    public void testFindWhichCourse_Appetizer() {
        groups.add(group1);
        int courseNumber = GroupAlgorithm.findWhichCourse(pair2, groups);
        assertEquals(1, courseNumber, "Expected course number for appetizer is 1");
    }

    /**
     * Test to ensure the correct course number is returned when the pair is the kitchen owner for a main course.
     */
    @Test
    public void testFindWhichCourse_MainCourse() {
        group1.setCourse(Course.MAIN);
        groups.add(group1);
        int courseNumber = GroupAlgorithm.findWhichCourse(pair2, groups);
        assertEquals(2, courseNumber, "Expected course number for main course is 2");
    }

    /**
     * Test to ensure the correct course number is returned when the pair is the kitchen owner for a dessert.
     */
    @Test
    public void testFindWhichCourse_Dessert() {
        group1.setCourse(Course.DESSERT);
        groups.add(group1);
        int courseNumber = GroupAlgorithm.findWhichCourse(pair2, groups);
        assertEquals(3, courseNumber, "Expected course number for dessert is 3");
    }

    /**
     * Test to ensure the method returns 0 when the pair does not match any kitchen owner in any group.
     */
    @Test
    public void testFindWhichCourse_NoMatch() {
        Pair unmatchedPair = new Pair(new Participant("5", "New", FoodPreference.VEGAN, 30, Gender.FEMALE, new Kitchen(KitchenExists.YES, 0,8.673368271555807, 50.5941282715558)), new Participant("6", "Another", FoodPreference.VEGAN, 35, Gender.MALE,new Kitchen(KitchenExists.YES, 0,8.673368271555807, 50.5941282715558)), FoodPreference.MEAT, false);
        int courseNumber = GroupAlgorithm.findWhichCourse(unmatchedPair, groups);
        assertEquals(0, courseNumber, "Expected course number when no match is found should be 0");
    }

    /**
     * Tests creating a group where the first pair can cook and becomes the kitchen owner.
     */
    @Test
    public void testHandleCreateGroup_FirstPairCanCook() {
        List<Group> result = new ArrayList<>();
        groupAlgorithm.handleCreateGroup(result, pairCourseMap, pair1, pair2, pair3, Course.APPETIZER, pairs, groups);
        assertEquals(1, result.size());
        assertEquals(pair1, result.get(0).getKitchenOwner());
    }

    /**
     * Tests creating a group where the second pair can cook and becomes the kitchen owner.
     */
    @Test
    public void testHandleCreateGroup_SecondPairCanCook() {
        pair1.setHasCooked(true);
        List<Group> result = new ArrayList<>();
        groupAlgorithm.handleCreateGroup(result, pairCourseMap, pair1, pair2, pair3, Course.APPETIZER, pairs, groups);
        assertEquals(pair2, result.get(0).getKitchenOwner());
    }

    /**
     * Tests creating a group where the third pair can cook and becomes the kitchen owner.
     */
    @Test
    public void testHandleCreateGroup_ThirdPairCanCook() {
        pair1.setHasCooked(true);
        pair2.setHasCooked(true);
        List<Group> result = new ArrayList<>();
        groupAlgorithm.handleCreateGroup(result, pairCourseMap, pair1, pair2, pair3, Course.APPETIZER, pairs, groups);
        assertEquals(pair3, result.get(0).getKitchenOwner());
    }

    /**
     * Tests creating a group where no pairs can cook, resulting in no kitchen owner being set.
     */
    @Test
    public void testHandleCreateGroup_NoPairCanCook() {
        pair1.setHasCooked(true);
        pair2.setHasCooked(true);
        pair3.setHasCooked(true);
        List<Group> result = new ArrayList<>();
        groupAlgorithm.handleCreateGroup(result, pairCourseMap, pair1, pair2, pair3, Course.APPETIZER, pairs, groups);
        assertNull(result.get(0).getKitchenOwner());
    }

    /**
     * Tests that the correct courses are assigned to exactly nine pairs.
     */
    @Test
    public void testAssignCourses_ExactNinePairs() {
        pairs = Arrays.asList(pair1, pair2, pair3, pair4, pair5, pair6, pair7, pair8, pair9);
        courseAssignments = GroupAlgorithm.assignCourses(pairs);
        assertEquals(Course.DESSERT, courseAssignments.get(pair1));
        assertEquals(Course.DESSERT, courseAssignments.get(pair2));
        assertEquals(Course.DESSERT, courseAssignments.get(pair3));
        assertEquals(Course.MAIN, courseAssignments.get(pair4));
        assertEquals(Course.MAIN, courseAssignments.get(pair5));
        assertEquals(Course.MAIN, courseAssignments.get(pair6));
        assertEquals(Course.APPETIZER, courseAssignments.get(pair7));
        assertEquals(Course.APPETIZER, courseAssignments.get(pair8));
        assertEquals(Course.APPETIZER, courseAssignments.get(pair9));
    }

    /**
     * Tests that no exceptions are thrown and the method behaves correctly with fewer than nine pairs.
     */
    @Test
    public void testAssignCourses_LessThanNinePairs() {
        pairs = Arrays.asList(pair1, pair2, pair3); // Only three pairs
        courseAssignments = GroupAlgorithm.assignCourses(pairs);
        assertNull(courseAssignments);
    }

    /**
     * Tests that extra pairs beyond the ninth are not assigned courses.
     */
    @Test
    public void testAssignCourses_MoreThanNinePairs() {
        pairs = Arrays.asList(pair1, pair2, pair3, pair4, pair5, pair6, pair7, pair8, pair9, pair10); // Ten pairs
        courseAssignments = GroupAlgorithm.assignCourses(pairs);
        assertEquals(9, courseAssignments.size());
        assertTrue(!courseAssignments.containsKey(pair10));
    }

    /**
     * Tests the behavior when an empty list of pairs is provided.
     */
    @Test
    public void testAssignCourses_EmptyList() {
        pairs = Collections.emptyList();
        courseAssignments = GroupAlgorithm.assignCourses(pairs);
        assertNull(courseAssignments);
    }

    /**
     * Tests sorting with an empty list of pairs.
     */
    @Test
    public void testSortByPairFoodPreference_EmptyList() {
        List<Pair> emptyList = Collections.emptyList();
        assertTrue(GroupAlgorithm.sortByPairFoodPreference(emptyList).isEmpty(), "Sorted list should be empty.");
    }

    /**
     * Tests sorting when there is only one pair.
     */
    @Test
    public void testSortByPairFoodPreference_SingleElement() {
        List<Pair> singleList = Collections.singletonList(pair1);
        assertEquals(singleList, GroupAlgorithm.sortByPairFoodPreference(singleList), "Sorted list should be the same as input.");
    }

    /**
     * Tests sorting of pairs with identical food preferences.
     */
    @Test
    public void testSortByPairFoodPreference_IdenticalPreferences() {
        List<Pair> identicalList = Arrays.asList(pair2, pair4); // Both have MEAT preference
        pair2.setFoodPreference(FoodPreference.MEAT);
        pair4.setFoodPreference(FoodPreference.MEAT);
        List<Pair> sortedList = GroupAlgorithm.sortByPairFoodPreference(identicalList);
        assertEquals(Arrays.asList(pair2, pair4), sortedList, "Sorted list should maintain the order of pairs.");
    }

    /**
     * Tests sorting of pairs with different food preferences.
     */
    @Test
    public void testSortByPairFoodPreference_DifferentPreferences() {
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        pairs.add(pair4);
        pair2.setFoodPreference(FoodPreference.VEGAN);
        pair4.setFoodPreference(FoodPreference.VEGGIE);
        List<Pair> sortedList = GroupAlgorithm.sortByPairFoodPreference(pairs);
        assertEquals(Arrays.asList(pair1, pair3, pair4, pair2), sortedList, "Pairs should be sorted by food preference assuming an inherent order.");
    }

    /**
     * Test to ensure that the MEAT food preference returns the correct rank.
     */
    @Test
    public void testGetFoodPreferenceRank_MEAT() {
        assertEquals(1, GroupAlgorithm.getFoodPreferenceRank(FoodPreference.MEAT), "MEAT should return rank 1.");
    }

    /**
     * Test to ensure that the NONE food preference returns the correct rank.
     */
    @Test
    public void testGetFoodPreferenceRank_NONE() {
        assertEquals(2, GroupAlgorithm.getFoodPreferenceRank(FoodPreference.NONE), "NONE should return rank 2.");
    }

    /**
     * Test to ensure that the VEGGIE food preference returns the correct rank.
     */
    @Test
    public void testGetFoodPreferenceRank_VEGGIE() {
        assertEquals(3, GroupAlgorithm.getFoodPreferenceRank(FoodPreference.VEGGIE), "VEGGIE should return rank 3.");
    }

    /**
     * Test to ensure that the VEGAN food preference (handled by the default case) returns the correct rank.
     */
    @Test
    public void testGetFoodPreferenceRank_VEGAN() {
        assertEquals(4, GroupAlgorithm.getFoodPreferenceRank(FoodPreference.VEGAN), "VEGAN should return rank 4.");
    }

    /**
     * Tests the behavior of sorting an empty list of pairs.
     */
    @Test
    public void testSortByAverageAgeGroup_EmptyList() {
        List<Pair> emptyList = Collections.emptyList();
        assertTrue(GroupAlgorithm.sortByAverageAgeGroup(emptyList).isEmpty(), "Sorted empty list should remain empty.");
    }

    /**
     * Tests the behavior of sorting a list containing a single pair.
     */
    @Test
    public void testSortByAverageAgeGroup_SingleElement() {
        List<Pair> singleList = Collections.singletonList(pair1);
        assertEquals(singleList, GroupAlgorithm.sortByAverageAgeGroup(singleList), "Sorted single-element list should remain unchanged.");
    }

    /**
     * Tests sorting stability with pairs having identical average ages.
     */
    @Test
    public void testSortByAverageAgeGroup_IdenticalAges() {
        List<Pair> identicalAges = Arrays.asList(pair1, pair4);
        Dilek.setAge(20);
        Ali.setAge(20);
        List<Pair> sortedList = GroupAlgorithm.sortByAverageAgeGroup(identicalAges);
        assertEquals(Arrays.asList(pair1, pair4), sortedList, "Sorted list should maintain the original order with identical ages.");
    }

    /**
     * Tests sorting functionality with pairs having varying average ages.
     */
    @Test
    public void testSortByAverageAgeGroup_VaryingAges() {
        pairs.add(pair1);
        pairs.add(pair2);
        pairs.add(pair3);
        pairs.add(pair4);
        List<Pair> sortedList = GroupAlgorithm.sortByAverageAgeGroup(pairs);
        assertEquals(Arrays.asList(pair1, pair2, pair3, pair4), sortedList, "Pairs should be sorted by average age in ascending order.");
    }

    /**
     * Tests the average food preference value calculation on an empty cluster.
     * Expected to handle empty input gracefully, potentially returning 0.0.
     */
    @Test
    public void testClusterAverageFoodPreferenceValue_EmptyCluster() {
        List<Pair> emptyCluster = Collections.emptyList();
        double result = GroupAlgorithm.clusterAverageFoodPreferenceValue(emptyCluster);
        assertEquals(0.0, result, "Average food preference value of an empty cluster should be 0.0.");
    }

    /**
     * Tests the average food preference value calculation for a cluster containing a single pair.
     * Expected to return the food preference value of the single pair.
     */
    @Test
    public void testClusterAverageFoodPreferenceValue_SinglePair() {
        List<Pair> singlePairCluster = Collections.singletonList(pair1);
        double expectedValue = pair1.getAverageFoodPreferenceValue();
        double result = GroupAlgorithm.clusterAverageFoodPreferenceValue(singlePairCluster);
        assertEquals(expectedValue, result, "Average should match the single pair's food preference value.");
    }

    /**
     * Tests the average food preference value calculation for a cluster with multiple pairs.
     * Expected to return the correct average of all pairs' food preference values.
     */
    @Test
    public void testClusterAverageFoodPreferenceValue_MultiplePairs() {
        double expectedResult = (pair1.getAverageFoodPreferenceValue() + pair2.getAverageFoodPreferenceValue() +
                pair3.getAverageFoodPreferenceValue() + pair4.getAverageFoodPreferenceValue()) / 4;
        pairs.addAll(Arrays.asList(pair1, pair2, pair3, pair4));
        double result = GroupAlgorithm.clusterAverageFoodPreferenceValue(pairs);
        assertEquals(expectedResult, result, "Average should be the mean of all pairs' food preferences.");
    }

    /**
     * Test the average age group calculation with an empty cluster.
     */
    @Test
    public void testClusterAverageAgeGroup_EmptyCluster() {
        List<Pair> emptyCluster = new ArrayList<>();
        assertEquals(0.0, GroupAlgorithm.clusterAverageAgeGroup(emptyCluster), "Average age group for an empty cluster should be 0.0.");
    }

    /**
     * Test the average age group calculation with a single pair in the cluster.
     */
    @Test
    public void testClusterAverageAgeGroup_SinglePair() {
        List<Pair> singlePairCluster = Arrays.asList(pair1);
        assertEquals(1, GroupAlgorithm.clusterAverageAgeGroup(singlePairCluster), "Average age group for a single pair cluster should match the pair's age group.");
    }

    /**
     * Test the average age group calculation with multiple pairs in the cluster.
     */
    @Test
    public void testClusterAverageAgeGroup_MultiplePairs() {
        pairs.addAll(Arrays.asList(pair1, pair2, pair3, pair4));
        assertEquals(2.5, GroupAlgorithm.clusterAverageAgeGroup(pairs), "Average age should be correctly calculated for a cluster of multiple pairs.");
    }

    /**
     * Tests the clusterFemaleProportion calculation with a cluster containing no female participants.
     */
    @Test
    public void testClusterFemaleProportion_NoFemales() {
        List<Pair> testPairs = Arrays.asList(new Pair(Bob, Numan, FoodPreference.MEAT, false),
                new Pair(Ali, Joe, FoodPreference.VEGAN, false));
        double proportion = GroupAlgorithm.clusterFemaleProportion(testPairs);
        assertEquals(0.0, proportion, "Proportion should be 0.0 where there are no female participants.");
    }

    /**
     * Tests the clusterFemaleProportion calculation with a cluster where all participants are female.
     */
    @Test
    public void testClusterFemaleProportion_AllFemales() {
        List<Pair> testPairs = Arrays.asList(new Pair(Alice, Jane, FoodPreference.MEAT, false),
                new Pair(Dilek, Zuhal, FoodPreference.MEAT, false));
        double proportion = GroupAlgorithm.clusterFemaleProportion(testPairs);
        assertEquals(1.0, proportion, "Proportion should be 1.0 where all participants are female.");
    }

    /**
     * Tests the clusterFemaleProportion calculation with a mixed-gender cluster.
     */
    @Test
    public void testClusterFemaleProportion_MixedGenders() {
        List<Pair> testPairs = Arrays.asList(new Pair(Alice, Bob, FoodPreference.MEAT, false),
                new Pair(Jane, Joe, FoodPreference.MEAT, false));
        double proportion = GroupAlgorithm.clusterFemaleProportion(testPairs);
        assertEquals(0.5, proportion, "Proportion should be 0.5 in a mixed-gender cluster.");
    }

    /**
     * Tests the clusterFemaleProportion calculation with an empty cluster.
     */
    @Test
    public void testClusterFemaleProportion_EmptyCluster() {
        List<Pair> testPairs = Arrays.asList();
        double proportion = GroupAlgorithm.clusterFemaleProportion(testPairs);
        assertEquals(0.0, proportion, "Proportion should be 0.0 in an empty cluster.");
    }

    /**
     * Test the clusterMaxDistance method with an empty cluster.
     * Expected to return 0.0 since there are no pairs to calculate the distance from.
     */
    @Test
    public void testClusterMaxDistance_EmptyCluster() {
        List<Pair> emptyCluster = new ArrayList<>();
        double result = groupAlgorithm.clusterMaxDistance(emptyCluster, partyLocation);
        assertEquals(0.0, result, 0.01, "Max distance for an empty cluster should be 0.0");
    }

    /**
     * Test the clusterMaxDistance method with a cluster containing a single pair.
     * Expected to return the distance from the pair's location to the party location.
     */
    @Test
    public void testClusterMaxDistance_SinglePairCluster() {
        List<Pair> singlePairCluster = Arrays.asList(pair1);
        double expectedDistance = SpinfoodEvent.findDistance(pair1.getKitchenLocation(), partyLocation);
        double result = groupAlgorithm.clusterMaxDistance(singlePairCluster, partyLocation);
        assertEquals(expectedDistance, result, 0.01, "Max distance for a single pair cluster should be the pair's distance to the party");
    }

    /**
     * Test the clusterMaxDistance method with a cluster containing multiple pairs where no pair's distance exceeds the initial maxDistance.
     * Expected to return the distance from the farthest pair's location to the party location.
     */
    @Test
    public void testClusterMaxDistance_MultiplePairsCluster() {
        List<Pair> multiplePairCluster = Arrays.asList(pair1, pair2, pair3, pair4);
        double maxExpectedDistance = Math.max(SpinfoodEvent.findDistance(pair1.getKitchenLocation(), partyLocation),
                SpinfoodEvent.findDistance(pair2.getKitchenLocation(), partyLocation));
        double result = groupAlgorithm.clusterMaxDistance(multiplePairCluster, partyLocation);
        assertEquals(maxExpectedDistance, result, 0.01, "Max distance for multiple pairs should be the farthest pair's distance to the party");
    }

    /**
     * Test to verify that the getPathLengthScore returns 1.0 when there is no increase in distance between old and new clusters.
     * This tests the condition where the new cluster distance equals the old cluster distance.
     */
    @Test
    public void testGetPathLengthScore_NoIncreaseInDistance() {
        double score = GroupAlgorithm.getPathLengthScore(Arrays.asList(pair1), Arrays.asList(pair1), partyLocation);
        assertEquals(1.0, score, "Score should be 1.0 when there is no increase in distance.");
    }

    /**
     * Test to verify that the getPathLengthScore returns 0.0 when the new cluster distance is more than double the old cluster distance.
     * This tests the condition where the new cluster distance significantly exceeds the old cluster distance.
     */
    @Test
    public void testGetPathLengthScore_MoreThanDoubleOldDistance() {
        pair1.getParticipant1().getKitchen().setLocation(new Location(0.0, 0.0));
        pair2.getParticipant1().getKitchen().setLocation(new Location(0.0, 0.0));
        pair3.getParticipant1().getKitchen().setLocation(new Location(50.0, 50.0));
        pair4.getParticipant1().getKitchen().setLocation(new Location(50.0, 50.0));
        double score = GroupAlgorithm.getPathLengthScore(Arrays.asList(pair1,pair2), Arrays.asList(pair3, pair4), partyLocation);
        assertEquals(0.0, score, "Score should be 0.0 when new distance is more than double the old distance.");
    }

    /**
     * Test to verify that the getPathLengthScore calculates a proportional score when the new cluster distance is greater than the old but not more than double.
     * This tests the intermediate condition for distance increases.
     */
    @Test
    public void testGetPathLengthScore_IncreaseButNotDouble() {
        pair1.getParticipant1().getKitchen().setLocation(new Location(1.0, 0.0));
        pair2.getParticipant1().getKitchen().setLocation(new Location(1.0, 0.0));
        pair3.getParticipant1().getKitchen().setLocation(new Location(1.0, 1.0));
        pair4.getParticipant1().getKitchen().setLocation(new Location(1.0, 1.0));
        double score = GroupAlgorithm.getPathLengthScore(Arrays.asList(pair1, pair2), Arrays.asList(pair3, pair4), partyLocation);
        assertTrue(score > 0.0 && score < 1.0, "Score should be between 0.0 and 1.0 when new distance increases but not more than double.");
    }

    /**
     * Test to verify the weighting for the first priority.
     * This method should return 0.6 as defined for the 1st priority.
     */
    @Test
    public void testGetWeighting_FirstPriority() {
        double weight = GroupAlgorithm.getWeighting(1);
        assertEquals(0.6, weight, "Weight for first priority should be 0.6.");
    }

    /**
     * Test to verify the weighting for the second priority.
     * This method should return 0.25 as defined for the 2nd priority.
     */
    @Test
    public void testGetWeighting_SecondPriority() {
        double weight = GroupAlgorithm.getWeighting(2);
        assertEquals(0.25, weight, "Weight for second priority should be 0.25.");
    }

    /**
     * Test to verify the weighting for the third priority.
     * This method should return 0.1 as defined for the 3rd priority.
     */
    @Test
    public void testGetWeighting_ThirdPriority() {
        double weight = GroupAlgorithm.getWeighting(3);
        assertEquals(0.1, weight, "Weight for third priority should be 0.1.");
    }

    /**
     * Test to verify the weighting for the fourth priority.
     * This method should return 0.05 as defined for the 4th priority.
     */
    @Test
    public void testGetWeighting_FourthPriority() {
        double weight = GroupAlgorithm.getWeighting(4);
        assertEquals(0.05, weight, "Weight for fourth priority should be 0.05.");
    }

    /**
     * Test to verify the weighting for an undefined priority.
     * This method should return 0.0 as defined for any priority not explicitly defined (default case).
     */
    @Test
    public void testGetWeighting_UndefinedPriority() {
        double weight = GroupAlgorithm.getWeighting(5); // Using 5 as an example of an undefined priority
        assertEquals(0.0, weight, "Weight for undefined priority should be 0.0.");
    }

    /**
     * Test calculatePairScore with maximum food preference deviation.
     * Expecting a lower score due to the maximal deviation in food preference.
     */
    @Test
    public void testCalculatePairScore_MaxFoodPreferenceDeviation() {
        Pair newPair = new Pair(Jane, Ali, FoodPreference.VEGAN, false); // Maximally different food preference
        double score = groupAlgorithm.calculatePairScore(newPair, Arrays.asList(pair1), 1, 0, 0, 0, 0, partyLocation);
        assertTrue(score < 1.0, "Score should be less than 1.0 due to maximum food preference deviation.");
    }

    /**
     * Test calculatePairScore with minimal age difference.
     * Expecting a higher score due to minimal age difference.
     */
    @Test
    public void testCalculatePairScore_MinimalAgeDifference() {
        Pair newPair = new Pair(Alice, Joe, FoodPreference.MEAT, false); // Minimal age difference
        double score = groupAlgorithm.calculatePairScore(newPair, Arrays.asList(pair1), 0, 1, 0, 0, 0, partyLocation);
        assertTrue(score > 0.0, "Score should be higher due to minimal age difference.");
    }

    /**
     * Test calculatePairScore for gender diversity impact.
     * Adding a pair that improves gender balance should increase the score.
     */
    @Test
    public void testCalculatePairScore_GenderDiversityImprovement() {
        Pair newPair = new Pair(Alice, Jane, FoodPreference.MEAT, false); // Improves gender diversity
        double score = groupAlgorithm.calculatePairScore(pair1, Arrays.asList(pair1, pair2), 0, 0, 1, 0, 0, partyLocation);
        assertTrue(score > 0.5, "Score should be higher due to improved gender diversity.");
    }

    /**
     * Test calculatePairScore for path length criterion.
     * Expecting a score decrease when path length significantly increases.
     */
    @Test
    public void testCalculatePairScore_PathLengthIncrease() {
        Pair newPair = new Pair(Alice, Dilek, FoodPreference.MEAT, false); // Likely to increase path length significantly
        double score = groupAlgorithm.calculatePairScore(newPair, Arrays.asList(pair1, pair3), 0, 0, 0, 1, 0, partyLocation);
        assertTrue(score < 1.0, "Score should decrease due to significant increase in path length.");
    }

    /**
     * Test the scenario when validCandidates is empty, which should not modify the cluster.
     */
    @Test
    public void testFindPairsForCluster_EmptyValidCandidates() {
        groupAlgorithm.findPairsForCluster(validCandidates, cluster, availablePairs, eventPairs,
                foodPreference, ageDifference, genderDiversity,
                pathLength, numberOfElements, partyLocation);
        assertTrue(cluster.isEmpty());
    }

    /**
     * Test adding a pair to the cluster when it is valid and there are no conflicting pairs.
     */
    @Test
    public void testFindPairsForCluster_AddPairToCluster() {
        validCandidates.add(new Pair(Alice, Bob, FoodPreference.MEAT, false));
        groupAlgorithm.findPairsForCluster(validCandidates, cluster, availablePairs, eventPairs,
                foodPreference, ageDifference, genderDiversity,
                pathLength, numberOfElements, partyLocation);
        assertEquals(1, cluster.size());
    }

    /**
     * Test not adding a pair to the cluster due to conflict with an existing pair.
     */
    @Test
    public void testFindPairsForCluster_ConflictingPair() {
        cluster.add(new Pair(Jane, Joe, FoodPreference.MEAT, false));
        Pair conflictingPair = new Pair(Alice, Bob, FoodPreference.VEGAN, false);
        validCandidates.add(conflictingPair);

        groupAlgorithm.findPairsForCluster(validCandidates, cluster, availablePairs, eventPairs,
                foodPreference, ageDifference, genderDiversity,
                pathLength, numberOfElements, partyLocation);
        assertEquals(1, cluster.size());  // Only the original pair should be present
        assertFalse(cluster.contains(conflictingPair));
    }

    /**
     * Test the scenario when the cluster reaches its maximum size limit.
     */
    @Test
    public void testFindPairsForCluster_MaxSizeCluster() {
        validCandidates.addAll(Arrays.asList(pair1,pair2,pair3,pair4,pair5,pair6,pair7,pair8,pair9,pair10));
        availablePairs.addAll(Arrays.asList(pair1,pair2,pair3,pair4,pair5,pair6,pair7,pair8,pair9,pair10));
        groupAlgorithm.findPairsForCluster(validCandidates, cluster, availablePairs, eventPairs,
                foodPreference, ageDifference, genderDiversity,
                pathLength, numberOfElements, partyLocation);
        assertEquals(9, cluster.size());
    }


}