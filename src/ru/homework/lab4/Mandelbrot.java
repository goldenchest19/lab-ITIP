package ru.homework.lab4;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{
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
        double z_real = 0;
        // для мнимой части
        double z_imaginary = 0;

        while (iteration < MAX_ITERATIONS && z_real * z_real + z_imaginary * z_imaginary < 4)
        {
            double z_realUpdated = z_real * z_real - z_imaginary * z_imaginary + x;
            double z_imaginaryUpdated = 2 * z_real * z_imaginary + y;
            z_real = z_realUpdated;
            z_imaginary = z_imaginaryUpdated;
            iteration += 1;
        }

        // если алгоритм дошел до MAX_ITERATIONS
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }

        return iteration;
    }
}
