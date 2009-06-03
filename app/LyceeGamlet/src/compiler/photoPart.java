package compiler;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
 
/**
 *
 * @author s1au_k14
 */
public class photoPart extends JApplet {
    public void init(){
	myCanvas canvas = new myCanvas();
	canvas.setBackground(Color.white);
	add(canvas,BorderLayout.CENTER);
	myOption option = new myOption(canvas);
	add(option,BorderLayout.SOUTH);
    }
}
 
class myCanvas extends Canvas {
    BufferedImage img = null;
    int click = -1;
    public boolean setImage(String url){
        boolean found = false;
        try {
            img = ImageIO.read(new File(url));
            setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
            found = true;
            repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File not Found!");
        }
        return found;
    }
 
    public void paint(Graphics g){
        if (img != null){
            g.drawImage(img, 0, 0, null);
            if (click > -1){
                g.drawRect((click % 5) * (img.getWidth() / 5),
                        (click / 5) * (img.getHeight() / 5),
                        img.getWidth() / 5, img.getHeight() / 5);
            }
        }
    }
 
    public void setRect(int c){
        click = c;
        repaint();
    }
 
    public void saveImage(String url){
        if (url.contains(".jpg") || url.contains(".gif") || url.contains(".bmp")){
            try{
                Robot robot = new Robot();
                Rectangle screenRect = new Rectangle((click % 5) * (img.getWidth() / 5) + 5
                    ,(click / 5) * (img.getHeight() / 5) + 30,
                    img.getWidth() / 5, img.getHeight() / 5);
                BufferedImage image = robot.createScreenCapture(screenRect);
                if (url.contains(".jpg"))
                    ImageIO.write(image, "jpg", new File(url));
                else if (url.contains(".gif"))
                    ImageIO.write(image, "gif", new File(url));
                else if (url.contains(".bmp"))
                    ImageIO.write(image, "bmp", new File(url));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            JOptionPane.showMessageDialog(null,
                    "Only can save JPG or GIF or BMP");
        }
    }
}
 
class myOption extends JPanel {
    myCanvas canvas;
    int value;
    JFileChooser chooser;
    JButton rect,savePhoto;
    int click = 0;
    public myOption(myCanvas c){
        canvas = c;
        rect = new JButton("Rect");
        rect.setEnabled(false);
        rect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				if (click == -1)
                	savePhoto.setEnabled(true);
                canvas.setRect(click++);
                if (click == 25)
                    click = 0;
 
            }
        });
        add(rect);
        chooser = new JFileChooser();
        JButton openPhoto = new JButton("Open Photo");
        openPhoto.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                value = chooser.showOpenDialog(null);
                if (value == 0){
                    String check = chooser.getSelectedFile().getPath();
                    rect.setEnabled(canvas.setImage
                            (check.substring(0,check.lastIndexOf("\\") + 1) +
                            chooser.getSelectedFile().getName()));
                }
            }
        });
        add(openPhoto);
        savePhoto = new JButton("Save Photo");
        savePhoto.setEnabled(false);
        savePhoto.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                value = chooser.showSaveDialog(null);
                if (value == 0){
                    String check = chooser.getSelectedFile().getPath();
                    canvas.saveImage(check.substring(0,check.lastIndexOf("\\") + 1) +
                            chooser.getSelectedFile().getName());
                }
            }
        });
        add(savePhoto);
    }
}
