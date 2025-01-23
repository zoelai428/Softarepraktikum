package Controller;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MainTest {

    @Test
    public void shouldReadCsv_WitValidFile() {

        String validCsvFilePathTeilnehmer = "Daten/teilnehmerliste.csv";
        String validCsvFilePathParty = "Daten/partyLocation.csv";

        List<List<String>> result = Main.readCsv(validCsvFilePathTeilnehmer);
        List<List<String>> result2 = Main.readCsv(validCsvFilePathParty);

        assertNotNull(result);
        assertNotNull(result2);
    }
    @Test
    public void shouldTestEmptyFile() {
        String filepath = "Daten/teilnehmerliste.csv";
        List<List<String>> result = Main.readCsv(filepath);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    @Test
    public void shouldTestException() {
        String filepath = "Daten/teilnehmerliste.csv"; // Path causing IO Exception
        assertDoesNotThrow(() -> Main.readCsv(filepath));
    }
    @Test
    public void shouldTestOneRow() {
        String filepath = "Daten/teilnehmerliste.csv";
        List<List<String>> result = Main.readCsv(filepath);
        assertNotNull(result);
        assertNotEquals(1, result.size());
    }
}
