
package ch.epfl.cs107.play.game.arpg.actor.collectables;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.CollectableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class ARPGCollectableAreaEntity extends CollectableAreaEntity {
	private boolean picked;
	public ARPGCollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position){
		super(area, orientation, position);
	}

	
	
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}
	

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	 public void Collection(){
		 super.Collection();
		 setPicked();
	 }

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
	}
	public void setPicked() {
		picked=true;
	}
	public boolean getPicked() {
		return picked;
	}
	
	
	


	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
}