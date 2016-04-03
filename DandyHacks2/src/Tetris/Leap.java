package Tetris;
/*
 * Jeremiah Bill
 * VirtualTetris using LeapMotion controller
 * DandyHacks 2016
 */

import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;


class Leap extends Listener {
	Robot robot;
	boolean flag = false;
	static int x;

	public void onInit(Controller controller){
		System.out.println("Init");
	}
	public void onConnect (Controller controller){
		System.out.println("Connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);

	}
	public void onFrame(Controller controller) {
		//System.out.println("Frame available");
		try{
			robot = new Robot();
		}
		catch(Exception e){
			System.out.println("Exception");
		}
		controller.config().setFloat("Gesture.Swipe.MinLength", 25.0f);
		controller.config().setFloat("Gesture.Swipe.MinVelocity", 50f);
		controller.config().save();
		controller.config().setFloat("Gesture.Circle.MinArc", .5f);
		controller.config().save();

		Frame frame = controller.frame();

		/*System.out.println("Frame id: " + frame.id()
		+ ", timestamp: " + frame.timestamp()
		+ ", hands: " + frame.hands().count()
		+ ", fingers: " + frame.fingers().count()
		+ ", tools: " + frame.tools().count()
		+ ", gestures " + frame.gestures().count());*/
		HandList hand =  frame.hands();
		//System.out.println(hand.count());
		boolean press = false;
		
		if(!frame.gestures().isEmpty()){
			for(Gesture g : frame.gestures()){
				if(g.type() == Gesture.Type.TYPE_KEY_TAP && !isFist(2*hand.get(0).sphereRadius())){
					KeyTapGesture k = new KeyTapGesture(g);
					System.out.println("ROTATE RIGHT");
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
					


				}
				else if(g.type() == Gesture.Type.TYPE_CIRCLE){
					CircleGesture c = new CircleGesture(g);
					System.out.println(c.progress());
					if(c.progress()==1){
						robot.keyPress(KeyEvent.VK_UP);
						robot.keyRelease(KeyEvent.VK_UP);
					}
				}
			}
		}

		else{
			if(hand.count() >0){

				float palm = hand.get(0).stabilizedPalmPosition().getX();
				if(  palm<= -160){
					System.out.println(0);
					x =0;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
				}
				else if(palm >= -160 && palm <=-120){
					System.out.println(1);
					x=1;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
				}
				else if(palm >= -120 && palm <=-80){
					x=2;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(2);
				}
				else if(palm >= -80 && palm <=-40){
					x=3;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(3);
				}
				else if(palm >= -40 && palm <=0){
					x=4;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(4);
				}
				else if(palm >= 0 && palm <=40){
					x=5;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(5);
				}
				else if(palm >= 40 && palm <=80){
					x=6;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(6);
				}
				else if(palm >= 80 && palm <=120){
					x=7;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(7);
				}
				else if(palm >= 120 && palm <=160){
					x=8;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(8);
				}
				else if(palm >= 160 ){
					x=9;
					robot.keyPress(KeyEvent.VK_LEFT);
					robot.keyRelease(KeyEvent.VK_LEFT);
					System.out.println(9);
				}
			}
		}

	}


	public boolean isFist(float radius){
		if(Math.abs(radius) <=80){
			return true;
		}
		else{
			return false;
		}
	}

	public void onDisconnect(Controller controller){
		System.out.println("Discon");
	}
	public void onExit(Controller controller){
		System.out.println("Exit");
	}

}
