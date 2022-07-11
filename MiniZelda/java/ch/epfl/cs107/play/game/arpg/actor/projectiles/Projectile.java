package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;

import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

abstract public class Projectile extends MovableAreaEntity implements FlyableEntity, Interactor, Interactable{
	private int speed;
	private int maxDistance;
	private boolean collision;
	Vector originalPosition;
	Vector maxPosition;

	public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int distance) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position);
		this.speed=speed;
		maxDistance=distance;
		originalPosition=position.toVector();
		maxPosition=originalPosition.add(getOrientation().toVector().mul(maxDistance));
	}

	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
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
		return false;
	}

	public void update(float deltaTime) {

		if (collision || !isDisplacementOccurs()) 
			getOwnerArea().unregisterActor(this);

		if (getCurrentCells().get(0).toVector().equals(maxPosition))
			collision=true;

		super.update(deltaTime);
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public int getSpeed() {
		return speed;
	}

	public Vector getOrigin() {
		return originalPosition;
	}
}
