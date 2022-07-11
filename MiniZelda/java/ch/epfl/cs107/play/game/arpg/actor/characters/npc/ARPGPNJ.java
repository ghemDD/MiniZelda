package ch.epfl.cs107.play.game.arpg.actor.characters.npc;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPNJ extends MovableAreaEntity {
	protected int ANIMATION_DURATION=8;
	private Sprite[][] sprites = RPGSprite.extractSprites("zelda/character", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT});
	private Animation[] animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
	private boolean canMove=true;
	private State currentState;
	private float inactiveDelta;

	public ARPGPNJ(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		currentState=State.IDLE;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
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
		animations[getOrientation().ordinal()].draw(canvas);

	}

	public void update(float deltaTime) {
		if (inactiveDelta>0)
			--inactiveDelta;

		if (inactiveDelta<0)
			inactiveDelta=0;

		switchState();

		if (canMove)
			animations[getOrientation().ordinal()].update(deltaTime);

		super.update(deltaTime);
	}

	/**
	 * 
	 * @param orientation
	 */
	public void talkMode(Orientation orientation) {
		if (!isDisplacementOccurs()) {
			//resetMotion();
			orientate(orientation.opposite());
			System.out.println(getOrientation());
			canMove=false;
		}
	}

	public void switchMove() {
		canMove=true;
	}

	public void setAnimation(Animation[] animations) {
		this.animations=animations;
	}

	private enum State {
		INACTIVE,
		IDLE;
	}

	public void switchState() {

		switch(currentState) {

		case IDLE :

			//Generic displacement method similar to the movement of FlameSkull
			if (canMove)
				randomMove(ANIMATION_DURATION*2);

			//Generation of a random value for the moment of inactivity of the monster
			int random=RandomGenerator.getInstance().nextInt(100);

			if (random>=94) {
				int deltaIdle=RandomGenerator.getInstance().nextInt(24);
				inactiveDelta=deltaIdle+25;
				currentState=State.INACTIVE;
			}
			break;

		case INACTIVE : 
			if (!super.isDisplacementOccurs())
				animations[getOrientation().ordinal()].reset();

			if (inactiveDelta<=0)
				currentState=State.IDLE;

		}
	}

}