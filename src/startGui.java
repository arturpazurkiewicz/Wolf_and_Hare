import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

/**
 * Klasa tworząca gui konfiguracyjne
 */
public class startGui {
    /**
     * Panel na którym ustawiane są obiekty
     */
    JPanel mainPanel;
    /**
     * Przycisk zatwierdzający
     */
    private JButton agreeButton;
    /**
     * Miejsce do wpisywania ilości wierszy
     */
    private JTextField mField;
    /**
     * Miejsce do wpisywania ilości kolumn
     */
    private JTextField nField;
    private JTextField k;
    /**
     * Główna ramka ustawiania
     */
    private JFrame frame;

    /**
     * Inicjalizacja klasy
     * @param frame ramka główna
     */
    public startGui(JFrame frame) {
        this.frame=frame;
        agreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        MyKey myKey = new MyKey();
        mField.addKeyListener(myKey);
        nField.addKeyListener(myKey);
        k.addKeyListener(myKey);


        agreeButton.addActionListener(new ActionListener() {
            /**
             * Otiwera JOptionPane a następnie uruchamia program Hunting i zamyka startGui
             * @see Hunting
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    NumberFormat format = NumberFormat.getInstance();
                    NumberFormatter formatter = new NumberFormatter(format);
                    formatter.setValueClass(Integer.class);
                    formatter.setMinimum(0);
                    formatter.setMaximum(Integer.valueOf(mField.getText()) * Integer.valueOf(nField.getText()) - 1);
                    formatter.setAllowsInvalid(false);
                    formatter.setCommitsOnValidEdit(true);
                    JFormattedTextField field = new JFormattedTextField(formatter);
                    if ((Integer.valueOf(mField.getText()) * Integer.valueOf(nField.getText()) - 1)==0) {
                        field.setValue(0);
                    }else{
                        field.setValue(1);
                    }
                    JOptionPane.showConfirmDialog(mainPanel, field, "Podaj ilość zajęcy", JOptionPane.DEFAULT_OPTION);
                    System.out.println(field);
                    Hunting hunting = new Hunting(Integer.valueOf(mField.getText()), Integer.valueOf(nField.getText()), (int) field.getValue(),Integer.valueOf(k.getText()));
                    hunting.initialization();
                    frame.dispose();
                }catch (Exception s){
                    JOptionPane.showMessageDialog(mainPanel,"Błędne dane\nPamiętaj, że obydwie kolumny muszą być dodatnie", "Błąd wprowadzania danych", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        agreeButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    agreeButton.doClick();
                }
            }
        });
    }

    /**
     * Nadpisuje listener klawiszy dla pól. Pozwala na wpisanie jedynie cyfr
     */
    class MyKey extends KeyAdapter{
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
                e.consume();
            }
        }
    }
}
