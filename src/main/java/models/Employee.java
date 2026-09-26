package models;

// Cada objeto del JSON se convierte en un Employee: los nombres de los campos tienen que coincidir con las claves del archivo
public class Employee {
    private String firstName;
    private String middleName;
    private String lastName;
    private String usernameBase;
    private String password;
    private String status;

    public String getFirstName(){ return firstName; }
    public String getMiddleName(){ return middleName; }
    public String getLastName(){ return lastName; }
    public String getUsernameBase(){ return usernameBase; }
    public String getPassword(){ return password; }
    public String getStatus(){ return status; }

    @Override
    public String toString(){
        return firstName + " " + middleName + " " + lastName;
    }
}
