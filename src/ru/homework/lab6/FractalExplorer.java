package ru.homework.lab6;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * данный класс позволяет исследовать различные области фрактала, путем
 * его создания, отображения через графический интерфейс Swing и обработки
 * событий, вызванных взаимодействием приложения с пользователем
 */
public class FractalExplorer {
    // размер экрана, является шириной и высотой для состояния программ
    private int size;
    //  этот объект используется ссылкой на базовый класс, для отображения других фракталов
    private JImageDisplay jImageDisplay;
    //  этот объект используется ссылкой на базовый класс, для отображения других фракталов
    private FractalGenerator fractalGenerator;
    //данный объект указывает диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double range;
    private JComboBox comboBox;
    private JButton resetButton;
    private JButton saveButton;

    private int rowsRemain;

    //конструктор, принимает значение размера отображения
    public FractalExplorer(int display_size) {
        size = display_size;
        range = new Rectangle2D.Double();
        fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(range);
        jImageDisplay = new JImageDisplay(display_size, display_size);
    }

    //данный метод инициализирует графический интерфейс Swing
    public void createAndShowGUI() {
        jImageDisplay.setLayout(new BorderLayout());
        JFrame frame = new JFrame();

        frame.add(jImageDisplay, BorderLayout.CENTER);

        //reset
        resetButton = new JButton("Reset");
        ResetButton clearAction = new ResetButton();
        resetButton.addActionListener(clearAction);

        //щелчок мыши
        MyMouseListener mouse = new MyMouseListener();
        jImageDisplay.addMouseListener(mouse);

        //close
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        String[] items = {"Mandelbrot", "Tricorn", "BurningShip"};
        comboBox = new JComboBox(items);

        JLabel jLabel = new JLabel("Fractal:");
        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        jPanel.add(comboBox);
        frame.add(jPanel, BorderLayout.NORTH);

        ChooseButton chooseAction = new ChooseButton();
        comboBox.addActionListener(chooseAction);

        //save 
        saveButton = new JButton("Save Image");
        SaveButton saveAction = new SaveButton();
        saveButton.addActionListener(saveAction);

        JPanel panelButtons = new JPanel();
        panelButtons.add(resetButton);
        panelButtons.add(saveButton);
        frame.add(panelButtons, BorderLayout.SOUTH);

        // операции ниже правильно размечают содержимое окна
        // делают его видимы, и затем запрещают измерять размер его кеша
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    //вспомогательный метод для вывода на экран фрактала
    //cоздаем для каждой строки отдельный рабочий объект
    //с помощью запускаем фоновый поток(т.е. запускаем задачу в фоновом режиме)
    private void drawFractal() {
        enableIO(false);
        //rowsRemain = общему кол-ву строк, которые надо нарисовать
        rowsRemain = size;
        for (int y = 0; y < size; y++) {
            FractalWorker frac = new FractalWorker(y);
            frac.execute();
        }
    }

    //вкл/выкл кнопки
    private void enableIO(boolean val) {
        comboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }

    // внутренний класс для обработки событий от кнопки сброса
//    обработчик сбрасывает диапазон к начальному, определенному генератором,
//    а затем перерисовывается фрактал
    public class ResetButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }
    //добавление выпадающего списка в реализацию ActionListener
    public class ChooseButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            //извлекаем выбранный элемент из виджета
            String name = (String) combo.getSelectedItem();
            if (name == "Mandelbrot") {
                fractalGenerator = new Mandelbrot();
            } if (name == "Tricorn") {
                fractalGenerator = new Tricorn();
            } if (name == "BurningShip") {
                fractalGenerator = new BurningShip();
            }
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    public class SaveButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            //настройка средства выбора файлов, чтобы сохранять только в PNG
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int showResult = chooser.showSaveDialog(jImageDisplay);
            //если результат операции выбора файла == JFileChooser.APPROVE_OPTION
            //продолжить сохранение, иначе - пользователь отменил сохранение
            if (showResult == JFileChooser.APPROVE_OPTION) {
                File directory = chooser.getSelectedFile();
                String directoryToString = directory.toString();
                try {
                    BufferedImage image = jImageDisplay.getImage();
                    ImageIO.write(image, "png", directory);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(chooser, exception.getMessage(),
                            "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    //данный класс при получении события о щелчке мышью, должен
    //отобразить пиксельные координаты щелчка в область фрактала
    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // проверяем, все ли строки перерисованы
            if (rowsRemain != 0) {
                return;
            }
            int x = e.getX();
            double xCoord = fractalGenerator.getCoord(range.x, range.x + range.width, size, x);

            int y = e.getY();
            double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, size, y);

            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }

    //создаем подкласс SwingWorker для вычисления значений цвета для одной строки фрактала
    private class FractalWorker extends SwingWorker<Object, Object> {
        //целочисленная y - координата вычисляемой строки
        private int y;

        //массив для хранения вычисленных значений RGB для каждого пикселя в этой строке
        private int[] rgbValues;

        public FractalWorker(int y) {
            this.y = y;
        }

        @Override
        protected Object doInBackground() throws Exception {
            rgbValues = new int[size];
            for (int x = 0; x < rgbValues.length; x++) {
                double xCoord = fractalGenerator.getCoord(range.x, range.x + range.width, size, x);
                double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, size, y);
                int iterations = fractalGenerator.numIterations(xCoord, yCoord);
                int rgbColor;
                if (iterations == -1)
                    rgbColor = 0;
                else {
                    float hue = 0.7f + (float) iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                rgbValues[x] = rgbColor;
            }
            return null;
        }

        @Override
        protected void done() {
            for (int i = 0; i < rgbValues.length; i++) {
                jImageDisplay.drawPixel(i, y, rgbValues[i]);
            }
            //перерисовываем строку
            jImageDisplay.repaint(0, 0, y, size, 1);
            //последний шаг данной операции
            rowsRemain--;
            if (rowsRemain == 0)
                enableIO(true);
        }
    }

    public static void main(String[] args) {

        FractalExplorer display = new FractalExplorer(800);
        display.createAndShowGUI();
        display.drawFractal();
    }
}