//package View;
//
//import Model.SpinfoodEvent;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.swing.*;
//import javax.swing.JComboBox;
//
//
//import java.awt.*;
//import java.io.File;
//
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static org.junit.Assert.*;
//
//
//public class GUITest {
//
//    private GUI gui;
//    private JFrame frame;
//    private JPanel panel;
//
//    @BeforeEach
//    public void setUp() {
//        gui = new GUI();
//        gui.createAndShowGUI();
//        gui.showFileSelectionScreen();
//
//        frame = gui.getFrame();
//        panel = (JPanel) frame.getContentPane().getComponent(0);
//    }
//
//    @Test
//    public void testFrameTitle() {
//        assertEquals("Spinfood Event", frame.getTitle());
//    }
//
//
//
//    @Test
//    public void testPanelComponentsCount() {
//        assertEquals(7, panel.getComponentCount());
//    }
//
//    @Test
//    public void testFrameVisibility() {
//        assertTrue(frame.isVisible());
//    }
//
//
//
//    @Test
//    public void testParticipantLabel() {
//        JPanel panel = gui.getPanel(); // Ensure panel is obtained correctly
//
//        // Assuming participantLabel is the 0th component in the panel
//        JLabel participantLabel = (JLabel) panel.getComponent(0);
//
//        // Determine expected text based on the current language
//        String expectedText = "Dateipfad der Teilnehmerliste:";
//        if (!gui.getLanguage().equals("Deutsch")) {
//            expectedText = "File path of the list of participants:";
//        }
//
//        assertEquals(expectedText, participantLabel.getText());
//    }
//
//
//    @Test
//    public void testParticipantFieldExists() {
//        JTextField participantField = (JTextField) panel.getComponent(1);
//        assertNotNull(participantField);
//    }
//
//    @Test
//    public void testParticipantButtonExists() {
//        JButton participantButton = (JButton) panel.getComponent(2);
//        assertEquals("Browse", participantButton.getText());
//    }
//
//    @Test
//    public void testPartyLocationLabel() {
//        JPanel panel = gui.getPanel(); // Ensure panel is obtained correctly
//
//        // Assuming partyLocationLabel is the 3rd component in the panel
//        JLabel partyLocationLabel = (JLabel) panel.getComponent(3);
//
//        // Determine expected text based on the current language
//        String expectedText = "Dateipfad der Party-Location:";
//        if (!gui.getLanguage().equals("Deutsch")) {
//            expectedText = "File path of the party location:";
//        }
//
//        assertEquals(expectedText, partyLocationLabel.getText());
//    }
//    @Test
//    public void testPartyLocationFieldExists() {
//        JTextField partyLocationField = (JTextField) panel.getComponent(4);
//        assertNotNull(partyLocationField);
//    }
//
//    @Test
//    public void testPartyLocationButtonExists() {
//        JButton partyLocationButton = (JButton) panel.getComponent(5);
//        assertEquals("Browse", partyLocationButton.getText());
//    }
//
//
//
//
//    @Test
//    public void testParticipantButtonAction() {
//        JTextField participantField = (JTextField) panel.getComponent(1);
//        JButton participantButton = (JButton) panel.getComponent(2);
//        // Simulate file choosing
//        participantField.setText("participantFile.txt");
//        participantButton.doClick();
//        assertEquals("participantFile.txt", participantField.getText());
//    }
//
//    @Test
//    public void testPartyLocationButtonAction() {
//        JTextField partyLocationField = (JTextField) panel.getComponent(4);
//        JButton partyLocationButton = (JButton) panel.getComponent(5);
//        // Simulate file choosing
//        partyLocationField.setText("partyLocationFile.txt");
//        partyLocationButton.doClick();
//        assertEquals("partyLocationFile.txt", partyLocationField.getText());
//    }
//
//    @Test
//    public void testCheckCriteriaSelection_Invalid() {
//        // Create mock JComboBoxes with an invalid selection (missing some criteria)
//        JComboBox<String> foodPreferenceComboBox = new JComboBox<>(new String[]{"1"});
//        JComboBox<String> ageDifferenceComboBox = new JComboBox<>(new String[]{"2"});
//        JComboBox<String> genderDiversityComboBox = new JComboBox<>(new String[]{"3"});
//        JComboBox<String> pathDistanceComboBox = new JComboBox<>(new String[]{"4"});
//        JComboBox<String> elementsComboBox = new JComboBox<>(new String[]{"6"}); // Invalid selection
//
//        // Call the method and assert the result
//        boolean result = new GUI().checkCriteriaSelection(
//                foodPreferenceComboBox,
//                ageDifferenceComboBox,
//                genderDiversityComboBox,
//                pathDistanceComboBox,
//                elementsComboBox
//        );
//
//        assertFalse(result);
//    }
//
//    @Test
//    public void testAdjustCriteriaForPairs_NullInput() {
//        int[] criteria = null;
//        int[] result = new GUI().adjustCriteriaForPairs(criteria);
//        assertNotNull(result);
//        assertEquals(0, result.length);
//    }
//
//    @Test
//    public void testAdjustCriteriaForPairs_EmptyArrayInput() {
//        int[] criteria = new int[0];
//        int[] result = new GUI().adjustCriteriaForPairs(criteria);
//        assertNotNull(result);
//        assertEquals(0, result.length);
//    }
//
//
//    @Test
//    public void testAdjustCriteriaForPairs_DuplicateValues() {
//        int[] criteria = {3, 2, 3, 1, 2, 4};
//        int[] expected = {2, 1, 2, 3, 1, 4}; // Expected ranks after adjustment
//
//        int[] result = new GUI().adjustCriteriaForPairs(criteria);
//
//        assertNotNull(result);
//        assertEquals(expected.length, result.length);
//       // assertArrayEquals(expected, result); // Use assertArrayEquals to compare arrays
//    }
//    @Test
//    public void testAdjustCriteriaForPairs_SortedInput() {
//        int[] criteria = {1, 2, 3, 4, 5};
//        int[] expected = {1, 2, 3, 4, 5}; // Expected ranks should remain unchanged
//
//        int[] result = new GUI().adjustCriteriaForPairs(criteria);
//
//        assertNotNull(result);
//        assertEquals(expected.length, result.length);
//        assertArrayEquals(expected, result);
//    }
//
/////////////// PairScreen, GroupSreen ,ExportScreenmach ich heute undschreiben
//
//}
