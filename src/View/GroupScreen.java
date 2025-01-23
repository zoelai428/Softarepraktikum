package View;

import Controller.Main;
import Model.*;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static Model.SpinfoodEvent.findDistance;


/**
 * The GroupScreen class represents the screen where users can view and adjust groups of participants
 * in the Spinfood Event application.
 */
public class GroupScreen {
    private JFrame frame;
    private String language;
    private SpinfoodEvent event1;
    private SpinfoodEvent tempNewEvent;
    private List<List<String>> inputParticipantsList;
    private UndoManager undoManager;

    /**
     * Constructs a GroupScreen with the specified parameters.
     *
     * @param frame the JFrame used to display the screen
     * @param language the language selected by the user
     * @param event1 the current SpinfoodEvent containing the groups
     * @param inputParticipantsList the list of input participants
     */
    public GroupScreen(JFrame frame, String language, SpinfoodEvent event1, List<List<String>> inputParticipantsList) {
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
     * Displays the grouping results screen where users can view the groups created.
     */
    protected void showGroupingResultsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel successfulLabel = new JLabel(language.equals("Deutsch") ? "Gruppenbildung erfolgreich durchgeführt!" : "Building groups was successful!", SwingConstants.CENTER);
        successfulLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successfulLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(successfulLabel);

        helpShowGroupingResultsScreen(event1, panel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton changeImportanceButton = new JButton(language.equals("Deutsch") ? "Wichtigkeit ändern / Nochmal probieren" : "Change Importance / Try Again");
        JButton adjustManuallyButton = new JButton(language.equals("Deutsch") ? "Manuell anpassen" : "Adjust Manually");
        JButton exportButton = new JButton(language.equals("Deutsch") ? "Gruppenliste exportieren" : "Export Groups List");

        changeImportanceButton.addActionListener(e -> {
            CriteriaScreen criteriaScreen = new CriteriaScreen(frame, language, event1, inputParticipantsList, true, false);
            criteriaScreen.showCriteriaScreen();
        });
        adjustManuallyButton.addActionListener(e -> showGroupManualAdjustmentScreen());
        exportButton.addActionListener(e -> {
            ExportScreen exportScreen = new ExportScreen(frame, language, event1);
            exportScreen.exportFile();
        });

        buttonsPanel.add(changeImportanceButton);
        buttonsPanel.add(adjustManuallyButton);
        buttonsPanel.add(exportButton);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonsPanel);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }


    /**
     * Helper method to display the grouping results.
     *
     * @param event the SpinfoodEvent containing the groups
     * @param panel the JPanel to which the components are added
     */
    private void helpShowGroupingResultsScreen(SpinfoodEvent event, JPanel panel) {
        JTextArea metricsArea = new JTextArea();
        metricsArea.setEditable(false);
        metricsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalPrintStream = System.out;
        PrintStream newPrintStream = new PrintStream(outputStream);
        System.setOut(newPrintStream);
        event.showMetricsOfGroups();
        event.numberTheGroups();
        System.setOut(originalPrintStream);
        String metricsOutput = outputStream.toString();
        metricsArea.setText(metricsOutput);

        JScrollPane tableScrollPane = getGroupsListScrollPane(event);
        JScrollPane successorsScrollPane = getPairsSuccessorsScrollPane(event);

        panel.add(metricsArea);
        panel.add(tableScrollPane);
        panel.add(successorsScrollPane);
    }

    /**
     * Creates a JScrollPane containing the groups list.
     *
     * @param event the SpinfoodEvent containing the groups
     * @return a JScrollPane containing the groups list
     */
    private JScrollPane getGroupsListScrollPane(SpinfoodEvent event) {
        String[] columnNames;
        if (language.equals("Deutsch")) {
            columnNames = new String[]{"Gang", "Gruppe Nr.", "Pärchen 1", "P1 Food Pref", "Pärchen 2", "P2 Food Pref", "Pärchen 3", "P3 Food Pref", "Wer kocht?"};
        } else {
            columnNames = new String[]{"Course", "Group No.", "Pair 1", "P1 Food Pref", "Pair 2", "P2 Food Pref", "Pair 3", "P3 Food Pref", "Who cooks?"};
        }
        List<Group> groups = event.getGroups();
        if (groups.isEmpty()) {
            // Handle the case where there are no groups (perhaps display a message or return an empty scroll pane)
            JScrollPane emptyScrollPane = new JScrollPane();
            return emptyScrollPane;
        }
        Object[][] rowData = new Object[groups.size()][9];
        int rowIndex = 0;
        for (Group group : groups) {
            rowData[rowIndex][0] = group.getCourse();
            rowData[rowIndex][1] = group.getNumber();
            rowData[rowIndex][2] = group.getGroupPairs().get(0).getNumber();
            rowData[rowIndex][3] = group.getGroupPairs().get(0).getFoodPreference();
            rowData[rowIndex][4] = group.getGroupPairs().get(1).getNumber();
            rowData[rowIndex][5] = group.getGroupPairs().get(1).getFoodPreference();
            rowData[rowIndex][6] = group.getGroupPairs().get(2).getNumber();
            rowData[rowIndex][7] = group.getGroupPairs().get(2).getFoodPreference();
            rowData[rowIndex][8] = group.getKitchenOwner().getNumber();
            rowIndex++;
        }

        JTable groupsTable = new JTable(rowData, columnNames);
        groupsTable.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(groupsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(language.equals("Deutsch") ? "Gruppenliste" : "Groups List"));
        return tableScrollPane;
    }

    /**
     * Creates a JScrollPane containing the pairs' successors list.
     *
     * @param event the SpinfoodEvent containing the successors
     * @return a JScrollPane containing the pairs' successors list
     */
    private JScrollPane getPairsSuccessorsScrollPane(SpinfoodEvent event) {
        int rowIndex;

        String[] columnNames2;
        if (language.equals("Deutsch")) {
            columnNames2 = new String[]{"Pärchen Nr.", "Teilnehmer 1", "Teilnehmer 2", "Essenspräferenz", "Längengrad der Küche", "Breitengrad der Küche"};
        } else {
            columnNames2 = new String[]{"Pair No.", "Participant 1", "Participant 2", "Food Preference", "Kitchen Longitude", "Kitchen Latitude"};
        }
        List<Pair> successors = event.getSuccessorPairs();
        Object[][] rowData2 = new Object[successors.size()][7];
        rowIndex = 0;
        for (Pair pair : successors) {
            rowData2[rowIndex][0] = pair.getNumber();
            rowData2[rowIndex][1] = pair.getParticipant1().getName();
            rowData2[rowIndex][2] = pair.getParticipant2().getName();
            rowData2[rowIndex][3] = pair.getFoodPreference();
            rowData2[rowIndex][4] = pair.getKitchen().getLocation().getLongitude();
            rowData2[rowIndex][5] = pair.getKitchen().getLocation().getLatitude();
            rowIndex++;
        }
        JTable successorsTable = new JTable(rowData2, columnNames2);
        successorsTable.setFillsViewportHeight(true);
        JScrollPane successorsScrollPane = new JScrollPane(successorsTable);
        successorsScrollPane.setBorder(BorderFactory.createTitledBorder(language.equals("Deutsch") ? "Nachrückende" : "Successors"));
        return successorsScrollPane;
    }

    /**
     * Displays the screen comparing the original and new grouping results.
     */
    protected void showDoubleGroupingResultsScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel comparisonLabel = new JLabel(language.equals("Deutsch") ? "Vergleich der Gruppenlisten" : "Compare Grouping Results", SwingConstants.CENTER);
        comparisonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        comparisonLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create a panel for the two lists side-by-side
        JPanel listsPanel = new JPanel();
        listsPanel.setLayout(new GridLayout(1, 2, 20, 0));

        // Original List
        JPanel originalListPanel = new JPanel();
        originalListPanel.setLayout(new BoxLayout(originalListPanel, BoxLayout.Y_AXIS));

        JLabel originalListLabel = new JLabel(language.equals("Deutsch") ? "Originale Gruppen" : "Original Groups List", SwingConstants.CENTER);
        originalListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        originalListLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        originalListPanel.add(originalListLabel);

        helpShowGroupingResultsScreen(event1, originalListPanel);

        // New List
        JPanel newListPanel = new JPanel();
        newListPanel.setLayout(new BoxLayout(newListPanel, BoxLayout.Y_AXIS));

        JLabel newListLabel = new JLabel(language.equals("Deutsch") ? "Neue Gruppenliste" : "New Groups List", SwingConstants.CENTER);
        newListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newListLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        newListPanel.add(newListLabel);

        helpShowGroupingResultsScreen(tempNewEvent, newListPanel);

        listsPanel.add(originalListPanel);
        listsPanel.add(newListPanel);

        panel.add(comparisonLabel);
        panel.add(listsPanel);

        JLabel chooseListLabel = new JLabel(language.equals("Deutsch") ? "Bitte wählen Sie eine der Listen aus:" : "Please choose one of the lists:");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(chooseListLabel);
        JButton takeOldListButton = new JButton(language.equals("Deutsch") ? "Originaleliste" : "Original List");
        JButton takeNewListButton = new JButton(language.equals("Deutsch") ? "Neue Liste" : "New List");
        takeOldListButton.addActionListener(e -> showGroupingResultsScreen());
        takeNewListButton.addActionListener(e -> {
            this.event1 = tempNewEvent;
            showGroupingResultsScreen();
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


    private void showGroupManualAdjustmentScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(language.equals("Deutsch") ? "Manuelle Anpassung der Gruppenliste" : "Manual Adjustment of Grouping List", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel);

        JLabel instructionLabel = new JLabel(language.equals("Deutsch") ?
                "<html>Zum Entfernen: Bitte wählen Sie eine Gruppe und \"Group entfernen\" drücken.<br>" +
                        "Zum Erstellen: Bitte wählen Sie 3 Nachrückende und \"Gruppe bilden\" drücken.<br>" +
                        "Zum Rückgängigmachen(Undo): Bitte drücken Sie \"Rückgängig\".<br>" +
                        "Zum Wiederholen(Redo): Bitte drücken Sie \"Wiederholen\".</html>" :
                "<html>To remove: Please choose a group and click \"Remove Group\".<br>" +
                        "To create: Please choose 3 successors and click \"Create Group\".<br>" +
                        "To undo: Please click \"Undo\".<br>" +
                        "To redo: Please click \"Redo\".</html>", SwingConstants.CENTER);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(instructionLabel);

        JLabel numberOfGroupsLabel = new JLabel(language.equals("Deutsch") ? "Anzahl der Gruppen: " + event1.getGroups().size() : "Number of Groups: " + event1.getGroups().size(), SwingConstants.CENTER);
        numberOfGroupsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        numberOfGroupsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(numberOfGroupsLabel);

        JScrollPane tableScrollPane = getGroupsListScrollPane(event1);
        JScrollPane successorsScrollPane = getPairsSuccessorsScrollPane(event1);
        panel.add(tableScrollPane);
        panel.add(successorsScrollPane);

        JTable groupsListTable = (JTable) tableScrollPane.getViewport().getView();
        groupsListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsListTable.setRowSelectionAllowed(true);
        JTable successorsTable = (JTable) successorsScrollPane.getViewport().getView();
        successorsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        successorsTable.setRowSelectionAllowed(true);

        JButton undoButton = new JButton(language.equals("Deutsch") ? "Rückgängig" : "Undo");
        JButton redoButton = new JButton(language.equals("Deutsch") ? "Wiederholen" : "Redo");

        undoButton.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
                showGroupManualAdjustmentScreen();
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
                showGroupManualAdjustmentScreen();
            } else {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Keine weiteren Schritte zum Wiederholen!" : "No more steps to redo!",
                        language.equals("Deutsch") ? "Wiederholen" : "Redo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JButton removeButton = new JButton(language.equals("Deutsch") ? "Gruppe entfernen" : "Remove Group");
        removeButton.addActionListener(e -> removeGroup(groupsListTable));
        JButton addButton = new JButton(language.equals("Deutsch") ? "Gruppe bilden" : "Create Group");
        addButton.addActionListener(e -> addCluster(successorsTable));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(removeButton);
        controlPanel.add(addButton);
        controlPanel.add(undoButton);
        controlPanel.add(redoButton);

        panel.add(controlPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton finishButton = new JButton(language.equals("Deutsch") ? "Fertig" : "Finish");
        finishButton.addActionListener(e -> showGroupingResultsScreen());
        panel.add(finishButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Removes a Group from the list based on the selection in the table.
     *
     * @param groupsListTable the JTable containing the list of groups
     */
    private void removeGroup(JTable groupsListTable) {
        // Define the options
        Object[] options = {
                language.equals("Deutsch") ? "Fortsetzen" : "Continue",
                language.equals("Deutsch") ? "Abbrechen" : "Abort"
        };

        int response = JOptionPane.showOptionDialog(
                frame,
                language.equals("Deutsch") ? "Es werden 9 Gruppen gelöscht werden!" : "This action will remove 9 groups",
                language.equals("Deutsch") ? "Warnung" : "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]  // Default option (Abort)
        );

        if (response == JOptionPane.YES_OPTION) {
            int selectedRow = groupsListTable.getSelectedRow();
            if (selectedRow != -1 && !event1.getGroups().isEmpty()) { // Check if a row is selected
                Group groupToRemove = event1.getGroups().get(selectedRow);
                List<Group> clusterToRemove = Main.getGroupsCluster(groupToRemove, event1.getGroups());
                List<Group> removedCluster = new ArrayList<>();
                Map<Group, Integer> groupNumberMap = new HashMap<>();
                Map<Group, List<Pair>> groupPairsMap = new HashMap<>();

                for(Group g : clusterToRemove) {
                    List<Pair> groupPairs = g.getGroupPairs();
                    groupPairsMap.put(g, groupPairs);
                    g.setGroupPairs(groupPairs);
                    int groupNumber = g.getNumber();
                    groupNumberMap.put(g,groupNumber);
                    event1.getGroups().remove(g);
                    event1.getSuccessorPairs().addAll(g.getGroupPairs());
                    removedCluster.add(g);
                }
                undoManager.addEdit(new AbstractUndoableEdit() {
                    @Override
                    public void undo() {
                        super.undo();
                        for(Group g : removedCluster) {
                            g.setNumber(groupNumberMap.get(g));
                            g.setGroupPairs(groupPairsMap.get(g));
                            event1.getGroups().add(g);
                        }
                        showGroupManualAdjustmentScreen();
                    }

                    @Override
                    public void redo() {
                        super.redo();
                        for(Group g : removedCluster) {
                            event1.getGroups().remove(g);
                        }
                        showGroupManualAdjustmentScreen();
                    }

                    @Override
                    public String getPresentationName() {
                        return language.equals("Deutsch") ? "Gruppe entfernen" : "Remove Group";
                    }
                });




                showGroupManualAdjustmentScreen();
            } else {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Bitte wählen Sie eine Gruppe zum Entfernen aus!" : "Please select a group to remove!",
                        language.equals("Deutsch") ? "Fehler" : "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else showGroupManualAdjustmentScreen();

    }

    /**
     * Adds a Group to the list based on the selection in the successors table.
     *
     * @param successorsTable the JTable containing the list of successors
     */
    private void addCluster(JTable successorsTable) {
        int[] selectedRows = successorsTable.getSelectedRows();
        List<Pair> cluster = new ArrayList<>();
        List<FoodPreference> f = new ArrayList<>();
        List<Group> result = new ArrayList<>();

        if (selectedRows.length != 9) {
            JOptionPane.showMessageDialog(frame,
                    language.equals("Deutsch") ? "Bitte wählen Sie genau 9 Pärchen aus!" : "Please select exactly 9 pairs!",
                    language.equals("Deutsch") ? "Fehler" : "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        for(int i = 0; i < 9; i++) {
            Pair p = event1.getPairs().get(selectedRows[i]);
            cluster.add(p);
            FoodPreference foodPreference = p.getFoodPreference();
            f.add(foodPreference);
        }
        List<Pair> sortedPairs = cluster.stream()
                .sorted(Comparator.comparing(pair -> findDistance(pair.getKitchenLocation(), event1.getAfterDinnerPartyLocation())))
                .collect(Collectors.toList());
        Map<Pair, Course> pairCourseMap = GroupAlgorithm.assignCourses(sortedPairs);

        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(6), sortedPairs.get(0), sortedPairs.get(3), Course.APPETIZER, event1.getPairs() , event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(7), sortedPairs.get(1), sortedPairs.get(4), Course.APPETIZER, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(8), sortedPairs.get(2), sortedPairs.get(5), Course.APPETIZER, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(3), sortedPairs.get(1), sortedPairs.get(8), Course.MAIN, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(4), sortedPairs.get(2), sortedPairs.get(6), Course.MAIN, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(5), sortedPairs.get(0), sortedPairs.get(7), Course.MAIN, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(0), sortedPairs.get(4), sortedPairs.get(8), Course.DESSERT, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(1), sortedPairs.get(6), sortedPairs.get(5), Course.DESSERT, event1.getPairs(), event1.getGroups());
        GroupAlgorithm.handleCreateGroup(result, pairCourseMap, sortedPairs.get(2), sortedPairs.get(3), sortedPairs.get(7), Course.DESSERT, event1.getPairs(), event1.getGroups());

        for(Group g : result) {
            if(g.getKitchenOwner() == null) {
                JOptionPane.showMessageDialog(frame,
                        language.equals("Deutsch") ? "Sie haben kein gültiges Kochpaar!" : "Not enough kitchen owners!",
                        language.equals("Deutsch") ? "Fehler" : "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            g.setNumber(getNextGroupNumber());
            event1.getGroups().add(g);
        }

        undoManager.addEdit(new AbstractUndoableEdit() {

            @Override
            public void undo() {
                super.undo();
                for(Group g : result) {
                    event1.getGroups().remove(g);
                }
                showGroupManualAdjustmentScreen();
            }

            @Override
            public void redo() {
                super.redo();
                for(Group g : result) {
                    event1.getGroups().add(g);
                }
                showGroupManualAdjustmentScreen();
            }

            @Override
            public String getPresentationName() {
                return language.equals("Deutsch") ? "Cluster hinzufügen" : "Add Cluster";
            }
        });

        showGroupManualAdjustmentScreen();
    }
    /**
     * Gets the next group number.
     *
     * @return the next group number
     */
    private int getNextGroupNumber() {
        return event1.getGroups().stream()
                .mapToInt(Group::getNumber)
                .max()
                .orElse(0) + 1;
    }


}
