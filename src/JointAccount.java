import java.time.LocalDate;

public class JointAccount extends Account {
    public JointAccount(String firstName, String lastName, String nin, String email, String phone,
                        LocalDate dob, String branch, double openingDeposit, String secondNin) {
        super(firstName, lastName, nin, email, phone, dob, branch, openingDeposit, secondNin);
    }
    public double minimumDeposit() { return 100000; }
    public String accountType() { return "Joint"; }
    public String specialRule() { return "Requires a second NIN"; }
}
