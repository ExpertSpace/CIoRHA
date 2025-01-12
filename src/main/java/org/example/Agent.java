package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Agent extends Thread {
    private static final String SEPARATOR_FOR_CSV_FILE = ",";
    private static final String POSITIVE_ANSWER = "OK";
    private static final String NEGATIVE_ANSWER = "Интернет не работает";

    private final String numberCabinet;
    private final String numberComputer;

    public Agent(String numberCabinet, String numberComputer) {
        this.numberCabinet = numberCabinet;
        this.numberComputer = numberComputer;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                Thread.sleep(1);
                Socket clientSocket = new Socket("bore.pub", /*portParserFromWeb()*/ 59797);

                OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
                writer.write(numberCabinet + SEPARATOR_FOR_CSV_FILE
                        + numberComputer + SEPARATOR_FOR_CSV_FILE
                        + setStatusMessage(true) + "\n");

                writer.flush();
                writer.close();
                clientSocket.close();
            } catch (IOException | InterruptedException e) {
                System.err.println(setStatusMessage(false));
            }
        }
    }

    private static int portParserFromWeb() {
//        String url = "https://anotepad.com/notes/abgaw9ea"; // Замените на нужный URL
//        String url = "https://ru.anotepad.com/notes/8t8q2n84"; // Замените на нужный URL
        String url = "https://ru.anotepad.com/notes/qjnw3gkg"; // Замените на нужный URL
        int port = 0;

        try {
            Document document = Jsoup.connect(url).get();

            String title = document.title();

            port = Integer.parseInt(formatStringToPort(title));

        } catch (IOException e) {
            System.err.println("Ошибка парсинга порта: " + e.getMessage());
        }

        return port;
    }

    private static String formatStringToPort(String str) {
        return str.replaceAll("[\\D+]", "").trim();
    }

    private static String setStatusMessage(boolean opt) {
        return opt ? POSITIVE_ANSWER : NEGATIVE_ANSWER;
    }
}
