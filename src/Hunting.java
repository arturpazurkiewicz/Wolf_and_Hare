import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

/**
 * Główna klasa programu symulacji.
 * @author Artur Pazurkiewicz
 * @version 1.0
 * @see Wolf
 * @see startGui
 * @see Hare
 */
public class Hunting extends JFrame {
    /**
     * Długość kolumn
     */
    static int m;
    /**
     * Długość wierszy
     */
    static int n;
    /**
     * Tablica wszystkich zajęcy
     * @see Hare
     */
    static ArrayList<Hare> flockOfHare = new ArrayList<>();
    /**
     * Łąka stworzona z int.
     * 1 = zając
     * 2 = wilk
     */
    static int[][] meadow;
    private JPanel[][] panels;
    /**
     * JPanel z łąką
     */
    static MyJPanel panel;
    /**
     * Wilk
     * @see Wolf
     */
    static Wolf wolf;
    /**
     * Współczynnik określający opóźnienie programu
     */
    static int k;
    /**
     * Wszystkie możliwe ruchy wilka/zająca
     */
    static ArrayList<Point> possibleMoves;
    /**
     * Główna ramka aplikacja
     */
    static Hunting frame;
    private JButton start = new JButton("Start");


    /**
     * Inicjalizacja symulacji
     * @param m Długość kolumn
     * @param n Długość wierszy
     * @param amountFlock Ilość zajęcy
     * @param k opóźnienie
     */
    Hunting(int m,int n,int amountFlock,int k){
        this.k=k;
        meadow= new int[m][n];
        panels = new JPanel[m][n];
        frame=this;
        Hunting.m =m;
        Hunting.n=n;
        possibleMoves = new ArrayList<>();
        for (int i = -1; i <2 ; i++) {
            for (int j = -1; j < 2; j++) {
                possibleMoves.add(new Point(i,j));
            }
        }possibleMoves.remove(4);
        panel = new MyJPanel();
        wolf = new Wolf();
        flockOfHare = Hunting.createFlockOfHare(amountFlock);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        add(panel);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread wolfThread = new Thread(wolf);
                ArrayList<Thread> threads= new ArrayList<>();
                for (Hare hare : flockOfHare) {
                    threads.add(new Thread(hare));
                }threads.add(wolfThread);
                for (Thread thread : threads){
                    thread.start();
                }
                start.setEnabled(false);
            }
        });
        menuBar.add(start);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Klasa rozszerzająca JPanel. Ustawia GridLayout i w każde jego pole wkłada JPanel
     */
    class MyJPanel extends JPanel {
        MyJPanel(){
            start.setEnabled(false);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    panels[i][j]=new JPanel();
                    add(panels[i][j]);
                }
            }
            setLayout(new GridLayout(m,n,5,5));
            start.setEnabled(true);
        }

        /**
         * Kolorując poszczególne JPanele w wyświetla odwzorowanie łąki w {@link MyJPanel panel}
         */
        @Override
        public synchronized void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        panels[i][j].setBackground(Color.GREEN);
                    }
                }
                for (Hare h : flockOfHare) {
                    if (!h.dead)
                        panels[h.x][h.y].setBackground(Color.BLACK);
                }
                panels[wolf.wolfX][wolf.wolfY].setBackground(Color.RED);
            }catch (ConcurrentModificationException e){
            }
        }
    }
    private static Random r = new Random();

    /**
     * Generator liczb losowych
     * @param amount ilość liczb
     * @return liczba losowa z przedziału od 0 do amount-1
     */
    static int randomNumber(int amount){
        try {
            r.nextInt(amount);
        }catch (IllegalArgumentException e) {
            return 0;
        }
        return r.nextInt(amount);
    }

    /**
     * Funkcja tworząca tablice zajęcy
     * @param amount ilość zajęcy
     * @return tablica zajęcy
     */
    static ArrayList<Hare> createFlockOfHare(int amount){
        ArrayList<Hare> flock = new ArrayList<>();
        for (int i=0;i<amount;i++){
            Hare h = new Hare();
            flock.add(h);
        }
        return flock;
    }

    /**
     * Funkcja uruchamiająca symulację
     */
    void initialization(){
        setVisible(true);
        setTitle("Symulacja dla "+flockOfHare.size()+" zajęcy");
        setSize(700,700);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Symulacja");
        startGui gui = new startGui(frame);
        frame.setContentPane(gui.mainPanel);
        frame.setSize(400,150);
        frame.setVisible(true);
    }
}
