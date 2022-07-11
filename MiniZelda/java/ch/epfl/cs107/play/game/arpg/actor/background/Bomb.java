package ch.epfl.cs107.play.game.arpg.actor.background;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Bomb extends AreaEntity implements Interactor {
	private int countdown;
	private final int ANIMATION_DURATION=2;
	private final static int DAMAGE=10;
	private RPGSprite spriteBlue;
	private RPGSprite spriteRed;
	private boolean isExploding;
	private BombHandler handler=new BombHandler();
	private TextGraphics message;
	
	private Sprite[][] sprites=new Sprite[4][2];
	private Animation[] animations=new Animation[4];
	private Sprite[][] spritesExplosion=new Sprite[4][7];
	private Animation[] animationsExplosion=new Animation[4];
	
	SoundAcoustics bombAlert=new SoundAcoustics(ResourcePath.getSounds("bombAlert"));
	
	
	//Constructor
	public Bomb(Area area, Orientation orientation, DiscreteCoordinates position) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position);
		spriteBlue=new RPGSprite("zelda/Bomb", 1.f, 1.f, this, new RegionOfInterest(0, 0, 16, 16));
		spriteRed=new RPGSprite("zelda/Bomb", 1.f, 1.f, this, new RegionOfInterest(16, 0, 16, 16));
		sprites=new Sprite[][]{{spriteBlue, spriteRed}, {}, {}, {}};
		animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		isExploding=false;
		message=new TextGraphics(Integer.toString(countdown), 0.4f, Color.RED);
		message.setParent(this);
		message.setAnchor(new Vector(-0.3f, 0.1f));
		countdown=100;
		bombAlert.shouldBeStarted();
		bombAlert.bip(getOwnerArea().getWindow());
		
		for(int i=0; i<7; ++i) {
			spritesExplosion[0][i]=new RPGSprite("zelda/explosion", 2, 2, this, new RegionOfInterest(i*32, 0, 32, 32));
		}
		
		animationsExplosion=RPGSprite.createAnimations(ANIMATION_DURATION, spritesExplosion, false);
	}

	//Graphics
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (isExploding) {
			if (!animationsExplosion[0].isCompleted())
			animationsExplosion[0].draw(canvas);
		}
		
		else {
			animations[0].draw(canvas);
		}
	}

	//Cells
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		List<DiscreteCoordinates> neigh=new ArrayList<>();
		DiscreteCoordinates actual=getCurrentMainCellCoordinates();
		
		//Construction of the adjacent cells (surrounding square of cells)
		for(int x=actual.x-1; x<=actual.x+1; ++x) {
			for(int y=actual.y-1; y<=actual.y+1; ++y) {
				if (x>=0 && y>=0) 
					neigh.add(new DiscreteCoordinates(x, y));
			}
		}
		return neigh;
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
			return false;
		}

		@Override
		public boolean isViewInteractable() {
			// TODO Auto-generated method stub
			return true;
		}

	//Interactor
	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		if (isExploding) 
			return true;
		
		return false;
	}


	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		if (isExploding)
			return true;
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	//Interaction handler for the bomb
	private class BombHandler implements ARPGInteractionVisitor {
    	
    	public void interactWith(Grass grass) {
    		if (isExploding) 
    			grass.slice();
    	}
    	
    	public void interactWith(ARPGPlayer player) {
    		if (isExploding)
    			player.damage(DAMAGE);	
    	}
    	
    	public void interactWith(Monster monster) {
    		if (isExploding)
    		monster.damageType(Monster.Vulnerabilities.FIRE, 1.f);
    	}
    	
    	public void interactWith(Rock rock) {
    		rock.damage(10.f);
    	}
    }
	
	//Actor
	public void update(float deltaTime) {
		if (countdown>0) {
			--countdown;
			animations[0].update(deltaTime);
		}
		
		if (isExploding)  {
			animationsExplosion[0].update(deltaTime);
			if (animationsExplosion[0].isCompleted())
				getOwnerArea().unregisterActor(this);
		}
				
		if (countdown==0) 
			isExploding=true;
	}
	
	//Setter
	public void setExplosion() {
		isExploding=true;
	}
	
	//Getter
	public boolean getExplosion() {
		return (isExploding);
	}
	
}
