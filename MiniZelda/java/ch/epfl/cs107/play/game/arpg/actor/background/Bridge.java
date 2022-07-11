package ch.epfl.cs107.play.game.arpg.actor.background;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bridge extends AreaEntity{
RPGSprite sprite;
	public Bridge(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sprite=new RPGSprite("zelda/bridge", 4, 3, this, new RegionOfInterest(0, 0, 64, 48), new Vector(0, 0), 1f, -2f);
		// TODO Auto-generated constructor stub
	}
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
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
		return false;
	}
	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		sprite.draw(canvas);
	}

}
