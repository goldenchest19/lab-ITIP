package ru.homework.lab5;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator{

    //константа с максимальным количеством итераций
    public static final int MAX_ITERATIONS = 2000;

    //метод позволяет генератору фракталов определить наиболее
    //«интересную» область комплексной плоскости для конкретного фрактала
    @Override
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    //метод реализует итеративную функцию для фрактала
    @Override
    public int numIterations(double x, double y){
        int iterartion = 0;

        //действительная часть
        double zReal = 0;
        //мнимая часть
        double zImg = 0;

        while (iterartion < MAX_ITERATIONS && (zReal * zReal + zImg * zImg) < 4){
            double zRealUpdated = zReal * zReal - zImg * zImg + x;
            double zImUpdated = -2 * zReal * zImg + y;
            zReal = zRealUpdated;
            zImg = zImUpdated;
            iterartion += 1;
        }
        //если дошли до MAX_ITERATIONS
        if (iterartion == MAX_ITERATIONS){
            return -1;
        }
        return iterartion;
    }

    public String toString(){
        return "Tricorn";
    }
}