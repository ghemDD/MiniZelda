package ch.epfl.cs107.play.game.arpg.actor.background;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends AreaEntity{
	private RPGSprite sprite=new RPGSprite("zelda/grass", 1, 1, this, new RegionOfInterest(0,0,16,16));
	private boolean sliced;
	Sprite[][] spritesG=new Sprite[4][4];
	Animation[] animationsG=new Animation[4];
	private final static int ANIMATION_DURATION=4;
	private final static double PROBABILITY_TO_DROP_ITEM=0.5;
	private final static double PROBABILITY_TO_DROP_HEART=0.5;
	

	public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		sliced=false;
		
		for(int i=0; i<4; ++i) {
			spritesG[0][i]=new RPGSprite("zelda/grass.sliced", 1, 2, this, new RegionOfInterest(i*32, 0, 32, 32));
		}

		animationsG=RPGSprite.createAnimations(ANIMATION_DURATION, spritesG, false);
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
		if (sliced)
			return false;
		
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
		
		if (sliced) {
			if (!animationsG[0].isCompleted())
				animationsG[0].draw(canvas);
		}
			
		else 
			sprite.draw(canvas);
	}
	
	public void slice() {
		sliced=true;
	}

	public void update(float deltaTime) {
		if (sliced) {
			
			animationsG[0].update(deltaTime);

			double random=RandomGenerator.getInstance().nextDouble();
			if (random<PROBABILITY_TO_DROP_ITEM) {
				random=RandomGenerator.getInstance().nextDouble();
				if (random<PROBABILITY_TO_DROP_HEART)
					getOwnerArea().registerActor(new Heart(getOwnerArea(), Orientation.DOWN, getCurrentCells().get(0)));
				else
					getOwnerArea().registerActor(new Coin(getOwnerArea(), Orientation.DOWN, getCurrentCells().get(0)));
			}

			getOwnerArea().unregisterActor(this);
			
			
		}
		
		super.update(deltaTime);
	}
	
}
