import java.sql.*;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class AccountNumberGenerator {
    private static final Map<String, String> BRANCH_CODES = new HashMap<>();
    static {
        BRANCH_CODES.put("Kampala", "KLA");
        BRANCH_CODES.put("Gulu", "GUL");
        BRANCH_CODES.put("Mbarara", "MBR");
        BRANCH_CODES.put("Jinja", "JIN");
        BRANCH_CODES.put("Mbale", "MBA");
    }

    public static String branchCode(String branch) {
        return BRANCH_CODES.getOrDefault(branch, "UNK");
    }

    public static String nextAccountNumber(Connection conn, String branch) throws SQLException {
        String code = branchCode(branch);
        int year = Year.now().getValue();
        createCounterTable(conn);

        PreparedStatement find = conn.prepareStatement(
                "SELECT next_value FROM AccountCounters WHERE branch_code=? AND account_year=?");
        find.setString(1, code);
        find.setInt(2, year);
        ResultSet rs = find.executeQuery();

        int nextValue;
        if (rs.next()) {
            nextValue = rs.getInt("next_value");
            PreparedStatement upd = conn.prepareStatement(
                    "UPDATE AccountCounters SET next_value=? WHERE branch_code=? AND account_year=?");
            upd.setInt(1, nextValue + 1);
            upd.setString(2, code);
            upd.setInt(3, year);
            upd.executeUpdate();
            upd.close();
        } else {
            nextValue = 1;
            PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO AccountCounters(branch_code, account_year, next_value) VALUES(?,?,?)");
            ins.setString(1, code);
            ins.setInt(2, year);
            ins.setInt(3, 2);
            ins.executeUpdate();
            ins.close();
        }
        rs.close();
        find.close();
        return String.format("%s-%d-%06d", code, year, nextValue);
    }

    private static void createCounterTable(Connection conn) {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE AccountCounters (branch_code TEXT(10), account_year INTEGER, next_value INTEGER)");
        } catch (SQLException ignored) {
            // Table already exists.
        }
    }
}
