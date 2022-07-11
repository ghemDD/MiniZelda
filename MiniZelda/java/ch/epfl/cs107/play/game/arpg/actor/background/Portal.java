package ch.epfl.cs107.play.game.arpg.actor.background;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Portal extends AreaEntity{
	
	private static int ANIMATION_DURATION=8;
	private boolean isOpening;
	private Animation[] currentAnimations;
	
	private Sprite[][] spritesIdle=new Sprite[4][8];
	private Animation[] animationsIdle=new Animation[4];
	
	private Sprite[][] spritesOpening=new Sprite[4][8];
	private Animation[] animationsOpening=new Animation[4];
	
	private Sprite[][] spritesClosing=new Sprite[4][8];
	//private Animation[] animationsClosing=new Animation[4];
	

	public Portal(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		// TODO Auto-generated constructor stub
		
		for(int i=0; i<8; ++i) {
			spritesIdle[0][i]=new RPGSprite("portal", 3, 3, this, new RegionOfInterest(i*64, 0, 64, 64));
			spritesOpening[0][i]=new RPGSprite("portal", 3, 3, this, new RegionOfInterest(i*64, 64, 64, 64));
			spritesClosing[0][i]=new RPGSprite("portal", 3, 3, this, new RegionOfInterest(i*64, 128, 64, 64));
		}

		animationsIdle=RPGSprite.createAnimations(ANIMATION_DURATION, spritesIdle, true);
		animationsOpening=RPGSprite.createAnimations(ANIMATION_DURATION, spritesIdle, true);
		//animationsClosing=RPGSprite.createAnimations(ANIMATION_DURATION, spritesIdle, true);
		
		isOpening=true;
		currentAnimations=animationsOpening;
		getOwnerArea().registerActor(new Door("zelda/Temple", new DiscreteCoordinates(4, 1),  Logic.TRUE, getOwnerArea(), Orientation.UP, new DiscreteCoordinates(position.x+1, position.y)));
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
		currentAnimations[0].draw(canvas);
	}
	
	public void update(float deltaTime) {
		if (isOpening) {
			if (currentAnimations[0].isCompleted())
				currentAnimations=animationsIdle;
			isOpening=false;
		}	
		
		currentAnimations[0].update(deltaTime);
	}
	
	public void setOpen() {
		isOpening=true;
	}
}
