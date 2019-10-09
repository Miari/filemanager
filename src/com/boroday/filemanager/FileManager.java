package com.boroday.filemanager;

import java.io.*;

public class FileManager {

    public static int calculateFiles(String path) throws FileNotFoundException {
        int count = 0;
        File pathToTheDestination = new File(path);
        if (!pathToTheDestination.exists()) {
            throw new FileNotFoundException("Folder " + path + " does not exists, calculateFiles method cannot be applied");
        }
        File[] files = pathToTheDestination.listFiles();
        if (files == null) {
            System.err.println("Content of " + path + " cannot be read, calculateFiles method cannot be applied to the " + pathToTheDestination);
            return count;
        }
        for (File innerFile : files) {
            if (!innerFile.isDirectory()) {
                count++;
            } else {
                count += calculateFiles(innerFile.toString());
            }
        }
        return count;
    }

    public static int calculateDirs(String path) throws FileNotFoundException {
        int count = 0;
        File pathToTheDestination = new File(path);
        if (!pathToTheDestination.exists()) {
            throw new FileNotFoundException("Folder " + path + " does not exists, calculateDirs method cannot be applied");
        }
        File[] files = pathToTheDestination.listFiles();
        if (files == null) {
            System.err.println("Content of " + path + " cannot be read, calculateDirs method cannot be applied to the " + pathToTheDestination);
            return count;
        }
        for (File innerFile : files) {
            if (innerFile.isDirectory()) {
                count++;
                count += calculateDirs(innerFile.toString());
            }
        }
        return count;
    }

    public static void copy(String from, String to) throws IOException {
        File pathFrom = new File(from);
        File pathTo = new File(to);
        if (!pathTo.exists()){
            if (!pathTo.mkdir()){
                System.err.println("Directory " + pathTo + " was not created in copy method");
            }
        }
        if (!pathFrom.exists()) {
            throw new FileNotFoundException("Target or/and destination folder does not exists, copy method cannot be applied");
        }

        File[] files = pathFrom.listFiles();
        if (files != null) { //path.listFiles возвращает null, если нет доступа к папке, список файлов которой мы пытаемся получить. Например, C:\Windows\Configuration
            for (File file : files) {
                if (file.isDirectory()) {
                    File newFile = new File(pathTo, file.getName());
                    if(!newFile.mkdir()){
                        System.err.println("Directory " + newFile + " was not created in copy method");
                    }
                    copy(file.toString(), newFile.toString());
                } else {
                    copyFiles(file, new File(pathTo, file.getName()));
                }
            }
        } else {
            System.err.println("Content of " + pathFrom + " cannot be read in copy method");
        }
    }

    private static void copyFiles(File from, File to) throws IOException {
        try (InputStream inputStream = new FileInputStream(from);  OutputStream outputStream = new FileOutputStream(to)) {
            int count;
            byte[] buffer = new byte[8196];
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        }
    }

    public static void move(String from, String to) throws Exception {
        File pathFrom = new File(from);
        File pathTo = new File(to);
        if (!pathFrom.exists()) {
            throw new FileNotFoundException("Target or/and destination folder does not exists, move method cannot be applied");
        }
        if (!pathFrom.renameTo(pathTo)) {
            System.err.println("Folder's structure was not moved in moved method");
        }
    }
}


