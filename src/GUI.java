import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * @author Julia Chilczuk
 */

public class GUI extends JFrame{

    public static void createGUI() throws ParseException {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel inputPanel = getjPanel();


        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        buttonPanel.add(okButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setBounds(100, 100, 500, 500);
        frame.setVisible(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source == okButton) {
                    ifEmpty(inputPanel);
                    /*ifValidName(nameField.getText());
                    ifValidName(surnameField.getText());
                    ifValidDate(dateField.getText());
                    if (!ifValidPesel(peselField.getText(), dateField.getText())) {
                        JOptionPane.showMessageDialog(null, "Wprowadzono błędny PESEL", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                    ifValidEmail(emailField.getText());
                    if (ifEmpty(frame) && !ifValidName(nameField.getText()) && !ifValidName(surnameField.getText()) && !ifValidDate(dateField.getText()) && !ifValidPesel(peselField.getText(), dateField.getText()) && !ifValidEmail(emailField.getText())) {
                        save(nameField.getText(), surnameField.getText(), dateField.getText(), peselField.getText(), emailField.getText(), getGender(genderPanel));
                    }*/
                }
            }
        });
    }

    private static JPanel getjPanel() {
        JPanel inputPanel = new JPanel();

        JTextField nameField = new JTextField( 10);
        nameField.setBounds(50, 20, 200, 30);
        JTextField surnameField = new JTextField(10);
        surnameField.setBounds(50, 40, 200, 30);
        JTextField dateField = new JTextField(10);
        dateField.setBounds(50, 60, 200, 30);
        JTextField peselField = new JTextField(10);
        peselField.setBounds(50, 80, 200, 30);
        JTextField emailField = new JTextField(10);
        emailField.setBounds(50, 100, 200, 30);

        JLabel name = new JLabel("Name");
        inputPanel.add(name);
        inputPanel.add(nameField);

        JLabel surname = new JLabel("Surname");
        inputPanel.add(surname);
        inputPanel.add(surnameField);

        JLabel date = new JLabel("Date of birth");
        inputPanel.add(date);
        inputPanel.add(dateField);

        JLabel pesel = new JLabel("PESEL");
        inputPanel.add(pesel);
        inputPanel.add(peselField);

        JLabel email = new JLabel("E-mail");
        inputPanel.add(email);
        inputPanel.add(emailField);

        JLabel gender = new JLabel("Gender");
        JPanel genderPanel = getGenderJPanel();
        inputPanel.add(gender);
        inputPanel.add(genderPanel);

        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        return inputPanel;
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


    public static boolean ifValidName(String name) {
        if (name != null && !name.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Wprowadzone imię jest błędne", "Błąd", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean ifValidPesel(String pesel, String date) {
        if(!pesel.isEmpty() && !date.isEmpty()){
            String[] dmy = date.split("/");
            String day = dmy[0];
            String month = dmy[1];
            String year = dmy[2];

            if (pesel.length() != 11 || !pesel.matches("\\d+")
                    || Integer.parseInt(year) <= 1900 || Integer.parseInt(year) >= 2024) {
                return false;
            }

            //dwie pierwsze cyfy peselu to 2 ostatnie cyfry roku urodzenia
            if (year.charAt(3) != pesel.charAt(1) || year.charAt(2) != pesel.charAt(0)) {
                return false;
            }

            //3. i 4. cyfra peselu to numer miesiąca


            int monthInPesel = Integer.parseInt(String.valueOf(pesel.charAt(2)) + String.valueOf(pesel.charAt(3)));
            if (Integer.parseInt(year) >= 1900 && Integer.parseInt(year) <= 1999) {
                return (Objects.equals(month, String.valueOf(monthInPesel)));
            }

            //po 2000 roku dodajemy 20

            if (Integer.parseInt(year) >= 2000 && Integer.parseInt(year) <= 2024) {
                return (Objects.equals(Integer.parseInt(month) + 20, monthInPesel));

            }


            //5. i 6. cyfra peselu to numer dnia miesiąca
            int dayInPesel = Integer.parseInt(String.valueOf(pesel.charAt(4)) + String.valueOf(pesel.charAt(5)));
            if (!Objects.equals(day, String.valueOf(dayInPesel))) {
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
        return false;
    }


    public static boolean ifValidDate(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); //setLenient used to set the leniency of the interpretation of date and time
        try {
            Date date = sdf.parse(dateStr);
            Date currentDate = new Date();
            if (!date.before(currentDate) || !dateStr.equals(sdf.format(date))) {
                JOptionPane.showMessageDialog(null, "Wprowadzona data jest błędna", "Błąd", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (ParseException ignored) {
        }
        return true;
    }

    private static String getGender(JPanel genderPanel) {
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

    private static boolean ifEmpty(JPanel panel) {
        int counter = 0;
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField textField) {
                if (textField.getText().isEmpty()) {
                    textField.setBackground(Color.PINK);
                    counter = counter + 1;
                } else if (!textField.getText().isEmpty()) {
                    textField.setBackground(Color.WHITE);
                }
            }
        }
        if (counter > 0) {
            JOptionPane.showMessageDialog(null, "Wymagane wypełnienie zaznaczonych pól", "Błąd", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

        public static boolean ifValidEmail(String email) {
            String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            if(!email.matches(regex)){
                JOptionPane.showMessageDialog(null, "Wprowadzony adres e-mail jest błędny", "Błąd", JOptionPane.ERROR_MESSAGE);
                return  false;
            }
            return true;
        }

        private static void save (String name, String surname, String date, String pesel, String email, String gender) {

            Object[] options = {"Zatwierdź"};

            int acceptResult = JOptionPane.showOptionDialog(null,
                    "Wprowadzone dane:\n" +
                            "Imię: " + name + "\n" +
                            "Nazwisko: " + surname + "\n" +
                            "Data urodzenia: " + date + "\n" +
                            "Pesel: " + pesel + "\n" +
                            "e-mail: " + email + "\n" +
                            "Płeć: " + gender + "\n",
                    "Potwierdzenie",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (acceptResult == JOptionPane.OK_OPTION) {
                Properties properties = new Properties();
                properties.setProperty("Imie", name);
                properties.setProperty("Nazwisko", surname);
                properties.setProperty("Data urodzenia", date);
                properties.setProperty("Pesel", pesel);
                properties.setProperty("E-mail", email);
                properties.setProperty("Plec", gender);

                try (FileOutputStream fileOutputStream = new FileOutputStream("resources/user.properties")) {
                    properties.store(fileOutputStream, "Dane uzytkownika");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                User user = GUI.createUser(properties);
                System.out.println("Utworzono nowego użytkownika: " + user);
            }
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