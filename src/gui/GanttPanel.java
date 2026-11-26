package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GanttPanel extends JPanel {

    private List<GuiModels.TimelineBlock> blocks = Collections.emptyList();
    private String titleText = "Gantt Chart";

    public GanttPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Execution Timeline (Gantt Chart)"));
    }

    public void setOutput(GuiModels.SimulationOutput output) {
        if (output == null) {
            this.blocks = Collections.emptyList();
            this.titleText = "Gantt Chart";
        } else {
            this.blocks = output.blocks;
            this.titleText = "Gantt Chart — " + output.schedulerLabel;
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 13f));
        g2.drawString(titleText, 16, 36);

        int legX = 16;
        int legY = 54;
        drawLegendItem(g2, legX, legY, new Color(70, 130, 180), "Process Execution");
        drawLegendItem(g2, legX + 165, legY, new Color(220, 70, 60), "Context Switch (CS)");
        drawLegendItem(g2, legX + 335, legY, new Color(215, 215, 215), "CPU Idle");

        if (blocks.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.drawString("No timeline blocks to display. Click 'Run Simulation'.", 16, 110);
            g2.dispose();
            return;
        }

        int totalEnd = blocks.get(blocks.size() - 1).endTime;
        int leftMargin = 20;
        int topMargin = 85;
        int barHeight = 64;
        int availableWidth = Math.max(400, getWidth() - leftMargin * 2);
        double unitWidth = Math.max(26.0, availableWidth / (double) Math.max(1, totalEnd));

        for (GuiModels.TimelineBlock b : blocks) {
            int x = leftMargin + (int) Math.round(b.startTime * unitWidth);
            int w = Math.max(18, (int) Math.round((b.endTime - b.startTime) * unitWidth));

            Color fill;
            if (b.isContextSwitch || "CS".equals(b.label)) {
                fill = new Color(220, 70, 60);
            } else if ("IDLE".equals(b.label)) {
                fill = new Color(215, 215, 215);
            } else {
                fill = colorForProcess(b.label);
            }

            g2.setColor(fill);
            g2.fillRoundRect(x, topMargin, w - 2, barHeight, 8, 8);

            g2.setColor(Color.DARK_GRAY);
            g2.drawRoundRect(x, topMargin, w - 2, barHeight, 8, 8);

            g2.setColor(Color.WHITE);
            if ("IDLE".equals(b.label)) {
                g2.setColor(Color.DARK_GRAY);
            }
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
            String text = b.label + " [" + b.startTime + "-" + b.endTime + "]";
            FontMetrics fm = g2.getFontMetrics();
            if (fm.stringWidth(text) < w - 6) {
                int tx = x + (w - 2 - fm.stringWidth(text)) / 2;
                int ty = topMargin + (barHeight + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(text, tx, ty);
            } else {
                g2.drawString(b.label, x + 4, topMargin + barHeight / 2 + 4);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
            g2.drawString(String.valueOf(b.startTime), x - 2, topMargin + barHeight + 18);
        }

        int endX = leftMargin + (int) Math.round(totalEnd * unitWidth);
        g2.setColor(Color.BLACK);
        g2.drawString(String.valueOf(totalEnd), endX - 4, topMargin + barHeight + 18);

        g2.dispose();
    }

    private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String label) {
        g2.setColor(color);
        g2.fillRect(x, y - 10, 14, 14);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRect(x, y - 10, 14, 14);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
        g2.drawString(label, x + 20, y + 1);
    }

    private Color colorForProcess(String label) {
        Color[] palette = {
                new Color(52, 120, 196),
                new Color(46, 148, 110),
                new Color(184, 112, 34),
                new Color(122, 82, 178),
                new Color(38, 142, 166)
        };
        int idx = Math.abs(label.hashCode()) % palette.length;
        return palette[idx];
    }
}
