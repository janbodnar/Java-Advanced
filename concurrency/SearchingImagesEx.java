package com.zetcode;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;

/*
 * This program initiates a background search for
 * various image files in a user's home directory.
 */

public class SearchingImagesEx extends JFrame {

    private JTextArea area;
    private JLabel lbl;
    private JButton btn;

    public SearchingImagesEx() {

        initUI();
    }

    private void initUI() {

        area = new JTextArea(20, 40);
        area.setEditable(false);
        var scrollPane = new JScrollPane(area);

        btn = new JButton("Start");
        btn.addActionListener(new StartSearchAction());

        lbl = new JLabel("Files found: ");

        createLayout(scrollPane, btn, lbl);

        setTitle("Searching for files");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup(CENTER)
                .addComponent(arg[0])
                .addGroup(gl.createSequentialGroup()
                        .addComponent(arg[1])
                        .addComponent(arg[2]))
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
                .addGroup(gl.createParallelGroup(BASELINE)
                        .addComponent(arg[1])
                        .addComponent(arg[2]))
        );

        pack();
    }

    private class StartSearchAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            doStartSearch();
        }

        private void doStartSearch() {

            btn.setEnabled(false);

            var tw = new FileTreeWalker();
            tw.execute();
        }
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var ex = new SearchingImagesEx();
            ex.setVisible(true);
        });
    }

    private class FileTreeWalker extends SwingWorker<Void, Path>
            implements FileVisitor<Path> {

        private final PathMatcher matcher;
        private int count = 0;

        public FileTreeWalker() {

            var str = "glob:**.{png,PNG,gif,GIF,jpg,jpeg,JPG,JPEG}";
            matcher = FileSystems.getDefault().getPathMatcher(str);
        }

        @Override
        protected Void doInBackground() throws IOException {

            Path path = Paths.get(System.getProperty("user.home"));
            Files.walkFileTree(path, this);
            return null;
        }

        @Override
        protected void process(List<Path> chunks) {

            for (Path path : chunks) {

                area.append(path.toString() + "\n");
                count++;

                lbl.setText(String.format("Files found: %d", count));
            }
        }

        @Override
        protected void done() {

            btn.setEnabled(true);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs){

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)  {

            if (matcher.matches(file)) {

                publish(file);
            }

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {

            return FileVisitResult.TERMINATE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

            return FileVisitResult.CONTINUE;
        }
    }
}
