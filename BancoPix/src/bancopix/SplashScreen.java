
package bancopix;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel logoLabel;

    public SplashScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 450;
        int height = 320;
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        JPanel content = new JPanel();
        content.setBackground(new Color(0xA4A4A4));
        content.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(0xFFFFFF));

        ImageIcon icon = new ImageIcon("src/bancopix/logo.png");
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        JLabel slogan = new JLabel("Seu dinheiro 100% seguro.");
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        slogan.setForeground(new Color(0x17FF0F));
        centerPanel.add(slogan);
        centerPanel.add(Box.createVerticalStrut(10));

        content.add(centerPanel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(width, 20));
        progressBar.setForeground(new Color(0x17FF0F));
        progressBar.setBackground(Color.LIGHT_GRAY);
        progressBar.setStringPainted(false);
        content.add(progressBar, BorderLayout.SOUTH);

        setContentPane(content);

        simulateLoading();
    }

    private void simulateLoading() {
        Timer loadingTimer = new Timer();
        loadingTimer.scheduleAtFixedRate(new TimerTask() {
            int value = 0;
            @Override
            public void run() {
                progressBar.setValue(value);
                value += 2;
                if (value > 100) {
                    loadingTimer.cancel();
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        LoginFrame login = new LoginFrame();
                        login.setVisible(true);
                    });
                }
            }
        }, 0, 50);
    }

    public static void main(String[] args) {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
    }
}
