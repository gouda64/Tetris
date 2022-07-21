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
    private javax.swing.Timer timer;

    public SidePanel(TetrisPanel tetris) {
        this.tetris = tetris;
        this.setPreferredSize(new Dimension(150, 600));
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel highText = new JLabel("High score: ");
        JLabel scoreText = new JLabel("Score: ");
        JLabel lineText = new JLabel("Lines cleared: ");
        JLabel levelText = new JLabel("Level: ");
        this.add(highText);
        this.add(highScoreLabel);
        this.add(levelText);
        this.add(levelLabel);
        this.add(scoreText);
        this.add(scoreLabel);
        this.add(lineText);
        this.add(linesLabel);
        start();
    }

    public void start() {
        timer = new javax.swing.Timer(50, this);
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
    }
}
