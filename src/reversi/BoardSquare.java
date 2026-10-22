package reversi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BoardSquare extends JButton{
	Color drawColor;
	Color borderColor;
	int borderSize;
	IController controller;
	IModel model;
	int player;
	//to store the squares coords
	int x;
	int y;
	//to store which frame it is so they can show reversed view
	int frame;
	
	public BoardSquare(IController controller, IModel model, int x, int y, int frame) {
		this.controller = controller;
		this.model = model;
		this.x = x;
		this.y = y;
		this.player = model.getPlayer();
		this.frame = frame; 
		drawColor = Color.green;
		borderColor = Color.black;
		borderSize = 3; 
		setMinimumSize(new Dimension(30,30));
		setPreferredSize(new Dimension(50,50));
		addActionListener(new ButtonPress());
	}
	
	public void paintComponent(Graphics g) {
		//Border
		g.setColor(borderColor);
		g.fillRect(0, 0, 50, 50);
		//Rectangle
		g.setColor(drawColor);
		g.fillRect(borderSize, borderSize, 50-(borderSize*2), 50-(borderSize*2));
		if (frame == 1) {
			paintCircle1(g); 
		}
		else {
			paintCircle2(g);
		}
	}
	
	//Player 1 = White 
	//Player 2 = Black
	
	public void paintCircle1(Graphics g) {
		int color = model.getBoardContents(x, y);
		if (color == 1) {  
			g.setColor(Color.BLACK);
			g.drawOval(2, 2, 46, 46);
			g.setColor(Color.WHITE);
			g.fillOval(2, 2, 46, 46);
		}
		else if (color == 2){
			g.setColor(Color.WHITE);
			g.drawOval(2, 2, 46, 46);
			g.setColor(Color.BLACK);
			g.fillOval(2, 2, 46, 46);
		}
		else {
		}
	}
	
	public void paintCircle2(Graphics g) {
		int width = model.getBoardWidth() - 1;
		int height = model.getBoardHeight() - 1;
		int color = model.getBoardContents(width - x, height - y);
		if (color == 1) {  
			g.setColor(Color.BLACK);
			g.drawOval(2, 2, 46, 46);
			g.setColor(Color.WHITE);
			g.fillOval(2, 2, 46, 46);
		}
		else if (color == 2){
			g.setColor(Color.WHITE);
			g.drawOval(2, 2, 46, 46);
			g.setColor(Color.BLACK);
			g.fillOval(2, 2, 46, 46);
		}
		else {	
		}
	}
	
	
	
	public class ButtonPress implements ActionListener{
		@Override 
		public void actionPerformed(ActionEvent e) {
			if (frame == 1) {
				controller.squareSelected(frame, x, y);
			}
			else if (frame == 2) { //Have implemented this to get it to correctly show the values on reverse board
				int width = model.getBoardWidth() - 1;
				int height = model.getBoardHeight() - 1;
				controller.squareSelected(frame, width - x, height - y);
			}
		}
	}
	
}
