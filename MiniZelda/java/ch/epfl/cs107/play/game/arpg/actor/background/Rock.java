package ch.epfl.cs107.play.game.arpg.actor.background;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends AreaEntity {
	//Obstacle used to block the access to the bridge once the orb is lighten up
	//In the scenario, the player has to find the bomb hidden in the village
	
	//Resistance/HP of the Rock
	private float hp;
	private RPGSprite sprite=new RPGSprite("rock.1", 1, 1, this, new RegionOfInterest(0, 0, 16, 16), new Vector(0, 0), 1f, 1000f);

	public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position);
		hp=10.f;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		sprite.draw(canvas);
	}
	
	public void damage(float damage) {
		hp-=damage;
	}
	
	public void update(float deltaTime) {
		if (hp<=0)
			getOwnerArea().unregisterActor(this);
	}
}
