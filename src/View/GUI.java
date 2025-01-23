package View;

import Model.*;

import javax.swing.*;


/**
 * The graphical user interface for the event.
 */

public class GUI {
    JFrame frame;
//    JPanel panel;
//    private String language = "Deutsch";
//    private SpinfoodEvent event1;
//    private List<List<String>> inputParticipantsList;


    // Method to get the JFrame instance
//    public JFrame getFrame() {
//        return this.frame;
//    }
//    public JPanel getPanel() {
//        return panel;
//    }
//    public Object getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(String english) {
//        language=english;
//    }

    public void createAndShowGUI() {
        frame = new JFrame("Spinfood Event");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        StartScreen startScreen = new StartScreen(frame);
        startScreen.showLanguageSelectionScreen();

        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}