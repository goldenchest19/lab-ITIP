package ru.homework.lab8;

import java.io.*;
import java.net.*;

public class CrawlerTask implements Runnable {

    URLPool URLPool;

    public CrawlerTask(URLPool pool) {
        URLPool = pool;
    }

    public static void request(PrintWriter out, URLDepthPair pair) throws MalformedURLException {
        out.println("GET " + pair.getPath() + " HTTP/1.1");
        out.println("Host: " + pair.getHost());
        out.println("Connection: close");
        out.println();
        out.flush();
    }

    @Override
    public void run() {
        while (true) {
            URLDepthPair currentPair = URLPool.getPair();
            try {
                Socket socket = new Socket(currentPair.getHost(), 80);
                socket.setSoTimeout(10000);
                try {
                    //поток вывода, экземпляр данного класса нужен для вызова метода
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    request(out, currentPair);
                    String line = in.readLine();
                    while (line != null) {
                        if (line.contains(URLDepthPair.URL_PREFIX) && line.indexOf('"') != -1) {
                            StringBuilder currentLink = new StringBuilder();
                            int count = line.indexOf(URLDepthPair.URL_PREFIX);
                            while (line.charAt(count) != '"' && line.charAt(count) != ' ') {
                                currentLink.append(line.charAt(count));
                                count++;
                            }
                            //для каждого найденного URL создаем новую пару
                            //и добавляем ее к пулу адресов, увеличивая глубину исходной пары на 1
                            URLDepthPair newPair = new URLDepthPair(currentLink.toString(), currentPair.depth + 1);
                            URLPool.addPair(newPair);
                        }
                        line = in.readLine();
                    }
                    socket.close();
                } catch (SocketTimeoutException e) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("IOEception caught");
            }
        }
    }
}