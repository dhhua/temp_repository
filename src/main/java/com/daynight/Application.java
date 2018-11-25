package com.daynight;


import com.daynight.service.ExcelService;
import com.daynight.service.ResourceFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ResourceFileManager resourceFileManager;

    private static MainGUI mainGUI;

    public static void main(String[] args) {
        mainGUI = new MainGUI();
        SpringApplication.run(Application.class, args);

    }

    public void run(String... args) throws Exception {
//        excelService.handleSource(resourceFileManager.getFile(), "核对客户 (2)");
        JFrame frame = new JFrame("ui");
        frame.setContentPane(mainGUI.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //获取屏幕获取工具
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension screenSize = tool.getScreenSize();
        int x = (int) screenSize.getWidth();
        int y = (int) screenSize.getHeight();
        frame.setBounds(x - 1200 >> 1, (y - 900 >> 1) - 32, 1200, 900);

        excelService.handleStatics(Arrays.asList(resourceFileManager.getStaticsFile(), resourceFileManager.getStaticsFile2()));
    }


}
