package com.daynight;

import com.daynight.service.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

/**
 * @author donghonghua
 * @date 2018/11/26
 */
public class MainFrame extends JFrame implements ActionListener {

    private Logger logger = LoggerFactory.getLogger(MainFrame.class);

    private ExcelService excelService;

    /**
     * 选项卡布局
     */
    private JTabbedPane tabPane = new JTabbedPane();

    private Container con = new Container();
    private JButton chooseSrcButton = new JButton("选择终端文件");
    private JButton chooseStatistics = new JButton("选择统计文件夹");
    JTextField text1 = new JTextField();
    JTextField text2 = new JTextField();
    JButton exportSrc = new JButton("导出");
    JButton exportStatistics = new JButton("导出");
    JFileChooser fileChooser = new JFileChooser();
    JLabel logLabel = new JLabel("日志：");
    JTextArea textArea = new JTextArea();
    public MainFrame() {

        super("文件导出");

        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        // 设定窗口出现位置
        setLocation(new Point((int) (lx / 2) - 280, (int) (ly / 2) - 200));
        // 设定窗口大小
        setSize(560, 400);
        // 设置布局

        setContentPane(tabPane);
        chooseSrcButton.setBounds(20, 20, 130, 25);
        text1.setBounds(160, 20, 300, 25);
        exportSrc.setBounds(470, 20, 60, 25);
        chooseStatistics.setBounds(20, 60, 130, 25);
        text2.setBounds(160, 60, 300, 25);
        exportStatistics.setBounds(470, 60, 60, 25);
        logLabel.setBounds( 20, 90, 100, 25);
        textArea.setBounds(20, 120, 500, 200);

        chooseStatistics.addActionListener(this);
        chooseSrcButton.addActionListener(this);
        exportSrc.addActionListener(this);
        exportStatistics.addActionListener(this);

        con.add(chooseSrcButton);
        con.add(text1);
        con.add(exportSrc);
        con.add(chooseStatistics);
        con.add(text2);
        con.add(exportStatistics);
        con.add(logLabel);
        con.add(textArea);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabPane.add("统计", con);

    }

    /**
     * 时间监听的方法
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(chooseSrcButton)) {

            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int state = fileChooser.showOpenDialog(null);
            if (state == JFileChooser.SAVE_DIALOG) {
                return;
            } else {
                File file = fileChooser.getSelectedFile();
                text1.setText(file.getAbsolutePath());
            }
        } else if (e.getSource().equals(chooseStatistics)) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = fileChooser.showOpenDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File directory = fileChooser.getSelectedFile();
                text2.setText(directory.getAbsolutePath());
            }
        } else if (e.getSource().equals(exportSrc)) {

            int state = fileChooser.showSaveDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getName();
                if (!(fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"))) {
                    JOptionPane.showMessageDialog(null, "选择的文件名后缀不正确!", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (StringUtils.isEmpty(text1.getText())) {
                    JOptionPane.showMessageDialog(null, "还没有选择终端文件!", "提示", JOptionPane.ERROR_MESSAGE);
                } else {
                    excelService.handleSource(new File(text1.getText()), "终端", file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "导出成功", "提示", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } else if (e.getSource().equals(exportStatistics)) {
            int state = fileChooser.showSaveDialog(null);
            if (state == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String fileName = file.getName();
                if (!(fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"))) {
                    JOptionPane.showMessageDialog(null, "选择的文件名后缀不正确!", "提示", JOptionPane.ERROR_MESSAGE);
                } else if (StringUtils.isEmpty(text2.getText())) {
                    JOptionPane.showMessageDialog(null, "还没有选择文件夹!", "提示", JOptionPane.ERROR_MESSAGE);
                } else {
                    excelService.handleStatics(Arrays.asList(new File(text2.getText()).listFiles()), file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "导出成功", "提示", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    public void setExcelService(ExcelService excelService) {
        this.excelService = excelService;
    }
}