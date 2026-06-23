import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class BankApplicationForm extends JFrame {
    private JTextField firstNameField = new JTextField(18);
    private JTextField lastNameField = new JTextField(18);
    private JTextField ninField = new JTextField(18);
    private JTextField secondNinField = new JTextField(18);
    private JTextField emailField = new JTextField(18);
    private JTextField confirmEmailField = new JTextField(18);
    private JTextField phoneField = new JTextField(18);
    private JPasswordField pinField = new JPasswordField(18);
    private JPasswordField confirmPinField = new JPasswordField(18);
    private JComboBox<Integer> yearBox = new JComboBox<>();
    private JComboBox<String> monthBox = new JComboBox<>();
    private JComboBox<Integer> dayBox = new JComboBox<>();
    private JComboBox<String> accountTypeBox = new JComboBox<>(new String[]{"", "Savings", "Current", "Fixed Deposit", "Student", "Joint"});
    private JComboBox<String> branchBox = new JComboBox<>(new String[]{"", "Kampala", "Gulu", "Mbarara", "Jinja", "Mbale"});
    private JTextField depositField = new JTextField(18);
    private JTextArea summaryArea = new JTextArea(7, 55);
    private Map<String, JLabel> errors = new LinkedHashMap<>();

    public BankApplicationForm() {
        setTitle("First Bank Uganda - New Account Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        fillYearsAndMonths();
        updateDays();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildForm() {
        JPanel wrapper = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 5);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        row = addRow(form, c, row, "First Name", firstNameField, "firstName");
        row = addRow(form, c, row, "Last Name", lastNameField, "lastName");
        row = addRow(form, c, row, "National ID (NIN)", ninField, "nin");
        row = addRow(form, c, row, "Second NIN (Joint only)", secondNinField, "secondNin");
        row = addRow(form, c, row, "Email", emailField, "email");
        row = addRow(form, c, row, "Confirm Email", confirmEmailField, "confirmEmail");
        row = addRow(form, c, row, "Phone Number", phoneField, "phone");
        row = addRow(form, c, row, "PIN", pinField, "pin");
        row = addRow(form, c, row, "Confirm PIN", confirmPinField, "confirmPin");

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.add(yearBox); dobPanel.add(monthBox); dobPanel.add(dayBox);
        row = addRow(form, c, row, "Date of Birth", dobPanel, "dob");
        row = addRow(form, c, row, "Account Type", accountTypeBox, "accountType");
        row = addRow(form, c, row, "Branch", branchBox, "branch");
        row = addRow(form, c, row, "Opening Deposit (UGX)", depositField, "deposit");

        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        wrapper.add(new JLabel("First Bank Uganda - New Bank Account Opening Form"), BorderLayout.NORTH);
        wrapper.add(form, BorderLayout.CENTER);
        wrapper.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);
        summaryArea.setBorder(BorderFactory.createTitledBorder("Account Summary is Below:"));
        return wrapper;
    }

    private int addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent input, String key) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(input, c);
        JLabel err = new JLabel(" ");
        err.setForeground(Color.RED);
        errors.put(key, err);
        c.gridx = 2; c.weightx = 1;
        panel.add(err, c);
        return row + 1;
    }

    private JPanel buildButtons() {
        JButton submit = new JButton("Submit");
        JButton reset = new JButton("Reset");
        submit.addActionListener(e -> submitForm());
        reset.addActionListener(e -> resetForm());
        JPanel panel = new JPanel();
        panel.add(submit); panel.add(reset);
        return panel;
    }

    private void fillYearsAndMonths() {
        int now = Year.now().getValue();
        for (int y = now - 75; y <= now - 18; y++) yearBox.addItem(y);
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        for (String m : months) monthBox.addItem(m);
        yearBox.addActionListener(e -> updateDays());
        monthBox.addActionListener(e -> updateDays());
    }

    private void updateDays() {
        Integer year = (Integer) yearBox.getSelectedItem();
        int month = monthBox.getSelectedIndex() + 1;
        if (year == null || month < 1) return;
        int oldDay = dayBox.getSelectedItem() == null ? 1 : (Integer) dayBox.getSelectedItem();
        dayBox.removeAllItems();
        int days = YearMonth.of(year, month).lengthOfMonth();
        for (int d = 1; d <= days; d++) dayBox.addItem(d);
        dayBox.setSelectedItem(Math.min(oldDay, days));
    }

    private void submitForm() {
        clearErrors();
        List<String> problems = validateInputs();
        if (!problems.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", problems), "Fix these errors", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Account account = buildAccount();
            String accountNo = AccessDatabase.save(account);
            String record = String.format("ACC: %s | %s | %s | %s | DOB %s | %s | Deposit %,.0f | %s",
                    accountNo, account.getFullName(), account.accountType(), account.getBranch(),
                    account.getDob().format(DateTimeFormatter.ISO_DATE), account.getPhone(),
                    account.getOpeningDeposit(), account.getEmail());
            summaryArea.setText(record + "\n" + account.specialRule());
            JOptionPane.showMessageDialog(this, "Account saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Account buildAccount() {
        String type = selected(accountTypeBox);
        LocalDate dob = selectedDob();
        return AccountFactory.create(type, text(firstNameField), text(lastNameField), text(ninField), text(emailField),
                text(phoneField), dob, selected(branchBox), Double.parseDouble(text(depositField)), text(secondNinField));
    }

    private List<String> validateInputs() {
        List<String> problems = new ArrayList<>();
        checkName("firstName", text(firstNameField), "First name", problems);
        checkName("lastName", text(lastNameField), "Last name", problems);
        checkNin("nin", text(ninField), "NIN", problems);

        String email = text(emailField);
        String confirmEmail = text(confirmEmailField);
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) addProblem("email", "Enter a valid email.", problems);
        if (!email.equals(confirmEmail)) addProblem("confirmEmail", "Emails must match.", problems);

        if (!text(phoneField).matches("^\\+256\\d{9}$")) addProblem("phone", "Use +256XXXXXXXXX.", problems);

        String pin = new String(pinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();
        if (!pin.matches("^\\d{4,6}$")) addProblem("pin", "PIN must be 4-6 digits.", problems);
        if (pin.matches("^(\\d)\\1+$")) addProblem("pin", "PIN cannot be one repeated digit.", problems);
        if (!pin.equals(confirmPin)) addProblem("confirmPin", "PINs must match.", problems);

        if (selectedDob() == null) addProblem("dob", "Choose a valid birth date.", problems);
        else {
            int age = Period.between(selectedDob(), LocalDate.now()).getYears();
            if (age < 18 || age > 75) addProblem("dob", "Age must be 18-75.", problems);
        }

        String type = selected(accountTypeBox);
        if (type.isEmpty()) addProblem("accountType", "Choose account type.", problems);
        String branch = selected(branchBox);
        if (branch.isEmpty()) addProblem("branch", "Choose branch.", problems);

        double deposit = -1;
        try { deposit = Double.parseDouble(text(depositField)); }
        catch (NumberFormatException ex) { addProblem("deposit", "Deposit must be a number.", problems); }

        if (!type.isEmpty() && deposit >= 0 && selectedDob() != null) {
            Account temp = buildAccount();
            if (deposit < temp.minimumDeposit()) addProblem("deposit", "Minimum for " + type + " is UGX " + String.format("%,.0f", temp.minimumDeposit()) + ".", problems);
            int age = Period.between(selectedDob(), LocalDate.now()).getYears();
            if (type.equals("Student") && (age < 18 || age > 25)) addProblem("dob", "Student age must be 18-25.", problems);
            if (type.equals("Joint")) checkNin("secondNin", text(secondNinField), "Second NIN", problems);
        }
        return problems;
    }

    private void checkName(String key, String value, String label, List<String> problems) {
        if (!value.matches("^[A-Za-z]{2,30}$")) addProblem(key, label + " must use letters only, 2-30 chars.", problems);
    }

    private void checkNin(String key, String value, String label, List<String> problems) {
        if (!value.matches("^[A-Z0-9]{14}$")) addProblem(key, label + " must be 14 uppercase letters or digits.", problems);
    }

    private void addProblem(String key, String message, List<String> problems) {
        errors.get(key).setText(message);
        problems.add(message);
    }

    private void clearErrors() {
        for (JLabel label : errors.values()) label.setText(" ");
    }

    private LocalDate selectedDob() {
        try {
            Integer y = (Integer) yearBox.getSelectedItem();
            int m = monthBox.getSelectedIndex() + 1;
            Integer d = (Integer) dayBox.getSelectedItem();
            return LocalDate.of(y, m, d);
        } catch (Exception ex) { return null; }
    }

    private String text(JTextField field) { return field.getText().trim(); }
    private String selected(JComboBox<String> box) { Object v = box.getSelectedItem(); return v == null ? "" : v.toString().trim(); }

    private void resetForm() {
        for (JTextField field : new JTextField[]{firstNameField, lastNameField, ninField, secondNinField, emailField,
                confirmEmailField, phoneField, depositField}) field.setText("");
        pinField.setText(""); confirmPinField.setText("");
        accountTypeBox.setSelectedIndex(0); branchBox.setSelectedIndex(0);
        summaryArea.setText(""); clearErrors();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApplicationForm().setVisible(true));
    }
}
