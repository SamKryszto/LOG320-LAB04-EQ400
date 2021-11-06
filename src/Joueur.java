import java.util.ArrayList;

public class Joueur {

    private boolean isWhite;
    private ArrayList<Jeton> listeJeton;

    public Joueur(boolean isWhite, ArrayList<Jeton> listeJeton) {
        this.isWhite = isWhite;
        this.listeJeton = listeJeton;
    }

    public boolean isWhite() {
        return this.isWhite;
    }

    public void setWhite(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public void retraitJeton(Jeton jeton) {
        this.listeJeton.remove(jeton);
    }

    public void addJeton(Jeton jeton) {
        this.listeJeton.add(jeton);
    }
}
