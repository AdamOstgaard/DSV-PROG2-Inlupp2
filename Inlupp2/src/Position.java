public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "x:" + getX() + "y:" + getY();
    }

    @Override
    public boolean equals(Object position) {
        return position != null
                && position instanceof Position
                && this.getX() == ((Position) position).getX()
                && this.getY() == ((Position) position).getY();
    }

    @Override
    public int hashCode() {
        return (getY() * 3) + (getX() * 7);
    }
}
