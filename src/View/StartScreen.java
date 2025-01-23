package View;

import Controller.Main;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * The StartScreen class represents the initial screen of the Spinfood Event application.
 * It allows users to select a language, choose files for participants and party location,
 * and proceed with reading and processing the files.
 */
public class StartScreen {
    private JFrame frame;
    private JPanel panel;
    private String language = "Deutsch";
    private SpinfoodEvent event1;
    private List<List<String>> inputParticipantsList;

    /**
     * Constructs a StartScreen with the specified JFrame.
     *
     * @param frame the JFrame used to display the screens
     */
    public StartScreen(JFrame frame) {
        this.frame = frame;
    }

    /**
     * Displays the language selection screen.
     */
    protected void showLanguageSelectionScreen() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("" +
                "<html>Willkommen ins Spinfood Event!<br>" +
                "Welcome to the Spinfood Event!<br><br><br>" +
                "Wählen Sie eine Sprache aus:<br>" +
                "Choose a language:</html>", SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] languages = {"Deutsch", "English"};
        JComboBox<String> languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedIndex(0);  // default Deutsch
        languageComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        languageComboBox.addActionListener(e -> language = (String) languageComboBox.getSelectedItem());
        Dimension comboBoxSize = new Dimension(100, languageComboBox.getPreferredSize().height);
        languageComboBox.setMaximumSize(comboBoxSize);

        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> showFileSelectionScreen());

        panel.add(Box.createVerticalGlue());
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(languageComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(startButton);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the file selection screen where users can select files for participants and party location.
     */
    protected void showFileSelectionScreen() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel participantLabel = new JLabel(language.equals("Deutsch") ? "Dateipfad der Teilnehmerliste:" : "File path of the list of participants:");
        JTextField participantField = new JTextField(20);
        JButton participantButton = new JButton("Browse");
        participantButton.addActionListener(e -> chooseFile(participantField));

        JLabel partyLocationLabel = new JLabel(language.equals("Deutsch") ? "Dateipfad der Party-Location:" : "File path of the party location:");
        JTextField partyLocationField = new JTextField(20);
        JButton partyLocationButton = new JButton("Browse");
        partyLocationButton.addActionListener(e -> chooseFile(partyLocationField));

        JButton readFilesButton = new JButton(language.equals("Deutsch") ? "Datei einlesen" : "Read files");
        readFilesButton.addActionListener(e -> readFiles(participantField.getText(), partyLocationField.getText()));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(participantLabel, gbc);
        gbc.gridx = 1;
        panel.add(participantField, gbc);
        gbc.gridx = 2;
        panel.add(participantButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(partyLocationLabel, gbc);
        gbc.gridx = 1;
        panel.add(partyLocationField, gbc);
        gbc.gridx = 2;
        panel.add(partyLocationButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(readFilesButton, gbc);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Opens a file chooser dialog and sets the selected file's path in the given text field.
     *
     * @param textField the JTextField to set the selected file's path
     */
    private void chooseFile(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Reads the participant and party location files and initializes the SpinfoodEvent.
     *
     * @param filepathInputList the file path of the participant list
     * @param filepathPartyLocation the file path of the party location
     */
    private void readFiles(String filepathInputList, String filepathPartyLocation) {
        try {
            inputParticipantsList = Main.readCsv(filepathInputList);
            List<String> partyLocationList = Main.readCsv(filepathPartyLocation).get(0);

            Location partyLocation = new Location(
                    Double.parseDouble(partyLocationList.get(0)),
                    Double.parseDouble(partyLocationList.get(1))
            );
            event1 = new SpinfoodEvent(partyLocation);
            event1.createInitialParticipantsAndPairs(inputParticipantsList);

            showSuccessfulStartScreen(event1);
        } catch (Exception e) {
            String message = language.equals("Deutsch") ? "Dateien können nicht erfolgreich gelesen werden!" : "Files cannot be read successfully!";
            JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the screen indicating that files were successfully read and shows the event details.
     *
     * @param event1 the SpinfoodEvent initialized with the read files
     */
    private void showSuccessfulStartScreen(SpinfoodEvent event1) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel successfulLabel = new JLabel(language.equals("Deutsch") ? "Dateien erfolgreich eingelesen!" : "Files read successfully!", SwingConstants.CENTER);
        successfulLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successfulLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String participantsText = language.equals("Deutsch") ? "Anzahl der Teilnehmende: " : "Number of participants: ";
        String pairsText = language.equals("Deutsch") ? "Anzahl der angemeldeten Pärchen: " : "Number of pairs upon registration: ";
        String kitchensText = language.equals("Deutsch") ? "Anzahl der Küche: " : "Number of kitchens: ";

        JLabel participantsLabel = new JLabel(participantsText + event1.getParticipants().size(), SwingConstants.CENTER);
        JLabel pairsLabel = new JLabel(pairsText + event1.getPairs().size(), SwingConstants.CENTER);
        JLabel kitchensLabel = new JLabel(kitchensText + event1.getKitchens().size(), SwingConstants.CENTER);

        participantsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pairsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        kitchensLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startPairingButton = new JButton(language.equals("Deutsch") ? "Paarbildung fortfahren" : "Continue with pairing");
        startPairingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        CriteriaScreen criteriaScreen = new CriteriaScreen(this.frame, this.language, this.event1, this.inputParticipantsList, false, true);
        startPairingButton.addActionListener(e -> criteriaScreen.showCriteriaScreen());

        panel.add(Box.createVerticalGlue());
        panel.add(successfulLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(participantsLabel);
        panel.add(pairsLabel);
        panel.add(kitchensLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(startPairingButton);
        panel.add(Box.createVerticalGlue());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

}

