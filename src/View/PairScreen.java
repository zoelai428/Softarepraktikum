package View;

import Model.*;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static Model.SpinfoodEvent.findDistance;

/**
 * The PairScreen class represents the screen where users can view and adjust pairs of participants
 * in the Spinfood Event application.
 */
public class PairScreen {
    private JFrame frame;
    private String language;
    private SpinfoodEvent event1;
    private SpinfoodEvent tempNewEvent;
    private List<List<String>> inputParticipantsList;
    private UndoManager undoManager;

    /**
     * Constructs a PairScreen with the specified parameters.
     *
     * @param frame the JFrame used to display the screen
     * @param language the language selected by the user
     * @param event1 the current SpinfoodEvent containing the pairs
     * @param inputParticipantsList the list of input participants
     */
    public PairScreen(JFrame frame, String language, SpinfoodEvent event1, List<List<String>> inputParticipantsList) {
        this.frame = frame;
        this.language = language;
        this.event1 = event1;
        this.inputParticipantsList = inputParticipantsList;
        this.undoManager = new UndoManager();
    }

    /**
     * Sets the temporary new event for comparison.
     *
     * @param tempNewEvent the new SpinfoodEvent for comparison
     */
    public void setTempNewEvent(SpinfoodEvent tempNewEvent) {
        this.tempNewEvent = tempNewEvent;
    }

    /**
     * Displays the pairing results screen where users can view the pairs created.
     */
    protected void showPairingResultsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel successfulLabel = new JLabel(language.equals("Deutsch") ? "Paarbildung erfolgreich durchgeführt!" : "Building pairs was successful!", SwingConstants.CENTER);
        successfulLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successfulLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(successfulLabel);

        helpShowPairingResultsScreen(event1, panel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton changeImportanceButton = new JButton(language.equals("Deutsch") ? "Wichtigkeit ändern" : "Change Importance");
        JButton adjustManuallyButton = new JButton(language.equals("Deutsch") ? "Manuell anpassen" : "Adjust Manually");
        JButton continueButton = new JButton(language.equals("Deutsch") ? "Weiter zur Gruppenerstellung" : "Continue with Building Groups");

        CriteriaScreen criteriaScreen = new CriteriaScreen(this.frame, this.language, this.event1, this.inputParticipantsList);
        changeImportanceButton.addActionListener(e -> {
            criteriaScreen.setChange(true);
            criteriaScreen.setPairMatching(true);
            criteriaScreen.showCriteriaScreen();
        });
        adjustManuallyButton.addActionListener(e -> showPairManualAdjustmentScreen());
        continueButton.addActionListener(e -> {
            criteriaScreen.setChange(false);
            criteriaScreen.setPairMatching(false);
            criteriaScreen.showCriteriaScreen();
        });

        buttonsPanel.add(changeImportanceButton);
        buttonsPanel.add(adjustManuallyButton);
        buttonsPanel.add(continueButton);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonsPanel);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Helper method to display the pairing results.
     *
     * @param event the SpinfoodEvent containing the pairs
     * @param panel the JPanel to which the components are added
     */
    private void helpShowPairingResultsScreen(SpinfoodEvent event, JPanel panel) {
        JTextArea metricsArea = new JTextArea();
        metricsArea.setEditable(false);
        metricsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalPrintStream = System.out;
        PrintStream newPrintStream = new PrintStream(outputStream);
        System.setOut(newPrintStream);
        event.showMetricsOfPairs();
        System.setOut(originalPrintStream);
        String metricsOutput = outputStream.toString();
        metricsArea.setText(metricsOutput);

        JScrollPane tableScrollPane = getPairsListScrollPane(event);
        JScrollPane successorsScrollPane = getParticipantsSuccessorsScrollPane(event);

        panel.add(metricsArea);
        panel.add(tableScrollPane);
        panel.add(successorsScrollPane);
    }

    /**
     * Creates a JScrollPane containing the pairs list.
     *
     * @param event the SpinfoodEvent containing the pairs
     * @return a JScrollPane containing the pairs list
     */
    private JScrollPane getPairsListScrollPane(SpinfoodEvent event) {
        String[] columnNames;
        if (language.equals("Deutsch")) {
            columnNames = new String[]{"Pärchen Nr.", "Teilnehmer 1", "Teilnehmer 2", "Essenspräferenz", "Eingetragen als Pärchen"};
        } else {
            columnNames = new String[]{"Pair No.", "Participant 1", "Participant 2", "Food Preference", "Registered As Pair"};
        }
        Object[][] rowData = new Object[event.getPairs().size()][5];
        int rowIndex = 0;
        for (Pair pair : event.getPairs()) {
            rowData[rowIndex][0] = pair.getNumber();
            rowData[rowIndex][1] = pair.getParticipant1().getName();
            rowData[rowIndex][2] = pair.getParticipant2().getName();
            rowData[rowIndex][3] = pair.getFoodPreference();
            rowData[rowIndex][4] = pair.isRegisteredAsPair();
            rowIndex++;
        }

        JTable pairsTable = new JTable(rowData, columnNames);
        pairsTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(pairsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(language.equals("Deutsch") ? "Pärchenliste" : "Pairs List"));
        return tableScrollPane;
    }

    /**
     * Creates a JScrollPane containing the participants' successors list.
     *
     * @param event the SpinfoodEvent containing the successors
     * @return a JScrollPane containing the participants' successors list
     */
    private JScrollPane getParticipantsSuccessorsScrollPane(SpinfoodEvent event) {
        int rowIndex;

        String[] columnNames2;
        if (language.equals("Deutsch")) {
            columnNames2 = new String[]{"Name", "Essenspräferenz", "Alter", "Geschlecht", "Verfügbare Küche"};
        } else {
            columnNames2 = new String[]{"Name", "Food Preference", "Age", "Gender", "Available Kitchen"};
        }
        List<Participant> successors = event.getSuccessorParticipants();
        Object[][] rowData2 = new Object[successors.size()][5];
        rowIndex = 0;
        for (Participant participant : successors) {
            rowData2[rowIndex][0] = participant.getName();
            rowData2[rowIndex][1] = participant.getFoodPreference();
            rowData2[rowIndex][2] = participant.getAge();
            rowData2[rowIndex][3] = participant.getGender();
            if (participant.getKitchen() == null)
                rowData2[rowIndex][4] = "NO";
            else rowData2[rowIndex][4] = participant.getKitchen().getExists();
            rowIndex++;
        }
        JTable successorsTable = new JTable(rowData2, columnNames2);
        successorsTable.setFillsViewportHeight(true);
        JScrollPane successorsScrollPane = new JScrollPane(successorsTable);
        successorsScrollPane.setBorder(BorderFactory.createTitledBorder(language.equals("Deutsch") ? "Nachrückende" : "Successors"));
        return successorsScrollPane;
    }

    /**
     * Displays the screen comparing the original and new pairing results.
     */
    protected void showDoublePairingResultsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel comparisonLabel = new JLabel(language.equals("Deutsch") ? "Vergleich der Pärchenlisten" : "Compare Pairing Results", SwingConstants.CENTER);
        comparisonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        comparisonLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create a panel for the two lists side-by-side
        JPanel listsPanel = new JPanel();
        listsPanel.setLayout(new GridLayout(1, 2, 20, 0));

        // Original List
        JPanel originalListPanel = new JPanel();
        originalListPanel.setLayout(new BoxLayout(originalListPanel, BoxLayout.Y_AXIS));

        JLabel originalListLabel = new JLabel(language.equals("Deutsch") ? "Originale Pärchenliste" : "Original Pairing List", SwingConstants.CENTER);
        originalListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        originalListLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        originalListPanel.add(originalListLabel);

        helpShowPairingResultsScreen(event1, originalListPanel);

        // New List
        JPanel newListPanel = new JPanel();
        newListPanel.setLayout(new BoxLayout(newListPanel, BoxLayout.Y_AXIS));

        JLabel newListLabel = new JLabel(language.equals("Deutsch") ? "Neue Pärchenliste" : "New Pairing List", SwingConstants.CENTER);
        newListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newListLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        newListPanel.add(newListLabel);

        helpShowPairingResultsScreen(tempNewEvent, newListPanel);

        listsPanel.add(originalListPanel);
        listsPanel.add(newListPanel);

        panel.add(comparisonLabel);
        panel.add(listsPanel);

        JLabel chooseListLabel = new JLabel(language.equals("Deutsch") ? "Bitte wählen Sie eine der Listen aus:" : "Please choose one of the lists:");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(chooseListLabel);
        JButton takeOldListButton = new JButton(language.equals("Deutsch") ? "Originaleliste" : "Original List");
        JButton takeNewListButton = new JButton(language.equals("Deutsch") ? "Neue Liste" : "New List");
        takeOldListButton.addActionListener(e -> showPairingResultsScreen());
        takeNewListButton.addActionListener(e -> {
            this.event1 = tempNewEvent;
            showPairingResultsScreen();
        });
        buttonsPanel.add(takeOldListButton);
        buttonsPanel.add(takeNewListButton);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonsPanel);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the screen for manually adjusting the pairs.
     */
    protected void showPairManualAdjustmentScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(language.equals("Deutsch") ? "Manuelle Anpassung der Pärchenliste" : "Manual Adjustment of Pairing List", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel);

        JLabel instructionLabel = new JLabel(language.equals("Deutsch") ?
                "<html>Zum Entfernen: Bitte wählen Sie ein Paar und \"Paar entfernen\" drücken.<br>" +
                        "Zum Erstellen: Bitte wählen Sie 2 Nachrückende und \"Paar bilden\" drücken.<br>" +
                        "Zum Rückgängigmachen(Undo): Bitte drücken Sie \"Rückgängig\".<br>" +
                        "Zum Wiederholen(Redo): Bitte drücken Sie \"Wiederholen\".</html>" :
                "<html>To remove: Please choose a pair and click \"Remove Pair\".<br>" +
                        "To create: Please choose 2 successors and click \"Create Pair\".<br>" +
                        "To undo: Please click \"Undo\".<br>" +
                        "To redo: Please click \"Redo\".</html>", SwingConstants.CENTER);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(instructionLabel);

        JLabel numberOfPairsLabel = new JLabel(language.equals("Deutsch") ? "Anzahl der Pärchen: " + event1.getPairs().size() : "Number of Pairs: " + event1.getPairs().size(), SwingConstants.CENTER);
        numberOfPairsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        numberOfPairsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(numberOfPairsLabel);

        JScrollPane tableScrollPane = getPairsListScrollPane(event1);
        JScrollPane successorsScrollPane = getParticipantsSuccessorsScrollPane(event1);
        panel.add(tableScrollPane);
        panel.add(successorsScrollPane);

        JTable pairsListTable = (JTable) tableScrollPane.getViewport().getView();
        pairsListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pairsListTable.setRowSelectionAllowed(true);
        JTable successorsTable = (JTable) successorsScrollPane.getViewport().getView();
        successorsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        successorsTable.setRowSelectionAllowed(true);

        JButton undoButton = new JButton(language.equals("Deutsch") ? "Rückgängig" : "Undo");
        JButton redoButton = new JButton(language.equals("Deutsch") ? "Wiederholen" : "Redo");

        undoButton.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
                showPairManualAdjustmentScreen();
            } else {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Keine weiteren Schritte zum Rückgängigmachen!" : "No more steps to undo!",
                        language.equals("Deutsch") ? "Rückgängig" : "Undo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        redoButton.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
                showPairManualAdjustmentScreen();
            } else {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Keine weiteren Schritte zum Wiederholen!" : "No more steps to redo!",
                        language.equals("Deutsch") ? "Wiederholen" : "Redo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JButton removeButton = new JButton(language.equals("Deutsch") ? "Paar entfernen" : "Remove Pair");
        removeButton.addActionListener(e -> removePair(pairsListTable));
        JButton addButton = new JButton(language.equals("Deutsch") ? "Paar bilden" : "Create Pair");
        addButton.addActionListener(e -> addPair(successorsTable));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(removeButton);
        controlPanel.add(addButton);
        controlPanel.add(undoButton);
        controlPanel.add(redoButton);

        panel.add(controlPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton finishButton = new JButton(language.equals("Deutsch") ? "Fertig" : "Finish");
        finishButton.addActionListener(e -> showPairingResultsScreen());
        panel.add(finishButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Removes a pair from the list based on the selection in the table.
     *
     * @param pairsListTable the JTable containing the list of pairs
     */
    private void removePair(JTable pairsListTable) {
        int selectedRow = pairsListTable.getSelectedRow();
        if (selectedRow != -1) { // Check if a row is selected
            Pair pairToRemove = event1.getPairs().get(selectedRow);

            if (pairToRemove.isRegisteredAsPair()) {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Die haben als Pärchen ins Event angemeldet!" : "They registered as a pair to the event!",
                        language.equals("Deutsch") ? "Fehler" : "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Participant participant1 = pairToRemove.getParticipant1();
            Participant participant2 = pairToRemove.getParticipant2();
            FoodPreference foodPreference = pairToRemove.getFoodPreference();
            boolean isKitchenOwner = pairToRemove.getPariticipant2IsKitchenOwner();
            int pairNumber = pairToRemove.getNumber();

            event1.getPairs().remove(pairToRemove);

            undoManager.addEdit(new AbstractUndoableEdit() {
                private final Pair removedPair = new Pair(participant1, participant2, foodPreference, isKitchenOwner);

                @Override
                public void undo() {
                    super.undo();
                    removedPair.setNumber(pairNumber);
                    event1.getPairs().add(removedPair);
                    showPairManualAdjustmentScreen();
                }

                @Override
                public void redo() {
                    super.redo();
                    event1.getPairs().remove(removedPair);
                    showPairManualAdjustmentScreen();
                }

                @Override
                public String getPresentationName() {
                    return language.equals("Deutsch") ? "Paar entfernen" : "Remove Pair";
                }
            });

            showPairManualAdjustmentScreen();
        } else {
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Bitte wählen Sie ein Pärchen zum Entfernen aus!" : "Please select a pair to remove!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Adds a pair to the list based on the selection in the successors table.
     *
     * @param successorsTable the JTable containing the list of successors
     */
    private void addPair(JTable successorsTable) {
        int[] selectedRows = successorsTable.getSelectedRows();

        if (selectedRows.length != 2) {
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Bitte wählen Sie genau 2 Teilnehmende aus!" : "Please select exactly 2 participants!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Participant participant1 = event1.getSuccessorParticipants().get(selectedRows[0]);
        Participant participant2 = event1.getSuccessorParticipants().get(selectedRows[1]);
        if (((participant1.getFoodPreference().equals(FoodPreference.MEAT)) // criteria 6.1
                && ((participant2.getFoodPreference().equals(FoodPreference.VEGAN)) || (participant2.getFoodPreference().equals(FoodPreference.VEGGIE))))
                || ((participant2.getFoodPreference().equals(FoodPreference.MEAT))
                && ((participant1.getFoodPreference().equals(FoodPreference.VEGAN)) || (participant1.getFoodPreference().equals(FoodPreference.VEGGIE))))) {
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Essenpräferenzen passen nicht!" : "Food preferences are not compatible!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (participant1.getKitchen() == null && participant2.getKitchen() == null) {
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Keine Küche von den beiden vorhanden!" : "No kitchen is available for the pair!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if ((participant1.getKitchen() != null) && (participant2.getKitchen() != null)
                && (participant1.getKitchen().equals(participant2.getKitchen()))) { // criteria 6.5 same address (kitchen) is not allowed
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Die wohnen in derselben WG!" : "They live in the same shared apartment!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Create a new pair
        Integer kitchenOwner = 1;
        if (participant1.getKitchen() == null)
            kitchenOwner = 2;
        if (participant1.getKitchen() != null && participant2.getKitchen() != null) // criteria 6.3
            if (findDistance(participant2.getKitchen().getLocation(), event1.getAfterDinnerPartyLocation())
                    < findDistance(participant1.getKitchen().getLocation(), event1.getAfterDinnerPartyLocation()))
                kitchenOwner = 2;
        Pair newPair = new Pair(participant1, participant2, PairAlgorithm.findPairFoodPreference(participant1, participant2), kitchenOwner == 2);
        newPair.setNumber(getNextPairNumber());
        event1.getPairs().add(newPair);

        undoManager.addEdit(new AbstractUndoableEdit() {
            private final Pair addedPair = newPair;

            @Override
            public void undo() {
                super.undo();
                event1.getPairs().remove(addedPair);
                showPairManualAdjustmentScreen();
            }

            @Override
            public void redo() {
                super.redo();
                event1.getPairs().add(addedPair);
                showPairManualAdjustmentScreen();
            }

            @Override
            public String getPresentationName() {
                return language.equals("Deutsch") ? "Paar hinzufügen" : "Add Pair";
            }
        });

        showPairManualAdjustmentScreen();
    }

    /**
     * Gets the next pair number.
     *
     * @return the next pair number
     */
    private int getNextPairNumber() {
        return event1.getPairs().stream()
                .mapToInt(Pair::getNumber)
                .max()
                .orElse(0) + 1;
    }

}
