package com.tars;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class StyledButtonUI extends BasicButtonUI {

    @Override
    public void installUI(JComponent component) {
        super.installUI(component);
        AbstractButton button = (AbstractButton) component;
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(5, 15, 5, 15));
    }

    @Override
    public void paint(Graphics graphics, JComponent component) {
        AbstractButton button = (AbstractButton) component;
        paintBackground(graphics, component, button.getModel().isPressed() ? 2 : 0);
        super.paint(graphics, component);
    }

    private void paintBackground(Graphics graphics, JComponent component, int yOffset) {
        Dimension size = component.getSize();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(component.getBackground().darker());
        graphics2D.fillRoundRect(0, yOffset, size.width, size.height - yOffset, 10, 10);
        graphics2D.setColor(component.getBackground());
        graphics2D.fillRoundRect(0, yOffset, size.width, size.height + yOffset - 5, 10, 10);
    }

}
