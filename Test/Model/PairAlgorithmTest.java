package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static Model.PairAlgorithm.findParticipantsWithPartner;
import static Model.PairAlgorithm.findParticipantsWithoutPartner;
import static org.junit.jupiter.api.Assertions.*;

public class PairAlgorithmTest {
    private SpinfoodEvent spinfoodEvent;
    private PairAlgorithm pairAlgorithm;
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
        Eman = new Participant("11", "Eman", FoodPreference.VEGGIE, 35, Gender.FEMALE, new Kitchen(KitchenExists.MAYBE, 3, 5.5, 5.5));


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

        pairAlgorithm = new PairAlgorithm();
    }
    /**
     * Test to ensure pairs are created based on age difference priority
     */
    @Test
    public void shouldTestCreatePairs_AgeDifferencePriority() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(2, 1, 3);

        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test to ensure pairs are created based on gender diversity priority.
     */
    @Test
    public void shouldTestCreatePairs_GenderDiversityPriority() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(3, 2, 1);

        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test to ensure no pairs are created when conditions do not match.
     */
    @Test
    public void shouldTestCreatePairs_NoPairsCreated() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(1, 2, 3);

        assertEquals(0, event.getPairs().size());
    }

    /**
     * Test to ensure pairs are created based on food preference priority.
     */
    @Test
    public void shouldTestCreatePairs_FoodPreferencePriority() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(3, 1, 2);

        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test to ensure no pairs are created when kitchen match condition is not met.
     */
    @Test
    public void shouldTestCreatePairs_NoKitchenMatch() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "no", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "female", "no", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(1, 2, 3);

        assertEquals(0, event.getPairs().size());
    }

    /**
     * Test to ensure no pairs are created when participants have different food preferences.
     */
    @Test
    public void shouldTestCreatePairs_DifferentFoodPreferences() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "MEAT", "26", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(1, 2, 3);

        assertEquals(0, event.getPairs().size());
    }

    /**
     * Test to ensure pairs are created when participants are in the same age group.
     */
    @Test
    public void shouldTestCreatePairs_SameAgeGroup() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "21", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(2, 1, 3);

        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test to ensure no pairs are created when there are no participants.
     */
    @Test
    public void shouldTestCreatePairs_NoParticipants() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        event.createPairs(1, 2, 3);
        assertEquals(0, event.getPairs().size());
    }

    /**
     * Test to find participants without a partner.
     */
    @Test
    public void shouldTestFindParticipantsWithoutPartner() {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        spinfoodEvent.createInitialParticipantsAndPairs(inputList);

        List<Participant> participantsWithoutPartner = findParticipantsWithoutPartner(spinfoodEvent.getParticipants(), spinfoodEvent.getPairs());

        assertNotNull(participantsWithoutPartner);
        assertEquals(2, participantsWithoutPartner.size());
    }

    /**
     * Test to find participants with a partner.
     */
    @Test
    public void shouldTestFindParticipantsWithPartner() {

        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        spinfoodEvent.createInitialParticipantsAndPairs(inputList);
        spinfoodEvent.createPairs(1, 2, 3);

        List<Participant> participantsWithPartner = findParticipantsWithPartner(spinfoodEvent.getParticipants(), spinfoodEvent.getPairs());

        assertNotNull(participantsWithPartner);
        assertEquals(2, participantsWithPartner.size());
    }

    /**
     * Test to ensure pairs are created when participants have the same gender.
     */
    @Test
    public void shouldTestCreatePairs_SameGender() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(3, 2, 1);

        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test to ensure when foodPreference is given higher priority, pairs are created based on same food preference and
     * participants with different food preferences are not paired.
     */
    @Test
    public void shouldTestCreatePairs_FoodPreferencePriority2() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "MEAT", "26", "male", "yes", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "MEAT", "29", "male", "no", "1.0", "8.718914539788807", "50.590899839788804"));
        inputList.add(Arrays.asList("2", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person3", "VEGAN", "26", "male", "no", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(1, 2, 3); // Food preference has higher priority

        assertEquals(1, event.getPairs().size());
        assertEquals("Person1", event.getPairs().get(0).getParticipant1().getName());
        assertEquals("Person2", event.getPairs().get(0).getParticipant2().getName());


        // Check that "Person1" and "Person3" are not paired
        boolean isPerson1AndPerson3Paired = event.getPairs().stream()
                .anyMatch(pair -> (pair.getParticipant1().getName().equals("Person1") && pair.getParticipant2().getName().equals("Person3"))
                        || (pair.getParticipant1().getName().equals("Person3") && pair.getParticipant2().getName().equals("Person1")));

        assertFalse(isPerson1AndPerson3Paired, "Person1 and Person3 should not be paired");

    }

    /**
     * Test to ensure when age difference is given higher priority, pairs are created based on same age group and
     * participants with different age groups are not paired.
     */
    @Test
    public void shouldTestCreatePairs_AgeDifferencePriority2() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        inputList.add(Arrays.asList("2", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person3", "VEGAN", "26", "male", "no", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(2, 1, 3); // Age difference has higher priority

        assertEquals(1, event.getPairs().size());
        assertEquals("Person2", event.getPairs().get(0).getParticipant1().getName());
        assertEquals("Person3", event.getPairs().get(0).getParticipant2().getName());

        boolean isPerson12Paired = event.getPairs().stream()
                .anyMatch(pair -> pair.getParticipant1().getName().equals("Person1") || pair.getParticipant2().getName().equals("Person2"));

        assertFalse(isPerson12Paired, "Person1 and 2 should not be paired");

    }

    /**
     * Test to ensure when gender diversity is given higher priority, pairs are created based
     * on diversity and participants with the same gender are not paired.
     */
    @Test
    public void shouldTestCreatePairs_GenderDiversityPriority2() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGGIE", "21", "male", "maybe", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGGIE", "26", "female", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        inputList.add(Arrays.asList("2", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person3", "VEGAN", "26", "male", "no", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(3, 2, 1); // Gender diversity has higher priority

        assertEquals(1, event.getPairs().size());
        assertEquals("Person2", event.getPairs().get(0).getParticipant1().getName());
        assertEquals("Person3", event.getPairs().get(0).getParticipant2().getName());

        boolean isPerson1AndPerson3Paired = event.getPairs().stream()
                .anyMatch(pair -> (pair.getParticipant1().getName().equals("Person1") && pair.getParticipant2().getName().equals("Person3"))
                        || (pair.getParticipant1().getName().equals("Person3") && pair.getParticipant2().getName().equals("Person1")));

        assertFalse(isPerson1AndPerson3Paired, "Person1 and Person3 should not be paired");
    }

    /**
     * Test to ensure when food,age,gender is same for all participants, pairs are created based on any priority.
     */
    @Test
    public void shouldTestCreatePairs_SameFoodPreferenceAgeGender() {
        SpinfoodEvent event = new SpinfoodEvent(new Location(0.0, 0.0));
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("0", "004670cb-47f5-40a4-87d8-5276c18616ec", "Person1", "VEGAN", "26", "male", "yes", "3.0", "8.673368271555807", "50.5941282715558"));
        inputList.add(Arrays.asList("1", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person2", "VEGAN", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        inputList.add(Arrays.asList("2", "01a099db-22e1-4fc3-bbf5-db738bc2c10b", "Person3", "VEGAN", "26", "male", "yes", "1.0", "8.718914539788807", "50.590899839788804"));
        event.createInitialParticipantsAndPairs(inputList);

        event.createPairs(1, 2, 3); // Any priority
        assertEquals(1, event.getPairs().size());
    }

    /**
     * Test sorting when all participants have the same food preference as the participant.
     */
    @Test
    public void testSortParticipantsBySameFoodPreferenceFirst_AllSame() {
        Participant participant = Alice;
        List<Participant> candidates = Arrays.asList(Bob, Jane, Numan);

        List<Participant> sortedList = pairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);

        // Assert the order - should be unchanged since all have the same preference (MEAT)
        assertEquals(Arrays.asList(Bob, Jane, Numan), sortedList);
    }

    /**
     * Test sorting when some participants have a different food preference from the participant.
     */
    @Test
    public void testSortParticipantsBySameFoodPreferenceFirst_SomeDifferent() {
        Participant participant = Jane;
        List<Participant> candidates = Arrays.asList(Joe, Numan, Dilek);
        Jane.setFoodPreference(FoodPreference.VEGGIE);

        List<Participant> sortedList = pairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);

        // Assert the order - VEGETARIAN (Jane) should come first, then others
        assertEquals(Arrays.asList(Joe, Numan, Dilek), sortedList);
    }

    /**
     * Test sorting when all participants have different food preferences from the participant.
     */
    @Test
    public void testSortParticipantsBySameFoodPreferenceFirst_AllDifferent() {
        Participant participant = Ali;
        List<Participant> candidates = Arrays.asList(Ziyan, Zoe, Eman);

        List<Participant> sortedList = pairAlgorithm.sortParticipantsBySameFoodPreferenceFirst(participant, candidates);

        // Assert the order - Any order is valid as they all differ from participant's preference (MEAT)
        assertEquals(candidates.size(), sortedList.size());
    }

    /**
     * Test for food preference MEAT compared to MEAT, NONE, VEGGIE, VEGAN.
     */
    @Test
    public void testGetFoodPreferencePriority_MeatVsOtherPreferences() {
        assertEquals(1, pairAlgorithm.getFoodPreferencePriority(FoodPreference.MEAT, FoodPreference.MEAT));
        assertEquals(2, pairAlgorithm.getFoodPreferencePriority(FoodPreference.MEAT, FoodPreference.NONE));
        assertEquals(3, pairAlgorithm.getFoodPreferencePriority(FoodPreference.MEAT, FoodPreference.VEGGIE));
        assertEquals(4, pairAlgorithm.getFoodPreferencePriority(FoodPreference.MEAT, FoodPreference.VEGAN));
    }

    /**
     * Test for food preference NONE compared to NONE, MEAT, VEGGIE, VEGAN.
     */
    @Test
    public void testGetFoodPreferencePriority_NoneVsOtherPreferences() {
        assertEquals(1, pairAlgorithm.getFoodPreferencePriority(FoodPreference.NONE, FoodPreference.NONE));
        assertEquals(2, pairAlgorithm.getFoodPreferencePriority(FoodPreference.NONE, FoodPreference.MEAT));
        assertEquals(3, pairAlgorithm.getFoodPreferencePriority(FoodPreference.NONE, FoodPreference.VEGGIE));
        assertEquals(4, pairAlgorithm.getFoodPreferencePriority(FoodPreference.NONE, FoodPreference.VEGAN));
    }

    /**
     * Test for food preference VEGGIE compared to VEGGIE, VEGAN, NONE, MEAT.
     */
    @Test
    public void testGetFoodPreferencePriority_VeggieVsOtherPreferences() {
        assertEquals(1, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGGIE, FoodPreference.VEGGIE));
        assertEquals(2, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGGIE, FoodPreference.VEGAN));
        assertEquals(3, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGGIE, FoodPreference.NONE));
        assertEquals(4, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGGIE, FoodPreference.MEAT));
    }

    /**
     * Test for food preference VEGAN compared to VEGAN, VEGGIE, NONE, MEAT.
     */
    @Test
    public void testGetFoodPreferencePriority_VeganVsOtherPreferences() {
        assertEquals(1, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGAN, FoodPreference.VEGAN));
        assertEquals(2, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGAN, FoodPreference.VEGGIE));
        assertEquals(3, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGAN, FoodPreference.NONE));
        assertEquals(4, pairAlgorithm.getFoodPreferencePriority(FoodPreference.VEGAN, FoodPreference.MEAT));
    }

    /**
     * Test when both participants have the same food preference.
     */
    @Test
    public void testFindPairFoodPreference_SamePreference() {
        FoodPreference result = pairAlgorithm.findPairFoodPreference(Alice, Bob);
        assertEquals(FoodPreference.MEAT, result);
    }

    /**
     * Test when one participant has a MEAT preference and the other has a different preference.
     */
    @Test
    public void testFindPairFoodPreference_OneMeatOneVegan() {
        FoodPreference result = pairAlgorithm.findPairFoodPreference(Alice, Joe);
        assertEquals(FoodPreference.MEAT, result);
    }

    /**
     * Test when one participant has a NONE preference and the other has a different preference.
     */
    @Test
    public void testFindPairFoodPreference_OneNoneOneMeat() {
        FoodPreference result = pairAlgorithm.findPairFoodPreference(Eman, Zoe);
        assertEquals(FoodPreference.MEAT, result);
    }

    /**
     * Test when participants have different preferences neither of which is MEAT or NONE.
     */
    @Test
    public void testFindPairFoodPreference_DifferentPreferencesNeitherMeatNorNone() {
        FoodPreference result = pairAlgorithm.findPairFoodPreference(Joe, Eman);
        assertEquals(FoodPreference.VEGAN, result);
    }
    /**
     * Test when one participant has a NONE preference and the other has a VEGAN preference.
     */
    @Test
    public void testFindPairFoodPreference_OneNoneOneVegan() {
        Joe.setFoodPreference(FoodPreference.VEGAN);
        Zuhal.setFoodPreference(FoodPreference.NONE);
        FoodPreference result = pairAlgorithm.findPairFoodPreference(Zuhal, Joe);
        assertEquals(FoodPreference.NONE, result);
    }

    /**
     * Tests the sorting method when food preference is the highest priority and the sorting is not restricted
     * to participants matching the primary preference only. This test ensures that participants are first sorted by food preference,
     * followed by age and gender diversity, verifying that the primary sort criterion takes precedence.
     * foodPreference < ageDifference < genderDiversity
     */
    @Test
    public void testSortByBestCandidates_FoodPreferenceHigh_NotRestricted() {
        foodPreference = 1;
        ageDifference = 2;
        genderDiversity = 3;
        boolean restrictedToPriorityOnePreference = false;
        Bob.setFoodPreference(FoodPreference.MEAT);
        Joe.setFoodPreference(FoodPreference.VEGAN);
        Zoe.setFoodPreference(FoodPreference.VEGAN);
        Alice.setFoodPreference(FoodPreference.VEGAN);
        Bob.setAge(30);
        Joe.setAge(30);
        Zoe.setAge(20);
        Alice.setAge(20);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertEquals(sortedCandidates.get(0), Zoe);
        assertEquals(sortedCandidates.get(1),Joe);
    }
    /**
     * Tests the sorting method when food preference is the highest priority and the sorting is not restricted
     * to participants matching the primary preference only. This test ensures that participants are first sorted by food preference,
     * followed by age and gender diversity, verifying that the primary sort criterion takes precedence.
     * foodPreference < genderDiversity < ageDifference
     */
    @Test
    public void testSortByBestCandidates_FoodPreferenceHigh_NotRestricted2() {
        foodPreference = 1;
        ageDifference = 3;
        genderDiversity = 2;
        boolean restrictedToPriorityOnePreference = false;
        Bob.setFoodPreference(FoodPreference.MEAT);
        Joe.setFoodPreference(FoodPreference.VEGAN);
        Zoe.setFoodPreference(FoodPreference.VEGAN);
        Alice.setFoodPreference(FoodPreference.VEGAN);
        Bob.setAge(30);
        Joe.setAge(30);
        Zoe.setAge(20);
        Alice.setAge(20);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertEquals(sortedCandidates.get(0), Joe);
        assertEquals(sortedCandidates.get(1),Zoe);
    }

    /**
     * Tests the sorting method when age difference is the highest priority and the sorting is not restricted.
     * Participants should be sorted primarily by age proximity to the reference participant, ensuring that
     * those closest in age to the reference participant appear first in the sorted list.
     * ageDifference < foodPreference < genderDiversity
     */
    @Test
    public void testSortByBestCandidates_AgeDifferenceHigh_NotRestricted() {
        foodPreference = 2;
        ageDifference = 1;
        genderDiversity = 3;

        boolean restrictedToPriorityOnePreference = false;

        Bob.setAge(30);
        Joe.setAge(30);
        Zoe.setAge(20);
        Alice.setAge(20);

        Bob.setFoodPreference(FoodPreference.MEAT);
        Joe.setFoodPreference(FoodPreference.VEGAN);
        Zoe.setFoodPreference(FoodPreference.VEGAN);
        Alice.setFoodPreference(FoodPreference.VEGAN);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertTrue(sortedCandidates.getFirst().equals(Zoe));
        // Verify that the first sorted participant is closest in age to Alice
        assertTrue(sortedCandidates.get(1).equals(Joe));
        // Verify that the second sorted participant is closest in food preference to Alice
    }

    /**
     * Tests the sorting method when age difference is the highest priority and the sorting is not restricted.
     * Participants should be sorted primarily by age proximity to the reference participant, ensuring that
     * those closest in age to the reference participant appear first in the sorted list.
     * ageDifference < genderDiversity < foodPreference
     */
    @Test
    public void testSortByBestCandidates_AgeDifferenceHigh_NotRestricted2() {
        foodPreference = 3;
        ageDifference = 1;
        genderDiversity = 2;
        boolean restrictedToPriorityOnePreference = false;
        Zuhal.setAge(30);
        Joe.setAge(30);
        Zoe.setAge(20);

        List<Participant> candidates = Arrays.asList(Zuhal, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertEquals(sortedCandidates.getFirst(), Zoe);
        // Verify that the first sorted participant is closest in age to Alice
        assertEquals(sortedCandidates.get(1), Joe);
        // Verify that the second sorted participant has a different gender from Alice
    }

    /**
     * Tests sorting by gender diversity as the highest priority without restricting to the primary preference.
     * This test ensures that the first sorted participant has a different gender from the reference participant, Alice,
     * thus validating that gender diversity is considered before other sorting criteria.
     * genderDiversity < ageDifference < foodPreference
     */
    @Test
    public void testSortByBestCandidates_GenderDiversityHigh_NotRestricted() {
        foodPreference = 3;
        ageDifference = 2;
        genderDiversity = 1;
        boolean restrictedToPriorityOnePreference = false;
        Bob.setAge(40);
        Joe.setAge(30);
        Zoe.setAge(20);
        Alice.setAge(20);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertNotEquals(sortedCandidates.get(0).getGender(), Alice.getGender());
        // Check that the first sorted participant has a different gender from Alice
        assertEquals(sortedCandidates.get(0), Joe);
        assertEquals(sortedCandidates.get(1), Bob);
    }
    /**
     * Tests sorting by gender diversity as the highest priority without restricting to the primary preference.
     * This test ensures that the first sorted participant has a different gender from the reference participant, Alice,
     * thus validating that gender diversity is considered before other sorting criteria.
     * genderDiversity< foodPreference < ageDifference
     */
    @Test
    public void testSortByBestCandidates_GenderDiversityHigh_NotRestricted2() {
        foodPreference = 2;
        ageDifference = 3;
        genderDiversity = 1;
        boolean restrictedToPriorityOnePreference = false;
        Bob.setFoodPreference(FoodPreference.MEAT);
        Joe.setFoodPreference(FoodPreference.VEGAN);
        Alice.setFoodPreference(FoodPreference.VEGAN);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe);
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertNotEquals(sortedCandidates.get(0).getGender(), Alice.getGender());
        // Check that the first sorted participant has a different gender from Alice
        assertEquals(sortedCandidates.get(0), Joe);
        assertEquals(sortedCandidates.get(1), Bob);
    }

    /**
     * Tests sorting when food preference is the primary criterion but the sorting is restricted only to those
     * who match the primary preference of the reference participant. This ensures that only participants
     * with the same food preference as Alice are considered in the sorted list.
     */
    @Test
    public void testSortByBestCandidates_FoodPreferenceHigh_Restricted() {
        foodPreference = 1;
        ageDifference = 2;
        genderDiversity = 3;
        boolean restrictedToPriorityOnePreference = true;
        Alice.setFoodPreference(FoodPreference.VEGAN);
        Bob.setFoodPreference(FoodPreference.VEGAN);
        Zoe.setFoodPreference(FoodPreference.VEGAN);
        Joe.setFoodPreference(FoodPreference.MEAT);

        List<Participant> candidates = Arrays.asList(Bob, Joe, Zoe); // Assume all but Joe match food preference
        List<Participant> sortedCandidates = PairAlgorithm.sortByBestCandidates(
                foodPreference, ageDifference, genderDiversity,
                restrictedToPriorityOnePreference, Alice, candidates);

        assertFalse(sortedCandidates.getLast().equals(Joe));
        // Verify that all sorted candidates have the same food preference as Alice, since sorting is restricted
    }

    // TODO: check if the pairs in the output pair list is valid
}
