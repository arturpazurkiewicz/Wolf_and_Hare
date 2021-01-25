import javax.swing.*;
import java.awt.*;
import java.awt.font.ImageGraphicAttribute;
import java.util.ArrayList;

/**
 * Klasa tworząca wilka
 */
public class Wolf implements Runnable{
    /**
     * Pozycja w osi m
     */
    int wolfX;
    /**
     * Pozycja w osi n
     */
    int wolfY;

    /**
     * Inicjalizacja wilka w losowym miejscu
     */
    Wolf(){
        wolfX= Hunting.randomNumber(Hunting.m);
        wolfY=Hunting.randomNumber(Hunting.n);
        Hunting.meadow[wolfX][wolfY]=2;
    }

    /**
     * Funkcja wybierająca najbliższego losowego zająca
     * @return zwraca najbliższego zająca
     * @see Hare
     */
    synchronized Hare chooseHare(){
        ArrayList<Hare> selected = new ArrayList<>();
        loop:
        for (int i = 1; i < Math.max(Hunting.m,Hunting.n); i++) {
            for (int j = 0; j < Hunting.flockOfHare.size(); j++) {
                if (Hunting.flockOfHare.get(j).disanceInMoves(this)==i)
                    selected.add(Hunting.flockOfHare.get(j));
            }
            if (selected.size()!=0)
                break loop;
        }
        Hare selectedHare = selected.get(Hunting.randomNumber(selected.size()));
        //System.out.println("Wybrano "+selectedHare.x+" "+selectedHare.y);
        return selectedHare;
    }

    /**
     * Funkcja przesuwająca wilka w stronę zająca. W przypadku gdy wilk jest wystarczająco blisko zjada zająca
     * @param hare zając w kierunku którego wilk ma się przesunąć
     * @see Hare
     */
    synchronized void moveToHare(Hare hare){
        int disanceInMoves = hare.disanceInMoves(this);
        if (hare.disanceInMoves(this)==1) {
                move(hare.x - wolfX, hare.y - wolfY);
                hare.dead = true;
                System.out.println("!!!!zjada");
                Hunting.meadow[wolfX][wolfY] = 2;
                Hunting.flockOfHare.remove(hare);
                Hunting.panel.repaint();
            try {
                Thread.sleep((Hunting.k/2+Hunting.randomNumber(Hunting.k))*4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            int dy = 0;
            int dx = 0;
            ArrayList<Point> moves = (ArrayList<Point>) Hunting.possibleMoves.clone();
            while (moves.size() > 0) {
                Point s = moves.get(Hunting.randomNumber(moves.size()));
                dx = s.x;
                dy = s.y;
                if (0 <= dx + wolfX && dx + wolfX < Hunting.m && 0 <= dy + wolfY && dy + wolfY < Hunting.n) {
                    move(dx, dy);
                    if (disanceInMoves > hare.disanceInMoves(this)) {
                        //System.out.println("Jest");
                        break;
                    } else {
                        move(-dx, -dy);
                        moves.remove(s);
                    }
                } else {
                    moves.remove(s);
                }
            }
            Hunting.panel.repaint();
        }
    }

    /**
     * Funkcja przesuwająca wilka o zadaną wartość
     * @param dx przesunięcie w osi m
     * @param dy przesunięcie w osi n
     */
    synchronized void move(int dx,int dy) {
        Hunting.meadow[wolfX][wolfY] = 0;
        wolfX += dx;
        wolfY += dy;
        Hunting.meadow[wolfX][wolfY] = 2;
            //System.out.println("Wilk jest w" + wolfX + " " + wolfY);
    }

    /**
     * Wątek obiektu wilk. Ma za zadanie zjedzenie wsystkich zajęcy
     * @see Hare
     */
    @Override
    public void run() {
        while (Hunting.flockOfHare.size()>0){
       // while (true){
            try {
                Thread.sleep(Hunting.k/2+Hunting.randomNumber(Hunting.k));
                {moveToHare(chooseHare());}
                    //System.out.println("1");
            } catch (InterruptedException e) {
                   // System.out.println("2");
                e.printStackTrace();
            }catch (IndexOutOfBoundsException e){
                break;
            }
            Thread.yield();
        }
        Hunting.meadow[wolfX][wolfY] = 2;
        Hunting.panel.repaint();
        System.out.println("KONIEC");
        JLabel icon = new JLabel(new ImageIcon("wolf.jpg"));
        JOptionPane.showMessageDialog(Hunting.frame,icon,"KONIEC",JOptionPane.DEFAULT_OPTION);
    }
}