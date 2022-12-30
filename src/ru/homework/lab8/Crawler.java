package ru.homework.lab8;

import java.util.LinkedList;
import java.util.Scanner;

public class Crawler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        http://www.consultant.ru/
//        http://www.chebddut.narod.ru/
        String url = scanner.nextLine();
        int maxDepth = scanner.nextInt();
        int countThreads = scanner.nextInt();

        URLPool pool = new URLPool(maxDepth);
        //поместим указанный пользователем адрес в пул с глубиной 0
        pool.addPair(new URLDepthPair(url, 0));
        for (int i = 0; i < countThreads; i++) {
            CrawlerTask c = new CrawlerTask(pool);
            Thread thread = new Thread(c);
            thread.start();
        }
        while (countThreads != pool.getWaits()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        try {
            showResult(pool.getViewedLinks());
        } catch (NullPointerException e) {
            System.out.println("usage: java Crawler " + url + " " + maxDepth + " " + countThreads);
        }
        System.exit(0);
    }


    public static void showResult(LinkedList<URLDepthPair> list) {
        for (URLDepthPair c : list) {
            System.out.println("Depth : " + c.getDepth() + "\tLink : " + c.getUrl());
        }
    }
}



