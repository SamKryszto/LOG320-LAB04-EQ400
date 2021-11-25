import java.util.ArrayList;
import java.awt.Point;


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

    public void retraitJeton(int x, int y) {
        Point pos = new Point(x,y);
        for(int i = 0; i < listeJeton.size(); i++){
            if(listeJeton.get(i).getPosition().equals(pos)){
                this.listeJeton.remove(listeJeton.get(i));
            }
        }
        
    }

    public void addJeton(Jeton jeton) {
        this.listeJeton.add(jeton);
    }

    public ArrayList<Jeton> getListeJeton() {
        return this.listeJeton;
    }
}
