import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class Triangle {
    private final Point position;
    private final Color color;
    private final Path2D path2D;


    public Triangle(double side, Color color, Point position) {
        this.position = position;
        this.color = color;
        path2D = new Path2D.Double();
        //setPreferredSize(new Dimension((int)side,(int)side));
        //setBounds(position.x, position.y, (int)side, (int)side);
        drawPath(side);
    }

    private void drawPath(double side) {
        double xOffset = position.x - (side / 2);
        double yOffset = position.y - (side * 1.25);
        double x = (side / 2.0) * (1 - 1 / Math.sqrt(3));
        double y = (3.0 * side / 4.0);

        path2D.moveTo(x + xOffset, y + yOffset);
        path2D.lineTo((side - x) + xOffset, y + yOffset);
        path2D.lineTo((side / 2.0) + xOffset, (side * 1.25) + yOffset);
        path2D.closePath();
    }

    protected void paint(Graphics g) {
        //super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(color);
        g2.fill(path2D);
    }
}
