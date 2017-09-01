import atan.model.*;
import java.util.*;
import java.io.*;

/**
 * AI22 Coursework 2
 * Defender controller for 5 a side team.
 * 15/11/03
 * Greg, Lawrence and Johnathan 
 */
public class Defender implements Controller {

    /*------------- Data members ------------- */

    private Player player;     // The player 'attached' to the controller.

    private double ballDist;   // Distance to the ball.
    private double ballDir;    // Direction of the ball.

    private double goalDist;   // Distance to the opponent's goal.
    private double goalDir;    // Direction of the opponent's goal.
    private double ownGoalDist;
    private double ownGoalDir;

    private boolean gameOn;    // Is the game currently on?

    private double[] playerDists = new  double[3];   // Distance to our team's forward
    private double[] playerDirs = new double[3];    // Direction to our team's forward
    //private boolean seePlayer[];       // Do we see our forward?

    private double[] oppDists = new double[2];
    private double[] oppDirs = new double[2];
    private boolean seeOpposition; 

    /*------------- Constructors ------------- */

    /*------------- Public Methods -------------*/

    /**
     * Reset the state of the controller if required, and perform
     * any other housekeeping tasks. This method is called before any 
     * of the info* methods.
     */
    public void preInfo(){
        // To start with, assume we can't see our forward. 
	//seeForward = false;
    //    seeOpposition = false;
    }

    public void postInfo(){
	// If the game hasn't started, don't do 
	// anything.
	if(!gameOn) return;

	System.out.println("Debug");	

	if(Math.abs(playerDirs[closestForward()]) < 4) {
		
		int n = closestForward();
	    // If we see our forward and we're close enough to the, 
	    // ball, try to pass to our forward.
	    if(ballDist < 0.7) {
		int temp = (int)playerDists[n] * 6;
		getPlayer().kick(temp, playerDirs[n]);
	    }
	} else {
	    // Otherwise, kick it toward the goal	    
	    if(ballDist < 0.7) {
		getPlayer().kick(60, goalDir);
	    }
	}

	// If we're pointing towards the ball (less than 4
	// degrees off-line), head towards it.
	if ((Math.abs(ballDir) < 4) &&
	(cosRule(ownGoalDist, ballDist, ownGoalDir, ballDir) < 50) &&
	(cosRule(playerDists[1], ballDist, playerDirs[1], ballDir) < ballDist)) {
	    getPlayer().dash(60);    // 60% power dash.
	}
	// Otherwise, turn towards the ball.
	else if((cosRule(ownGoalDist, ballDist, ownGoalDir, ballDir) < 50) &&
	(cosRule(playerDists[1], ballDist, playerDirs[1], ballDir) < ballDist)) {
	    getPlayer().turn(ballDir);
	}
    
	else if((Math.abs(oppDirs[closestEnemy()]) < 4) && 
	(cosRule(ownGoalDist, oppDists[closestEnemy()], ownGoalDir, oppDirs[closestEnemy()]) < 50)) {
		getPlayer().dash(60);
	}
	
	else {
		getPlayer().turn(oppDirs[closestEnemy()]);
	}
	
	}
    
  

    /* Accessor and modifier for the Player object. */

    public Player getPlayer (){
	return player;
    }

    public void setPlayer (Player p){
	player = p;
    }

    /**
     * Each info* method might be called multiple times, depending on
     * the number a particular type of object that are currently in view.
     */

    /**
     ** cosRule
     **
     ** Works out the distance between two objects given distances
     ** and angles.
     **
     ** @returns distance between two objects
    **/

    public double cosRule(double b, double c, double X, double Y)
    {
      
      if (X < 0) X = X + 360; // convert to 0 < X < 360
      if (Y < 0) Y = Y + 360;       
      double A = Math.abs(X-Y); // Difference between angles
      

      A = (A / 180) * Math.PI; // convert to radians 	
      double a = Math.sqrt((b*b) + (c*c) - ((2*c*b)*Math.cos(A))); // cosine rule 
      return a;
    }
    
    /** closestForward
      *
      * finds the closest unobstructed forward
      *
      * @return the player's array reference
     */
     
    public int closestForward()
    {
	    if((playerDists[0] < playerDists[2])
	    && ((Math.abs(playerDirs[0]) != (Math.abs(oppDirs[0])) 
	    && (Math.abs(playerDirs[2]) != Math.abs(oppDirs[1]))))) {
		    return 0;
	    }
	    
	    else {
		    return 2;
	    }
    }
    
    
    /** closestEnemy
      *
      * finds the closest attacker, without the ball
      *
      * @return the array reference for them
     */
     
     public int closestEnemy()
     {
	     if((oppDists[0] < oppDists[1]) &&
	     (cosRule(oppDists[0], ballDist, oppDirs[0], ballDir) < 
	     cosRule(oppDists[1], ballDist, oppDirs[1], ballDir))) {
		     
		     return 0;
	     }
	     
	     else {
		     return 1;
	     }
     }

    public void infoSeeFlagRight(int id, double distance, double direction){}

    public void infoSeeFlagLeft(int id, double distance, double direction){}

    public void infoSeeFlagOwn(int id, double distance, double direction){} 

    public void infoSeeFlagOther(int id, double distance, double direction){}

    public void infoSeeFlagCenter(int id, double distance, double direction){}
 
    public void infoSeeFlagCornerOwn(int id, double distance, double direction){}

    public void infoSeeFlagCornerOther(int id, double distance, double direction){}

    public void infoSeeFlagPenaltyOwn(int id, double distance, double direction){}

    public void infoSeeFlagPenaltyOther(int id, double distance, double direction){}

    public void infoSeeFlagGoalOwn(int id, double distance, double direction)
   {
     ownGoalDist = distance;
     ownGoalDir = direction;
   }

    public void infoSeeFlagGoalOther(int id, double distance, double direction){
	// Record the distance of and direction to the
	// opponent's goal.
	if(true) {
	    goalDist = distance;
	    goalDir  = direction;
	}
    }

    public void infoSeeLine(int id, double distance, double direction){}

    public void infoSeePlayerOther(int number, double distance, double direction){
   if (number==2) { // if we see the opposing forwards
     seeOpposition = true;
     oppDists[0] = distance;
     oppDirs[0] = direction;
  }
  if (number==4) {
	  seeOpposition = true;
	  oppDists[1] = distance;
	  oppDirs[1] = direction;
  }
	
	  
	  }

    public void infoSeePlayerOwn(int number, double distance, double direction){
	//Record where on the field our team's forward is. 
	//(The forward is determined in the DefaultTeam class to be player #3).	
	if (number == 2) {
	   // seeForward = true;
	    playerDists[0] = distance;
	    playerDirs[0] = direction;
	}
	
	if(number == 4) {
		//seeForward = true;
		playerDists[2] = distance;
		playerDirs[2] = direction;
	}
	
	if((number == 3) || (number == 5)) {
		playerDists[1] = distance;
		playerDirs[1] = direction;
		System.out.println("Debug");
	}
	

    }

    public void infoSeeBall(double distance, double direction){
	// Here, we just record the direction of and distance
	// to the ball.  The actual kick is perform in the 
	// postInfo method.
	ballDist = distance;
	ballDir  = direction;
    }

    /**
     * Responding to referee messages is optional.  For example,
     * you might want to respond to REFREE_MESSAGE_OFFSIDE_OWN 
     * messages to move your player back onside when necessary -
     * otherwise, the refree will move you to a random position
     * on your side automatically.
     */
    public void infoHearReferee(int refereeMessage){}

    /**
     * Your player should respond to a number of playMode messges.
     * Most importantly, while the PLAY_MODE_BEFORE_KICK_OFF mode
     * is active, a player can execute move() commands to move to
     * an appropriate position on the field.
     *
     * In this case, our player moves to a default position on
     * the appropriate side before kick off.
     */
    public void infoHearPlayMode(int playMode){
	// Get ready for kick off.
	if(playMode == Controller.PLAY_MODE_BEFORE_KICK_OFF){
	    gameOn = false;
	    this.pause(1000);
	    switch (this.getPlayer().getNumber()) {
	    case 1: this.getPlayer().move(-10, 0); break;
	    case 2: this.getPlayer().move(-20, 25); break;
	    case 3: this.getPlayer().move(-30, 20); break;
	    case 4: this.getPlayer().move(-20, -25); break;
	    case 5: this.getPlayer().move(-30, -20); break;
	    default: throw new Error("Number must be initialized before move.");
	    }
	}
	else {
	    gameOn = true; // The game has started.
	}
    }

    /**
     * The auditory model is disabled, so you won't hear any
     * messages from other players - no need to implement this
     * method.
     */
    public void infoHear(double direction, String message){}

    /**
     * You can save the information supplied when this method is
     * called, if you like.  In order to get the information, you 
     * have to make a call to your player's 'senseBody()' method.
     */
    public void infoSenseBody
	(int viewQuality, int viewAngle,
	 double stamina, double speed, double headAngle, int kickCount,
	 int dashCount, int turnCount, int sayCount, int turnNeckCount){}

    /*------------- Private Methods -------------*/
    
    public synchronized void pause (int ms) {
	try {
	    this.wait(ms);
	} catch (InterruptedException ex) {
	}
    }
}





