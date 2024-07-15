package com.zetcode;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.swing.GroupLayout.Alignment.CENTER;

/*
 * This program creates a supervisor worker
 *
 */
class MyLabel extends JLabel {

    public MyLabel(String text) {
        super(text);

        setPreferredSize(new Dimension(170, 40));
        setMaximumSize(new Dimension(Short.MAX_VALUE,
                Short.MAX_VALUE));

        setHorizontalAlignment(CENTER);
        setBackground(Color.lightGray);
    }

    @Override
    public boolean isOpaque() {

        return true;
    }
}

public class SupervisorWorkerEx extends JFrame {

    private final int NUMBER_OF_LABELS = 6;
    private ArrayList<MyLabel> labels;
    private JButton btn;
    private ExecutorService executorService;

    public SupervisorWorkerEx() {

        initUI();
    }

    private void initUI() {

        executorService = Executors.newFixedThreadPool(NUMBER_OF_LABELS);

        labels = new ArrayList<>(NUMBER_OF_LABELS);

        for (int i = 0; i < NUMBER_OF_LABELS; i++) {

            labels.add(new MyLabel("0"));
        }

        btn = new JButton("Start");
        btn.addActionListener(new StartAction());

        var windowAdapter = new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                System.out.println("shutting down");
                executorService.shutdown();
            }
        };

        addWindowListener(windowAdapter);

        createLayout();

        setTitle("Supervisor worker");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createLayout() {

        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup(CENTER)

                .addComponent(labels.get(0))
                .addGap(40)
                .addComponent(labels.get(1))
                .addComponent(labels.get(2))
                .addComponent(labels.get(3))
                .addComponent(labels.get(4))
                .addComponent(labels.get(5))
                .addComponent(btn)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(labels.get(0))
                .addGap(3)
                .addComponent(labels.get(1))
                .addGap(3)
                .addComponent(labels.get(2))
                .addGap(3)
                .addComponent(labels.get(3))
                .addGap(3)
                .addComponent(labels.get(4))
                .addGap(3)
                .addComponent(labels.get(5))
                .addGap(10)
                .addComponent(btn)
        );

        pack();
    }

    private class StartAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            var cmd = e.getActionCommand();

            if (null != cmd) {

                switch (cmd) {

                    case "Restore" -> doRestore();
                    case "Start" -> doStartWorkers();
                }
            }
        }

        private void doRestore() {

            btn.setText("Start");

            for (JLabel label : labels) {

                label.setText("0");
                label.setBackground(Color.lightGray);
            }
        }

        private void doStartWorkers() {

            btn.setEnabled(false);

            var latch = new CountDownLatch(NUMBER_OF_LABELS);
//            executorService = Executors.newFixedThreadPool(NUMBER_OF_LABELS);

            for (JLabel label : labels) {

                label.setBackground(Color.white);
                executorService.execute(new Counter(label, latch));
            }

            var supervisor = new Supervisor(latch);
            supervisor.execute();
        }
    }



    private class Supervisor extends SwingWorker<Void, Void> {

        private final CountDownLatch latch;

        public Supervisor(CountDownLatch latch) {

            this.latch = latch;
        }

        @Override
        protected Void doInBackground() throws InterruptedException {

            latch.await();
            return null;
        }

        @Override
        protected void done() {

            btn.setText("Restore");
            btn.setEnabled(true);
        }
    }

    private static class Counter extends SwingWorker<Void, Integer> {

        private final JLabel label;
        private final CountDownLatch latch;

        public Counter(JLabel label, CountDownLatch latch) {

            this.label = label;
            this.latch = latch;
        }

        @Override
        protected Void doInBackground() throws InterruptedException {

            var rand = new Random();

            int delay = rand.nextInt(50) + 10;

            for (int i = 1; i <= 100; i++) {

                publish(i);
                Thread.sleep(delay);
            }

            return null;
        }

        @Override
        protected void process(List<Integer> values) {
            label.setText(values.getLast().toString());
        }

        @Override
        protected void done() {

            label.setBackground(Color.green);
            latch.countDown();
        }
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new SupervisorWorkerEx();
            ex.setVisible(true);
        });
    }
}
