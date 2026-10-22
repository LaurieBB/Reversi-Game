package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class GUIView implements IView {
	IModel model;
	IController controller;
	
	JLabel message1 = new JLabel();
	JLabel message2 = new JLabel();
	JTextArea board1 = new JTextArea();
	JTextArea board2 = new JTextArea();
	JFrame frame1 = new JFrame();
	JFrame frame2 = new JFrame();
	
	@Override
	public void initialise(IModel model, IController controller) {
		this.model = model;
		this.controller = controller;	
		int x;
		int y;
		
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame1.setTitle("Player 1");
		frame1.setLocationRelativeTo(null); // centre on screen
		
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame2.setTitle("Player 2 ");
		frame2.setLocationRelativeTo(null); // centre on screen
		
		
		frame1.getContentPane().setLayout(new GridLayout(1,2));
		frame2.getContentPane().setLayout(new GridLayout(1,2));

		board1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5) );
		JPanel p1Panel = new JPanel();
		p1Panel.setBorder( BorderFactory.createLineBorder(Color.LIGHT_GRAY,3) );
		p1Panel.setLayout( new BorderLayout() );
		frame1.add(p1Panel);
		
		board2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5) );
		JPanel p2Panel = new JPanel();
		p2Panel.setBorder( BorderFactory.createLineBorder(Color.LIGHT_GRAY,3) );
		p2Panel.setLayout( new BorderLayout() );
		frame2.add(p2Panel); 

		message1.setFont( new Font( "Arial", Font.BOLD, 20 ));
		message2.setFont( new Font( "Arial", Font.BOLD, 20 ));

		board1.setFont( new Font( "Consolas", Font.BOLD, 20 ));
		board2.setFont( new Font( "Consolas", Font.BOLD, 20 ));
		board1.setPreferredSize(new Dimension(400,330));
		board2.setPreferredSize(new Dimension(400,330));
		
		//Player 1
		// Now we add the 'stuff' for each player to the panel for that player...
		message1.setText("Initial text goes here");
		p1Panel.add(message1,BorderLayout.NORTH);
		
		//This is the Grid Layout created for the buttons to be added to
		JPanel boardPanel1 = new JPanel();
		boardPanel1.setLayout(new GridLayout(model.getBoardHeight(), model.getBoardWidth()));
		boardPanel1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,3) );
		p1Panel.add(boardPanel1, BorderLayout.CENTER);
		for(y=0;y<model.getBoardHeight();y++) {
			for(x=0;x<model.getBoardWidth();x++) {
				boardPanel1.add(new BoardSquare(controller, model, x, y, 1));
			}
		}
		
		JPanel ButPanel1 = new JPanel();
		ButPanel1.setLayout(new GridLayout(2,0));
		p1Panel.add(ButPanel1, BorderLayout.SOUTH);
		
		// AI button
		JButton butAI1 = new JButton("Greedy AI (play white)");
		butAI1.addActionListener( new ActionListener() 
					{ public void actionPerformed(ActionEvent e) { controller.doAutomatedMove(1); } } );
		ButPanel1.add(butAI1);
		
		//Refresh Button
		JButton butRef1 = new JButton("Restart");
		butRef1.addActionListener( new ActionListener() 
					{ public void actionPerformed(ActionEvent e) {controller.startup(); } } );  
		ButPanel1.add(butRef1);
		
		
		//Player 2 
		message2.setText("Initial text goes here");
		p2Panel.add(message2,BorderLayout.NORTH);
		
		//This is the Grid Layout created for the buttons to be added to
		JPanel boardPanel2 = new JPanel();
		boardPanel2.setLayout(new GridLayout(model.getBoardHeight(), model.getBoardWidth()));
		boardPanel2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,3) );
		p2Panel.add(boardPanel2, BorderLayout.CENTER);
		for(y=0;y<model.getBoardHeight();y++) {
			for(x=0;x<model.getBoardWidth();x++) {
				boardPanel2.add(new BoardSquare(controller, model, x, y, 2));
			}
		}
		
		JPanel ButPanel2 = new JPanel();
		ButPanel2.setLayout(new GridLayout(2,0));
		p2Panel.add(ButPanel2, BorderLayout.SOUTH);
		
		// AI button
		JButton butAI2 = new JButton("Greedy AI (play black)");
		butAI2.addActionListener( new ActionListener() 
					{ public void actionPerformed(ActionEvent e) { controller.doAutomatedMove(2); } } );
		ButPanel2.add(butAI2);
		
		//Refresh Button
		JButton butRef2 = new JButton("Restart");
		butRef2.addActionListener(new ActionListener()
				{ public void actionPerformed(ActionEvent e) {controller.startup(); } } ); 
		ButPanel2.add(butRef2);
		
		frame1.pack();
		frame1.setVisible(true);
		
		frame2.pack();
		frame2.setVisible(true);
	}
	

	@Override
	public void refreshView() {
		frame1.repaint();
		frame2.repaint();
	}

	@Override
	public void feedbackToUser(int player, String message) {
		if ( player == 1 )
			message1.setText(message);
		else if ( player == 2 )
			message2.setText(message);
	}

}
