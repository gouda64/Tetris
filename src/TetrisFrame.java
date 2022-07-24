import java.awt.*;
import javax.swing.*;

public class TetrisFrame extends JFrame
{
    public TetrisFrame() {
        this.setTitle("Tetris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());

        TetrisPanel tetris = new TetrisPanel();
        this.add(tetris);
        this.add(new SidePanel(tetris.tetris));

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

