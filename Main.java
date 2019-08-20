import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.util.Random;
import java.io.IOException;
import java.time.Duration;

public class Main {
    int rows, cols;
    String name1, name2;

    JFrame frame = new JFrame("Memory");
    JPanel infoPanel = new JPanel();
    JPanel panel = new JPanel();

    Random rand = new Random();

    String labelNames[] = {"Car", "Elephant", "Phone", "Tree", "Person", "Computer", "Toy", "Paper", "Book", "Desk", "Bed", "Glasses", "Keyboard", "Ring", "Fan", "Sock", "Sign", "Eraser",
                           "Pencil", "Bow", "Cat", "Boat", "Fork", "Spoon", "Door", "Clock", "Bottle", "Purse", "Brush", "Camera", "Money", "Bread", "Screw", "Mirror", "Cork", "Sponge",
                           "Banana", "Bowl", "Tomato", "Key", "Nail", "Hammer", "Chain", "Table", "Towel", "Cord", "Thread", "Chalk", "Baby", "Candy"};

    JPanel panels[][];
    JButton btns[][];
    JLabel labels[][];
    JLabel cardFronts[][];

    Timer gameTimer;
    JLabel timerLabel;
    long lastTickTime;

    JLabel player1Games = new JLabel("" + GameRunner.player1Won), player2Games = new JLabel("" + GameRunner.player2Won);

    JLabel player1 = new JLabel(), player2 = new JLabel();
    JLabel player1Turn, player2Turn;
    int player1Score = 0, player2Score = 0;
    int turn = 1;

    JButton selected[] = {new JButton(), new JButton()};
    int picked = 0;

    Timer timer = new Timer(2000, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            checkMatch();
        }
    });

    public Main(String name1, String name2, int r, int c) {
        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }*/

        rows = r;
        cols = c;

        timerLabel = new JLabel(String.format("%04d:%02d:%02d.%03d", 0, 0, 0, 0));

        gameTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long runningTime = System.currentTimeMillis() - lastTickTime;
                Duration duration = Duration.ofMillis(runningTime);
                long hours = duration.toHours();
                duration = duration.minusHours(hours);
                long minutes = duration.toMinutes();
                duration = duration.minusMinutes(minutes);
                long millis = duration.toMillis();
                long seconds = millis / 1000;
                timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        });

        try {
            player1Turn = new JLabel(new ImageIcon(ImageIO.read(new File("icons\\dot.png")).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
            player2Turn = new JLabel(new ImageIcon());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.name1 = name1;
        this.name2 = name2;

        player1.setText(name1 + ": 0");
        player2.setText(name2 + ": 0");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenSize = new Dimension((int)((((screenSize.getHeight()*4)/5)/rows)*2)/3, (int)((screenSize.getHeight()*4)/5)/rows);

        if (Toolkit.getDefaultToolkit().getScreenSize().getWidth() < ((cols * screenSize.getWidth()) + ((cols + 1) * 5))) {
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenSize = new Dimension((int)((screenSize.getWidth()*5)/6)/cols, (int)((((screenSize.getWidth()*5)/6)/cols)*3)/2);
        }

        int fontSize;
        int largestWord = 0;
        JLabel testFont = new JLabel();

        for (int i = 0; i < (rows*cols)/2; i++) {
            if (testFont.getFontMetrics(testFont.getFont()).stringWidth(labelNames[i]) > largestWord) largestWord = testFont.getFontMetrics(testFont.getFont()).stringWidth(labelNames[i]);
        }

        fontSize = (int)(12 * ((double)screenSize.getWidth()/(double)largestWord));
        fontSize = Math.min(fontSize, (int)screenSize.getHeight());

        Font useFont = new Font(testFont.getFont().getName(), Font.PLAIN, fontSize);

        panels = new JPanel[rows][cols];
        btns = new JButton[rows][cols];
        labels = new JLabel[rows][cols];
        cardFronts = new JLabel[rows][cols];

        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[0].length; j++) {
                panels[i][j] = new JPanel();
            }
        }

        for (int i = 0; i < btns.length; i++) {
            for (int j = 0; j < btns[0].length; j++) {
                btns[i][j] = new JButton();
            }
        }

        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[0].length; j++) {
                labels[i][j] = new JLabel();
            }
        }

        for (int i = 0; i < cardFronts.length; i++) {
            for (int j = 0; j < cardFronts[0].length; j++) {
                cardFronts[i][j] = new JLabel();
            }
        }

        timer.setRepeats(false);

        player1Games.setMinimumSize(new Dimension(25, 32));
        player1Games.setPreferredSize(new Dimension(25, 32));
        player1Games.setMaximumSize(new Dimension(25, 32));
        player2Games.setMinimumSize(new Dimension(25, 32));
        player2Games.setPreferredSize(new Dimension(25, 32));
        player2Games.setMaximumSize(new Dimension(25, 32));
        player1Games.setFont(new Font(player1Games.getFont().getName(), Font.BOLD, player1Games.getFont().getSize() + 30));
        player2Games.setFont(new Font(player2Games.getFont().getName(), Font.BOLD, player2Games.getFont().getSize() + 30));

        player1Turn.setMinimumSize(new Dimension(15, 15));
        player1Turn.setPreferredSize(new Dimension(15, 15));
        player1Turn.setMaximumSize(new Dimension(15, 15));
        player2Turn.setMinimumSize(new Dimension(15, 15));
        player2Turn.setPreferredSize(new Dimension(15, 15));
        player2Turn.setMaximumSize(new Dimension(15, 15));

        player1.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 10));
        player2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 7));
        player1.setFont(new Font(player1.getFont().getName(), Font.BOLD, player1.getFont().getSize() + 2));
        player2.setFont(new Font(player2.getFont().getName(), Font.BOLD, player2.getFont().getSize() + 2));

        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints infoGBC = new GridBagConstraints();

        infoGBC.gridheight = 2;
        infoGBC.insets = new Insets(0, 5, 0, 5);

        infoPanel.add(player1Games,infoGBC);

        infoGBC.gridheight = 1;
        infoGBC.insets = new Insets(0, 0, 0, 0);

        infoPanel.add(player1Turn, infoGBC);
        infoPanel.add(player1, infoGBC);
        infoPanel.add(player2, infoGBC);
        infoPanel.add(player2Turn, infoGBC);

        infoGBC.gridheight = 2;
        infoGBC.insets = new Insets(0, 5, 0, 5);

        infoPanel.add(player2Games, infoGBC);

        infoGBC.gridy = 1;
        infoGBC.gridx = 2;
        infoGBC.gridwidth = 2;
        infoGBC.insets = new Insets(0, 0, 0, 0);

        infoPanel.add(timerLabel, infoGBC);
        infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Scores"));

        panel.setLayout(new GridLayout(rows, cols, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        int row;
        int column;

        for (int i = 0; i < (rows*cols)/2; i++) {
            for (int j = 0; j < 2; j++) {
                do {
                    row = rand.nextInt(rows);
                    column = rand.nextInt(cols);
                } while (!labels[row][column].getText().equals(""));
                labels[row][column].setText(labelNames[i]);

                try {
                    labels[row][column].setIcon(new ImageIcon(ImageIO.read(new File("icons\\" + labelNames[i] + ".png")).getScaledInstance((int)screenSize.getWidth() - 10, (int)screenSize.getWidth() - 10, java.awt.Image.SCALE_SMOOTH)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                panels[i][j].setLayout(new OverlayLayout(panels[i][j]));

                btns[i][j].setBorder(BorderFactory.createEmptyBorder());
                btns[i][j].setContentAreaFilled(false);
                try {
                    btns[i][j].setIcon(new ImageIcon(ImageIO.read(new File("card.png")).getScaledInstance((int)screenSize.getWidth(), (int)screenSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    cardFronts[i][j].setIcon(new ImageIcon(ImageIO.read(new File("cardFront.png")).getScaledInstance((int)screenSize.getWidth(), (int)screenSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                btns[i][j].addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selected[picked] = (JButton) e.getSource();
                            removeBtn(selected[picked]);
                            picked++;
                            if (picked == 2) {
                                int row0, column0;
                                int row1, column1;

                                row0 = findIndexRow(selected[0], rows, cols);
                                row1 = findIndexRow(selected[1], rows, cols);

                                column0 = findIndexColumn(selected[0], rows, cols);
                                column1 = findIndexColumn(selected[1], rows, cols);

                                if (!labels[row0][column0].getText().equals(labels[row1][column1].getText())) {
                                    timer.start();
                                } else {
                                    checkMatch();
                                }
                            }
                        }
                    });
                panels[i][j].add(btns[i][j]);

                labels[i][j].setFont(useFont);
                labels[i][j].setPreferredSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
                labels[i][j].setMaximumSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
                labels[i][j].setMinimumSize(new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight()));
                labels[i][j].setHorizontalAlignment(JLabel.CENTER);
                labels[i][j].setHorizontalTextPosition(JLabel.CENTER);
                labels[i][j].setVerticalTextPosition(JLabel.TOP);
                panels[i][j].add(labels[i][j]);
                panels[i][j].add(cardFronts[i][j]);
                panel.add(panels[i][j]);
            }
        }

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        frame.add(infoPanel, gbc);
        gbc.gridy = 1;

        frame.add(panel, gbc);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        lastTickTime = System.currentTimeMillis();
        gameTimer.start();
    }

    public void checkMatch() {
        int row0, column0;
        int row1, column1;

        row0 = findIndexRow(selected[0], rows, cols);
        row1 = findIndexRow(selected[1], rows, cols);

        column0 = findIndexColumn(selected[0], rows, cols);
        column1 = findIndexColumn(selected[1], rows, cols);

        if (!labels[row0][column0].getText().equals(labels[row1][column1].getText())) {
            selected[0].setVisible(true);
            selected[1].setVisible(true);
            if (turn == 1) turn++;
            else turn--;
        } else {
            if (turn == 1) {
                player1Score++;
                player1.setText(name1 + ": " + player1Score);
            } else {
                player2Score++;
                player2.setText(name2 + ": " + player2Score);
            }
        }
        picked = 0;

        if (turn == 1) {
            try {
                player1Turn.setIcon(new ImageIcon(ImageIO.read(new File("icons\\dot.png")).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
                player2Turn.setIcon(new ImageIcon());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                player2Turn.setIcon(new ImageIcon(ImageIO.read(new File("icons\\dot.png")).getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH)));
                player1Turn.setIcon(new ImageIcon());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ((player1Score + player2Score == (rows*cols)/2) || (player1Score > ((rows*cols)/2)/2) || (player2Score > ((rows*cols)/2)/2)) {
            String winner;

            gameTimer.stop();
            if (player1Score > player2Score) {
                winner = name1 + " wins!";
                GameRunner.player1Won++;
                player1Games.setText("" + GameRunner.player1Won);
            } else if (player2Score > player1Score) {
                winner = name2 + " wins!";
                GameRunner.player2Won++;
                player2Games.setText("" + GameRunner.player2Won);
            } else {
                winner = "It's a tie!";
            }

            Object[] options = {"New Game", "Cancel"};
            if (JOptionPane.showOptionDialog(frame, winner, winner, JOptionPane.PLAIN_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == 0) {
                new GridDialog(name1, name2, rows*cols);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    public void removeBtn(JButton b) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Component components[] = panels[i][j].getComponents();
                if (contains(components, b)) {
                    btns[i][j].setVisible(false);
                    panels[i][j].revalidate();
                    panels[i][j].repaint();
                }
            }
        }
    }

    public boolean contains(Component components[], JButton b) {
        for (Component c: components) {
            if (c == b) {
                return true;
            }
        }

        return false;
    }

    public int findIndexRow(JButton b, int r, int c) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (btns[i][j] == b) return i;
            }
        }

        return -1;
    }

    public int findIndexColumn(JButton b, int r, int c) {
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (btns[i][j] == b) return j;
            }
        }

        return -1;
    }
}
