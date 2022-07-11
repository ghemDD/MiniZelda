package ch.epfl.cs107.play.game.areagame.actor;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class CollectableAreaEntity extends AreaEntity {
	 
	 
	 public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		 super(area, orientation, position);
	 }
	 
	 
	 
	 public void Collection(){
		 getOwnerArea().unregisterActor(this);
		 
	 }
	 
	

}