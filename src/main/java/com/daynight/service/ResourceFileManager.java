package com.daynight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Component
public class ResourceFileManager {

    private static Logger logger = LoggerFactory.getLogger(ResourceFileManager.class);

    public File getFile() {
        try {
            return ResourceUtils.getFile("classpath:终端明细.xls");
        } catch (FileNotFoundException e) {
            logger.error("failed to read file! exception:{}", e.getMessage());
        }
        return null;
    }

    public File getStaticsFile() {
        try {
            return ResourceUtils.getFile("classpath:终端.xls");
        } catch (FileNotFoundException e) {
            logger.error("failed to read file! exception:{}", e.getMessage());
        }
        return null;
    }

    public File getStaticsFile2() {
        try {
            return ResourceUtils.getFile("classpath:备货.xls");
        } catch (FileNotFoundException e) {
            logger.error("failed to read file! exception:{}", e.getMessage());
        }
        return null;
    }
}
