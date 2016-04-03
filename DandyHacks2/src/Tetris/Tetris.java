package Tetris;
/*
 * Jeremiah Bill
 * VirtualTetris using LeapMotion controller
 * DandyHacks 2016
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.leapmotion.leap.Controller;



public class Tetris extends JFrame {

	private static final long serialVersionUID = 1L;
	JLabel score;
	static boolean flag = false;
	static boolean endGame = false;
	static JFrame newFrame = new JFrame(); 

	public static void main(String[] args) {


		JOptionPane popUp = new JOptionPane("Try again some other time",JOptionPane.INFORMATION_MESSAGE);
	

		Leap listener = new Leap();
		Controller control = new Controller();
		control.addListener(listener);

		int input = JOptionPane.showOptionDialog(null, "Welcome to Virtual Tetris, place a hand over the LeapMotion sensor to begin\n"
				+ "Directions: "
				+ "Use one hand to control each falling brick.\n"
				+ "1.) Move your hand within the bounds of the laptop screen, your location will correlate to bricks on game display\n"
				+ "2.) Executing a circle motion with your finger will rotate the brick clockwise\n"
				+ "3.) Using your index finger execute a tap in mid-air to drop the brick directly down\n"
				+ "4.)Press 'OK' to start...Have fun,!", "Virtual Tetris", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if(input == popUp.OK_OPTION){
			Tetris game = new Tetris();
			game.setVisible(true);
		}
		else{
			newFrame.add(popUp);
			newFrame.setSize(700, 200);
			newFrame.setVisible(true);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			newFrame.setLocation(dim.width/2-newFrame.getSize().width/2, dim.height/2-newFrame.getSize().height/2);
			newFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			
		}
		System.out.println("Press enter");
		try{

			System.in.read();



		}
		catch(IOException e){
			e.printStackTrace();

		}

		control.removeListener(listener);
	}


	public Tetris() {

		score = new JLabel(" 0");
		add(score, BorderLayout.NORTH);
		Board board = new Board(this);
		add(board);
		board.start();
		setSize(400, 600);
		setTitle("Tetris");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public JLabel getscore() {
		return score;
	}


}