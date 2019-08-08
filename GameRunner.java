import javax.swing.*;

public class GameRunner {
    public static int player1Won = 0, player2Won = 0;

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GridDialog();
            }
        });
    }
}
