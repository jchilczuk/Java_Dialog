import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa zawierająca testy jednostkowe najważniejszych metod klasy GUI.
 *
 * @author Natalia Topczewska
 * @author Julia Chilczuk
 */
class GUITest {

    @org.junit.jupiter.api.Test
    void isValidName() {
        Assertions.assertTrue(GUI.isValidName("Natalia"));
        assertNotEquals(GUI.isValidName("Natalia1"), true);
    }

    @org.junit.jupiter.api.Test
    void ifValidPesel() {
        assertFalse(GUI.ifValidPesel("032530079", "22/01/2022"));
        assertTrue(GUI.ifValidPesel("03253007921", "30/05/2003"));
        assertFalse(GUI.ifValidPesel("03253007921", "22/11/2022"));
    }

    @org.junit.jupiter.api.Test
    void ifValidDate() {
        assertFalse(GUI.ifValidDate("1234"));
        assertFalse(GUI.ifValidDate("22/13/2024"));
        assertFalse(GUI.ifValidDate("29/02/2023"));
        assertTrue(GUI.ifValidDate("29/02/2024"));
        assertTrue(GUI.ifValidDate("12/05/2002"));
    }

    @org.junit.jupiter.api.Test
    void isValidEmail() {
        assertFalse(GUI.isValidEmail("www.pl"));
        assertFalse(GUI.isValidEmail("a2@pl"));
        assertTrue(GUI.isValidEmail("pw@pw.pl"));
    }
}