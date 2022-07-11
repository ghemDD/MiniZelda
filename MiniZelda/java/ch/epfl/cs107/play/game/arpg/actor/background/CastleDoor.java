package ch.epfl.cs107.play.game.arpg.actor.background;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.area.RouteChateau;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CastleDoor extends Door {
	//State of the door : TRUE/OPEN FALSE/CLOSED
	private boolean isOpening=false; 
							 
	private RPGSprite sprite=new RPGSprite("zelda/castleDoor.open", 2, 2, this, new RegionOfInterest(0,0,32,32));
	//private final static int ANIMATION_DURATION=8;

	public CastleDoor(String destination, DiscreteCoordinates otherSideCoordinates, Logic signal, Area area,
	Orientation orientation, DiscreteCoordinates position) {
		super(destination, otherSideCoordinates, signal, area, orientation, position);
		
		// TODO Auto-generated constructor stub
	}

	public CastleDoor(String string, DiscreteCoordinates discreteCoordinates, Logic true1, RouteChateau routeChateau,
			Orientation down, DiscreteCoordinates discreteCoordinates2, DiscreteCoordinates discreteCoordinates3) {
		// TODO Auto-generated constructor stub
		super(string, discreteCoordinates, true1, routeChateau, down, discreteCoordinates2, discreteCoordinates3);
	}

	public void setState(boolean open) {
		isOpening=open;
	}
	
	@Override
	  public boolean takeCellSpace() {
	        return (!(isOpening));
	    }
	 
	@Override
	  public boolean isViewInteractable(){
	        return (!(isOpening));
	    }
	
	
	public void draw(Canvas canvas) {
		if (isOpening) 	
			sprite.draw(canvas);
	}
	
	public void setSignal(Logic signal) {
		super.setSignal(signal);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
		//System.out.println("accept");
	}
	
	public void update(float deltaTime) {
	}
}
