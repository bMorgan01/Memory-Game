import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class GridDialog {
    private JDialog dialog = new JDialog();

    private JTextField name1 = new JTextField("Player 1"), name2 = new JTextField("Player 2");
    private SpinnerModel cards = new SpinnerNumberModel(12, 6, (new File("icons").list().length * 2) - 2, 2);
    private JSpinner numCards = new JSpinner(cards);

    public GridDialog(String str1, String str2, int cards) {
        name1.setText(str1);
        name2.setText(str2);
        numCards.setValue(cards);
        createDialog();
    }

    public GridDialog() {
        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }*/
        createDialog();
    }

    public void createDialog() {
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(2, 5, 10, 5);
        JLabel title = new JLabel("Board Size Picker", JLabel.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, title.getFont().getSize() + 2));
        dialog.add(title, c);

        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(2, 5, 2, 5);
        dialog.add(new JLabel("Name 1:", JLabel.RIGHT), c);
        dialog.add(name1, c);

        c.gridy = 2;
        dialog.add(new JLabel("Name 2:", JLabel.RIGHT), c);
        dialog.add(name2, c);

        c.gridy = 3;
        dialog.add(new JLabel("# of Cards:", JLabel.RIGHT), c);
        dialog.add(numCards, c);

        c.gridy = 4;
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Main(name1.getText(), name2.getText(), factor((Integer)numCards.getValue())[0], factor((Integer)numCards.getValue())[1]);
                dialog.dispose();
            }
        });
        dialog.add(okButton, c);

        c.gridx = 1;
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(cancelButton, c);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(dim.width/2-dialog.getSize().width/2, dim.height/2-dialog.getSize().height/2);
    }

    private int[] factor(int num) {
        double fac1 = 1, fac2;
        double lowest = num;
        int ans[] = {0, 0};

        while(fac1 <= Math.sqrt(num)) {
            fac2 = num/fac1;

            if (trunc(fac2) == fac2) {
                if ((fac1 + fac2) < lowest) {
                    lowest = fac1 + fac2;
                    ans[0] = (int)fac1;
                    ans[1] = (int)fac2;
                }
            }

            fac1++;
        }

        return ans;
    }

    public static double trunc(double d) {
        return ((long)(d * 1)) / 1;
    }
}
