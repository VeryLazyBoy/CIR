package com.cir.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class FileUtility {
    private static boolean hasFile(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    return true;
                }
            }
        }
        return false;
    }
    static int  i = 0;
    public static ArrayList<File> getAllDirsContainingFiles(File dir) {
        ArrayList<File> results = new ArrayList<>();
        System.out.println(++i);
        if (dir.isDirectory()) {
            if (hasFile(dir)) {
                results.add(dir);
            }
            for (File f : dir.listFiles()) {
                results.addAll(getAllDirsContainingFiles(f));
            }
        }
        System.out.println("blalb");
        return results;
    }

    public static ArrayList<String> getAbsolutePath(ArrayList<File> files) {
        ArrayList<String> results = new ArrayList<String>();
        for (File f : files) {
            results.add(f.getAbsolutePath());
        }
        return results;
    }

    private static boolean isXmlFile(File entry) {
        String extension = "";
        String fileName = entry.getName();

        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i + 1);
        }

        return extension.equalsIgnoreCase("xml");
    }

    public static Collection<File> listXMLFileTree(File dir) {
        Collection<File> fileTree = new ArrayList<>();

        if (dir == null || dir.listFiles() == null) {
            return fileTree;
        }

        for (File entry : dir.listFiles()) {
            if (entry.isFile() && isXmlFile(entry)) {
                fileTree.add(entry);
            } else {
                fileTree.addAll(listXMLFileTree(entry));
            }
        }

        return fileTree;
    }
}
