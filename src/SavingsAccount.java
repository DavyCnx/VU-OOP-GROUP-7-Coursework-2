import java.time.LocalDate;

public class SavingsAccount extends Account {
    public SavingsAccount(String firstName, String lastName, String nin, String email, String phone,
                          LocalDate dob, String branch, double openingDeposit, String secondNin) {
        super(firstName, lastName, nin, email, phone, dob, branch, openingDeposit, secondNin);
    }
    public double minimumDeposit() { return 50000; }
    public String accountType() { return "Savings"; }
    public String specialRule() { return "Earns interest, no overdraft"; }
}
