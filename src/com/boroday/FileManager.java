package com.boroday;

import java.io.*;

public class FileManager {

    public static int calculateFiles(String path) throws FileNotFoundException {
        int count = 0;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Folder " + path + " does not exists");
        }
        if (file.isFile()) {
            return 0;
        }
        count = defineQuantityOfFiles(path, count);
        return count;
    }

    public static int calculateDirs(String path) throws FileNotFoundException {
        int count = 0;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("Folder " + path + " does not exists");
        }
        if (file.isFile()) {
            return 0;
        }
        count = defineQuantityOfDirs(path, count);
        return count;
    }

    public static void copy(String from, String to) throws Exception {
        File pathFrom = new File(from);
        File pathTo = new File(to);
        if (!pathTo.exists() || !pathFrom.exists()) {
            throw new FileNotFoundException("Target or/and destination folder does not exists");
        }
        File[] array = pathFrom.listFiles();
        if (array != null) {
            if (pathFrom.listFiles().length > 0) {
                copyFolderAndFiles(from, to, false);
            }
        } else {
            System.out.println("Content of " + pathFrom + " cannot be read");
        }
    }

    public static void move(String from, String to) throws Exception {
        File pathFrom = new File(from);
        File pathTo = new File(to);
        if (!pathTo.exists() || !pathFrom.exists()) {
            throw new FileNotFoundException("Target or/and destination folder does not exists");
        }
        File[] array = pathFrom.listFiles();
        if (array != null) {
            if (pathFrom.listFiles().length > 0) {
                copyFolderAndFiles(from, to, true);
            }
        } else {
            System.out.println("Content of " + pathFrom + " cannot be read");
        }
    }

    private static int defineQuantityOfDirs(String path, int count) {
        File pathToTheDestination = new File(path);
        File[] array = pathToTheDestination.listFiles();
        if (array == null) {
            System.out.println("Content of " + path + " cannot be read");
            return count;
        } else {
            for (File file : pathToTheDestination.listFiles()) {
                if (file.isDirectory()) {
                    count++;
                    count = defineQuantityOfDirs(file.toString(), count);
                }
            }
            return count;
        }
    }

    private static int defineQuantityOfFiles(String path, int count) {
        File pathToTheDestination = new File(path);
        File[] array = pathToTheDestination.listFiles();
        if (array == null) {
            System.out.println("Content of " + path + " cannot be read");
            return count;
        } else {
            for (File file : pathToTheDestination.listFiles()) {
                if (!file.isDirectory()) {
                    count++;
                } else {
                    count = defineQuantityOfFiles(file.toString(), count);
                }
            }
            return count;
        }
    }

    private static void copyFolderAndFiles(String from, String to, boolean moveFlag) throws IOException {
        File fileFrom = new File(from);
        File[] array = fileFrom.listFiles();
        if (array != null) {
            for (File file : fileFrom.listFiles()) {
                String name = file.getName();
                File fileTo = new File(to + "\\" + name);
                if (file.isDirectory()) {
                    if (!fileTo.exists()) {
                        fileTo.mkdir();
                    }
                    copyFolderAndFiles(fileFrom + "\\" + name, to + "\\" + name, moveFlag);
                    if (moveFlag) {
                        file.delete();
                    }
                } else {
                    if (!fileTo.exists()) {
                        fileTo.createNewFile();
                    }
                    InputStream inputStream = new FileInputStream(file);
                    OutputStream outputStream = new FileOutputStream(fileTo);
                    try {
                        int count;
                        byte[] buffer = new byte[8196];
                        while ((count = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, count);
                        }
                    } finally {
                        inputStream.close();
                        outputStream.close();
                        if (moveFlag) {
                            file.delete();
                        }
                    }
                }
            }
        } else {
            System.out.println("Content of " + from + " cannot be read");
        }
    }
}


