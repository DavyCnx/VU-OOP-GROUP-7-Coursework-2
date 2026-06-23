import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class AccessDatabase {
    private static final String DB_PATH = "database/FirstBankUganda.accdb";

    public static String save(Account account) throws Exception {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            File dbFile = new File(DB_PATH);
            dbFile.getParentFile().mkdirs();
            String url = "jdbc:ucanaccess://" + dbFile.getPath() + ";newDatabaseVersion=V2010";
            try (Connection conn = DriverManager.getConnection(url)) {
                createAccountsTable(conn);
                String accountNo = AccountNumberGenerator.nextAccountNumber(conn, account.getBranch());
                insertAccount(conn, accountNo, account);
                return accountNo;
            }
        } catch (ClassNotFoundException ex) {
            return saveToCsvFallback(account);
        }
    }

    private static void createAccountsTable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE Accounts (" +
                    "account_no TEXT(30), first_name TEXT(30), last_name TEXT(30), nin TEXT(14), " +
                    "email TEXT(100), phone TEXT(20), dob TEXT(20), account_type TEXT(30), " +
                    "branch TEXT(30), opening_deposit DOUBLE, second_nin TEXT(14), special_rule TEXT(100))");
        } catch (SQLException ignored) {
            // Table already exists.
        }
    }

    private static void insertAccount(Connection conn, String accountNo, Account account) throws SQLException {
        String sql = "INSERT INTO Accounts(account_no, first_name, last_name, nin, email, phone, dob, " +
                "account_type, branch, opening_deposit, second_nin, special_rule) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNo);
            ps.setString(2, account.getFirstName());
            ps.setString(3, account.getLastName());
            ps.setString(4, account.getNin());
            ps.setString(5, account.getEmail());
            ps.setString(6, account.getPhone());
            ps.setString(7, account.getDob().format(DateTimeFormatter.ISO_DATE));
            ps.setString(8, account.accountType());
            ps.setString(9, account.getBranch());
            ps.setDouble(10, account.getOpeningDeposit());
            ps.setString(11, account.getSecondNin());
            ps.setString(12, account.specialRule());
            ps.executeUpdate();
        }
    }

    private static String saveToCsvFallback(Account account) throws IOException {
        File dir = new File("database");
        dir.mkdirs();
        File file = new File(dir, "FirstBankUganda_records.csv");
        boolean newFile = !file.exists();
        String accountNo = AccountNumberGenerator.branchCode(account.getBranch()) + "-" +
                java.time.Year.now().getValue() + "-CSV001";
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            if (newFile) {
                out.println("account_no,first_name,last_name,nin,email,phone,dob,account_type,branch,opening_deposit,second_nin,special_rule");
            }
            out.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%.0f,%s,%s%n",
                    accountNo, account.getFirstName(), account.getLastName(), account.getNin(), account.getEmail(),
                    account.getPhone(), account.getDob(), account.accountType(), account.getBranch(),
                    account.getOpeningDeposit(), empty(account.getSecondNin()), account.specialRule());
        }
        return accountNo;
    }

    private static String empty(String value) {
        return value == null ? "" : value;
    }
}
