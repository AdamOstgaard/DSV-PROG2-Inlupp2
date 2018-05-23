import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Map extends JPanel {
    private Image backgroundImage;
    private boolean isReady;
    private ArrayList<IMapIsReadyListener> mapIsReadyListeners;

    public Map() {
        super(null);
        mapIsReadyListeners = new ArrayList<>();
        try {
            backgroundImage = loadBackgroundImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        this.setPreferredSize(getMapDimension());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setIsReady(graphics.drawImage(backgroundImage, 0, 0, null));
    }

    private BufferedImage loadBackgroundImage() throws IOException {
        BufferedImage img;
        try {
            return ImageIO.read(new URL("http://oi65.tinypic.com/2zz7ndz.jpg"));
        } catch (Exception e) {
            throw (new IOException("Map could not be downloaded. Please check map Path and internet connection.", e));
        }
    }

    public Dimension getMapDimension() {
        return new Dimension(backgroundImage.getWidth(null), backgroundImage.getHeight(null));
    }

    public boolean getIsReady() {
        return isReady;
    }

    private void setIsReady(boolean ready) {
        if (ready != isReady) {
            isReady = ready;
            notifyMapIsReadyListeners(ready);
        }
    }

    public void addMapIsReadyListener(IMapIsReadyListener listener) {
        mapIsReadyListeners.add(listener);
    }

    private void notifyMapIsReadyListeners(boolean ready) {
        for (IMapIsReadyListener listener : mapIsReadyListeners) {
            listener.readyStateChanged(ready);
        }
    }
}
