package com.tars;

import javax.swing.*;
import java.awt.*;

public class StyledFrame extends JFrame {

    public StyledFrame(String title) {
        super(title);

        // Rounded background simulation
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension size = getSize();
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground().darker());
                g2.fillRoundRect(0, 0, size.width, size.height, 20, 20);

                g2.setColor(getBackground());
                g2.fillRoundRect(2, 2, size.width - 4, size.height - 4, 20, 20);
            }
        });

        getContentPane().setBackground(new Color(220, 220, 220)); // Light grey as default
    }
}
