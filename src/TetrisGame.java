import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TetrisGame extends JFrame implements ActionListener
{
    public TetrisGame() {
        this.setTitle("Start");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setSize(450, 380);

        JLabel logo = new JLabel(new ImageIcon("./stuff/bad tetris.png"));
        logo.setBounds(0, 0, 450, 175);
        this.getContentPane().add(logo);

        JButton start = new JButton("Start");
        start.setActionCommand("Start");
        start.addActionListener(this);
        start.setBounds(125, 175, 200, 50);
        this.getContentPane().add(start);

        JButton info = new JButton("How To");
        info.setActionCommand("How To");
        info.addActionListener(this);
        info.setBounds(125, 230, 200, 50);
        this.getContentPane().add(info);

        JButton creds = new JButton("Credits");
        creds.setActionCommand("Credits");
        creds.addActionListener(this);
        creds.setBounds(125, 285, 200, 50);
        this.getContentPane().add(creds);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new TetrisGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();

        switch (c) {
            case "Start" -> {
                new TetrisFrame();
                this.dispose();
            }
            case "How To" -> {
                new HowToFrame();
                this.dispose();
            }
            case "Credits" -> {
                new CreditsFrame();
                this.dispose();
            }
        }
    }

    public class HowToFrame extends JFrame implements ActionListener {
        public HowToFrame() {
            this.setTitle("How To");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setLayout(new FlowLayout());
            this.setSize(315, 290);

            this.getContentPane().add(new JLabel("This is a bad replication of classic game Tetris."));
            this.getContentPane().add(new JLabel("Different shaped blocks will fall from the sky."));
            this.getContentPane().add(new JLabel("You can clear lines by filling them up with blocks."));
            this.getContentPane().add(new JLabel("If the blocks stack up to the ceiling, you lose!"));
            this.getContentPane().add(new JLabel("Use the arrow keys to move your block, and"));
            this.getContentPane().add(new JLabel("the down arrow to descend faster."));
            this.getContentPane().add(new JLabel("Press 'z' or the up arrow to rotate your block, "));
            this.getContentPane().add(new JLabel("and press the space bar to land immediately."));
            this.getContentPane().add(new JLabel("Also, press 'p' to pause (but don't try to cheat!)"));

            JButton back = new JButton("Back");
            back.setActionCommand("Back");
            back.addActionListener(this);
            back.setPreferredSize(new Dimension(100, 40));
            this.getContentPane().add(back);

            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Back")) {
                new TetrisGame();
                this.dispose();
            }
        }
    }
    public class CreditsFrame extends JFrame implements ActionListener {
        public CreditsFrame() {
            this.setTitle("Credits");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setLayout(new FlowLayout());
            this.setSize(315, 225);

            this.getContentPane().add(new JLabel("Game concept and mechanics and stuff"));
            this.getContentPane().add(new JLabel("by Alexey Pajitnov and the Tetris company"));
            this.getContentPane().add(new JLabel("(Obnoxious, as I've been told) sounds"));
            this.getContentPane().add(new JLabel("by Zapsplat and Fesliyan Studios."));
            this.getContentPane().add(new JLabel("NO credits for me because I made this"));
            this.getContentPane().add(new JLabel("       halfway and then left it for like a month.       "));


            JButton back = new JButton("Back");
            back.setActionCommand("Back");
            back.addActionListener(this);
            back.setPreferredSize(new Dimension(100, 40));
            this.getContentPane().add(back);

            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Back")) {
                new TetrisGame();
                this.dispose();
            }
        }
    }
}
