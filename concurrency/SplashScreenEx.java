package com.zetcode;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;

/*
 * This program creates a splash screen before
 * displaying the main application window.
 *
 */
public class SplashScreenEx extends JWindow {

    private final int DELAY = 3000;

    public SplashScreenEx() {

        initUI();
    }

    private void initUI() {

        var cpane = (JPanel) getContentPane();

        cpane.setBorder(BorderFactory.createLineBorder(
                Color.darkGray, 3));
        cpane.setBackground(Color.white);

        var nameLabel = new JLabel("F manager", JLabel.CENTER);
        var copyLabel = new JLabel("Copyright 2019, Jan Bodnar",
                JLabel.CENTER);
        copyLabel.setFont(new Font("Serif", Font.BOLD, 13));

        createLayout(nameLabel, copyLabel);

        setSize(450, 120);
        setLocationRelativeTo(null);
        doDelay();
    }

    private void doDelay() {

        var worker = new MyWorker();
        worker.execute();
    }

    private class MyWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws InterruptedException {

            Thread.sleep(DELAY);
            return null;
        }

        @Override
        protected void done() {

            setVisible(false);
            createMainWindow();
        }
    };

    private void createLayout(Component... arg) {

        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(arg[0], GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                .addComponent(arg[1], GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addPreferredGap(RELATED, GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(arg[1])
        );

        pack();
    }

    private void createMainWindow() {

        var frame = new JFrame("Application");
        frame.setSize(350, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new SplashScreenEx();
            ex.setVisible(true);
        });
    }
}
