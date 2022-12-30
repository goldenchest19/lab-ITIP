package ru.homework.lab6;


import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    // специальное число, ибо если доходит до него значит точка находится в наборе
    public static final int MAX_ITERATIONS = 2000;

//   данный метод позволяет генератору фракталов определить наиболее "интересную" область
//   комплексной плоскости для конкретного фракталаa
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    // реализует итеративную функцию для фрактала Мандельброта
    @Override
    public int numIterations(double x, double y) {
        int iteration = 0;

        // для действительной части
        double zReal = 0;
        // для мнимой части
        double zImg = 0;

        while (iteration < MAX_ITERATIONS && zReal * zReal + zImg * zImg < 4)
        {
            double zRealUpdate = zReal * zReal - zImg * zImg + x;
            double zImgUpdate = 2 * zReal * zImg + y;
            zReal = zRealUpdate;
            zImg = zImgUpdate;
            iteration += 1;
        }

        // если алгоритм дошел до MAX_ITERATIONS
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
