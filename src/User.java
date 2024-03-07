/**
 * Klasa opisująca pojedynczego użytkownika.
 *
 * @author Natalia Topczewska
 * @author Julia Chilczuk
 */
public class User {

    private String name;
    private String surname;
    private String dob;
    private String pesel;
    private String gender;
    private String email;

    public User(String name, String surname, String dob, String pesel, String gender, String email) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.pesel = pesel;
        this.gender = gender;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob='" + dob + '\'' +
                ", pesel='" + pesel + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
