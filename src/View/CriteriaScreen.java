package View;

import Model.Pair;
import Model.Participant;
import Model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The CriteriaScreen class represents the screen where users can set the importance of various criteria
 * for either matching pairs or building groups in the Spinfood Event application.
 */
public class CriteriaScreen {
    private JFrame frame;
    private String language;
    private SpinfoodEvent event1;
    private List<List<String>> inputParticipantsList;
    private boolean change;
    private boolean pairMatching;

    /**
     * Constructs a CriteriaScreen with the specified parameters.
     *
     * @param frame the JFrame used to display the screens
     * @param language the language selected by the user
     * @param event1 the current SpinfoodEvent
     * @param inputParticipantsList the list of input participants
     * @param change whether the criteria are being changed
     * @param pairMatching whether the criteria are for pair matching or group building
     */
    public CriteriaScreen(JFrame frame, String language, SpinfoodEvent event1, List<List<String>> inputParticipantsList, boolean change, boolean pairMatching) {
        this.frame = frame;
        this.language = language;
        this.event1 = event1;
        this.inputParticipantsList = inputParticipantsList;
        this.change = change;
        this.pairMatching = pairMatching;
    }

    /**
     * Constructs a CriteriaScreen with the specified parameters, with default values for change and pairMatching.
     *
     * @param frame the JFrame used to display the screens
     * @param language the language selected by the user
     * @param event1 the current SpinfoodEvent
     * @param inputParticipantsList the list of input participants
     */
    public CriteriaScreen(JFrame frame, String language, SpinfoodEvent event1, List<List<String>> inputParticipantsList) {
        this.frame = frame;
        this.language = language;
        this.event1 = event1;
        this.inputParticipantsList = inputParticipantsList;
    }

    /**
     * Sets the change flag.
     *
     * @param change the new value for the change flag
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    /**
     * Sets the pairMatching flag.
     *
     * @param pairMatching the new value for the pairMatching flag
     */
    public void setPairMatching(boolean pairMatching) {
        this.pairMatching = pairMatching;
    }

    /**
     * Displays the criteria selection screen where users can set the importance of various criteria.
     */
    protected void showCriteriaScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel;
        if (change)
            titleLabel = new JLabel(language.equals("Deutsch") ? "Wichtigkeit der Kriterien ändern" : "Change Importance of Criteria", SwingConstants.CENTER);
        else {
            if (pairMatching)
                titleLabel = new JLabel(language.equals("Deutsch") ? "Paarbildung" : "Matching Pairs", SwingConstants.CENTER);
            else
                titleLabel = new JLabel(language.equals("Deutsch") ? "Gruppenbildung" : "Building Groups", SwingConstants.CENTER);
        }

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String headerMessage = language.equals("Deutsch")
                ? "Bitte wählen Sie die Wichtigkeit der folgenden Kriterien aus (1 - wichtigste; 5 - am wenigsten wichtig):"
                : "Please choose the importance of the following criteria (1 - most important; 5 - least important):";
        JLabel criteriaMessageLabel = new JLabel(headerMessage, SwingConstants.CENTER);
        criteriaMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        criteriaMessageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] criteriaOptions = {"1", "2", "3", "4", "5"};

        JComboBox<String> foodPreferenceComboBox = new JComboBox<>(criteriaOptions);
        foodPreferenceComboBox.setSelectedItem("1");
        JComboBox<String> ageDifferenceComboBox = new JComboBox<>(criteriaOptions);
        ageDifferenceComboBox.setSelectedItem("2");
        JComboBox<String> genderDiversityComboBox = new JComboBox<>(criteriaOptions);
        genderDiversityComboBox.setSelectedItem("3");
        JComboBox<String> pathDistanceComboBox = new JComboBox<>(criteriaOptions);
        pathDistanceComboBox.setSelectedItem("4");
        JComboBox<String> elementsComboBox = new JComboBox<>(criteriaOptions);
        elementsComboBox.setSelectedItem("5");

        String[] criteriaLabels = {
                language.equals("Deutsch") ? "Ähnliche Essenspräferenzen" : "Similar Food Preference",
                language.equals("Deutsch") ? "Ähnliches Alter" : "Similar Age",
                language.equals("Deutsch") ? "Vielfältige Geschlechter" : "Diverse Gender",
                language.equals("Deutsch") ? "Minimale Pfadlänge" : "Minimal Path Distance",
                language.equals("Deutsch") ? "Anzahl der Elemente" : "Number of Elements"
        };

        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setLayout(new GridLayout(5, 2, 10, 10));
        criteriaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        criteriaPanel.add(new JLabel(criteriaLabels[0]));
        criteriaPanel.add(foodPreferenceComboBox);
        criteriaPanel.add(new JLabel(criteriaLabels[1]));
        criteriaPanel.add(ageDifferenceComboBox);
        criteriaPanel.add(new JLabel(criteriaLabels[2]));
        criteriaPanel.add(genderDiversityComboBox);
        criteriaPanel.add(new JLabel(criteriaLabels[3]));
        criteriaPanel.add(pathDistanceComboBox);
        criteriaPanel.add(new JLabel(criteriaLabels[4]));
        criteriaPanel.add(elementsComboBox);

        JButton startAlgorithmButton;
        if (!change)
            if (pairMatching)
                startAlgorithmButton = new JButton(language.equals("Deutsch") ? "Paarbildung anfangen" : "Start pairing");
            else startAlgorithmButton = new JButton(language.equals("Deutsch") ? "Gruppenbildung anfangen" : "Start grouping");
        else startAlgorithmButton = new JButton(language.equals("Deutsch") ? "Änderungen Anwenden" : "Apply Changes");
        startAlgorithmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startAlgorithmButton.addActionListener(e -> {
            boolean valid = checkCriteriaSelection(
                    foodPreferenceComboBox,
                    ageDifferenceComboBox,
                    genderDiversityComboBox,
                    pathDistanceComboBox,
                    elementsComboBox
            );

            if (valid) {
                int foodPreference = Integer.parseInt((String) foodPreferenceComboBox.getSelectedItem());
                int ageDifference = Integer.parseInt((String) ageDifferenceComboBox.getSelectedItem());
                int genderDiversity = Integer.parseInt((String) genderDiversityComboBox.getSelectedItem());
                int pathDistance = Integer.parseInt((String) pathDistanceComboBox.getSelectedItem());
                int numberOfElements = Integer.parseInt((String) elementsComboBox.getSelectedItem());

                if (pairMatching)
                    handlePairMatching(foodPreference, ageDifference, genderDiversity);
                else
                    handleGroupMatching(foodPreference, ageDifference, genderDiversity, pathDistance, numberOfElements);
            } else {
                String message = language.equals("Deutsch") ? "Bitte wählen Sie jede Bewertung von 1 bis 5 genau einmal aus!" : "Please make sure to choose each ranking from 1 to 5 exactly once!";
                JOptionPane.showMessageDialog(frame, message, "Warnung", JOptionPane.WARNING_MESSAGE);
            }
        });

        panel.add(Box.createVerticalGlue());
        panel.add(titleLabel);
        panel.add(criteriaMessageLabel);
        panel.add(criteriaPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(startAlgorithmButton);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Build participants into pairs based on the importance of criteria. (1 - most important, 5 - least important)
     * The smallest number among the three would become 1, the biggest would become 3, and the middle would become 2.
     * Path distance and number of elements are irrelevant to pair matching.
     * @param foodPreference how important similar food preference is
     * @param ageDifference how important similar age is
     * @param genderDiversity how important diverse gender is
     */
    private void handlePairMatching(int foodPreference, int ageDifference, int genderDiversity) {
        int[] criteriaImportance = new int[]{foodPreference, ageDifference, genderDiversity};
        int[] adjustedCriteria;
        adjustedCriteria = adjustCriteriaForPairs(criteriaImportance);
        if (!change) {
            event1.createPairs(adjustedCriteria[0], adjustedCriteria[1], adjustedCriteria[2]);
            event1.numberThePairs();
            System.out.println("Original Criteria "+ adjustedCriteria[0]+" "+ adjustedCriteria[1]+ " "+adjustedCriteria[2]);
            PairScreen pairScreen = new PairScreen(this.frame, this.language, this.event1, this.inputParticipantsList);
            pairScreen.showPairingResultsScreen();
        } else {
            SpinfoodEvent newEvent = new SpinfoodEvent(event1.getAfterDinnerPartyLocation());
            newEvent.createInitialParticipantsAndPairs(inputParticipantsList);
            System.out.println("New Criteria "+ adjustedCriteria[0]+" "+ adjustedCriteria[1]+ " "+adjustedCriteria[2]);
            newEvent.createPairs(adjustedCriteria[0], adjustedCriteria[1], adjustedCriteria[2]);
            newEvent.numberThePairs();
            PairScreen pairScreen = new PairScreen(this.frame, this.language, this.event1, this.inputParticipantsList);
            pairScreen.setTempNewEvent(newEvent);
            pairScreen.showDoublePairingResultsScreen();
        }
    }

    /**
     * Build pairs into groups based on the importance of criteria. (1 - most important, 5 - least important)
     * @param foodPreference how important similar food preference is
     * @param ageDifference how important similar age is
     * @param genderDiversity how important diverse gender is
     * @param pathDistance how important short path distance is
     * @param numberOfElements how important number of elements is
     */
    private void handleGroupMatching(int foodPreference, int ageDifference, int genderDiversity, int pathDistance, int numberOfElements) {
        boolean done = false;
        SpinfoodEvent newEvent = new SpinfoodEvent(event1.getAfterDinnerPartyLocation());
        ;
        while (!done) {
            newEvent = new SpinfoodEvent(event1.getAfterDinnerPartyLocation());
            newEvent.createInitialParticipantsAndPairs(inputParticipantsList);
            newEvent.setPairs(new ArrayList<>());
            for (Pair pair : event1.getPairs()) {
                Participant participant1 = null;
                Participant participant2 = null;
                for (Participant p : newEvent.getParticipants()) {
                    if (pair.getParticipant1().getId().equals(p.getId()))
                        participant1 = p;
                    if (pair.getParticipant2().getId().equals(p.getId()))
                        participant2 = p;
                }
                List<Pair> updatedPairs = newEvent.getPairs();
                updatedPairs.add(new Pair(participant1, participant2, pair.getFoodPreference(), pair.getPariticipant2IsKitchenOwner()));
                newEvent.setPairs(updatedPairs);
            }
            newEvent.numberThePairs();
            newEvent.createGroups(foodPreference, ageDifference, genderDiversity, pathDistance, numberOfElements);
            int numPairsInEvent = newEvent.getPairs().size();
            if (numberOfElements < 3 && newEvent.getGroups().size() >= numPairsInEvent-10)
                done = true;
            if (numberOfElements >= 3 && newEvent.getGroups().size() >= numPairsInEvent-28)
                done = true;
        }

        if (!change) {
            this.event1 = newEvent;
            GroupScreen groupScreen = new GroupScreen(this.frame, this.language, this.event1, this.inputParticipantsList);
            groupScreen.showGroupingResultsScreen();
        } else {
            GroupScreen groupScreen = new GroupScreen(this.frame, this.language, this.event1, this.inputParticipantsList);
            groupScreen.setTempNewEvent(newEvent);
            groupScreen.showDoubleGroupingResultsScreen();
        }
    }

    /**
     * Checks if the criteria selections are valid.
     *
     * @param foodPreferenceComboBox the JComboBox for food preference
     * @param ageDifferenceComboBox the JComboBox for age difference
     * @param genderDiversityComboBox the JComboBox for gender diversity
     * @param pathDistanceComboBox the JComboBox for path distance
     * @param elementsComboBox the JComboBox for number of elements
     * @return true if the selections are valid, false otherwise
     */
    private boolean checkCriteriaSelection(
            JComboBox<String> foodPreferenceComboBox,
            JComboBox<String> ageDifferenceComboBox,
            JComboBox<String> genderDiversityComboBox,
            JComboBox<String> pathDistanceComboBox,
            JComboBox<String> elementsComboBox
    ) {
        Set<String> selectedCriteria = new HashSet<>();
        selectedCriteria.add((String) foodPreferenceComboBox.getSelectedItem());
        selectedCriteria.add((String) ageDifferenceComboBox.getSelectedItem());
        selectedCriteria.add((String) genderDiversityComboBox.getSelectedItem());
        selectedCriteria.add((String) pathDistanceComboBox.getSelectedItem());
        selectedCriteria.add((String) elementsComboBox.getSelectedItem());

        return selectedCriteria.size() == 5 && selectedCriteria.containsAll(Set.of("1", "2", "3", "4", "5"));
    }


    /**
     * Adjusts the criteria importance for pairs.
     *
     * @param criteria an array of criteria importance values
     * @return an array of adjusted criteria importance values
     */
    private int[] adjustCriteriaForPairs(int[] criteria) {
        if (criteria == null || criteria.length == 0) {
            return new int[0];
        }
        int[] sortedCriteria = criteria.clone();
        Arrays.sort(sortedCriteria);
        Map<Integer, Integer> valueToRank = new HashMap<>();
        int rank = 1;
        for (int i = 0; i < sortedCriteria.length; i++) {
            if (!valueToRank.containsKey(sortedCriteria[i])) {
                valueToRank.put(sortedCriteria[i], rank);
                rank++;
            }
        }
        int[] result = new int[criteria.length];
        for (int i = 0; i < criteria.length; i++) {
            result[i] = valueToRank.get(criteria[i]);
        }
        return result;
    }
}
