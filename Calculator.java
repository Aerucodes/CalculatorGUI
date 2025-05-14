import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private JLabel operationLabel; // To show the current operation
    private StringBuilder currentInput = new StringBuilder();
    private double result = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    // Windows 11 Calculator Reference Colors
    private final Color BG_COLOR = new Color(30, 32, 36, 220); // dark, semi-transparent
    private final Color BTN_COLOR = new Color(45, 47, 51, 230); // slightly lighter, semi-transparent
    private final Color BTN_OP_COLOR = new Color(60, 63, 70, 230); // for operators
    private final Color BTN_EQ_COLOR = new Color(0, 120, 215, 255); // blue for '='
    private final Color BTN_EQ_TEXT_COLOR = Color.WHITE;
    private final Color BTN_TEXT_COLOR = Color.WHITE;
    private final Color BTN_OP_TEXT_COLOR = Color.WHITE;
    private final Font BTN_FONT = new Font("Segoe UI", Font.PLAIN, 22);
    private final Font DISP_FONT = new Font("Segoe UI", Font.BOLD, 38);
    private final Font OP_FONT = new Font("Segoe UI", Font.PLAIN, 18);

    public Calculator() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(0, 0));

        // Operation label
        operationLabel = new JLabel(" ");
        operationLabel.setFont(OP_FONT);
        operationLabel.setForeground(new Color(180, 180, 180));
        operationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        operationLabel.setBorder(new EmptyBorder(8, 16, 0, 16));
        operationLabel.setBackground(BG_COLOR);

        // Display field
        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(DISP_FONT);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(new Color(30, 32, 36, 220));
        display.setForeground(BTN_TEXT_COLOR);
        display.setBorder(new EmptyBorder(8, 16, 24, 16));

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(BG_COLOR);
        displayPanel.add(operationLabel, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.CENTER);
        add(displayPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 4, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(BG_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] buttons = {
            "C", "", "", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", "%"
        };

        for (String text : buttons) {
            if (text.isEmpty()) {
                panel.add(new JLabel());
                continue;
            }
            final String btnText = text;

            JButton tempBtn = new JButton(btnText);
            tempBtn.setFont(BTN_FONT);
            tempBtn.setFocusPainted(false);
            tempBtn.setOpaque(false);
            tempBtn.setContentAreaFilled(false);
            tempBtn.setBorder(new RoundedBorder(22));
            tempBtn.setForeground(BTN_TEXT_COLOR);

            if (btnText.equals("=")) {
                tempBtn.setBackground(BTN_EQ_COLOR);
                tempBtn.setForeground(BTN_EQ_TEXT_COLOR);
            } else if ("+-*/%".contains(btnText)) {
                tempBtn.setBackground(BTN_OP_COLOR);
                tempBtn.setForeground(BTN_OP_TEXT_COLOR);
            } else if (btnText.equals("C")) {
                tempBtn.setBackground(new Color(200, 50, 50, 230));
                tempBtn.setForeground(Color.WHITE);
            } else {
                tempBtn.setBackground(BTN_COLOR);
                tempBtn.setForeground(BTN_TEXT_COLOR);
            }

            final RoundedButton btn = new RoundedButton(
                tempBtn.getText(),
                tempBtn.getBackground(),
                tempBtn.getForeground(),
                22,
                BTN_FONT
            );

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (btnText.equals("=")) {
                        btn.setBackground(new Color(0, 100, 180, 255));
                    } else if ("+-*/%".contains(btnText)) {
                        btn.setBackground(new Color(80, 90, 110, 255));
                    } else if (btnText.equals("C")) {
                        btn.setBackground(new Color(220, 80, 80, 255));
                    } else {
                        btn.setBackground(new Color(60, 63, 70, 255));
                    }
                    btn.repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (btnText.equals("=")) {
                        btn.setBackground(BTN_EQ_COLOR);
                    } else if ("+-*/%".contains(btnText)) {
                        btn.setBackground(BTN_OP_COLOR);
                    } else if (btnText.equals("C")) {
                        btn.setBackground(new Color(200, 50, 50, 230));
                    } else {
                        btn.setBackground(BTN_COLOR);
                    }
                    btn.repaint();
                }
            });

            btn.addActionListener(this);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("0123456789.".contains(cmd)) {
            if (startNewNumber) {
                currentInput.setLength(0);
                startNewNumber = false;
            }
            if (cmd.equals(".") && currentInput.toString().contains(".")) return;
            currentInput.append(cmd);
            display.setText(currentInput.toString());
            updateOperationLabel();
        } else if ("+-*/%".contains(cmd)) {
            if (currentInput.length() > 0) {
                calculate();
            }
            operator = cmd;
            startNewNumber = true;
            updateOperationLabel();
        } else if (cmd.equals("=")) {
            calculate();
            operator = "";
            startNewNumber = true;
            updateOperationLabel();
        } else if (cmd.equals("C")) {
            result = 0;
            operator = "";
            currentInput.setLength(0);
            display.setText("0");
            startNewNumber = true;
            operationLabel.setText(" ");
        }
    }

    private void calculate() {
        double input = currentInput.length() > 0 ? Double.parseDouble(currentInput.toString()) : result;
        switch (operator) {
            case "+": result += input; break;
            case "-": result -= input; break;
            case "*": result *= input; break;
            case "/": result = input == 0 ? 0 : result / input; break;
            case "%": result = result % input; break;
            default: result = input; break;
        }
        display.setText((result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result));
        currentInput.setLength(0);
        currentInput.append(result);
    }

    private void updateOperationLabel() {
        String op = "";
        switch (operator) {
            case "+": op = "+"; break;
            case "-": op = "-"; break;
            case "*": op = "×"; break;
            case "/": op = "÷"; break;
            case "%": op = "%"; break;
        }
        if (!operator.isEmpty() && !startNewNumber) {
            operationLabel.setText(display.getText() + " " + op);
        } else if (!operator.isEmpty() && startNewNumber) {
            operationLabel.setText(result + " " + op);
        } else {
            operationLabel.setText(" ");
        }
    }

    // Custom rounded border for buttons (not used directly, see RoundedButton)
    static class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius); }
        public boolean isBorderOpaque() { return false; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(80, 80, 80, 80));
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }

    // Custom rounded button with background and foreground color
    static class RoundedButton extends JButton {
        private Color bg;
        private Color fg;
        private int radius;
        public RoundedButton(String text, Color bg, Color fg, int radius, Font font) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            this.radius = radius;
            setFont(font);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(fg);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Draw the button text centered
            FontMetrics fm = g2.getFontMetrics(getFont());
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - 3;
            g2.setColor(fg);
            g2.setFont(getFont());
            g2.drawString(text, x, y);

            g2.dispose();
        }
        @Override
        public void setBackground(Color bg) {
            this.bg = bg;
            repaint();
        }
        @Override
        public void setForeground(Color fg) {
            this.fg = fg;
            super.setForeground(fg);
            repaint();
        }
        @Override
        public boolean isContentAreaFilled() {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }
}