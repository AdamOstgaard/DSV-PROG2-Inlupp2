import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class Triangle extends JComponent {
    private final Position position;
    private final Color color;
    private final Path2D path2D;
    private final int side;


    public Triangle(Position position, int side, Color color) {
        this.position = position;
        this.color = color;
        this.side = side;

        path2D = new Path2D.Double();

        setPreferredSize(new Dimension(side, side));
        setBounds(getPositionOffset().getX(), getPositionOffset().getY(), side, side);

        drawPath(side);
    }

    private void drawPath(int side) {
        double yOffset = (side * 0.5);
        double startX = (side / 2.0) * (1 - 1 / Math.sqrt(3));
        double startY = (3.0 * side / 4.0);

        path2D.moveTo(startX, startY - yOffset);
        path2D.lineTo((side - startX), startY - yOffset);
        path2D.lineTo((side / 2.0), (side * 1.25) - yOffset);
        path2D.closePath();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(color);
        g2.fill(path2D);
    }

    private Position getPositionOffset() {
        return new Position(position.getX() - side / 2, (int) (position.getY() - side / 1.25));
    }

    public Position getPosition() {
        return position;
    }
}
