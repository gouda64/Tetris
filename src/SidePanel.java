import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SidePanel extends JPanel implements ActionListener
{
    private TetrisPanel tetris;
    private int score = 0;
    private int lines = 0;
    private int highScore = 0;
    private int level = 0;
    private JLabel scoreLabel = new JLabel("" + score);
    private JLabel linesLabel = new JLabel("" + lines);
    private JLabel highScoreLabel = new JLabel("" + highScore);
    private JLabel levelLabel = new JLabel("" + level);
    private JLabel nextLabel = new JLabel("");
    private int nextPiece;
    private javax.swing.Timer timer;

    public SidePanel(TetrisPanel tetris) {
        this.tetris = tetris;
        this.setPreferredSize(new Dimension(150, 600));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(new JLabel("High score: "));
        this.add(highScoreLabel);
        this.add(new JLabel("Level: "));
        this.add(levelLabel);
        this.add(new JLabel("Score: "));
        this.add(scoreLabel);
        this.add(new JLabel("Lines cleared: "));
        this.add(linesLabel);
        this.add(new JLabel(" "));
        this.add(new JLabel("Next piece:"));
        this.add(new JLabel(" "));
        this.add(nextLabel);

        start();
    }

    public void start() {
        timer = new javax.swing.Timer(2, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (tetris.score != score) {
            score = tetris.score;
            scoreLabel.setText("" + score);
        }
        if (tetris.linesCleared != lines) {
            lines = tetris.linesCleared;
            linesLabel.setText("" + lines);
        }
        if (tetris.highScore != highScore) {
            highScore = tetris.highScore;
            highScoreLabel.setText("" + highScore);
        }
        if (tetris.level != level) {
            level = tetris.level;
            levelLabel.setText("" + level);
        }
        if (tetris.nextBlock != nextPiece) {
            nextPiece = tetris.nextBlock;
            ImageIcon img = new ImageIcon("./stuff/block" + nextPiece + ".png");
            Image rzImg = img.getImage().getScaledInstance((int) (img.getIconWidth()*2.0/3), (int) (img.getIconHeight()*2.0/3), Image.SCALE_SMOOTH);

            nextLabel.setIcon(new ImageIcon(rzImg));
        }
    }
}
