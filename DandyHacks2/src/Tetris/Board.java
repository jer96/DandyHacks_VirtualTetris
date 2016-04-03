package Tetris;
/*
 * Jeremiah Bill
 * VirtualTetris using LeapMotion controller
 * DandyHacks 2016
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;

import Tetris.Shape.block;


public class Board extends JPanel  implements ActionListener {
	private static final long serialVersionUID = 1L;

	//Instance variables
	JLabel score;
	static Shape currentPiece;
	static block[] board;
	public static int boardWidth = 10;
	public static int boardHeight = 22;
	boolean fallDone = false;
	boolean start = false;
	int lineRemove = 0;
	 int X = 0;
	 int Y = 0;
	Timer timer;
	Robot robot ;


	Frame frame  = new Frame();
	Pointable point = frame.pointables().frontmost();


	public Board(Tetris bloc) {

		setBackground(Color.black);
		currentPiece = new Shape();
		timer = new Timer(300, this);
		timer.start(); 
		score =  bloc.getscore();
		board = new block[boardWidth * boardHeight];
		addKeyListener(new KeyListen());		
		clearBoard();  
		setFocusable(true);
		try{
			robot = new Robot();
		}
		catch(Exception e){
			System.out.println("No robot");
		}
	}

	//Action Listener 
	public void actionPerformed(ActionEvent e) {
		if (fallDone) {
			fallDone = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}


	public int squareWidth(){ 
		return (int)(getSize().getWidth()) / boardWidth; 
	}
	public int squareHeight(){
		return ((int) getSize().getHeight()) / boardHeight;
	}
	public static block shapeAt(int x, int y){ 
		return board[(y * boardWidth) + x]; 
	}


	public void start()
	{

		start = true;
		fallDone = false;
		lineRemove = 0;
		clearBoard();

		newPiece();
		timer.start();
	}

	public void paint(Graphics g)
	{ 
		super.paint(g);

		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - (boardHeight * squareHeight());


		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				block shape = shapeAt(j, (boardHeight - i - 1));
				if (shape != block.NoShape)
					drawSquare(g, 0 + j * squareWidth(),boardTop + i * squareHeight(), shape);
			}
		}

		if (currentPiece.getShape() != block.NoShape) {
			for (int i = 0; i < 4; i++) {
				int x = (X + currentPiece.x(i));
				int y = (Y - currentPiece.y(i));
				drawSquare(g, 0 + x * squareWidth(),boardTop + (boardHeight - y - 1) * squareHeight(),currentPiece.getShape());
			}
		}
	}
	//drops piece to bottom
	private void dropDown()
	{
		int newY = Y;
		while (newY > 0) {
			if (!tryMove(currentPiece, X, newY - 1))
				break;
			newY--;
		}
		pieceDropped();
	}
	//Method to move piece one line down
	private void oneLineDown()
	{
		if (!tryMove(currentPiece, X, Y - 1))
			pieceDropped();
	}

	//fills board with empty spaces to clear board
	private void clearBoard()
	{
		for (int i = 0; i < boardHeight * boardWidth; ++i)
			board[i] = block.NoShape;
	}

	//Generates new piece once piece is dropped
	private void pieceDropped()
	{
		for (int i = 0; i < 4; ++i) {
			int x = (X + currentPiece.x(i));
			int y = (Y - currentPiece.y(i));
			board[(y * boardWidth) + x] = currentPiece.getShape();
		}

		removeLine();

		if (!fallDone)
			newPiece();
	}

	//new piece generator
	private void newPiece()
	{
		currentPiece.setRandomShape();
		X = boardWidth / (2 + 1);
		Y = boardHeight - 1 + currentPiece.minY();

		if (!tryMove(currentPiece, X, Y)) {
			currentPiece.setShape(block.NoShape);
			timer.stop();
			start = false;
			score.setText("Game Over!!!!! "+ "Score: " +  String.valueOf(lineRemove));
			
		}
	}

	//Moves piece within constraints
	private boolean tryMove(Shape newPiece, int newX, int newY)
	{
		for (int i = 0; i < 4; i++) {
			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);
			if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight){
				return false;
			}
			if (shapeAt(x, y) != block.NoShape){
				return false;
			}
		}

		currentPiece = newPiece;
		X = newX;
		Y = newY;
		repaint();
		return true;
	}

	private void removeLine()
	{
		int fullLines = 0;

		for (int i = (boardHeight - 1); i >= 0; i--) {
			boolean lineFull = true;
			for (int j = 0; j < boardWidth; j++) {
				if (shapeAt(j, i) == block.NoShape) {
					lineFull = false;
					break;
				}
			}

			if (lineFull) {
				fullLines++;
				for (int k = i; k < boardHeight - 1; k++) {
					for (int j = 0; j < boardWidth; j++)
						board[(k * boardWidth) + j] = shapeAt(j, k + 1);
				}
			}
		}

		if (fullLines > 0) {
			lineRemove += fullLines;
			score.setText("Score: " + String.valueOf(lineRemove));
			fallDone = true;
			currentPiece.setShape(block.NoShape);
			repaint();
		}
	}

	private void drawSquare(Graphics page, int x, int y, block shape)
	{
		Color colors[] = { new Color(0, 0, 0), new Color(0,255,255), 
				new Color(255,0,0), new Color(0,0,255), 
				new Color(123,104,238), new Color(255,255,0), 
				new Color(0,255,0), new Color(255,0,255)
		};

		page.setFont(new Font("Bauhaus 93", Font.PLAIN , 35));
		page.drawString("TETRIS", 130 , 40);

		Color color = colors[shape.ordinal()];
		page.setColor(color);
		page.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
		page.setColor(color.brighter());
		page.drawLine(x, y + squareHeight() - 1, x, y);
		page.drawLine(x, y, x + squareWidth() - 1, y);
		page.setColor(color.darker());
		page.drawLine(x + 1, y + squareHeight() - 1,x + squareWidth() - 1, y + squareHeight() - 1);
		page.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);

		page.setFont(new Font("Bauhaus 93", Font.PLAIN , 20));
		page.drawString("Jeremiah Bill", 120 , 60);
	}

	class KeyListen extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if (!start || currentPiece.getShape() == block.NoShape) {  
				return;
			}

			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				//X = Leap.x;
				tryMove(currentPiece, Leap.x , Y);
				System.out.println(X + " " + Y);
				break;
			case KeyEvent.VK_UP:
				tryMove(currentPiece.rotateRight(), Leap.x, Y);
				System.out.println(X + " " + Y);
				break;
			case KeyEvent.VK_SPACE:
				X = Leap.x;
				dropDown();
				break;

			}

		}
	}

}