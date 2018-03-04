package org.lunapark.dev.scaffold;

import org.lunapark.dev.scaffold.layout.EventListener;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Creator {

    private String sProjectName;
    private String sPackageName;
    private String sSourceDir = "e:\\LibGDX_template\\template\\";

    private String sReplacePackageMask = "{$PACKAGE}";
    private String sReplaceNameMask = "{$NAME}";

    private Set<String> excludeSet;

    private EventListener eventListener;
    private String targetDir;

    Creator(EventListener eventListener) {
        this.eventListener = eventListener;

    }

    void startGenerate(String sProjectName, String sPackageName, String sSource, String targetDir) {
        // Check strings for null etc
        if (sProjectName != null && sPackageName != null && sSource != null) {
            if (sProjectName.length() < 1) sProjectName = "MyGame";
            if (sPackageName.length() < 1) sPackageName = "org.gamepackage";
            this.sProjectName = sProjectName.trim();
            this.sPackageName = sPackageName.trim();
            this.sSourceDir = sSource;
            this.targetDir = targetDir;
            ArrayList<File> files = new ArrayList<>();
            listDirectory(sSourceDir, files);
            showFiles(files);
        }
    }

    private void showFiles(ArrayList<File> files) {
        createExclude(files);
        targetDir = targetDir + "\\" + sProjectName;
        for (File file : files) {
            String sAbsPath = file.getAbsolutePath();
            String ext = getFileExt(sAbsPath);
            if (ext.equals(".exclude")) {
                continue;
            }
            String newPath = sAbsPath
                    .replace(sSourceDir, targetDir)
                    .replace(sReplacePackageMask, sPackageName)
                    .replace(sReplaceNameMask, sProjectName);

            if (excludeSet.contains(ext)) {
                copyFile(file, newPath);
            } else {
                copyAndModifyFile(file, newPath);
            }
        }

        // Mission Complete
        eventListener.onEvent("New -> Import Project ...");
        eventListener.complete();
    }

    private void copyFile(File file, String newPath) {
        File fileTarget = new File(newPath);
        fileTarget.getParentFile().mkdirs();
        System.out.println(String.format("Copy: %s", fileTarget.getName()));
        try {
            Files.copy(file.toPath(), fileTarget.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createExclude(ArrayList<File> files) {
        excludeSet = new HashSet<>();
        for (File file : files) {
            if (file.getName().equals(".exclude")) {
                exclusions(file);
                break;
            }
        }
    }

    private void exclusions(File file) {
        String line;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(String.format("Ext: %s", line));
                excludeSet.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileExt(String name) {
        String extension = "";
        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i);
        }
        return extension;
    }

    private void listDirectory(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listDirectory(file.getAbsolutePath(), files);
                }
            }
        } else {
            System.out.println("Dir empty");
        }
    }

    private void copyAndModifyFile(File file, String newPath) {
        copyFile(file, newPath);
        Path path = Paths.get(newPath);
        Charset charset = StandardCharsets.UTF_8;

        String content;
        try {
            content = new String(Files.readAllBytes(path), charset);
            content = content.replace(sReplacePackageMask, sPackageName);
            content = content.replace(sReplaceNameMask, sProjectName);
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fail(String message) {
        eventListener.onEvent(message + " failed");
    }
}
