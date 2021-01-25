import java.awt.*;
import java.util.ArrayList;

/**
 * Klasa zajęcy
 */
public class Hare implements Runnable{
    /**
     * Pozycja w osi m
     */
    int x;
    /**
     * Pozycja w osi n
     */
    int y;
    /**
     * Informuje czy zając jeszcze żyje
     */
    boolean dead=false;
    Hare(){
        boolean bad=true;
        while (bad){
            x= Hunting.randomNumber(Hunting.m);
            y=Hunting.randomNumber(Hunting.n);
            if (Hunting.meadow[x][y]==0){
                Hunting.meadow[x][y]=1;
                bad=false;
            }
        }
    }

    /**
     * Funkcja licząca odległość zająca od wilka
     * @param wolf wilk względem którego odległość zostanie obliczona
     * @return odległość w ilościach ruchu
     * @see Wolf
     */
    int disanceInMoves(Wolf wolf){
        if (dead)
            return 999999;
        return Math.max(Math.abs(wolf.wolfX-x),Math.abs(wolf.wolfY-y));
    }

    /**
     * Funkcja przesuwająca zająca w taki sposób, by owca była dalej od wilka
     * @param wolf wilk od którego zająca ucieka
     * @see Wolf
     */
    synchronized void runFromWolf(Wolf wolf){
        int disanceInMoves = disanceInMoves(wolf);
        int dy;
        int dx;
        boolean t=false;
        ArrayList<Point> moves =(ArrayList<Point>) Hunting.possibleMoves.clone();
        while (moves.size()>0) {
            Point s = moves.get(Hunting.randomNumber(moves.size()));
            dx = s.x;
            dy = s.y;
           // System.out.println(dx+" w "+dy);
            if (0<=dx+x&&dx+x<Hunting.m&&0<=dy+y&&dy+y<Hunting.n) {
                if (Hunting.meadow[x+dx][y+dy]!=1&&Hunting.meadow[x+dx][y+dy]!=2) {
                    move(dx, dy);
                    if (disanceInMoves < disanceInMoves(wolf)) {
                       // System.out.println("Ucieka");
                       // System.out.println(dx+" owca "+dy);
                        t=true;
                        break;
                    }else {
                        moves.remove(s);
                        move(-dx, -dy);
                    }
                }else {
                    moves.remove(s);
                }
            }else {
                moves.remove(s);
            }
        }
        if (!t){
            moves =(ArrayList<Point>) Hunting.possibleMoves.clone();
            while (moves.size()>0) {
                Point s = moves.get(Hunting.randomNumber(moves.size()));
                dx = s.x;
                dy = s.y;
                // System.out.println(dx+" w "+dy);
                if (0<=dx+x&&dx+x<Hunting.m&&0<=dy+y&&dy+y<Hunting.n) {
                    if (Hunting.meadow[x+dx][y+dy]!=1&&Hunting.meadow[x+dx][y+dy]!=2) {
                        move(dx, dy);
                        if (disanceInMoves == disanceInMoves(wolf)) {
                            //System.out.println("chowa");
                            //System.out.println(dx+" owca "+dy);
                            break;
                        }else {
                            moves.remove(s);
                            move(-dx, -dy);
                        }
                    }else {
                        moves.remove(s);
                    }
                }else {
                    moves.remove(s);
                }
            }
        }
       // System.out.println("Owca jest w " + x + " " + y);
        Hunting.panel.repaint();
    }

    /**
     * Funkcja przesuwająca zająca o zadaną wartość
     * @param dx przesunięcie w osi m
     * @param dy przesunięcie w osi n
     */
    synchronized void move(int dx,int dy) {
        if (!dead) {
           // System.out.println(x + " !!!! " + y);
            Hunting.meadow[x][y] = 0;
            x += dx;
            y += dy;
           // System.out.println(x + " !!!! " + y);
            Hunting.meadow[x][y] = 1;
            //Hunting.meadow[Hunting.wolf.wolfX][Hunting.wolf.wolfY]=2;
        }
    }

    /**
     * Wątek obiektu zając. Ma za zadanie ucieczkę od wilka
     * @see Wolf
     */
    @Override
    public void run() {
        synchronized (this) {
            while (!dead) {
                try {
                    Thread.sleep(Hunting.k/2+Hunting.randomNumber(Hunting.k));
                    runFromWolf(Hunting.wolf);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.yield();
            }
        }
    }
}