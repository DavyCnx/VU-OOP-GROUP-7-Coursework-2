import java.time.LocalDate;

public abstract class Account {
    protected String firstName;
    protected String lastName;
    protected String nin;
    protected String email;
    protected String phone;
    protected LocalDate dob;
    protected String branch;
    protected double openingDeposit;
    protected String secondNin;

    public Account(String firstName, String lastName, String nin, String email, String phone,
                   LocalDate dob, String branch, double openingDeposit, String secondNin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nin = nin;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.branch = branch;
        this.openingDeposit = openingDeposit;
        this.secondNin = secondNin;
    }

    public abstract double minimumDeposit();
    public abstract String accountType();
    public abstract String specialRule();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getNin() { return nin; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getDob() { return dob; }
    public String getBranch() { return branch; }
    public double getOpeningDeposit() { return openingDeposit; }
    public String getSecondNin() { return secondNin; }
}
