package reversi;

public class ReversiController implements IController{
	IModel model;
	IView view;
	
	java.util.Random rand = new java.util.Random();
	
	@Override
	public void initialise(IModel model, IView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void startup() {
		model.setFinished(false);
		// Initialise board
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < height ; y++ )
				model.setBoardContents(x, y, 0);
		// Consider setting up any initial pieces here in your own controller
		model.setBoardContents(width/2 - 1, height/2 - 1, 1);
		model.setBoardContents(width/2, height/2, 1);
		model.setBoardContents(width/2, height/2 - 1, 2);
		model.setBoardContents(width/2 - 1, height/2, 2);
		// Refresh all messages and frames
		
		//is the player initialised to 0?? added to try and fix this
		model.setPlayer(1); 
		view.feedbackToUser(1, "White player - choose where to put your piece");
		view.feedbackToUser(2, "Black player - not your turn");
		view.refreshView();
	}

	@Override
	public void update() {
		int player = model.getPlayer();
		int check=0;
		int x;
		int y;
		int white=0;
		int black=0;

		if(player == 1) {
			view.feedbackToUser(1, "White player - choose where to put your piece");
			view.feedbackToUser(2, "Black player - not your turn");
		}
		else {
			view.feedbackToUser(1, "White player - not your turn");
			view.feedbackToUser(2, "Black player - choose where to put your piece");
		}
		
		check = checkMoves();
		
		if (check == 0) {
			if (player == 1) {
				model.setPlayer(2);
				check = checkMoves();
				
				if (check == 0) {
					model.setFinished(true);
				}
				else {
					update();
				}
			}
			else {
				model.setPlayer(1);
				check = checkMoves();
				
				if (check == 0) {
					model.setFinished(true);
				}
				else {
					update();
				}
			}
		}
		else
			model.setFinished(false);
		
		//used from original code to check for empty squares
		int finished=0;
		for (x = 0 ; x < model.getBoardWidth() ; x++ )
			for (y = 0 ; y < model.getBoardHeight() ; y++ )
				if ( model.getBoardContents(x, y) == 1 || model.getBoardContents(x, y) == 2 )
					finished++; // There is a filled square
		if (finished == model.getBoardHeight()*model.getBoardWidth())
			model.setFinished(true);
		
		
		
		//prints correct data if the game is finished
		if (model.hasFinished()) {
			for (y=0;y<model.getBoardHeight();y++) {
				for (x=0;x<model.getBoardWidth();x++) {
					if(model.getBoardContents(x, y) == 1) 
						white++;
					else if (model.getBoardContents(x, y) == 2)
						black++;
				}
			}
			if (white>black) {
				view.feedbackToUser(1, "White won. White "+white+" to Black "+black+". Reset the game to replay.");
				view.feedbackToUser(2, "White won. White "+white+" to Black "+black+". Reset the game to replay.");
			}
			else if (black>white) {
				view.feedbackToUser(1, "Black won. Black "+black+" to White "+white+". Reset the game to replay.");
				view.feedbackToUser(2, "Black won. Black "+black+" to White "+white+". Reset the game to replay.");
			}
			else if (black==white) {
				view.feedbackToUser(1, "Draw. Both players ended with "+black+" pieces. Reset the game to replay.");
				view.feedbackToUser(2, "Draw. Both players ended with "+black+" pieces. Reset the game to replay.");
			}
		}
		view.refreshView();
	}
	
	//to check if there are any possible moves for a player
	public int checkMoves() {
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		int player = model.getPlayer();
		int x;
		int y;
		int i;
		int check=0;
		
		for (y=0;y<height;y++) {
			for (x=0;x<width;x++) {
				if(model.getBoardContents(x, y) == 0)
				{
					int [] takes = pieceTakes(player, x, y);
					for(i=0;i<8;i++) {
						if(takes[i] > 0) {
							check += 1;
						}
					}
				}
			}
		}
		
		return check;
	}

	@Override
	public void squareSelected(int player, int x, int y) {
		if (model.getPlayer() != player) {
			view.feedbackToUser(player, "It is not your turn!");
			return;
		}
		// The finished flag never gets set by this controller, but the SimpleTestModel could set it
		if ( model.hasFinished() )
		{
			view.feedbackToUser(player, "Somehow the game has finished!" );
			return; // Don't do the set board contents
		}
		
		//if board is not empty square you cannot place pieces there
		if (model.getBoardContents(x, y) != 0) 
			return;
		
		//This section is used to check if the square is valid
		int i;
		int check=0;
		int [] takes = pieceTakes(player, x, y);
		for(i=0;i<8;i++) {
			if(takes[i] > 0) {
				check = 1;
				moves(player, x, y, i, takes[i]);
			}
		}
		if (check == 1) {
			model.setBoardContents(x, y, player);
			if(player == 1) {
				model.setPlayer(2);
			}
			else {
				model.setPlayer(1);
			}
			view.refreshView();
			update();
		}
	}
	
	//count the number of pieces that could be taken in a direction
	public int nopieces (int player, int x, int y, int xoffset, int yoffset) {
		int totsum = 0;
		int newx = x+xoffset;
		int newy = y+yoffset;
		if (newx<model.getBoardWidth() && newy<model.getBoardHeight() && newx>=0 && newy>=0) {
			int square = model.getBoardContents(newx, newy);
			if (square != player && square != 0) { 
				totsum += nopieces(player, newx, newy, xoffset, yoffset);
				return totsum + 1;
			}
			else if (square == player)
				return totsum;
			else 
				return totsum - model.getBoardWidth(); //negative to show invalid move
		}
		else
			return - model.getBoardWidth(); //as no same colour has therefore been found
	}
	
	//collect all the no. pieces that could be taken in all directions
	public int[] pieceTakes(int player, int x, int y) {
		int[] takes = new int[8];
		takes[0] = nopieces(player, x, y, 1, 0); //pos x direction 
		takes[1] = nopieces(player, x, y, -1, 0); //neg x direction
		takes[2] = nopieces(player, x, y, 0, 1); //pos y direction
		takes[3] = nopieces(player, x, y, 0, -1); //neg y direction
		takes[4] = nopieces(player, x, y, 1, 1); //pos x pos y direction
		takes[5] = nopieces(player, x, y, -1, -1); //neg x neg y direction
		takes[6] = nopieces(player, x, y, -1, 1); //neg x pos y direction
		takes[7] = nopieces(player, x, y, 1, -1); //pos x neg y direction
		
		return takes;
	}
	
	//to take opponents pieces and to change the color of them 
	public void validMove (int player, int x, int y, int xoffset, int yoffset, int num) {
		int totsum = 0;
		int newx = x+xoffset;
		int newy = y+yoffset;
		model.setBoardContents(newx, newy, player);
		if (num > 0)
			validMove(player, newx, newy, xoffset, yoffset, num-1);
	}
	
	//to change the pieces from the values in "takes" array in squareSelected
	public void moves (int player, int x, int y, int dir, int num) {
		if (dir == 0) { 
			validMove(player, x, y, 1, 0, num);
		}
		else if (dir == 1) { 
			validMove(player, x, y, -1, 0, num);
		}
		else if (dir == 2) { 
			validMove(player, x, y, 0, 1, num);
		}
		else if (dir == 3) { 
			validMove(player, x, y, 0, -1, num);
		}
		else if (dir == 4) { 
			validMove(player, x, y, 1, 1, num);
		}
		else if (dir == 5) { 
			validMove(player, x, y, -1, -1, num);
		}
		else if (dir == 6) { 
			validMove(player, x, y, -1, 1, num);
		}
		else if (dir == 7) { 
			validMove(player, x, y, 1, -1, num);
		}
	}

	@Override
	public void doAutomatedMove(int player) {
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		int x;
		int y;
		int i;
		int tottakes=0;
		int max=0;
		int maxx=0;
		int maxy=0;
		
		for (y=0;y<height;y++) {
			for (x=0;x<width;x++) {
				if (model.getBoardContents(x, y) == 0) {
					int [] takes = pieceTakes(player, x, y);
					for(i=0;i<8;i++) {
						if(takes[i] > 0) {
							tottakes += takes[i]; 
						}
					}
					if (tottakes > max) {
						maxx = x;
						maxy = y;
						max = tottakes;
					}
				}
				tottakes = 0;
			}
		}
		
		squareSelected(player, maxx, maxy);
		view.refreshView();
	}

}
