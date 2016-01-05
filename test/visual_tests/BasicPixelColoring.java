package visual_tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicPixelColoring extends JPanel {

    private BufferedImage canvas;

    public BasicPixelColoring(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.BLUE);
        drawRect(Color.RED, 0, 0, width/2, height/2);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void drawLine(Color c, int x1, int y1, int x2, int y2) {
        // Implement line drawing
        repaint();
    }

    public void drawRect(Color c, int x1, int y1, int width, int height) {
        int color = c.getRGB();
        // Implement rectangle drawing
        for (int x = x1; x < x1 + width; x++) {
            for (int y = y1; y < y1 + height; y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    public void drawOval(Color c, int x1, int y1, int width, int height) {
        // Implement oval drawing
        repaint();
    }



    public static void main(String[] args) {
        int width = 601;
        int height = 241;
        JFrame frame = new JFrame("Direct draw demo");

        BasicPixelColoring panel = new BasicPixelColoring(width, height);
        
        frame.add(panel);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.fillCanvas(Color.BLUE);
         
        //frame.setSize(screenSize.width, screenSize.height);
    }


}