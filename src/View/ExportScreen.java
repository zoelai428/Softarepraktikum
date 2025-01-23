package View;

import Model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The ExportScreen class represents the screen where users can export event data to a CSV file
 * in the Spinfood Event application.
 */
public class ExportScreen {
    private JFrame frame;
    private String language;
    private SpinfoodEvent event1;

    /**
     * Constructs an ExportScreen with the specified parameters.
     *
     * @param frame the JFrame used to display the screen
     * @param language the language selected by the user
     * @param event1 the current SpinfoodEvent containing the data to be exported
     */
    public ExportScreen(JFrame frame, String language, SpinfoodEvent event1) {
        this.frame = frame;
        this.language = language;
        this.event1 = event1;
    }

    /**
     * Displays the export file screen where users can choose a file path and name to export the event data.
     */
    protected void exportFile() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel chooseFileLabel = new JLabel(language.equals("Deutsch") ? "Dateipfad und Namen wählen: " : "Choose filepath and name: ", SwingConstants.CENTER);
        chooseFileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseFileLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton chooseFileButton = new JButton(language.equals("Deutsch") ? "Wählen" : "Choose");
        chooseFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel chosenFilePathLabel = new JLabel("", SwingConstants.CENTER);
        chosenFilePathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chosenFilePathLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(language.equals("Deutsch") ? "Datei auswählen" : "Select File");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV file", "csv"));

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".csv")) {
                    filePath += ".csv";
                }
                chosenFilePathLabel.setText(filePath);
            }
        });

        JButton exportButton = new JButton(language.equals("Deutsch") ? "Exportieren" : "Export");
        exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        exportButton.addActionListener(e -> {
            String filePath = chosenFilePathLabel.getText();
            if (!filePath.isEmpty()) {
                event1.writeCsvMilestone2(filePath);
                JOptionPane.showMessageDialog(frame, language.equals("Deutsch") ? "Datei exportiert!" : "File exported!");
            } else {
                JOptionPane.showMessageDialog(frame, language.equals("Deutsch") ? "Bitte wählen Sie eine Datei." : "Please choose a file.");
            }
        });

        panel.add(chooseFileLabel);
        panel.add(chooseFileButton);
        panel.add(chosenFilePathLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(exportButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
}
