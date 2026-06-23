import java.time.LocalDate;

public class AccountFactory {
    public static Account create(String type, String firstName, String lastName, String nin, String email,
                                 String phone, LocalDate dob, String branch, double deposit, String secondNin) {
        switch (type) {
            case "Savings": return new SavingsAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, secondNin);
            case "Current": return new CurrentAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, secondNin);
            case "Fixed Deposit": return new FixedDepositAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, secondNin);
            case "Student": return new StudentAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, secondNin);
            case "Joint": return new JointAccount(firstName, lastName, nin, email, phone, dob, branch, deposit, secondNin);
            default: throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }
}
