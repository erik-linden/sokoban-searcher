import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Guireplay extends JFrame {
	Guireplay(State solution) // the frame constructor method
	{
		super("Sokoban Solution Display: ");

		setVisible(true); // display this frame

		setBounds(100, 100, 600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane(); // inherit main frame


		con.add(getPlot(solution));
		// customize panel here


		while (solution.parent != null) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			solution = solution.parent;
			con.removeAll();
			con.add(getPlot(solution));
			revalidate();
		}
	}

	private JPanel getPlot(State state)
	{
		JPanel pane = new JPanel(new GridLayout(Board.rows, Board.cols));
		for (byte i = 1; i <= Board.rows; i++) {
			for (byte j = 1; j <= Board.cols; j++) {
				JButton entity = new JButton();
				if (state.playerAt(i, j))
					entity.setBackground(Color.BLUE);
				else if (state.boxAt(i, j) && Board.goalAt(i, j))
					entity.setBackground(new Color(205,133,63));
				else if (state.boxAt(i, j))
					entity.setBackground(new Color(235,173,83));
				else if (Board.goalAt(i, j))
					entity.setBackground(Color.YELLOW);
				else if (Board.wallAt(i, j))
					entity.setBackground(Color.DARK_GRAY);
				else
					entity.setBackground(Color.LIGHT_GRAY);

				pane.add(entity);
			}
		}
		return pane;
	}
}
