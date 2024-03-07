import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

/**
 * Klasa prezentująca GUI, umożliwiająca wypełnienie pól i
 * uzyskanie wypełnionego danymi obiektu klasy User.
 *
 * @author Natalia Topczewska
 * @author Julia Chilczuk
 */

public class GUI {
    public static void createGUI() throws ParseException {
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField peselField = new JTextField(10);
        JTextField emailField = new JTextField(10);


        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel.add(new JLabel("Imię:"));
        myPanel.add(nameField);
        myPanel.add(Box.createVerticalStrut(20)); // Spacer

        myPanel.add(new JLabel("Nazwisko:"));
        myPanel.add(surnameField);
        myPanel.add(Box.createVerticalStrut(20)); // Spacer

        myPanel.add(new JLabel("Data urodzenia: (format: DD/MM/YYYY)"));
        myPanel.add(dateField);
        myPanel.add(Box.createVerticalStrut(20)); // Spacer

        myPanel.add(new JLabel("Pesel:"));
        myPanel.add(peselField);
        myPanel.add(Box.createVerticalStrut(20)); // Spacer

        myPanel.add(new JLabel("E-mail:"));
        myPanel.add(emailField);
        myPanel.add(Box.createVerticalStrut(20));

        myPanel.add(new JLabel("Płeć:"));
        JPanel genderPanel = getGenderJPanel();
        myPanel.add(genderPanel);

        int result;
        do {
            result = JOptionPane.showConfirmDialog(null, myPanel, "Dane użytkownika", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION && !isValidName(nameField.getText())) {
                JOptionPane.showMessageDialog(null, "Wprowadzono błędne imię", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else if (result == JOptionPane.OK_OPTION && !isValidName(surnameField.getText())) {
                JOptionPane.showMessageDialog(null, "Wprowadzono błędne nazwisko", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else if (result == JOptionPane.OK_OPTION && !ifValidDate(dateField.getText())) {
                JOptionPane.showMessageDialog(null, "Wprowadzono błędną datę urodzenia", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else if (result == JOptionPane.OK_OPTION && !ifValidPesel(peselField.getText(), dateField.getText())) {
                JOptionPane.showMessageDialog(null, "Wprowadzono błędny PESEL", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else if (result == JOptionPane.OK_OPTION && !isValidEmail(emailField.getText())) {
                JOptionPane.showMessageDialog(null, "Wprowadzono błędny adres e-mail", "Błąd", JOptionPane.ERROR_MESSAGE);
            }

        } while (result == JOptionPane.OK_OPTION &&
                (nameField.getText().isEmpty() ||
                        surnameField.getText().isEmpty() ||
                        !isValidName(nameField.getText()) ||
                        !isValidName(surnameField.getText()) ||
                        !ifValidPesel(peselField.getText(), dateField.getText()) ||
                        !ifValidDate(dateField.getText()) ||
                        !isValidEmail(emailField.getText())));


        if (result == JOptionPane.OK_OPTION) {
            //JOptionPane.showMessageDialog(null, "Dziękujemy za wprowadzenie danych :)");
            Object[] options = {"Zatwierdź"};

            int acceptResult = JOptionPane.showOptionDialog(null,
                    "Wprowadzone dane:\n" +
                            "Imię: " + nameField.getText() + "\n" +
                            "Nazwisko: " + surnameField.getText() + "\n" +
                            "Data urodzenia: " + dateField.getText() + "\n" +
                            "Pesel: " + peselField.getText() + "\n" +
                            "e-mail: " + emailField.getText() + "\n" +
                            "Płeć: " + getGender(genderPanel) + "\n",
                    "Potwierdzenie",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if( acceptResult == JOptionPane.OK_OPTION){
                Properties properties = new Properties();
                properties.setProperty("Imie", nameField.getText());
                properties.setProperty("Nazwisko", surnameField.getText());
                properties.setProperty("Data urodzenia", dateField.getText());
                properties.setProperty("Pesel", peselField.getText());
                properties.setProperty("E-mail", emailField.getText());
                properties.setProperty("Plec", getGender(genderPanel));

                try (FileOutputStream fileOutputStream = new FileOutputStream("resources/user.properties")) {
                    properties.store(fileOutputStream, "Dane uzytkownika");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                User user = createUser(properties);
                System.out.println("Utworzono nowego użytkownika: " + user);
            }
            /*else if (acceptResult == JOptionPane.NO_OPTION){
                JOptionPane.showMessageDialog(null, "Spróbuj jeszcze raz :)");
            }*/
        }
    }

        private static JPanel getGenderJPanel() {
            JRadioButton femaleRadioButton = new JRadioButton("Kobieta");
            JRadioButton maleRadioButton = new JRadioButton("Mężczyzna");
            JRadioButton otherRadioButton = new JRadioButton("Brak odpowiedzi");

            ButtonGroup genderGroup = new ButtonGroup();
            genderGroup.add(femaleRadioButton);
            genderGroup.add(maleRadioButton);
            genderGroup.add(otherRadioButton);

            JPanel genderPanel = new JPanel();
            genderPanel.setLayout(new BoxLayout(genderPanel, BoxLayout.Y_AXIS));
            genderPanel.add(femaleRadioButton);
            genderPanel.add(maleRadioButton);
            genderPanel.add(otherRadioButton);
            return genderPanel;
        }

        public static boolean isValidName(String name) {
            return name.matches("[a-zA-Z]+");
        }

        public static boolean ifValidPesel(String pesel, String date){
            String[] dmy = date.split("/");
            String day = dmy[0];
            String month = dmy[1];
            String year = dmy[2];

            if (pesel == null || pesel.length() != 11 || !pesel.matches("\\d+")
                    || Integer.parseInt(year) <=1900 || Integer.parseInt(year) >= 2024) {
                return false;
            }

            //dwie pierwsze cyfy peselu to 2 ostatnie cyfry roku urodzenia
            if (year.charAt(3) != pesel.charAt(1) || year.charAt(2) != pesel.charAt(0)){
                return false;
            }

            //3. i 4. cyfra peselu to numer miesiąca


            int monthInPesel = Integer.parseInt(String.valueOf(pesel.charAt(2)) + String.valueOf(pesel.charAt(3)));
            if (Integer.parseInt(year) >=1900 && Integer.parseInt(year) <= 1999){
                return (Objects.equals(month, String.valueOf(monthInPesel)));
            }

            //po 2000 roku dodajemy 20

            if (Integer.parseInt(year) >=2000 && Integer.parseInt(year) <= 2024){
                return (Objects.equals(Integer.parseInt(month)+20 , monthInPesel));

            }


            //5. i 6. cyfra peselu to numer dnia miesiąca
            int dayInPesel = Integer.parseInt(String.valueOf(pesel.charAt(4)) + String.valueOf(pesel.charAt(5)));
                if (!Objects.equals(day, String.valueOf(dayInPesel))){
                    return false;
            }

            //cyfra kontrolna
            int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
            int sum = 0;

            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(pesel.charAt(i)) * weights[i];
            }
            int lastNumber = Character.getNumericValue(pesel.charAt(10));
            int calculatedLastNumber = (10 - (sum % 10)) % 10;

            return lastNumber == calculatedLastNumber;
        }

        public static boolean ifValidDate(String dateStr) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false); //setLenient used to set the leniency of the interpretation of date and time
            try {
                Date date = sdf.parse(dateStr);
                Date currentDate = new Date();
                return date.before(currentDate) && dateStr.equals(sdf.format(date));
            } catch (ParseException e) {
                return false;
            }
        }

        private static String getGender (JPanel genderPanel){
            Component[] components = genderPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JRadioButton radioButton) {
                    if (radioButton.isSelected()) {
                        return radioButton.getText();
                    }
                }
            }
            return "Brak odpowiedzi";
        }

        public static boolean isValidEmail(String email) {
            String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(regex);
        }
        private static User createUser(Properties properties) {
            String name = properties.getProperty("Imie");
            String surname = properties.getProperty("Nazwisko");
            String dob = properties.getProperty("Data urodzenia");
            String pesel = properties.getProperty("Pesel");
            String email = properties.getProperty("E-mail");
            String gender = properties.getProperty("Plec");

            return new User(name, surname, dob, pesel, gender, email);
        }
    }