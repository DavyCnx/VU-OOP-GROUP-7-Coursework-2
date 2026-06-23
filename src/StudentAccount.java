import java.time.LocalDate;

public class StudentAccount extends Account {
    public StudentAccount(String firstName, String lastName, String nin, String email, String phone,
                          LocalDate dob, String branch, double openingDeposit, String secondNin) {
        super(firstName, lastName, nin, email, phone, dob, branch, openingDeposit, secondNin);
    }
    public double minimumDeposit() { return 10000; }
    public String accountType() { return "Student"; }
    public String specialRule() { return "Applicant age must be 18-25"; }
}
