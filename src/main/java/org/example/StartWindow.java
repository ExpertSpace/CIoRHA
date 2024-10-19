package org.example;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class StartWindow extends JFrame {
    private JTextField numberCabinetJTF;
    private JTextField numberComputerJTF;
    private JPanel panelMain;
    private JButton saveButton;

    private static String numberCabinet;
    private static String numberComputer;

    private static final String FILE_CONFIG_INFO = "config.info";

    public static void main(String[] args) {
        settingsStartWindow();
    }

    public StartWindow() {
        saveButton.addActionListener(event -> {
            numberCabinet = numberCabinetJTF.getText();
            numberComputer = numberComputerJTF.getText();

            writeFile(false, "Кабинет №" + numberCabinet, "Компьютер №" + numberComputer);

            new Agent(numberCabinet, numberComputer).start();
            setVisible(false);
        });
    }

    public static void settingsStartWindow() {
        StartWindow window = new StartWindow();

        window.setContentPane(window.panelMain);
        window.setSize(735, 156);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setTitle("Укажите расположение компьютера. Например: Кабинет № 101, Компьютер № 1");

        try {
            File configFile = new File(FILE_CONFIG_INFO);
            if (configFile.isFile()) {
                String[] configLines = Files.readAllLines(Paths.get(FILE_CONFIG_INFO)).toArray(new String[0]);
                if (configLines[0].equals("false")) {
                    window.setVisible(false);
                    new Agent(readFile()[1], readFile()[2]).start();
                } else {
                    window.setVisible(true);
                }
            } else {
                writeFile(true, null, null);
            }
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    private static void writeFile(boolean isStartRetry, String numberCabinet, String numberComputer) {
        try (FileWriter fileWriter = new FileWriter(FILE_CONFIG_INFO)) {
            fileWriter.write(isStartRetry + "\n" + numberCabinet + "\n" + numberComputer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String[] readFile() throws IOException {
        StringBuilder strings = new StringBuilder();

        try (FileReader fileReader = new FileReader(FILE_CONFIG_INFO)) {
            int ch;

            while ((ch = fileReader.read()) != -1) {
                strings.append((char) ch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings.toString().split("\n");
    }
}
