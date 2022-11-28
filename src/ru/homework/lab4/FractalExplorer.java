package ru.homework.lab4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

/**
 *  данный класс позволяет исследовать различные области фрактала, путем
 *  его создания, отображения через графический интерфейс Swing и обработки
 *  событий, вызванных взаимодействием приложения с пользователем
 */
public class FractalExplorer {
    // размер экрана, является шириной и высотой для состояния программ
    private int size;
//    данная ссылка необходима для обновления отображения в разных методах в процессе вычисления фрактала
    private JImageDisplay jImageDisplay;
//  этот объект используется ссылкой на базовый класс, для отображения других фракталов в будущем
    private FractalGenerator fractalGenerator;
//    указывает диапазон комплексной плоскости
    private Rectangle2D.Double range;

//конструктор принимает значение
//размера отображения в качестве аргумента,
    public FractalExplorer (int display_size) {
        size = display_size;
        range = new Rectangle2D.Double();
        fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(range);
        jImageDisplay = new JImageDisplay(display_size, display_size);
    }

//    данный метод инициализирует графический интерфейс Swing
    public void createAndShowGUI () {
        jImageDisplay.setLayout(new BorderLayout());
        JFrame frame = new JFrame();

        frame.add(jImageDisplay, BorderLayout.CENTER);

        // кнопка для сброса отображения
        JButton button = new JButton("Reset");
        frame.add(button, BorderLayout.SOUTH);
        MyActionListener clearAction = new MyActionListener();
        button.addActionListener(clearAction);

        MyMouseListener mouse = new MyMouseListener();
        jImageDisplay.addMouseListener(mouse);

//      операция закрытия по умолчанию
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);


        // операции ниже правильно размечают содержимое окна
        // делат его видимы, и затем запрещают измерять размер его кеша
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }


    // вспомогательный метод для вывода на экран фракталов
    private void drawFractal () {
        for (int x = 0; x < size; x ++) {
            for (int y = 0; y < size; y ++) {
                // получаем координа х, соответствующую координате пикселя Х
                double xCoord = fractalGenerator.getCoord(range.x,range.x + range.width, size, x);
                double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, size, y);
                int iterations = fractalGenerator.numIterations(xCoord,yCoord);

                // в таком случае точка не выходит за грациы, и мы устанавливаем пиксель
//                в черный цвет
                if (iterations == -1) {
                    jImageDisplay.drawPixel(x,y,0);
                }
                // выбираем значение на основание количества итераций
                // так как зачение варьируется от 0 до 1, получим плавную последовательность цветов
                else {
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    jImageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        // обновляем изображение
        jImageDisplay.repaint();
    }

    // внутренний класс для обработки событий от кнопки сброса
//    обработчик сбрасывыает диапазон к начальному, определенному генератором,
//    а затем перерисовывается фрактал
    public class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

//  данный класс при получение события о щелчке мыши, оторажает пиксельные координаты
//  щелчка в область фрактала
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = fractalGenerator.getCoord(range.x, range.x+range.width, size,x);

            int y = e.getY();
            double yCoord = fractalGenerator.getCoord(range.y, range.y+range.height, size,y);

            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    public static void main (String[] args) {

        FractalExplorer display = new FractalExplorer(800);
        display.createAndShowGUI();
        display.drawFractal();
    }
}
