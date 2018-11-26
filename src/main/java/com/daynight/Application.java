package com.daynight;


import com.daynight.service.ExcelService;
import com.daynight.service.ResourceFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ExcelService excelService;

    @Autowired
    private ResourceFileManager resourceFileManager;
    @Autowired
    private ApplicationContext applicationContext;

    private static MainFrame mainFrame;

    public static void main(String[] args) {
        mainFrame = new MainFrame();
        SpringApplication.run(Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        mainFrame.setExcelService(excelService);
    }
}


