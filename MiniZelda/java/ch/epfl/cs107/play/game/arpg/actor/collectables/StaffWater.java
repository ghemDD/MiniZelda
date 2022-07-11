package ch.epfl.cs107.play.game.arpg.actor.collectables;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class StaffWater extends ARPGCollectableAreaEntity{

	Sprite[][] sprites=new Sprite[4][8];
	Animation[] animations;
	private int ANIMATION_DURATION=8;
	
	//Sound Design
	private SoundAcoustics quest=new SoundAcoustics(ResourcePath.getSounds("questUpdate"));
	
	
	
	
	
	public StaffWater(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		//handler=new HeartHandler();
		
		for(int i=0; i<8; ++i) {
			sprites[0][i]=new RPGSprite("zelda/staff", 2.f, 2.f, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		
		animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
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
		return true;
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
		animations[0].setAnchor(new Vector(-0.5f,0));

		animations[0].draw(canvas);
	}
	
	public void update(float deltaTime) {

			
		animations[0].update(deltaTime);
		super.update(deltaTime);
	}
	public void playSound() {
		quest.shouldBeStarted();
		quest.bip(getOwnerArea().getWindow());
	}

	
}
