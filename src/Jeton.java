public class Jeton {
    private int posX;
    private int posY;
    private boolean isWhite;

    public Jeton(int posX, int posY, boolean isWhite) {
        this.posX = posX;
        this.posY = posY;
        this.isWhite = isWhite;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int x) {
        this.posX = x;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int y) {
        this.posY = y;
    }

    public boolean isWhite() {
        return this.isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }
}
