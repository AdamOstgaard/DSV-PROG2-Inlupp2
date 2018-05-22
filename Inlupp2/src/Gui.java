import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    public Gui(){
        this.setLayout(new BorderLayout());
        Map map = new Map();
        FeatureHandler featureHandler = new FeatureHandler();
        featureHandler.attach(map);
        add(map, BorderLayout.WEST);
        setMinimumSize(new Dimension(map.getMapDimension()));
        setVisible(true);

    }
}
