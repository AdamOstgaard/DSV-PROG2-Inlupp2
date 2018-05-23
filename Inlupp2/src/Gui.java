import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    private Map map;
    private FeatureHandler featureHandler;

    public Gui(){
        this.setLayout(new BorderLayout());
        map = new Map();
        featureHandler = new FeatureHandler();
        featureHandler.attach(map);
        add(map, BorderLayout.WEST);
        setMinimumSize(new Dimension(map.getMapDimension()));
        setVisible(true);

        JTextField searchText = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent) -> search(searchText.getText()));
        add(searchButton, BorderLayout.SOUTH);

    }

    private void search(String text) {
        featureHandler.getSelectedFeatures().forEach(p -> p.setSelected(false));
        if (text.isEmpty())
            return;

        featureHandler.getFeatures(text).forEach(p -> p.setSelected(true));
    }
}
