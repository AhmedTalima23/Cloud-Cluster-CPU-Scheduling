package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class MetricsPanel extends JPanel {

    private final DefaultTableModel model;
    private final JLabel avgWtLabel;
    private final JLabel avgTatLabel;
    private final JLabel cpuUtilLabel;

    public MetricsPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Scheduling Metrics & Averages"));

        String[] columns = {"Process", "Arrival", "Burst", "Finish", "Waiting Time (WT)", "Turnaround Time (TAT)", "Response Time (RT)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 8));
        avgWtLabel = new JLabel("Average Waiting Time: -");
        avgTatLabel = new JLabel("Average Turnaround Time: -");
        cpuUtilLabel = new JLabel("CPU Utilization: -");

        Font boldFont = avgWtLabel.getFont().deriveFont(Font.BOLD, 13f);
        avgWtLabel.setFont(boldFont);
        avgTatLabel.setFont(boldFont);
        cpuUtilLabel.setFont(boldFont);

        summaryPanel.add(avgWtLabel);
        summaryPanel.add(avgTatLabel);
        summaryPanel.add(cpuUtilLabel);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    public void setOutput(GuiModels.SimulationOutput output) {
        model.setRowCount(0);
        if (output == null) {
            avgWtLabel.setText("Average Waiting Time: -");
            avgTatLabel.setText("Average Turnaround Time: -");
            cpuUtilLabel.setText("CPU Utilization: -");
            return;
        }

        for (GuiModels.ProcessMetricRow r : output.processRows) {
            model.addRow(new Object[]{
                    r.processId,
                    r.arrival,
                    r.burst,
                    r.finish,
                    r.waitingTime,
                    r.turnaroundTime,
                    r.responseTime
            });
        }

        avgWtLabel.setText(String.format(Locale.US, "Average Waiting Time: %.2f", output.avgWaitingTime));
        avgTatLabel.setText(String.format(Locale.US, "Average Turnaround Time: %.2f", output.avgTurnaroundTime));
        cpuUtilLabel.setText(String.format(Locale.US, "CPU Utilization: %.2f%%", output.cpuUtilization));
    }
}
