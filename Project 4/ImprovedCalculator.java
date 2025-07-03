import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class ImprovedCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private double num1 = 0, num2 = 0, result = 0;
    private char operator = '\0';
    private boolean isOperatorPressed = false;
    private boolean hasDecimal = false;

    public ImprovedCalculator() {
        setTitle("Improved Calculator");
        setSize(350, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 10, 10));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "⌫"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Tahoma", Font.BOLD, 18));

            // UI color enhancements
            if (text.matches("[+\\-*/]")) {

                btn.setBackground(new Color(255, 204, 153)); // operator color
            } else if (text.equals("=")) {
                btn.setBackground(new Color(144, 238, 144)); // equals color
            } else if (text.equals("C")) {
                btn.setBackground(new Color(255, 160, 122)); // clear color
            } else if (text.equals("⌫")) {
                btn.setBackground(new Color(224, 224, 224));
            } else {
                btn.setBackground(new Color(240, 240, 240));
            }

            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        add(buttonPanel, BorderLayout.CENTER);
        addKeyListener(new KeyHandler());
        setFocusable(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("\\d")) {
            if (isOperatorPressed) {
                display.setText(command);
                isOperatorPressed = false;
                hasDecimal = false;
            } else {
                display.setText(display.getText() + command);
            }
        } else if (command.equals(".")) {
            if (!hasDecimal) {
                display.setText(display.getText() + ".");
                hasDecimal = true;
            }
        } else if (command.equals("C")) {
            clearAll();
        } else if (command.equals("⌫")) {
            String current = display.getText();
            if (!current.isEmpty()) {
                display.setText(current.substring(0, current.length() - 1));
                if (!display.getText().contains(".")) {
                    hasDecimal = false;
                }
            }
        } else if (command.equals("=")) {
            calculate();
        } else if (command.matches("[+\\-*/]")) {

            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
                operator = command.charAt(0);
                isOperatorPressed = true;
            }
        }
    }

    private void clearAll() {
        display.setText("");
        num1 = num2 = result = 0;
        operator = '\0';
        isOperatorPressed = false;
        hasDecimal = false;
    }

    private void calculate() {
        try {
            num2 = Double.parseDouble(display.getText());
            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if (num2 == 0) {
                        display.setText("Error: /0");
                        return;
                    }
                    result = num1 / num2;
                    break;
                default:
                    display.setText("Invalid");
                    return;
            }
            DecimalFormat df = new DecimalFormat("#.###");
            display.setText(df.format(result));
            isOperatorPressed = true;
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    class KeyHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            int code = e.getKeyCode();

            if (Character.isDigit(key)) {
                display.setText(display.getText() + key);
            } else if (key == '.') {
                if (!hasDecimal) {
                    display.setText(display.getText() + ".");
                    hasDecimal = true;
                }
            } else if (key == '+' || key == '-' || key == '*' || key == '/') {
                if (!display.getText().isEmpty()) {
                    num1 = Double.parseDouble(display.getText());
                    operator = key;
                    isOperatorPressed = true;
                    hasDecimal = false;
                }
            } else if (code == KeyEvent.VK_ENTER) {
                calculate();
            } else if (code == KeyEvent.VK_BACK_SPACE) {
                String current = display.getText();
                if (!current.isEmpty()) {
                    display.setText(current.substring(0, current.length() - 1));
                    if (!display.getText().contains(".")) {
                        hasDecimal = false;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImprovedCalculator().setVisible(true));
    }
}
