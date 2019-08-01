package com.zetcode;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This program reads HTML code from a selected webpage.
 * It utilizes an ExecutorService to do the expensive work.
 */
public class ReadWebPageEx extends JFrame {

    private JTextField field;
    private JTextArea area;

    public ReadWebPageEx() {

        initUI();
    }

    private void initUI() {

        field = new JTextField(30);
        field.addActionListener(new EnterAction());
        area = new JTextArea(10, 30);

        var scrollPane = new JScrollPane();
        scrollPane.getViewport().add(area);

        createLayout(field, scrollPane);

        setTitle("Reading web page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private class EnterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            readPage();
        }

        private void readPage() {

            var webPage = field.getText();

            ExecutorService executor
                    = Executors.newSingleThreadExecutor();
            Callable<List<String>> callable = new MyTask(webPage);

            Future<List<String>> future = executor.submit(callable);

            try {
                List<String> lines = future.get(15, TimeUnit.SECONDS);

                if (!area.getText().isEmpty()) {
                    area.setText("");
                }

                for (String line : lines) {
                    area.append(line + "\n");
                }

            } catch (InterruptedException | TimeoutException |
                    ExecutionException ex) {
                Logger.getLogger(ReadWebPageEx.class.getName()).log(
                        Level.INFO, null, ex);
            }

            executor.shutdown();
        }
    }

    private class MyTask implements Callable<List<String>> {

        private final String page;

        public MyTask(String page) {

            this.page = page;
        }

        @Override
        public List<String> call() throws IOException {

            List<String> lines = new ArrayList<>();
            var url = new URL(page);

            try (var br = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }

            return lines;
        }
    }

    private void createLayout(JComponent... arg) {

        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(arg[0], GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE)
                .addComponent(arg[1])
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0], GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(arg[1])
        );

        pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new ReadWebPageEx();
            ex.setVisible(true);
        });
    }
}
