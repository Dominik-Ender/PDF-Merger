package com.tars;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StyledScrollPaneUI extends BasicScrollPaneUI {

    private final int arc;
    private final Color outer;
    private final Color inner;

    public StyledScrollPaneUI() {
        this(15, null, null);
    }

    public StyledScrollPaneUI(int arc, Color outer, Color inner) {
        this.arc = Math.max(0, arc);
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        int w = c.getWidth();
        int h = c.getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color base = c.getBackground() != null ? c.getBackground() : UIManager.getColor("Panel.background");
            Color outerColor = this.outer != null ? this.outer : base.darker();
            Color innerColor = this.inner != null ? this.inner : base;

            // outer + inner rounded rects
            g2.setColor(outerColor);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            g2.setColor(innerColor);
            g2.fillRoundRect(2, 2, Math.max(0, w - 4), Math.max(0, h - 4), arc, arc);

            // clip to inner rounded rect so children are clipped to the same shape
            Shape oldClip = g2.getClip();
            RoundRectangle2D clip = new RoundRectangle2D.Double(2, 2, Math.max(0, w - 4), Math.max(0, h - 4), arc, arc);
            g2.setClip(clip);

            // paint components (viewport, scrollbars etc.)
            super.paint(g2, c);

            // restore clip
            g2.setClip(oldClip);
        } finally {
            g2.dispose();
        }
    }
}
