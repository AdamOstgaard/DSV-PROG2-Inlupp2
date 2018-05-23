import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class Feature extends Triangle implements MouseListener {
    private FeatureCategory type;
    private boolean isSelected;

    public Feature(Position position, FeatureCategory type) {
        super(position, 100, Color.BLUE);

        this.addMouseListener(this);
    }

    public FeatureCategory getType() {
        return type;
    }

    private void onLeftClick(MouseEvent e) {
        isSelected = true;
        leftMouseButtonClicked(e);
    }

    protected abstract void leftMouseButtonClicked(MouseEvent e);

    protected abstract void rightMouseButtonClicked(MouseEvent e);

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            onLeftClick(e);
        else if (e.getButton() == MouseEvent.BUTTON3)
            rightMouseButtonClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
