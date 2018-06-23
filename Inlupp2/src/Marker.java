import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class Marker extends JComponent {
    private final Path2D path2D;
    private final Feature owner;
    private final int side = 50;
    private boolean isSelected;

    Marker(Feature owner) {

        isSelected = false;
        this.owner = owner;

        path2D = new Path2D.Double();

        setPreferredSize(new Dimension(side, side));
        setBounds(getPositionOffset().getX(), getPositionOffset().getY(), side, side);

        drawPath();
    }

    public boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
        repaint();
    }

    private void drawPath() {
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

        if (isSelected) {
            g2.setPaint(Color.RED);
            g2.setStroke(new BasicStroke(10));
            g2.draw(path2D);
        }
        g2.setPaint(owner.getCategory().getColor());

        g2.fill(path2D);
    }

    private Position getPositionOffset() {
        return new Position(owner.getPosition().getX() - side / 2, (int) (owner.getPosition().getY() - side / 1.25));
    }
}
