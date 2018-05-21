import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Map extends JPanel{
    private Image backgroundImage;

    public Map(){
        try{
            backgroundImage = loadBackgroundImage();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(backgroundImage,0,0,null);
    }

    private BufferedImage loadBackgroundImage() throws IOException{
        BufferedImage img;
        try {
            return ImageIO.read(new URL("http://i65.tinypic.com/2zz7ndz.png"));
        }catch(Exception e){
            throw(new IOException("Map could not be downloaded. Please check map Path and internet connection.", e));
        }
    }

}
