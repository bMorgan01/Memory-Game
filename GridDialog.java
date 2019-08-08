import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GridDialog {
    private JDialog dialog = new JDialog();

    private JTextField name1 = new JTextField("Player 1"), name2 = new JTextField("Player 2");
    private SpinnerModel cards = new SpinnerNumberModel(12, 6, 100, 2);
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

        SpinnerModel players = new SpinnerNumberModel(2, 1, 4, 1);
        JSpinner numPlayers = new JSpinner(players);

        GridBagConstraints mainGBC = new GridBagConstraints();

        mainGBC.gridy = 0;
        mainGBC.gridwidth = 2;
        mainGBC.fill = GridBagConstraints.BOTH;
        mainGBC.insets = new Insets(2, 5, 2, 5);
        JLabel numPlayerTitle = new JLabel("Player Picker", JLabel.CENTER);
        numPlayerTitle.setFont(new Font(numPlayerTitle.getFont().getName(), Font.BOLD, numPlayerTitle.getFont().getSize() + 2));
        dialog.add(numPlayerTitle, mainGBC);

        mainGBC.gridy = 1;
        mainGBC.gridwidth = 1;
        dialog.add(new JLabel("# of Players:"), mainGBC);
        dialog.add(numPlayers, mainGBC);

        mainGBC.gridy = 2;
        JButton playersOk = new JButton("Ok");
        playersOk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int players = (Integer)numPlayers.getValue();
                dialog.getContentPane().removeAll();

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

                if (players > 1) {
                    c.gridy = 2;
                    dialog.add(new JLabel("Name 2:", JLabel.RIGHT), c);
                    dialog.add(name2, c);
                }

                c.gridy = players + 1;
                dialog.add(new JLabel("# of Cards:", JLabel.RIGHT), c);
                dialog.add(numCards, c);

                c.gridy = players + 2;
                JButton okButton = new JButton("Ok");
                okButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dialog.getContentPane().removeAll();

                            GridBagConstraints gbc = new GridBagConstraints();

                            gbc.gridy = 0;
                            gbc.gridwidth = 2;
                            gbc.fill = GridBagConstraints.BOTH;
                            gbc.insets = new Insets(2, 5, 2, 5);
                            JLabel playerTitle = new JLabel("Game Mode Picker", JLabel.CENTER);
                            playerTitle.setFont(new Font(playerTitle.getFont().getName(), Font.BOLD, playerTitle.getFont().getSize() + 2));
                            dialog.add(playerTitle, gbc);

                            if (players > 1) {
                                gbc.gridy = 1;
                                JButton localMultiplayerButton = new JButton("Local Multiplayer");
                                localMultiplayerButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new Main(name1.getText(), name2.getText(), factor((Integer)numCards.getValue())[0], factor((Integer)numCards.getValue())[1], players);
                                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    }
                                });
                                dialog.add(localMultiplayerButton, gbc);

                                gbc.gridy = 2;
                                JButton LANMultiplayerButton = new JButton("LAN Multiplayer");
                                LANMultiplayerButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new Main(name1.getText(), name2.getText(), factor((Integer)numCards.getValue())[0], factor((Integer)numCards.getValue())[1], players);
                                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    }
                                });
                                dialog.add(LANMultiplayerButton, gbc);

                                gbc.gridy = 3;
                                JButton onlineMultiplayerButton = new JButton("Online Multiplayer");
                                onlineMultiplayerButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    }
                                });
                                dialog.add(onlineMultiplayerButton, gbc);
                            } else {
                                gbc.gridy = 1;
                                JButton singleplayerButton = new JButton("Singleplayer");
                                singleplayerButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        new Main(name1.getText(), name2.getText(), factor((Integer)numCards.getValue())[0], factor((Integer)numCards.getValue())[1], players);
                                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    }
                                });
                                dialog.add(singleplayerButton, gbc);

                                gbc.gridy = 2;
                                JButton comButton = new JButton("Player v. COM");
                                comButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                        dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    }
                                });
                                dialog.add(comButton, gbc);
                            }

                            if (players > 1) gbc.gridy = 4;
                            else gbc.gridy = 3;
                            gbc.gridwidth = 1;
                            gbc.insets = new Insets(5, 5, 2, 5);
                            JButton backPlayerButton = new JButton("Back");
                            backPlayerButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                                    new GridDialog(name1.getText(), name2.getText(), factor((Integer)numCards.getValue())[0] * factor((Integer)numCards.getValue())[1]);
                                }
                            });
                            dialog.add(backPlayerButton, gbc);

                            JButton cancelPlayerButton = new JButton("Cancel");
                            cancelPlayerButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    dialog.dispose();
                                }
                            });
                            dialog.add(cancelPlayerButton, gbc);

                            dialog.revalidate();
                            dialog.repaint();
                            dialog.pack();
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

                dialog.revalidate();
                dialog.repaint();
                dialog.pack();
            }
        });

        dialog.add(playersOk, mainGBC);
        dialog.add(new JButton("Cancel"), mainGBC);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
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
