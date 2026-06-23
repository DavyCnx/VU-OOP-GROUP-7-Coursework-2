import java.time.LocalDate;

public class CurrentAccount extends Account {
    public CurrentAccount(String firstName, String lastName, String nin, String email, String phone,
                          LocalDate dob, String branch, double openingDeposit, String secondNin) {
        super(firstName, lastName, nin, email, phone, dob, branch, openingDeposit, secondNin);
    }
    public double minimumDeposit() { return 200000; }
    public String accountType() { return "Current"; }
    public String specialRule() { return "Overdraft allowed, no interest"; }
}
