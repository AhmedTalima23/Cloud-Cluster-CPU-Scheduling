package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class TierMatrixPanel extends JPanel {

    private final DefaultTableModel model;

    public TierMatrixPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Per-Step Tier 1 / Tier 2 Matrix"));

        String[] columns = {"Time", "Running / State", "Tier 1 (Round Robin)", "Tier 2 (Priority + Aging)"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void setOutput(GuiModels.SimulationOutput output) {
        model.setRowCount(0);
        if (output == null) {
            return;
        }
        List<GuiModels.StepSnapshot> list = output.snapshots;
        for (GuiModels.StepSnapshot s : list) {
            model.addRow(new Object[]{s.time, s.activeLabel, s.tier1Summary, s.tier2Summary});
        }
    }
}
