package ru.homework.lab8;

import java.util.LinkedList;


public class URLPool {
    //список всех адресов и относительную глубину поиска
    LinkedList<URLDepthPair> viewedLinks;
    LinkedList<URLDepthPair> foundedLink;
    int maxDepth;
    //количество потоков в ожидании
    int waits;

    public URLPool(int maxDepth) {
        this.maxDepth = maxDepth;
        foundedLink = new LinkedList<URLDepthPair>();
        viewedLinks = new LinkedList<URLDepthPair>();
        waits = 0;
    }

    public synchronized URLDepthPair getPair() {
        //если ни один адрес недоступен, то режим ожидания
        while (foundedLink.size() == 0) {
            waits++;
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception");
            }
            waits--;
        }
        return foundedLink.removeFirst();
    }

    public synchronized void addPair(URLDepthPair pair) {
        if (URLDepthPair.check(viewedLinks, pair)) {
            viewedLinks.add(pair);
            if (pair.getDepth() < maxDepth) {
                foundedLink.add(pair);
                //продолжаем работу потока, к которому ранее был вызван wait
                //в случае, когда новый адрес добавлен к пулу
                notify();
            }
        }
    }

    public int getWaits() {
        return waits;
    }

    public LinkedList<URLDepthPair> getViewedLinks() {
        return viewedLinks;
    }
}