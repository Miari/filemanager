package com.boroday;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FileManagerTest {
    private String basicPath = "D:\\JavaNew\\TestFolder";
    private String pathToCopy = "D:\\JavaNew\\TestCopy";

    @Before
    public void generateTestData() throws IOException {
        String str;
        for (int i = 0; i < 5; i++) {
            str = basicPath + "\\Folder" + i;
            File foldersPath = new File(str);
            assertTrue(foldersPath.mkdir());
            for (int j = 0; j < i; j++) {
                File filesPath = new File(str + "\\" + j + ".txt");
                assertTrue(filesPath.createNewFile());
            }
        }
        InputStream inputStream = new FileInputStream("test.txt");
        OutputStream outputStream = new FileOutputStream(basicPath + "\\Folder1\\0.txt");
        try {
            int count;
            byte[] buffer = new byte[8196];
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testCalculateFiles() throws FileNotFoundException {
        assertEquals(10, FileManager.calculateFiles(basicPath));
    }

    @Test
    public void testCalculateDirs() throws FileNotFoundException {
        assertEquals(5, FileManager.calculateDirs(basicPath));
    }

    @Test
    public void testCopy() throws Exception {
        FileManager.copy(basicPath, pathToCopy);
        assertEquals(5, FileManager.calculateDirs(pathToCopy));
        assertEquals(10, FileManager.calculateFiles(pathToCopy));

        InputStream inputStream = new FileInputStream(pathToCopy + "\\Folder1\\0.txt");
        byte[] buffer = new byte[8196];
        int count = inputStream.read(buffer);
        String str = new String(buffer, 0, count);
        assertEquals("Hi, free text is here!", str);
        inputStream.close();


        assertEquals(5, FileManager.calculateDirs(basicPath));
        assertEquals(10, FileManager.calculateFiles(basicPath));
    }

    @Test
    public void testMove() throws Exception {
        FileManager.move(basicPath, pathToCopy);
        assertEquals(5, FileManager.calculateDirs(pathToCopy));
        assertEquals(10, FileManager.calculateFiles(pathToCopy));

        InputStream inputStream = new FileInputStream(pathToCopy + "\\Folder1\\0.txt" );
        byte[] buffer = new byte[8196];
        int count = inputStream.read(buffer);
        String str = new String(buffer, 0, count);
        assertEquals("Hi, free text is here!", str);
        inputStream.close();


        assertEquals(0, FileManager.calculateDirs(basicPath));
        assertEquals(0, FileManager.calculateFiles(basicPath));
    }

    @After
    public void removeTestData() {
        File fileBasicPath = new File(basicPath);
        File filePathToCopy = new File (pathToCopy);
        String str;
        if (fileBasicPath.listFiles().length > 0) {
            for (int i = 0; i < 5; i++) {
                str = basicPath + "\\Folder" + i;
                File foldersPath = new File(str);
                for (int j = 0; j < i; j++) {
                    File filesPath = new File(str + "\\" + j + ".txt");
                    assertTrue(filesPath.delete());
                }
                assertTrue(foldersPath.delete());
            }
        }
        if (filePathToCopy.listFiles().length > 0) {
            for (int i = 0; i < 5; i++) {
                str = pathToCopy + "\\Folder" + i;
                File foldersPath = new File(str);
                for (int j = 0; j < i; j++) {
                    File filesPath = new File(str + "\\" + j + ".txt");
                    assertTrue(filesPath.delete());
                }
                assertTrue(foldersPath.delete());
            }
        }
    }
}
