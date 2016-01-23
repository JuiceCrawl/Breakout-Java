/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file implements the game of Breakout. An assigned from the online Standford course: CS106A.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 500;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/** Amount Y velocity is increased each cycle as a result of gravity */
	 private static final double GRAVITY = 4;
/** Animation delay or pause time between ball moves */
	 private static final int DELAY = 15;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
	//set screen size
		setSize(WIDTH,HEIGHT);
		pause(5);
		setupgame();
	//play game
		playgame();
	}
	private void setupgame(){
		//set up brick, mouse listeners, paddle, and ball	
				createbricks();
				addMouseListeners();
				createpaddle();
				createball();
	}
	
	private void playgame(){
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = GRAVITY;

		while(lose == false && bricks > 0){
			moveBall(vx, vy);
			checkCollision();			
			if (ball.getX() >= APPLICATION_WIDTH - (BALL_RADIUS*2)){
				vx = -vx;
			}
			if (ball.getX() <= 0){
				vx = -vx;
			}
			
			if (ball.getY() <= 0){
				vy = -vy;
			}
			//bottom wall
			if (ball.getY() >= APPLICATION_HEIGHT - (BALL_RADIUS*2)){
				vy = -vy;
				loseTurn();
				if(lose == false){
					createball();
				}
				
			}
	
		}

	}
		
	private void loseTurn(){
		remove(ball);	
		turns--;
		println(turns);
		if(turns == 0){
			lose = true;
			makeLabel("you lose!", APPLICATION_WIDTH/2, APPLICATION_HEIGHT/2);
		}
	}
	
	private void makeLabel(String str, double x, double y){
		GLabel label = new GLabel(str, x, y);
		label.setLabel(str);
		label.setFont("SansSerif-15");
		label.setColor(Color.black);
		add(label);
	}
	
/** creates colorful bricks to setup game */
	private void createbricks(){
		//create bricks 10 rows going horizontal.
		for(int i = 0; i< NBRICK_ROWS; i++){
			//create 10 bricks per column going vertical
			for(int j = 0; j < NBRICKS_PER_ROW; j++){
				brick = new GRect (BRICK_SEP + ((BRICK_WIDTH + BRICK_SEP)*i),BRICK_Y_OFFSET + ((BRICK_HEIGHT + BRICK_SEP)*j),BRICK_WIDTH,BRICK_HEIGHT);
				add(brick);
				// add colors to ever two y axis coordinates via the j for loop
				if(j == 0 || j == 1){
					brick.setColor(Color.red);
					brick.setFilled(true);
					brick.setFillColor(Color.red);
				}
				
				if(j == 2 || j == 3){
					brick.setColor(Color.orange);
					brick.setFilled(true);
					brick.setFillColor(Color.orange);
				}
				
				if(j == 4 || j == 5){
					brick.setColor(Color.yellow);
					brick.setFilled(true);
					brick.setFillColor(Color.yellow);
				}
				
				if(j == 6 || j == 7){
					brick.setColor(Color.green);
					brick.setFilled(true);
					brick.setFillColor(Color.green);
				}
				
				if(j == 8 || j == 9){
					brick.setColor(Color.cyan);
					brick.setFilled(true);
					brick.setFillColor(Color.cyan);
				}
				
			}
			
		}
		
	}
	/** Creates paddle off-set 30px from bottom */
	public void createpaddle(){
			paddle = new GRect (mouseX-getWidth(),getHeight()-PADDLE_Y_OFFSET,PADDLE_WIDTH,PADDLE_HEIGHT);
			paddle.setColor(Color.black);
			paddle.setFilled(true);
			paddle.setFillColor(Color.black);
			add(paddle);
	}
/** get mouseX coordinates and moves paddle */
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		paddle.move(e.getX()-paddle.getX(), 0);
		//stops paddle from moving past end of screen right
		if(mouseX >= APPLICATION_WIDTH - PADDLE_WIDTH){
			paddle.setLocation(APPLICATION_WIDTH - PADDLE_WIDTH, getHeight()-PADDLE_Y_OFFSET);
		}
		//stops paddle from moving past end of screen left
		if(mouseX <= 4){
			paddle.setLocation(4, getHeight()-PADDLE_Y_OFFSET);
		}
	}	
/** create a ball */	
	private void createball(){
		ball = new GOval(canvasCenterWidth()-BALL_RADIUS, canvasCenterHeight()-BALL_RADIUS, BALL_RADIUS*2, BALL_RADIUS*2);
		ball.setFilled(true);
		add(ball);	
	}
	
	private void moveBall(double vx, double vy){
		ball.move(vx, vy);
		pause(speed);
	}

	private void checkCollision(){
		
		collider = getCollidingObject();
		if(collider != null){
			if(collider == paddle){
				//trying to fix ball glued to paddle issue but not fixed yet
				if( (ball.getY() + (BALL_RADIUS *2)) > (paddle.getY()) ){
					if(firsttime){
						vy = -vy;
						firsttime = false;
						println(firsttime);
					}
					//normal case we want when the ball hits the paddle
				}else{
					vy = -vy;
				}
				
				
			}else{
				remove(collider);
				vy = -vy;
				bricks--;
				if(bricks == 0){
					makeLabel("you win!", APPLICATION_WIDTH/2, APPLICATION_HEIGHT/2);
//					remove(paddle);
//					remove(ball);
//					speed--;
//					run();
				}
			}
			//part of ball glued to paddle case we were trying to fix
		}else{
			firsttime = true;	
		}
	}
			
	private GObject getCollidingObject(){
		double x = ball.getX();
		double y = ball.getY(); 
		GObject topLeft = getElementAt(x,y);
		GObject topRight = getElementAt(ball.getX() + (BALL_RADIUS *2),y);
		GObject bottomLeft = getElementAt(x,ball.getY() + (BALL_RADIUS *2));
		GObject bottomRight = getElementAt(ball.getX() + (BALL_RADIUS *2),ball.getY() + (BALL_RADIUS *2));
		
		if(topLeft != null){
			return topLeft;
		}else if(topRight != null){
			return topRight;
		}else if(bottomLeft != null){
			return bottomLeft;
		}else if(bottomRight != null){
			return bottomRight;
		}else{
			return collider = null;
		}
		
	}
	
/*	
	private void topLeftCorner(){
		double x = ball.getX();
		double y = ball.getY(); 
		collider = getElementAt(x,y);
		
		if( collider != null && collider != paddle ){
			collider.setVisible(false);
			
		}
	}
	
	private void topRightCorner(){
		double x = ball.getX() + (BALL_RADIUS *2);
		double y = ball.getY(); 
		collider = getElementAt(x,y);
		
		if( collider != null && collider != paddle ){
			collider.setVisible(false);
		}
	}
	
	private void bottomLeftCorner(){
		double x = ball.getX();
		double y = ball.getY() + (BALL_RADIUS *2); 
		collider = getElementAt(x,y);
		
		if( collider != null && collider != paddle ){
			collider.setVisible(false);
		}else{
			return null;
		}
	}
	
	private void bottomRightCorner(){
		double x = ball.getX() + (BALL_RADIUS *2);
		double y = ball.getY() + (BALL_RADIUS *2); 
		collider = getElementAt(x,y);
		
		if( collider != null && collider != paddle ){
			collider.setVisible(false);
		}
	}
	*/
	private double canvasCenterWidth(){
		return (getWidth()/2);
	}
	private double canvasCenterHeight(){
		return (getHeight()/2);
	}
	private double mouseX;
	private GRect paddle;
	private GOval ball;
	private GRect brick;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private double vx, vy;
	private GObject collider;
	private int turns = NTURNS;
	private boolean lose = false;
	private int bricks = NBRICKS_PER_ROW * NBRICK_ROWS;
	private int speed = DELAY;
	private boolean firsttime = true;
}
