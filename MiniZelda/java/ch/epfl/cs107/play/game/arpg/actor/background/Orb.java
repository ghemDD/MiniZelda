package ch.epfl.cs107.play.game.arpg.actor.background;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Orb extends AreaEntity{
	//In order the temple, the player will need to cross a bridge : for now the bridge is not visible 
	//To make it visible, the player has to shoot the orb with a bow
	
	//Graphics
	private Sprite[][] sprites=new Sprite[4][6];
	private Animation[] animations=new Animation[4]; 
	private int ANIMATION_DURATION=8;
	
	//State of the signal
	private boolean signal=false;
	
	//Animation two sets possible : Blue (closed) / Green (open)
	private Animation currentAnimations;
	
	//Bridge that will activated when the orb signal is true
	private Bridge bridge=new Bridge(getOwnerArea(), Orientation.DOWN, new DiscreteCoordinates(15, 9));
	//Sound Design
	private SoundAcoustics quest=new SoundAcoustics(ResourcePath.getSounds("questUpdate"));
	
	
	//Constructor
	public Orb(Area area, Orientation orientation, DiscreteCoordinates position) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position);
		
		for(int i=0; i<6; ++i) {
			sprites[0][i]=new RPGSprite("zelda/orb", 1, 1, this, new RegionOfInterest(i*32, 0, 32, 32));
			sprites[1][i]=new RPGSprite("zelda/orb", 1, 1, this, new RegionOfInterest(i*32, 32, 32, 32));
		}
		
		animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		currentAnimations=animations[0];
	}

	//Cells
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	//Interactable
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
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
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	//Graphics
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		currentAnimations.draw(canvas);
		
		if (signal)
			bridge.draw(canvas);
	}
	
	//Actor
	public void update(float deltaTime) {	
		
		currentAnimations.update(deltaTime);
		super.update(deltaTime);
	}

	//Setter of the signal : used in the interaction handler of Arrow
	public void setState(boolean signal) {
		this.signal=signal;
		
		if (this.signal) {
			currentAnimations=animations[1];
			quest.shouldBeStarted();
			quest.bip(getOwnerArea().getWindow());
		}	
	}
}
