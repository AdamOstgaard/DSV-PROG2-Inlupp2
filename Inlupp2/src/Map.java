import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends JPanel {
    private Image backgroundImage;
    private boolean isReady;
    private ArrayList<IMapIsReadyListener> mapIsReadyListeners;

    public Map(File imageFile) throws IOException {
        super(null);
        mapIsReadyListeners = new ArrayList<>();
        backgroundImage = loadBackgroundImage(imageFile);
        this.setPreferredSize(getMapDimension());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setIsReady(graphics.drawImage(backgroundImage, 0, 0, null));
    }

    private BufferedImage loadBackgroundImage(File file) throws IOException {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image == null)
                throw new FileNotFoundException("File not found or is not image");
            return image;
        } catch (Exception e) {
            throw (new IOException("Map could not be loaded, make sure it is an image file!", e));
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
