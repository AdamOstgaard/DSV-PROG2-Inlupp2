import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    public Gui(){
        this.setLayout(new BorderLayout());
        add(new Map());
        setSize(new Dimension(600, 600));
        setVisible(true);

    }
}
