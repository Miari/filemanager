package com.boroday.filemanagertest;

import com.boroday.filemanager.FileManager;
import org.junit.*;

import java.io.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class FileManagerTest {
    private String basicPath = "TestFolder";
    private String pathToCopy = "TestCopy";

    @Before
    public void generateTestData() throws IOException {
        new File("TestFolder/Folder1").mkdirs();
        new File("TestFolder/Folder2/Folder_Level2_1").mkdirs();
        new File("TestFolder/Folder2/Folder_Level2_1/Folder_Level3_1").mkdir();
        new File("TestFolder/Folder2/Folder_Level2_1/Folder_Level3_2").mkdir();
        new File("TestFolder/Folder2/Folder_Level2_2").mkdir();
        new File("TestFolder/Folder2/Folder_Level2_2/file1.txt").createNewFile();
        try (OutputStream outputStream = new FileOutputStream("TestFolder/Folder2/Folder_Level2_2/file1.txt")) {
            outputStream.write("Hi, free text is here!".getBytes());
        } // в одну строку, как в ревью, сделать не могу, т.е. тогда outputStream остается незакрытым и я не могу удалить этот файл в  @AfterClass
        new File("TestFolder/Folder3").mkdir();
        new File("TestFolder/Folder3/file2.txt").createNewFile();
        new File("TestFolder/Folder3/file3.txt").createNewFile();
    }

    @Test
    public void testCalculateFiles() throws FileNotFoundException {
        assertEquals(3, FileManager.calculateFiles(basicPath));
    }

    @Test
    public void testCalculateDirs() throws FileNotFoundException {
        assertEquals(7, FileManager.calculateDirs(basicPath));
    }

    @Test
    public void testCopy() throws Exception {
        FileManager.copy(basicPath, pathToCopy);
        assertEquals(7, FileManager.calculateDirs(pathToCopy));
        assertEquals(3, FileManager.calculateFiles(pathToCopy));

        try (InputStream inputStream = new FileInputStream(pathToCopy + "/Folder2/Folder_Level2_2/file1.txt")) {
            byte[] buffer = new byte[8196];
            int count = inputStream.read(buffer);
            String str = new String(buffer, 0, count);
            assertEquals("Hi, free text is here!", str);
        }

        assertEquals(7, FileManager.calculateDirs(basicPath));
        assertEquals(3, FileManager.calculateFiles(basicPath));
    }

    @Test
    public void testMove() throws Exception {
        FileManager.move(basicPath, pathToCopy);
        assertEquals(7, FileManager.calculateDirs(pathToCopy));
        assertEquals(3, FileManager.calculateFiles(pathToCopy));

        try (InputStream inputStream = new FileInputStream(pathToCopy + "/Folder2/Folder_Level2_2/file1.txt")) {
            byte[] buffer = new byte[8196];
            int count = inputStream.read(buffer);
            String str = new String(buffer, 0, count);
            assertEquals("Hi, free text is here!", str);
            inputStream.close();
        }

        assertFalse(new File(basicPath).exists());
    }

        @After
        public void removeTestData () {
            new File("TestFolder/Folder1").delete();
            new File("TestFolder/Folder2/Folder_Level2_1/Folder_Level3_1").delete();
            new File("TestFolder/Folder2/Folder_Level2_1/Folder_Level3_2").delete();
            new File("TestFolder/Folder2/Folder_Level2_1").delete();
            new File("TestFolder/Folder2/Folder_Level2_2/file1.txt").delete();
            new File("TestFolder/Folder2/Folder_Level2_2").delete();
            new File("TestFolder/Folder2").delete();
            new File("TestFolder/Folder3/file2.txt").delete();
            new File("TestFolder/Folder3/file3.txt").delete();
            new File("TestFolder/Folder3").delete();
            new File("TestFolder").delete();

            new File("TestCopy/Folder1").delete();
            new File("TestCopy/Folder2/Folder_Level2_1/Folder_Level3_1").delete();
            new File("TestCopy/Folder2/Folder_Level2_1/Folder_Level3_2").delete();
            new File("TestCopy/Folder2/Folder_Level2_1").delete();
            new File("TestCopy/Folder2/Folder_Level2_2/file1.txt").delete();
            new File("TestCopy/Folder2/Folder_Level2_2").delete();
            new File("TestCopy/Folder2").delete();
            new File("TestCopy/Folder3/file2.txt").delete();
            new File("TestCopy/Folder3/file3.txt").delete();
            new File("TestCopy/Folder3").delete();
            new File("TestCopy").delete();
        }
    }
