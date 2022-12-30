package ru.homework.lab5;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *  данный класс позволяет исследовать различные области фрактала, путем
 *  его создания, отображения через графический интерфейс Swing и обработки
 *  событий, вызванных взаимодействием приложения с пользователем
 */
public class FractalExplorer {

    // размер экрана, является шириной и высотой для состояния программ
    private int size;

    //данная ссылка необходима для обновления отображения в разных методах в процессе вычисления фрактала
    private JImageDisplay jImageDisplay;

    //  этот объект используется ссылкой на базовый класс, для отображения других фракталов
    private FractalGenerator fractalGenerator;

    //данный объект указывает диапазон комплексной плоскости, которая выводится на экран
    private Rectangle2D.Double range;

    //конструктор, принимает значение размера отображения
    public FractalExplorer(int displaySize){
        size = displaySize;
        range = new Rectangle2D.Double();
        jImageDisplay = new JImageDisplay(displaySize, displaySize);
        fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(range);
    }

    //данный метод инициализирует графический интерфейс Swing
    public void createAndShowGUI(){
        jImageDisplay.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");

        frame.add(jImageDisplay, BorderLayout.CENTER);

        //reset
        JButton button = new JButton("RESET");
        ResetButton resetAction = new ResetButton();
        button.addActionListener(resetAction);

        //save
        JButton button1 = new JButton("SAVE");
        SaveButton saveButton = new SaveButton();
        button1.addActionListener(saveButton);

        JPanel jPanelSouth = new JPanel();
        jPanelSouth.add(button);
        jPanelSouth.add(button1);
        frame.add(jPanelSouth, BorderLayout.SOUTH);

        //close
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        String[] items = {"Mandelbrot", "Tricorn", "BurningShip"};
        JComboBox jComboBox = new JComboBox(items);

        //объект-пояснение к выпадающему списку
        JLabel jLabel = new JLabel("Type of fractal: ");
        JPanel jPanel = new JPanel();
        jPanel.add(jLabel);
        jPanel.add(jComboBox);
        frame.add(jPanel, BorderLayout.NORTH);

        ChooseButton chooseType = new ChooseButton();
        jComboBox.addActionListener(chooseType);

        //щелчок мыши
        MouseListener mouseClick = new MyMouseListener();
        jImageDisplay.addMouseListener(mouseClick);

        // операции ниже правильно размечают содержимое окна
        // делают его видимы, и затем запрещают измерять размер его кеша
        frame.pack ();
        frame.setVisible (true);
        frame.setResizable (false);
    }
    //вспомогательный метод для вывода на экран фрактала
    private void drawFractal(){
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                //x - пиксельная координата; xCoord - координата в пространстве фрактала
                double xCoord = FractalGenerator.getCoord (range.x, range.x + range.width, size, x);
                //аналогично
                double yCoord = FractalGenerator.getCoord (range.y, range.y + range.height, size, y);
                int iterations = fractalGenerator.numIterations(xCoord, yCoord);

                // в таком случае точка не выходит за грациы, и мы устанавливаем пиксель в черный
                if (iterations == -1){
                    jImageDisplay.drawPixel(x, y, 0);
                }
                //иначе выбираем значение(цвет) на основании количества итераций
                //тк значение варьируется от 0 до 1, получим плавную последовательность цветов
                //в пространстве HSW
                else {
                    float hue = 0.7f + (float) iterations / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    jImageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        //обновление изображения
        jImageDisplay.repaint();
    }

    // внутренний класс для обработки событий от кнопки сброса
//    обработчик сбрасывает диапазон к начальному, определенному генератором,
//    а затем перерисовывается фрактал
    public class ResetButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event){
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }
    //класс для сохранения изображения
    public class SaveButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            JFileChooser chooser = new JFileChooser();
            //настройка средства выбора файлов, чтобы сохранять только в PNG
            FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int showResult = chooser.showSaveDialog(jImageDisplay);
            //если результат операции выбора файла == JFileChooser.APPROVE_OPTION
            //продолжить сохранение, иначе - пользователь отменил сохранение
            if (showResult == JFileChooser.APPROVE_OPTION){
                //узнаем директорию, выбранную пользователем
                File directory = chooser.getSelectedFile();
                String directoryToString = directory.toString();
                //обрабатываем исключения метода write()
                try{
                    BufferedImage image = jImageDisplay.getImage();
                    ImageIO.write(image, "png", directory);
                } catch (Exception exception){
                    JOptionPane.showMessageDialog(chooser, exception.getMessage(),
                            "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    //данный класс при получении события о щелчке мышью, должен
    //отобразить пиксельные координаты щелчка в область фрактала
    public class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent click){
            int x = click.getX();
            double xCoord = fractalGenerator.getCoord(range.x, range.x + range.width, size,x);

            int y = click.getY();
            double yCoord = fractalGenerator.getCoord(range.y, range.y + range.height, size, y);

            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            drawFractal();
        }
    }
    //добавление выпадающего списка в реализацию ActionListener
    public class ChooseButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            JComboBox jComboBox = (JComboBox) event.getSource();
            //извлекаем выбранный элемент из виджета
            String type = (String) jComboBox.getSelectedItem();
            if (type == "Mandelbrot"){
                fractalGenerator = new Mandelbrot();
            } if (type == "Tricorn"){
                fractalGenerator = new Tricorn();
            } if (type == "BurningShip"){
                fractalGenerator = new BurningShip();
            }
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }
    
    


    public static void main(String[] args) {
        FractalExplorer display = new FractalExplorer(800);
        display.createAndShowGUI();
        display.drawFractal();
    }
}