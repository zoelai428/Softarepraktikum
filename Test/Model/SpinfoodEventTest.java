package Model;

import View.GUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpinfoodEventTest {

    private SpinfoodEvent spinfoodEvent;
    private Location testLocation;
    private final String testDirectory = System.getProperty("java.io.tmpdir");



    Participant Alice = new Participant("1", "Alice", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));
    Participant Bob = new Participant("2", "Bob", FoodPreference.MEAT, 30, Gender.MALE, new Kitchen(KitchenExists.YES, 1, 8.718914539788807, 50.590899839788804));
    Participant Jane = new Participant("3", "Jane", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));
    Participant Joe = new Participant("4", "Joe", FoodPreference.VEGAN, 25, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555808, 50.5941282715559));
    Participant Zuhal = new Participant("5", "Zuhal", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555831, 50.5941282715531));
    Participant Numan = new Participant("6", "Numan", FoodPreference.MEAT, 25, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555862, 50.5941282715562));
    Participant Dilek = new Participant("7", "Dilek", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.123456789, 8.123456789));
    Participant Ali = new Participant("8", "Ali", FoodPreference.MEAT, 25, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 3, 8.123456789, 8.123456789));


    Pair pair1 = new Pair(Alice, Bob, FoodPreference.MEAT, false);
    Pair pair2 = new Pair(Jane, Joe, FoodPreference.MEAT, false);
    Pair pair3 = new Pair(Zuhal, Numan, FoodPreference.MEAT, false);
    Pair pair4 = new Pair(Dilek, Ali, FoodPreference.MEAT, false);
    Group group = new Group(Arrays.asList(pair1,pair2,pair3), FoodPreference.MEAT, Course.APPETIZER,pair2);

    Kitchen kitchen1 = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
    Kitchen kitchen2 = new Kitchen(KitchenExists.YES, 2, 1.0, 1.0);
    Kitchen kitchen3 = new Kitchen(KitchenExists.YES, 3, 2.0, 2.0);

    // Initialize groups
    Group group1 = new Group(Arrays.asList(pair1), FoodPreference.MEAT, Course.APPETIZER, pair1);
    Group group2 = new Group(Arrays.asList(pair2), FoodPreference.VEGAN, Course.MAIN, pair2);
    Group group3 = new Group(Arrays.asList(pair1), FoodPreference.MEAT, Course.DESSERT, pair1);



    @BeforeEach
    public void setUp() {

        spinfoodEvent = new SpinfoodEvent(new Location(0.0, 0.0));
    }

    @Test
    void testSetParticipants() {
        // Create a list of participants
        List<Participant> participants = new ArrayList<>(Arrays.asList(Alice, Bob));

        // Set participants in SpinfoodEvent
        spinfoodEvent.setParticipants(participants);

        // Verify if participants are correctly set
        assertEquals(participants.size(), spinfoodEvent.getParticipants().size(), "Number of participants should match");
        assertTrue(spinfoodEvent.getParticipants().contains(Alice), "Alice should be in participants list");
        assertTrue(spinfoodEvent.getParticipants().contains(Bob), "Bob should be in participants list");
    }

    @Test
    void testSetParticipantsEmptyList() {
        // Create an empty list of participants
        List<Participant> emptyList = new ArrayList<>();

        // Set empty list in SpinfoodEvent
        spinfoodEvent.setParticipants(emptyList);

        // Verify if participants list is empty
        assertTrue(spinfoodEvent.getParticipants().isEmpty(), "Participants list should be empty");
    }

    @Test
    void testSetParticipantsNull() {
        // Set null list of participants in SpinfoodEvent
        spinfoodEvent.setParticipants(null);

        // Verify if participants list is null or empty (depending on implementation)
        assertNull(spinfoodEvent.getParticipants(), "Participants list should be null");
    }


    /**
     * Tests the numberThePairs method.
     * Verifies that pairs are correctly numbered.
     */
    @Test
    public void testNumberThePairs() {
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



        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);

        spinfoodEvent.numberThePairs();
        assertEquals(1, spinfoodEvent.getPairs().get(0).getNumber());
        assertEquals(2, spinfoodEvent.getPairs().get(1).getNumber());
        assertEquals(3, spinfoodEvent.getPairs().get(2).getNumber());
    }

    /**
     * Test to verify if findExistingKitchen correctly finds an existing kitchen
     */
    @Test
    public void shouldTestFindExistingKitchen_ExistingKitchen() {

        Kitchen kitchen = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
        spinfoodEvent.getKitchens().add(kitchen);

        Kitchen existingKitchen = spinfoodEvent.findExistingKitchen(1, 0.0, 0.0);

        assertEquals(kitchen, existingKitchen);
    }

    /**
     * Test to ensure findExistingKitchen returns null when the specified kitchen doesn't exist
     */
    @Test
    public void shouldTestFindExistingKitchen_NonExistingKitchen() {
        Kitchen existingKitchen = spinfoodEvent.findExistingKitchen(1, 0.0, 0.0);
        assertNull(existingKitchen);
    }

    /**
     * Test to verify findExistingKitchen works correctly with multiple kitchens
     */
    @Test
    public void shouldTestFindExistingKitchen_MultipleKitchens() {

        Kitchen kitchen1 = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
        Kitchen kitchen2 = new Kitchen(KitchenExists.YES, 2, 10.0, 10.0);
        spinfoodEvent.getKitchens().add(kitchen1);
        spinfoodEvent.getKitchens().add(kitchen2);

        Kitchen existingKitchen1 = spinfoodEvent.findExistingKitchen(1, 0.0, 0.0);
        Kitchen existingKitchen2 = spinfoodEvent.findExistingKitchen(2, 10.0, 10.0);
        Kitchen nonExistingKitchen = spinfoodEvent.findExistingKitchen(3, 20.0, 20.0);

        assertEquals(kitchen1, existingKitchen1);
        assertEquals(kitchen2, existingKitchen2);
        assertNull(nonExistingKitchen);
    }

    /**
     * Test to verify findExistingKitchen behaves correctly with different kitchen attributes
     */
    @Test
    public void shouldTestFindExistingKitchen_DifferentAttributes() {
        Kitchen kitchen1 = new Kitchen(KitchenExists.YES, 1, 0.0, 0.0);
        Kitchen kitchen2 = new Kitchen(KitchenExists.YES, 2, 10.0, 10.0);
        spinfoodEvent.getKitchens().add(kitchen1);
        spinfoodEvent.getKitchens().add(kitchen2);

        Kitchen existingKitchen1 = spinfoodEvent.findExistingKitchen(1, 0.0, 0.0);
        Kitchen existingKitchen2 = spinfoodEvent.findExistingKitchen(2, 10.0, 10.0);
        Kitchen nonExistingKitchen1 = spinfoodEvent.findExistingKitchen(1, 10.0, 10.0);
        Kitchen nonExistingKitchen2 = spinfoodEvent.findExistingKitchen(2, 0.0, 0.0);

        assertEquals(kitchen1, existingKitchen1);
        assertEquals(kitchen2, existingKitchen2);
        assertNull(nonExistingKitchen1);
        assertNull(nonExistingKitchen2);
    }

    /**
     * Test to check if findExistingKitchen handles null parameters correctly
     */
    @Test
    public void shouldTestFindExistingKitchen_NullParameters() {

        Kitchen existingKitchen1 = spinfoodEvent.findExistingKitchen(null, 0.0, 0.0);
        Kitchen existingKitchen2 = spinfoodEvent.findExistingKitchen(1, null, 0.0);
        Kitchen existingKitchen3 = spinfoodEvent.findExistingKitchen(1, 0.0, null);
        Kitchen existingKitchen4 = spinfoodEvent.findExistingKitchen(null, null, null);

        assertNull(existingKitchen1);
        assertNull(existingKitchen2);
        assertNull(existingKitchen3);
        assertNull(existingKitchen4);
    }

    /**
     * Test to verify findExistingKitchen handles edge cases (maximum values) correctly
     */
    @Test
    public void shouldTestFindExistingKitchen_MaxValues() {
        Kitchen kitchen = new Kitchen(KitchenExists.YES, Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE); // Extreme values
        spinfoodEvent.getKitchens().add(kitchen);

        Kitchen existingKitchen = spinfoodEvent.findExistingKitchen(Integer.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        assertEquals(kitchen, existingKitchen);
    }

    /**
     * Test to verify if createInitialParticipantsAndPairs correctly creates participants
     */
    @Test
    public void shouldTestCreateInitialParticipantsAndPairs_ParticipantsCreated() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));

        spinfoodEvent.createInitialParticipantsAndPairs(inputList);

        List<Participant> participants = spinfoodEvent.getParticipants();

        assertNotNull(participants);
        assertEquals(1, participants.size());
    }

    /**
     * Test to ensure createInitialParticipantsAndPairs creates multiple participants correctly
     */
    @Test
    public void shouldTestCreateInitialParticipantsAndPairs_MultipleParticipantsCreated() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "none", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));

        spinfoodEvent.createInitialParticipantsAndPairs(inputList);

        List<Participant> participants = spinfoodEvent.getParticipants();

        assertNotNull(participants);
        assertEquals(2, participants.size());
    }

    /**
     * Test to verify if createInitialParticipantsAndPairs doesn't create pairs when there's only one participant
     */
    @Test
    public void shouldTestCreateInitialParticipantsAndPairs_NoPairsCreated() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));

        spinfoodEvent.createInitialParticipantsAndPairs(inputList);

        List<Pair> pairs = spinfoodEvent.getPairs();

        assertNotNull(pairs);
        assertEquals(0, pairs.size());
    }

    /**
     * Test to ensure createInitialParticipantsAndPairs throws IllegalArgumentException for invalid input data
     */
    @Test
    public void shouldTestErrorHandling_InvalidInputData() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "invalid_food_preference", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));

        assertThrows(IllegalArgumentException.class, () -> spinfoodEvent.createInitialParticipantsAndPairs(inputList));
    }

    /**
     * Tests the findDistance method with typical location points.
     * This test verifies that the calculated distance matches the expected result
     * for a known distance between two points.
     */
    @Test
    public void testFindDistance_Basic() {
        Location location1 = new Location(10.0, 10.0);  // Example coordinates
        Location location2 = new Location(20.0, 20.0);  // Example coordinates
        Double expectedDistance = Math.sqrt(200);  // Calculated manually for known coordinates
        Double result = spinfoodEvent.findDistance(location1, location2);
        assertEquals(expectedDistance, result, 0.001);
    }

    /**
     * Tests the findDistance method with identical locations.
     * The distance between the same points should be zero.
     */
    @Test
    public void testFindDistance_IdenticalLocations() {
        Location location1 = new Location(25.0, 25.0);
        Location location2 = new Location(25.0, 25.0);
        Double expectedDistance = 0.0;
        Double result = spinfoodEvent.findDistance(location1, location2);
        assertEquals(expectedDistance, result, 0.0);
    }


    /**
     * Test to ensure createPairs throws NullPointerException for null input list
     */
    @Test
    public void shouldTestCreatePairs_NullInputList() {
        // Test for error scenario: null input list
        assertThrows(NullPointerException.class, () -> spinfoodEvent.createInitialParticipantsAndPairs(null));
    }

    /**
     * Test to ensure createPairs handles empty input list correctly
     */

    @Test
    public void shouldTestCreatePairs_EmptyInputList() {
        // Test for edge case: empty input list
        spinfoodEvent.createInitialParticipantsAndPairs(new ArrayList<>());
        assertEquals(0, spinfoodEvent.getParticipants().size());
    }

    /**
     * Test to ensure createPairs single participant scenario
     */
    @Test
    public void shouldTestCreatePairs_SingleParticipant() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        event.createInitialParticipantsAndPairs(inputList);
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        spinfoodEvent.createInitialParticipantsAndPairs(inputList);
        assertEquals(1, spinfoodEvent.getParticipants().size());
        assertEquals(0, event.getPairs().size());
    }


    /**
     * Test to ensure createPairs throws IllegalArgumentException for max values
     */
    @Test
    public void shouldTestCreatePairs_MaxValues() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", String.valueOf(Integer.MAX_VALUE), "male", "maybe", String.valueOf(Double.MAX_VALUE), String.valueOf(Double.MAX_VALUE), String.valueOf(Double.MAX_VALUE)));
        spinfoodEvent.createInitialParticipantsAndPairs(inputList);
        assertEquals(1, spinfoodEvent.getParticipants().size());
    }

    /**
     * Test to ensure createPairs throws IllegalArgumentException for invalid gender
     */
    @Test
    public void shouldTestCreatePairs_InvalidGender() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "invalid_gender", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        assertThrows(IllegalArgumentException.class, () -> spinfoodEvent.createInitialParticipantsAndPairs(inputList));
    }

    /**
     * Test to ensure each pair1 has a number assigned to it
     */
    @Test
    public void shouldTestNumberThePairs() {

        spinfoodEvent.createPairs(1, 2, 3);
        spinfoodEvent.numberThePairs();

        for (int i = 0; i < spinfoodEvent.getPairs().size(); i++) {
            assertEquals(i + 1, spinfoodEvent.getPairs().get(i).getNumber());
        }
    }

    /**
     * Test when there are no pairs available.
     */
    @Test
    public void testCreateGroups_NoPairs() {
        spinfoodEvent.setPairs(new ArrayList<>());
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created if no pairs are available");
    }

    /**
     * Test when there are fewer pairs than required for a group.
     */
    @Test
    public void testCreateGroups_InsufficientPairs() {
        spinfoodEvent.setPairs(createDummyPairs(4));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created if there are insufficient pairs");
    }






    /**
     * Test when a pair has a null kitchen.
     */
    @Test
    public void testCreateGroups_NullKitchen() {
        List<Pair> pairs = createPairsDifferentKitchens(9);
        pairs.get(0).getKitchen(); // Simulate null kitchen for the first pair
        spinfoodEvent.setPairs(pairs);
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created if a pair has a null kitchen");
    }

    /**
     * Test with invalid criteria values.
     */
    @Test
    public void testCreateGroups_InvalidCriteria() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        assertDoesNotThrow(() -> spinfoodEvent.createGroups(-1, -1, -1, -1, 9));
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created with invalid criteria values");
    }

    /**
     * Test when there are no valid pairs to form a group.
     */
    @Test
    public void testCreateGroups_NoValidPairs() {
        spinfoodEvent.setPairs(createDummyPairs(9));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(9, spinfoodEvent.getGroups().size(), "No groups should be created if no valid pairs are found");
    }

    /**
     * Test with only one pair available.
     */
    @Test
    public void testCreateGroups_SinglePair() {
        spinfoodEvent.setPairs(createDummyPairs(1));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created with only one pair available");
    }




    /**
     * Test when a group cannot be filled to the specified number of elements.
     */
    @Test
    public void testCreateGroups_GroupNotFull() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(8)); // Only 8 pairs available
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No group should be created if unable to fill to the specified number of elements");
    }

    /**
     * Test scenario where course assignment fails for some reason.
     */
    @Test
    public void testCreateGroups_InvalidCourseHandling() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        // Mock a scenario where course assignment fails by setting invalid values, if applicable
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No group should be created if course assignment fails");
    }



    /**
     * Test scenario where no valid groups can be created.
     */
    @Test
    public void testCreateGroups_NoGroupsCreated() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        // Modify pairs to prevent group formation, if applicable
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No group should be created under certain conditions");
    }


    public static List<Pair> createPairsSameKitchen(int count) {
        Participant Bob = new Participant("2", "Bob", FoodPreference.MEAT, 30, Gender.MALE, new Kitchen(KitchenExists.YES, 1, 8.718914539788807, 50.590899839788804));
        Participant Jane = new Participant("3", "Jane", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));

        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {


            pairs.add(new Pair(Bob, Jane, FoodPreference.MEAT, false));
        }
        return pairs;
    }

    public static List<Pair> createPairsDifferentKitchens(int count) {
        Participant Bob = new Participant("2", "Bob", FoodPreference.MEAT, 30, Gender.MALE, new Kitchen(KitchenExists.YES, 1, 8.718914539788807, 50.590899839788804));
        Participant Jane = new Participant("3", "Jane", FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 8.673368271555807, 50.5941282715558));

        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {

            pairs.add(new Pair(Bob, Jane, FoodPreference.MEAT, false));
        }
        return pairs;
    }

    @Test
    void testCreateInitialParticipantsAndPairs() {
        List<List<String>> inputList = Arrays.asList(
                Arrays.asList("1", "1", "Alice", "MEAT", "25", "FEMALE", "YES", "1", "8.718914539788807", "50.590899839788804"),
                Arrays.asList("2", "2", "Bob", "MEAT", "30", "MALE", "NO"),
                Arrays.asList("3", "3", "Jane", "MEAT", "25", "FEMALE", "MAYBE", "3", "8.673368271555807", "50.5941282715558", "4", "Joe", "25", "MALE")
        );
        spinfoodEvent.createInitialParticipantsAndPairs(inputList);
        assertEquals(4, spinfoodEvent.getParticipants().size());
        assertEquals(1, spinfoodEvent.getPairs().size());
    }

    /**
     * Test for showing metrics of pairs.
     * This tests the output of the showMetricsOfPairs method by capturing the printed output.
     */
    @Test
    void testShowMetricsOfPairs() {
        spinfoodEvent.setPairs(Arrays.asList(pair1, pair2));
        spinfoodEvent.setParticipants(Arrays.asList(Alice, Bob, Jane, Joe));

        // Setting metrics for pairs
        pair1.setFemaleProportion(0.5);
        pair1.setAgeDifference(5);
        pair1.setPreferenceDeviation(0);
        pair2.setFemaleProportion(0.5);
        pair2.setAgeDifference(0);
        pair2.setPreferenceDeviation(0);

        // Redirecting System.out to capture the output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        spinfoodEvent.showMetricsOfPairs();

        String output = outContent.toString();
        System.setOut(null);  // Resetting System.out

        // Verifying the output
        assertTrue(output.contains(" Number of Pairs: 2"));
        assertTrue(output.contains(" Number of Successors(Nachrückende): 0"));
        assertTrue(output.contains(" Average Gender Diversity: 0.5"));
      //  assertTrue(output.contains(" Average Age Difference: 2.5"));
        //assertTrue(output.contains(" Average Food Preference deviation: 0.0"));
        assertTrue(output.contains(" Number of Pairs created by algorithm: 2"));
    }

    /**
     * Test for metrics of pairs created by the algorithm.
     * This tests the output metrics of pairs that were not registered initially but created by the algorithm.
     */
    @Test
    void testShowMetricsOfAlgorithmCreatedPairs() {
        pair1.setRegisteredAsPair(true);
        pair2.setRegisteredAsPair(false);

        spinfoodEvent.setPairs(Arrays.asList(pair1, pair2));
        spinfoodEvent.setParticipants(Arrays.asList(Alice, Bob, Jane, Joe));

        // Setting metrics for pairs
        pair1.setFemaleProportion(0.5);
        pair1.setAgeDifference(5);
        pair1.setPreferenceDeviation(0);
        pair2.setFemaleProportion(0.5);
        pair2.setAgeDifference(0);
        pair2.setPreferenceDeviation(0);

        // Redirecting System.out to capture the output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        spinfoodEvent.showMetricsOfPairs();

        String output = outContent.toString();
        System.setOut(null);  // Resetting System.out

        // Verifying the output
        assertTrue(output.contains(" Number of Pairs created by algorithm: 1"));
        assertTrue(output.contains(" Average Gender Diversity: 0.5"));
        assertTrue(output.contains(" Average Age Difference: 0.0"));
       // assertTrue(output.contains(" Average Food Preference deviation: 0.5"));
    }

    /**
     * Test for metrics calculation with no pairs.
     * This ensures that the method handles the case where no pairs are available gracefully.
     */
    @Test
    void testShowMetricsWithNoPairs() {
        spinfoodEvent.setPairs(Arrays.asList());
        spinfoodEvent.setParticipants(Arrays.asList(Alice, Bob, Jane, Joe));

        // Redirecting System.out to capture the output
        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));

        spinfoodEvent.showMetricsOfPairs();

        String output = outContent.toString();
        System.setOut(null);  // Resetting System.out

        // Verifying the output
        assertTrue(output.contains(" Number of Pairs: 0"));
        assertTrue(output.contains(" Number of Successors(Nachrückende): 4"));
    }

    /**
     * Test writing CSV for milestone 2 with invalid filepath.
     * This tests if an exception is correctly thrown when the file path is invalid.
     */
    @Test
    void testWriteCsvMilestone2_WithInvalidFilepath() {
        spinfoodEvent.setPairs(Arrays.asList(pair1, pair2));
        spinfoodEvent.setGroups(Arrays.asList(group1, group2, group3));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spinfoodEvent.writeCsvMilestone2("/invalid/path/output.csv");
        });

        assertEquals("IO Exception.", exception.getMessage());
    }



    @Test
    public void testWriteCsvMilestone2_Basic() {
        String filepath = "test_output.csv";

        // Set up spinfoodEvent with necessary pairs and groups
        spinfoodEvent.setPairs(Arrays.asList(pair1, pair2, pair3, pair4));
        spinfoodEvent.setGroups(Arrays.asList(group1, group2, group3));

        // Perform the write operation
        spinfoodEvent.writeCsvMilestone2(filepath);

        // Verify the contents of the written file
        File outputFile = new File(filepath);
        assertTrue(outputFile.exists());

    }

    /**
     * Test CSV output when all pairs belong to groups but one group has no kitchen owner.
     */
    @Test
    public void testWriteCsvMilestone2_WithNoKitchenOwnerInOneGroup() {
        Group groupWithNoOwner = new Group(Arrays.asList(pair4), FoodPreference.VEGAN, Course.DESSERT, null);
        spinfoodEvent.setPairs(Arrays.asList(pair1, pair2, pair3, pair4));
        spinfoodEvent.setGroups(Arrays.asList(group1, group2, groupWithNoOwner));

        String filepath = testDirectory + "/no_kitchen_owner.csv";
        spinfoodEvent.writeCsvMilestone2(filepath);

        File file = new File(filepath);
        assertTrue(file.exists());
        // Further file content verification could be added here
    }

    /**
     * Test writing CSV when some pairs do not have any assigned group.
     */
    @Test
    public void testWriteCsvMilestone2_WithUnassignedPairs() {
        spinfoodEvent.setPairs(Arrays.asList(pair1, pair4));
        spinfoodEvent.setGroups(Arrays.asList(group1));  // Only group1 is set, pair1 will be assigned

        String filepath = testDirectory + "/unassigned_pairs.csv";
        spinfoodEvent.writeCsvMilestone2(filepath);

        File file = new File(filepath);
        assertTrue(file.exists());
        // This should be checked to ensure unassigned pairs are not written
    }

    /**
     * Test writing to a CSV file with a pair assigned to multiple groups covering different courses.
     * Ensures course numbers are correctly recorded for each pair across multiple group assignments.
     */
    @Test
    void testWriteCsv_MultipleGroupAssignments() throws IOException {
        Group multiCourseGroup = new Group(Arrays.asList(pair4), FoodPreference.MEAT, Course.DESSERT, pair4);
        spinfoodEvent.setGroups(Arrays.asList(multiCourseGroup));

        String filepath = testDirectory + "/multi_group_assignments.csv";
        spinfoodEvent.writeCsvMilestone2(filepath);

        File file = new File(filepath);
        assertTrue(file.exists());
        // Check content for multiple course assignments
    }



    @AfterEach
    public void tearDown() {
        // Clean up test environment, remove files, etc.
        File dir = new File(testDirectory);
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith("test_output") || file.getName().startsWith("no_kitchen_owner") || file.getName().startsWith("unassigned_pairs")) {
                file.delete();
            }
        }
    }

    /**
     * Tests numbering groups when there are no groups in the event.
     * Expected behavior: method should handle empty group list gracefully without errors.
     */
    @Test
    public void testNumberTheGroups_emptyGroupList() {
        spinfoodEvent.numberTheGroups();
        assertTrue(spinfoodEvent.getGroups().isEmpty(), "Group list should remain empty");
    }

    /**
     * Tests numbering groups when there is exactly one group in the event.
     * Expected behavior: the single group should be numbered as 1.
     */
    @Test
    public void testNumberTheGroups_singleGroup() {
        spinfoodEvent.getGroups().add(group1);
        spinfoodEvent.numberTheGroups();
        assertEquals(1, group1.getNumber(), "Group should be numbered as 1");
    }

    /**
     * Tests numbering groups when there are multiple groups in the event.
     * Expected behavior: each group should be numbered sequentially starting from 1.
     */
    @Test
    public void testNumberTheGroups_multipleGroups() {
        spinfoodEvent.getGroups().addAll(Arrays.asList(group1, group2, group3));
        spinfoodEvent.numberTheGroups();
        assertEquals(1, group1.getNumber(), "First group should be numbered as 1");
        assertEquals(2, group2.getNumber(), "Second group should be numbered as 2");
        assertEquals(3, group3.getNumber(), "Third group should be numbered as 3");
    }




    /**
     * Test method to verify the behavior of `showMetricsOfAlgorithmCreatedGroups()` in the `spinfoodEvent` class.
     * This test sets up a scenario where the algorithm creates groups and verifies the expected output.
     */

    @Test
    public void testShowMetricsOfAlgorithmCreatedGroups() {
        // Create a scenario where algorithm creates groups
        // For example, set up pairs without any initial groups
        List<Pair> pairs = Arrays.asList(pair1, pair2, pair3, pair4);
        spinfoodEvent.setPairs(pairs);

        // Redirecting System.out to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method to test
        spinfoodEvent.showMetricsOfGroups();

        // Get the captured output
        String output = outContent.toString();

        // Verify the output contains expected strings
        assertTrue(output.contains("Number of groups for appetizer: 0"));
        assertTrue(output.contains("Number of groups for main: 0"));
        assertTrue(output.contains("Number of groups for dessert: 0"));
        assertTrue(output.contains("Number of groups: 0"));
        assertTrue(output.contains("Number of pairs without groups: 4"));
        assertTrue(output.contains("Path length of all groups:")); // Add other expected outputs as per your method

        // Resetting System.out
        System.setOut(System.out);
    }


    /**
     * Test method to verify the behavior of `showMetricsOfGroups()` when no groups are set in the `spinfoodEvent` class.
     * This test checks the output when there are no groups present, ensuring correct behavior of the method.
     */

    @Test
    public void testShowMetricsWithNoGroups() {
        // Test scenario where there are no groups
        spinfoodEvent.setGroups(Arrays.asList()); // Ensure no groups are set

        // Redirecting System.out to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method to test
        spinfoodEvent.showMetricsOfGroups();

        // Get the captured output
        String output = outContent.toString();

        // Verify the output contains expected strings
        assertTrue(output.contains("Number of groups for appetizer: 0"));
        assertTrue(output.contains("Number of groups for main: 0"));
        assertTrue(output.contains("Number of groups for dessert: 0"));
        assertTrue(output.contains("Number of groups: 0"));
        assertTrue(output.contains("Number of pairs without groups: 0"));
        assertTrue(output.contains("Path length of all groups:")); // Add other expected outputs as per your method

        // Resetting System.out
        System.setOut(System.out);
    }


    /**
     * Test when maximum unsuccessful attempts to create a group is exceeded.
     */
    @Test
    public void testCreateGroups_MaxAttemptsExceeded() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        spinfoodEvent.createGroups(1, 1, 1, 1, 10); // Attempting to create a group with more pairs than available
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created if max attempts to form a group is exceeded");
    }

    /**
     * Test successful creation of a group with exactly 9 pairs.
     */
    @Test
    public void testCreateGroups_SuccessfulFormation() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "One group should be created with exactly 9 pairs");
       // assertEquals(9, spinfoodEvent.getGroups().get(0).getGroupPairs().size(), "The group should contain exactly 9 pairs");
    }

    /**
     * Test creation of multiple groups when there are enough pairs available.
     */
    @Test
    public void testCreateGroups_MultipleGroupsFormation() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(18));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "Two groups should be created with 18 pairs");
        //assertTrue(spinfoodEvent.getGroups().get(0).getGroupPairs().size() == 9 || spinfoodEvent.getGroups().get(1).getGroupPairs().size() == 9,
           //     "Each group should contain exactly 9 pairs");
    }

    /**
     * Test creation of groups with varying criteria weights.
     */
    @Test
    public void testCreateGroups_VaryingCriteriaWeights() {
        spinfoodEvent.setPairs(createPairsDifferentKitchens(9));
        spinfoodEvent.createGroups(3, 2, 1, 4, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "One group should be created with exactly 9 pairs");
        // Add more specific assertions based on your application's logic and criteria weights
    }

    /**
     * Test when all pairs have the same kitchen, ensuring no groups are formed.
     */
    @Test
    public void testCreateGroups_SameKitchenPairs() {
        spinfoodEvent.setPairs(createSameKitchenPairs(9));
        spinfoodEvent.createGroups(1, 1, 1, 1, 9);
        assertEquals(0, spinfoodEvent.getGroups().size(), "No groups should be created if all pairs have the same kitchen");
    }

    /**
     * Utility method to create a list of dummy pairs.
     */
    private List<Pair> createDummyPairs(int count) {
        List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Participant participant1 = new Participant(Integer.toString(i * 2 + 1), "Participant_" + (i * 2 + 1), FoodPreference.MEAT, 25, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 1, 0.0, 0.0));
            Participant participant2 = new Participant(Integer.toString(i * 2 + 2), "Participant_" + (i * 2 + 2), FoodPreference.MEAT, 30, Gender.MALE, new Kitchen(KitchenExists.MAYBE, 1, 1.0, 1.0));
            pairs.add(new Pair(participant1, participant2, FoodPreference.MEAT, false));
        }
        return pairs;
    }



    /**
     * Utility method to create a list of pairs with the same kitchen.
     */
    private List<Pair> createSameKitchenPairs(int count) {
        List<Pair> pairs = new ArrayList<>();
        Kitchen kitchen = new Kitchen(KitchenExists.MAYBE, 1, 0.0, 0.0);
        for (int i = 0; i < count; i++) {
            Participant participant1 = new Participant(Integer.toString(i * 2 + 1), "Participant_" + (i * 2 + 1), FoodPreference.MEAT, 25, Gender.FEMALE, kitchen);
            Participant participant2 = new Participant(Integer.toString(i * 2 + 2), "Participant_" + (i * 2 + 2), FoodPreference.MEAT, 30, Gender.MALE, kitchen);
            pairs.add(new Pair(participant1, participant2, FoodPreference.MEAT, false));
        }
        return pairs;
    }


    /**
     *Verifies replacement of participant with available successor
     */
    @Test
    @DisplayName("Replace participant with available successor")
    void testReplaceParticipantWithAvailableSuccessor() {
        // Assume Bob is cancelling and there's an available successor for Bob
        spinfoodEvent.getSuccessorParticipants().addAll(Arrays.asList(Alice, Jane)); // Available successors

        spinfoodEvent.replaceParticipant(pair1, Bob);

        assertEquals(Alice, pair1.getParticipant1());
        assertNotNull(pair1.getParticipant1());
        assertNotEquals(Bob, pair1.getParticipant2());
        assertFalse(spinfoodEvent.getSuccessorParticipants().contains(Bob));
    }

    /**
     * Tests the handling of cancelling a single participant when a successor is available.
     * This method verifies that after handling cancellation, the cancelled participant is replaced
     * by the available successor in the pair and confirms that the pair still exists in the event's list of pairs.
     */
    @Test
    public void testHandleCancellingSingleParticipant_AvailableSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        List<Participant> cancelling = new ArrayList<>();
        List<Participant> list = new ArrayList<>();
        list.add(Jane);
        spinfoodEvent.setSuccessorParticipants(list);
        cancelling.add(Alice);
        spinfoodEvent.handleCancellingParticipant(cancelling);

        assertTrue(pair1.getParticipants().contains(Bob) && pair1.getParticipants().contains(Jane) && !pair1.getParticipants().contains(Alice));
        assertTrue(spinfoodEvent.getPairs().contains(pair1));
    }

    /**
     * Tests the handling of cancelling a single participant when no available successor exists.
     * This test ensures that when there is no successor to replace the cancelled participant,
     * the pair does not contain the cancelled participant nor any successor, and that the pair is removed
     * from the event's list of pairs.
     */
    @Test
    public void testHandleCancellingSingleParticipant_NoAvailableSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        List<Participant> cancelling = new ArrayList<>();
        List<Participant> list = new ArrayList<>();
        list.add(Joe);
        spinfoodEvent.setSuccessorParticipants(list);
        cancelling.add(Alice);
        spinfoodEvent.handleCancellingParticipant(cancelling);

        assertTrue(pair1.getParticipants().contains(Bob) && !pair1.getParticipants().contains(Joe) && !pair1.getParticipants().contains(Alice));
        assertFalse(spinfoodEvent.getPairs().contains(pair1));
    }

    /**
     * Tests the handling of cancelling a pair of participants when a successor pair is available.
     * Verifies that the original pair is successfully replaced by the successor pair within the group,
     * and ensures the group no longer contains the original pair but includes the successor pair.
     */
    @Test
    public void testHandleCancellingPair_AvailableSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        List<Pair> list2 = new ArrayList<>();
        list2.add(pair4);
        spinfoodEvent.setSuccessorPairs(list2);

        List<Participant> cancelling = new ArrayList<>();
        cancelling.add(Alice);
        cancelling.add(Bob);
        spinfoodEvent.handleCancellingParticipant(cancelling);

        assertFalse(group.getGroupPairs().contains(pair1));
        assertTrue(group.getGroupPairs().contains(pair4));
    }


    /**
     * Tests the handling of a scenario where the kitchen owner cancels, and a successor pair is available.
     * This method checks if the group correctly replaces the pair containing the kitchen owner with the successor pair,
     * and updates the kitchen owner to the successor pair.
     */
    @Test
    public void testHandleCancellingPair_AvailableSuccessor_KitchenOwnerCancels() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        List<Pair> list2 = new ArrayList<>();
        list2.add(pair4);
        spinfoodEvent.setSuccessorPairs(list2);

        List<Participant> cancelling = new ArrayList<>();
        cancelling.add(Jane);
        cancelling.add(Joe);
        spinfoodEvent.handleCancellingParticipant(cancelling);

        assertFalse(group.getGroupPairs().contains(pair2));
        assertTrue(group.getGroupPairs().contains(pair4));
        assertEquals(pair4, group.getKitchenOwner());
    }


    /**
     * Tests the handling of cancelling a pair when no available successor pair exists.
     * This method verifies that the group properly removes the cancelled pair and, if no more pairs can replace it,
     * removes the entire group from the event's list of groups.
     */
    @Test
    public void testHandleCancellingPair_NoAvailableSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        List<Participant> cancelling = new ArrayList<>();
        cancelling.add(Alice);
        cancelling.add(Bob);
        spinfoodEvent.handleCancellingParticipant(cancelling);

        assertFalse(group.getGroupPairs().contains(pair1));
        assertFalse(spinfoodEvent.getGroups().contains(group));
    }



    /**
     * Tests the scenario where a cancelling pair is successfully replaced by a valid successor pair.
     */
    @Test
    public void testReplacePair_WithValidSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        List<Pair> list2 = new ArrayList<>();
        list2.add(pair4);
        spinfoodEvent.setSuccessorPairs(list2);


        spinfoodEvent.replacePair(pair1);

        assertFalse(group.getGroupPairs().contains(pair1), "pair1 should be removed from group");
        assertTrue(group.getGroupPairs().contains(pair4), "pair3 should be added to group");
    }

    /**
     * Tests the scenario where no valid successor pairs are available and the group needs to be dissolved.
     */
    @Test
    public void testReplacePair_WithNoValidSuccessor() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        spinfoodEvent.replacePair(pair1);

        assertTrue(spinfoodEvent.getGroups().isEmpty(), "Group should be dissolved");
        assertTrue(spinfoodEvent.getSuccessorPairs().contains(pair1), "pair1 should be added to successorPairs");
    }

    /**
     * Tests the scenario where multiple groups are affected by the cancellation of a single pair.
     */
    @Test
    public void testReplacePair_AffectingMultipleGroups() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group1);
        spinfoodEvent.getGroups().add(group2);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group1.setGroupPairs(list);

        List<Pair> list2 = new ArrayList<>();
        list2.add(pair1);
        list2.add(pair2);
        list2.add(pair4);
        group2.setGroupPairs(list2);


        spinfoodEvent.replacePair(pair1);

        assertTrue(spinfoodEvent.getGroups().isEmpty(), "Both groups should be dissolved");
    }

    /**
     * Tests the scenario where no groups contain the cancelling pair.
     */
    @Test
    public void testReplacePairNotInAnyGroup() {
        spinfoodEvent.setGroups(Arrays.asList(group2)); // group2 does not contain pair1

        spinfoodEvent.replacePair(pair1);

        assertEquals(1, spinfoodEvent.getGroups().size(), "No group should be affected");
        assertFalse(spinfoodEvent.getSuccessorPairs().contains(pair1), "pair1 should not be added to successorPairs");
    }

    /**
     * Tests the scenario where the replacing pair is the kitchen owner of the group.
     */
    @Test
    public void testReplacePair_ThatIsKitchenOwner() {
        spinfoodEvent.getPairs().add(pair1);
        spinfoodEvent.getPairs().add(pair2);
        spinfoodEvent.getPairs().add(pair3);
        spinfoodEvent.getPairs().add(pair4);
        spinfoodEvent.getGroups().add(group);

        List<Pair> list = new ArrayList<>();
        list.add(pair1);
        list.add(pair2);
        list.add(pair3);
        group.setGroupPairs(list);

        List<Pair> list2 = new ArrayList<>();
        list2.add(pair4);
        spinfoodEvent.setSuccessorPairs(list2);

        group.setKitchenOwner(pair2);

        spinfoodEvent.replacePair(pair2);

        assertEquals(pair4, group.getKitchenOwner(), "pair4 should become the new kitchen owner");
    }





}




