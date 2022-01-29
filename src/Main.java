import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String path = "C://Users//opife//OneDrive//College//Java//Netology//Java Core//files//Games";
    public static StringBuilder log = new StringBuilder();
    public static final DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public static void main(String[] args) {
        createDir(path);
        createDir("Games//src");
        createDir("Games//src//test");
        createDir("Games//src//main");
        createFile("Games//src//main//Main.java");
        createFile("Games//src//main//Utils.java");

        createDir("Games//res");
        createDir("Games//res//drawables");
        createDir("Games//res//vectors");
        createDir("Games//res//icons");

        createDir("Games//savegames");
        createDir("Games//temp");
        logSave(createFile("Games//temp//temp.txt"));

        GameProgress game1 = new GameProgress(11, 12, 13, 14);
        GameProgress game2 = new GameProgress(21, 22, 23, 24);
        GameProgress game3 = new GameProgress(31, 32, 33, 34);
        saveGame("Games//savegames//game1.dat", game1);
        saveGame("Games//savegames//game2.dat", game2);
        saveGame("Games//savegames//game3.dat", game3);
        zipFiles("Games//savegames//zip.zip", "Games/savegames/game1.dat,Games/savegames/game2.dat,Games/savegames/game3.dat");

        deleteFile("Games//savegames//game1.dat");
        deleteFile("Games//savegames//game2.dat");
        deleteFile("Games//savegames//game3.dat");

        openZip("Games//savegames//zip.zip", "Games/savegames/");

        System.out.println("Загрузка: " + openProgress("Games//savegames//game1.dat"));
        System.out.println("Загрузка: " + openProgress("Games//savegames//game2.dat"));
        System.out.println("Загрузка: " + openProgress("Games//savegames//game3.dat"));

    }

    public static void logPrint(String text) {
        Date date = new Date();
        System.out.println("[" + format.format(date) + "] " + text);
        log.append("[").append(format.format(date)).append("] ").append(text).append(System.lineSeparator());
    }

    public static void logSave(File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.append(log);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static File createFile(String fileName) {
        File myFile = new File(fileName);
        try {
            if (myFile.createNewFile()) {
                logPrint("Файл " + fileName + " успешно создан");
            }
        } catch (IOException ex) {
            logPrint("Файл " + fileName + " не создан! " + ex.getMessage());
        }
        return myFile;
    }

    private static void deleteFile(String fileName) {
        File myFile = new File(fileName);
        if (myFile.delete()) {
            logPrint("Файл " + fileName + " успешно удален");
        }
    }

    public static void createDir(String dirName) {
        File dir = new File(dirName);
        if (dir.mkdir()) {
            logPrint("Каталог " + dirName + " успешно создан");
        } else {
            logPrint("Каталог " + dirName + " не создан!");
        }
    }

    public static void saveGame(String path, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            logPrint("Игра " + game.toString() + " сохранена.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String path, String files) {
        String[] file = files.split(",");
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String s : file) {
                try (FileInputStream fis = new FileInputStream(s)) {
                    ZipEntry entry = new ZipEntry(s);
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    logPrint("Файл " + s + " заархивирован.");
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String zipPath, String extractPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                logPrint("Файл " + name + " разархивирован.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}

