package com.daynight;

import javax.swing.*;

public class MainGUI {
    public JPanel panel1;
    private JButton chooseSrcButton;
    private JButton exportSrcButton;
    private JButton 选择文件夹Button;
    private JTextField textField1;
    private JTextField textField2;
    private JButton 导出Button1;

    public MainGUI() {
        chooseSrcButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int rVal = fileChooser.showOpenDialog(chooseSrcButton);
            if (rVal == JFileChooser.APPROVE_OPTION) {
                textField1.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {

            }
        });
    }

}
