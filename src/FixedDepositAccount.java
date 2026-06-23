import java.time.LocalDate;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(String firstName, String lastName, String nin, String email, String phone,
                               LocalDate dob, String branch, double openingDeposit, String secondNin) {
        super(firstName, lastName, nin, email, phone, dob, branch, openingDeposit, secondNin);
    }
    public double minimumDeposit() { return 1000000; }
    public String accountType() { return "Fixed Deposit"; }
    public String specialRule() { return "Locked term, highest interest"; }
}
