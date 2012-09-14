import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Guireplay extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Guireplay(State state) // the frame constructor method
	{
		super("Sokoban Solution Display: ");

		setVisible(true); // display this frame

		setBounds(100, 100, 600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane(); // inherit main frame

		Stack<State> solutionStack = new Stack<State>();


		solutionStack.push(state);
		while (state.parent != null) {
			state = state.parent;
			solutionStack.push(state);
		}

		State parent = solutionStack.pop();
		while(solutionStack.size()>0) {
			BoardPosition playerPos = parent.playerPosition;
			con.add(getPlot(parent, playerPos));
			revalidate();
			pause();

			State child = solutionStack.pop();

			byte beforePushRow = child.playerPosition.row;
			byte beforePushCol = child.playerPosition.col;

			beforePushRow -= BoardConnectivity.rowMask[child.lastMove];
			beforePushCol -= BoardConnectivity.colMask[child.lastMove];

			BoardPosition endPos = new BoardPosition(beforePushRow, beforePushCol);
			Vector<Integer> movesList = parent.connectivity.backtrackPathMoves(endPos, playerPos);

			byte startRow = playerPos.row;
			byte startCol = playerPos.col;

			for(int i = movesList.size()-1; i >= 0; i--) {
				startRow += BoardConnectivity.rowMask[movesList.get(i)];
				startCol += BoardConnectivity.colMask[movesList.get(i)];

				playerPos = new BoardPosition(startRow, startCol);
				con.removeAll();
				con.add(getPlot(parent, playerPos));
				revalidate();
				pause();
			}

			parent = child;
		}
		con.removeAll();
		con.add(getPlot(parent, parent.playerPosition));
		revalidate();
		pause();
	}

	private JPanel getPlot(State state, BoardPosition playerPos)
	{
		JPanel pane = new JPanel(new GridLayout(Board.rows, Board.cols));
		for (byte i = 1; i <= Board.rows; i++) {
			for (byte j = 1; j <= Board.cols; j++) {
				JButton entity = new JButton();
				if (playerPos.equals(new BoardPosition(i, j)))
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

	private void pause() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
