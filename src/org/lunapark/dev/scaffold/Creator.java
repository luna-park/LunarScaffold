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
import java.util.regex.Pattern;

public class Creator {
//    ProjectName /app/src/main/java/ com/package / sName

    private final String appMaskSrc = "/app/src/main/java/";
    private final String appMaskAssets = "/app/src/main/assets/";
    private final String appMaskRes = "/app/src/main/res/values/";
    private final String appMaskManifest = "/app/src/main/";
    private final String appMaskGradle = "/app/";
    private final String coreMask = "/core/src/";
    private final String desktopMask = "/desktop/src/";

    private final String[] assetsDirs = {
            "Interface",
            "MatDefs",
            "Materials",
            "Models",
            "Scenes",
            "Shaders",
            "Sounds",
            "Textures"
    };


    private final String fileDesktopLauncher = "package %s;\n\npublic class DesktopLauncher {\n\n    public static void main(String[] args) {\n        Game game = new Game();\n        game.start();\n    }\n}\n";
    private final String fileGame = "package %s;\n\nimport com.jme3.app.SimpleApplication;\n\npublic class Game extends SimpleApplication {\n\n    public static void main(String[] args) {\n        Game app = new Game();\n        app.start();\n    }\n\n    @Override\n    public void simpleInitApp() {\n\n    }\n}\n";
    private final String fileActivity = "package %s;\n\nimport com.jme3.app.AndroidHarness;\n\npublic class MainActivity extends AndroidHarness {\n\n    public MainActivity() {\n        // Set main project class (fully qualified path)\n        appClass = Game.class.getCanonicalName();\n        // Options\n    }\n}\n";
    private final String fileManifest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n    package=\"%s\">\n\n    <application\n        android:allowBackup=\"true\"\n        \n        android:label=\"@string/app_name\"\n        android:launchMode=\"singleTask\"\n        android:supportsRtl=\"true\"\n        android:theme=\"@style/AppTheme\">\n        <activity\n            android:name=\".MainActivity\"\n            android:screenOrientation=\"landscape\">\n            <intent-filter>\n                <action android:name=\"android.intent.action.MAIN\" />\n\n                <category android:name=\"android.intent.category.LAUNCHER\" />\n            </intent-filter>\n        </activity>\n    </application>\n\n</manifest>";
    private final String fileStrings = "<resources>\n    <string name=\"app_name\">%s</string>\n</resources>\n";
    private final String fileStyles = "<resources>\n\n    <!-- Base application theme. -->\n    <style name=\"AppTheme\" parent=\"android:Theme.Holo.Light.DarkActionBar\">\n        <!-- Customize your theme here. -->\n    </style>\n\n</resources>";
    private final String fileGradleApp = "apply plugin: 'com.android.application'\n\nandroid {\n    compileSdkVersion 25\n    buildToolsVersion \"25.0.2\"\n    defaultConfig {\n        applicationId \"%s\"\n        minSdkVersion 15\n        targetSdkVersion 25\n        versionCode 1\n        versionName \"1.0\"\n        multiDexEnabled true\n    }\n    buildTypes {\n        release {\n            minifyEnabled false\n            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n        }\n    }\n}\n\ndependencies {\n    compile fileTree(dir: 'libs', include: ['*.jar'])\n}";
    private final String fileGradleRoot = "// Top-level build file where you can add configuration options common to all sub-projects/modules.\n\nbuildscript {\n    repositories {\n        jcenter()\n    }\n    dependencies {\n        classpath 'com.android.tools.build:gradle:2.3.1'\n\n        // NOTE: Do not place your application dependencies here; they belong\n        // in the individual module build.gradle files\n    }\n}\n\nallprojects {\n    repositories {\n        jcenter()\n        maven { url \"http://dl.bintray.com/jmonkeyengine/org.jmonkeyengine\" }\n    }\n\n    tasks.withType(JavaCompile) {\n        sourceCompatibility = \"1.7\"\n        targetCompatibility = \"1.7\"\n    }\n}\n\n// All modules:\n//    jme3-android-native\n//    jme3-android\n//    jme3-blender\n//    jme3-bullet-native-android\n//    jme3-bullet-native\n//    jme3-bullet\n//    jme3-core\n//    jme3-desktop\n//    jme3-effects\n//    jme3-examples\n//    jme3-ios\n//    jme3-jbullet\n//    jme3-jogg\n//    jme3-jogl\n//    jme3-lwjgl\n//    jme3-lwjgl3\n//    jme3-networking\n//    jme3-niftygui\n//    jme3-plugins\n//    jme3-terrain\n\ndef jme3 = [v:'3.1.0-stable', g:'org.jmonkeyengine']\n\nproject(\":core\") {\n    apply plugin: \"java\"\n    sourceSets.main.java.srcDirs = [\"src/\"]\n    dependencies {\n        // Basic\n        compile \"${jme3.g}:jme3-core:${jme3.v}\"\n\n        // Optional\n//        compile \"${jme3.g}:jme3-effects:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-plugins:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-jogg:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-terrain:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-blender:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-bullet-native:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-networking:${jme3.v}\"\n\n        // Resources\n//        compile \"net.sf.sociaal:jME3-testdata:3.0.0.20130526\"\n    }\n}\n\nproject(\":desktop\") {\n    apply plugin: \"java\"\n    sourceSets.main.java.srcDirs = [\"src/\"]\n    dependencies {\n        compile files(\"../app/src/main/assets\")\n        compile project(\":core\")\n        compile \"${jme3.g}:jme3-desktop:${jme3.v}\"\n        compile \"${jme3.g}:jme3-lwjgl:${jme3.v}\"\n    }\n}\n\nproject(\":app\") {\n    apply plugin: \"android\"\n    dependencies {\n        compile project(\":core\")\n        compile \"${jme3.g}:jme3-android:${jme3.v}\"\n        compile \"${jme3.g}:jme3-android-native:${jme3.v}\"\n//        compile \"${jme3.g}:jme3-bullet-native-android:${jme3.v}\"\n    }\n}\n\ntask clean(type: Delete) {\n    delete rootProject.buildDir\n}";


    private String sProjectName;
    private String sPackageName;
    private String sName;
    private String sPackageDirs;
    private String sSourceDir = "e:\\LibGDX_template\\template\\";

    private String sReplacePackageMask = "{$PACKAGE}";
    private String sReplaceNameMask = "{$NAME}";

    private Set<String> excludeSet;

    private EventListener eventListener;
    private int count;

    Creator(EventListener eventListener) {
        this.eventListener = eventListener;

    }

    void startGenerate(String sProjectName, String sPackageName, String sSource) {
//        this.sProjectName = sProjectName;
//        this.sPackageName = sPackageName;
        // Check strings for null etc
        if (sProjectName != null && sPackageName != null && sSource != null) {
            if (sProjectName.length() < 1) sProjectName = "MyGame";
            if (sPackageName.length() < 1) sPackageName = "org.gamepackage";
            this.sProjectName = sProjectName.trim();
            this.sPackageName = sPackageName.trim();
            this.sSourceDir = sSource;
            sName = sProjectName.toLowerCase();
            sPackageDirs = sPackageName.replace(".", "/");
//        startTheDance();
            ArrayList<File> files = new ArrayList<>();
//            sSourceDir = "e:\\LibGDX_template\\template\\";
            listDirectory(sSourceDir, files);
            showFiles(files);
        }
    }

    private void showFiles(ArrayList<File> files) {
        createExclude(files);
//        TODO Mock data
//        String targetDir = System.getProperty("user.dir") + "\\" + sProjectName;
        String targetDir = "E:\\__Testing\\" + sProjectName;
        for (File file : files) {
            String sAbsPath = file.getAbsolutePath();

//            System.out.println(String.format("%s", sAbsPath));
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

//            System.out.println(String.format("* %s", newPath));
//                String contentSrc = readFile(sAbsPath);
//                createFile(newPath, contentSrc);
                modifyAndCopyFile(file, newPath);
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

    private void modifyAndCopyFile(File file, String newPath) {
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

    private void createFile(String name, String content1) {

        System.out.println(String.format("Create file: %s", name));

//        System.out.println(name);
//        System.out.println(content);

//        content = content.replace("{$PACKAGE}", sPackageName)
//                .replace("{$NAME}", sProjectName);

        File file = new File(name);
        file.getParentFile().mkdirs();


        try {
            //проверяем, что если файл не существует то создаем его
            if (!file.exists()) {
                file.createNewFile();
            }

//            Path path = Paths.get(name);
//            Charset charset = StandardCharsets.UTF_8;
//
//            String content;
//
//            content = new String(Files.readAllBytes(path), charset);
//            content = content.replace(sReplacePackageMask, sPackageName)
//                    .replace(sReplaceNameMask, sProjectName);
//            Files.write(path, content.getBytes(charset));


            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст s файл
                out.print(content1);
                eventListener.onEvent("Created " + name);
            } catch (Exception e1) {
                fail("Write to file" + e1.getMessage());
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch (IOException e) {
            fail("Create " + name);
        }
    }

    private String readFile(String name) {
        System.out.println(String.format("Read file: %s", name));
        StringBuilder content = new StringBuilder();
        try {
            FileReader reader = new FileReader(name);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                content.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            fail("Read file: " + e.getMessage());
        }
        return content.toString();
    }

    private void fail(String message) {
        eventListener.onEvent(message + " failed");
    }
}
