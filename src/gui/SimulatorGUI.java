package gui;

import acs.ACSScheduler;
import core.Process;
import core.Scheduler;
import core.SimulationResult;
import core.Snapshot;
import sjf.PreemptiveSJF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulatorGUI extends JFrame {

    private final DefaultTableModel processTableModel;
    private final JTable processTable;
    private final JComboBox<String> schedulerCombo;
    private final JSpinner quantumSpinner;
    private final JSpinner csSpinner;

    private final GanttPanel ganttPanel;
    private final TierMatrixPanel tierMatrixPanel;
    private final MetricsPanel metricsPanel;

    private final Scheduler acsScheduler = new ACSScheduler();
    private final Scheduler sjfScheduler = new PreemptiveSJF();

    public SimulatorGUI() {
        super("CS241 - Cloud-Cluster CPU Scheduler Simulator (Swing GUI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 820);
        setLocationRelativeTo(null);

        String[] columns = {"Process ID", "Arrival Time", "Burst Time", "Initial Priority"};
        processTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        processTable = new JTable(processTableModel);
        processTable.setRowHeight(24);
        processTable.setFillsViewportHeight(true);

        schedulerCombo = new JComboBox<>(new String[]{
                "ACS (Adaptive Container Scheduler)",
                "Preemptive SJF"
        });
        quantumSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 100, 1));
        csSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));

        ganttPanel = new GanttPanel();
        tierMatrixPanel = new TierMatrixPanel();
        metricsPanel = new MetricsPanel();

        initLayout();
        loadTestCase1();
        runSelectedSimulation();
    }

    private void initLayout() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSplit.setResizeWeight(0.55);
        topSplit.setDividerLocation(660);

        JPanel leftInputPanel = new JPanel(new BorderLayout(6, 6));
        leftInputPanel.setBorder(BorderFactory.createTitledBorder("Process Input Table"));
        leftInputPanel.add(new JScrollPane(processTable), BorderLayout.CENTER);

        JPanel tableButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        JButton addRowBtn = new JButton("+ Add Process");
        JButton removeRowBtn = new JButton("- Remove Selected");
        JButton tc1Btn = new JButton("Load Test Case 1");
        JButton tc2Btn = new JButton("Load Test Case 2");

        addRowBtn.addActionListener(e -> {
            int nextIdx = processTableModel.getRowCount() + 1;
            processTableModel.addRow(new Object[]{"P" + nextIdx, 0, 4, 2});
        });
        removeRowBtn.addActionListener(e -> {
            int row = processTable.getSelectedRow();
            if (row >= 0) {
                processTableModel.removeRow(row);
            }
        });
        tc1Btn.addActionListener(e -> loadTestCase1());
        tc2Btn.addActionListener(e -> loadTestCase2());

        tableButtons.add(addRowBtn);
        tableButtons.add(removeRowBtn);
        tableButtons.add(tc1Btn);
        tableButtons.add(tc2Btn);
        leftInputPanel.add(tableButtons, BorderLayout.SOUTH);

        JPanel rightConfigPanel = new JPanel(new GridBagLayout());
        rightConfigPanel.setBorder(BorderFactory.createTitledBorder("Scheduler Parameters & Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        rightConfigPanel.add(new JLabel("Scheduler Algorithm:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        rightConfigPanel.add(schedulerCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        rightConfigPanel.add(new JLabel("Time Quantum (Q):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        rightConfigPanel.add(quantumSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        rightConfigPanel.add(new JLabel("Context Switch (CS):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        rightConfigPanel.add(csSpinner, gbc);

        JButton runButton = new JButton("Run Simulation");
        runButton.setFont(runButton.getFont().deriveFont(Font.BOLD, 14f));
        runButton.addActionListener(e -> runSelectedSimulation());

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 6, 8);
        rightConfigPanel.add(runButton, gbc);

        topSplit.setLeftComponent(leftInputPanel);
        topSplit.setRightComponent(rightConfigPanel);
        topSplit.setPreferredSize(new Dimension(1200, 220));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gantt Chart & Context Switches", ganttPanel);
        tabs.addTab("Tier 1 / Tier 2 Matrix", tierMatrixPanel);
        tabs.addTab("Metrics & Averages", metricsPanel);

        add(topSplit, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private void loadTestCase1() {
        processTableModel.setRowCount(0);
        processTableModel.addRow(new Object[]{"P1", 0, 4, 2});
        processTableModel.addRow(new Object[]{"P2", 1, 7, 3});
        processTableModel.addRow(new Object[]{"P3", 3, 2, 1});
        quantumSpinner.setValue(3);
        csSpinner.setValue(0);
    }

    private void loadTestCase2() {
        processTableModel.setRowCount(0);
        processTableModel.addRow(new Object[]{"P1", 0, 5, 4});
        processTableModel.addRow(new Object[]{"P2", 1, 6, 2});
        processTableModel.addRow(new Object[]{"P3", 4, 4, 3});
        quantumSpinner.setValue(3);
        csSpinner.setValue(1);
    }

    private List<Process> readProcessesFromTable() {
        if (processTable.isEditing() && processTable.getCellEditor() != null) {
            processTable.getCellEditor().stopCellEditing();
        }
        List<Process> list = new ArrayList<>();
        for (int i = 0; i < processTableModel.getRowCount(); i++) {
            String id = String.valueOf(processTableModel.getValueAt(i, 0)).trim();
            int arrival = Integer.parseInt(String.valueOf(processTableModel.getValueAt(i, 1)).trim());
            int burst = Integer.parseInt(String.valueOf(processTableModel.getValueAt(i, 2)).trim());
            int priority = Integer.parseInt(String.valueOf(processTableModel.getValueAt(i, 3)).trim());
            list.add(new Process(id, arrival, burst, priority));
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Please enter at least one process.");
        }
        return list;
    }

    private void runSelectedSimulation() {
        try {
            List<Process> processes = readProcessesFromTable();
            String algo = (String) schedulerCombo.getSelectedItem();
            int q = (Integer) quantumSpinner.getValue();
            int cs = (Integer) csSpinner.getValue();

            Scheduler scheduler = (algo != null && algo.startsWith("Preemptive SJF"))
                    ? sjfScheduler
                    : acsScheduler;

            SimulationResult result = scheduler.run(processes, q, cs);
            GuiModels.SimulationOutput output = convertResult(result);

            ganttPanel.setOutput(output);
            tierMatrixPanel.setOutput(output);
            metricsPanel.setOutput(output);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Simulation error: " + ex.getMessage(),
                    "Input / Execution Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private GuiModels.SimulationOutput convertResult(SimulationResult result) {
        List<GuiModels.TimelineBlock> blocks = new ArrayList<>();
        for (SimulationResult.TimelineSegment seg : result.getTimeline()) {
            blocks.add(new GuiModels.TimelineBlock(
                    seg.getLabel(),
                    seg.getStartTime(),
                    seg.getEndTime(),
                    seg.isContextSwitch()
            ));
        }

        List<GuiModels.StepSnapshot> snapshots = new ArrayList<>();
        for (Snapshot s : result.getSnapshots()) {
            String label = s.getNote().isEmpty()
                    ? s.getRunningProcess()
                    : s.getRunningProcess() + " (" + s.getNote() + ")";
            snapshots.add(new GuiModels.StepSnapshot(
                    s.getTime(),
                    label,
                    s.formatTier1().replaceFirst("^Tier 1: ", ""),
                    s.formatTier2().replaceFirst("^Tier 2: ", "")
            ));
        }

        List<GuiModels.ProcessMetricRow> rows = new ArrayList<>();
        for (Process p : result.getProcesses()) {
            rows.add(new GuiModels.ProcessMetricRow(
                    p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getFinishTime(),
                    p.getWaitingTime(),
                    p.getTurnaroundTime(),
                    p.getResponseTime()
            ));
        }

        String title = result.getSchedulerName() + " (Q=" + result.getQuantum() + ", CS=" + result.getContextSwitch() + ")";
        return new GuiModels.SimulationOutput(
                title,
                blocks,
                snapshots,
                rows,
                result.getAvgWaitingTime(),
                result.getAvgTurnaroundTime(),
                result.getCpuUtilization()
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulatorGUI gui = new SimulatorGUI();
            gui.setVisible(true);
        });
    }
}
