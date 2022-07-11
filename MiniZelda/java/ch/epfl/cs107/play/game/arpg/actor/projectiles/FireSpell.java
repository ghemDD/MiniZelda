
package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster.Vulnerabilities;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class FireSpell  extends AreaEntity implements Interactor{
	private final FireSpellHandler handler= new FireSpellHandler();
	private int strength;


	private int hp;
	public final static int ANIMATION_DURATION = 3;
	private final static int PROPAGATION_TIME_FIRE = 10;
	private final static int hpMax=20;
	private static int counter;
	Sprite[][] sprites=new Sprite[4][7];
	Animation[] animations=new Animation[4];

	public FireSpell(Area owner, Orientation orientation, DiscreteCoordinates startCoordinates, int strength) {
		super(owner, orientation, startCoordinates);
		RPGSprite sprite = new RPGSprite("zelda/fire", 1.f,1.f, this, new RegionOfInterest(0,0,16,32));

		for(int i=0; i<7; ++i) {
			sprites[0][i]=new RPGSprite("zelda/fire", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}

		animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		this.strength=strength;
		this.hp= hpMax;
		counter=0;

	}
	public void update(float deltaTime) {
		counter+=1;
		hp -=1;
		if(counter == PROPAGATION_TIME_FIRE) {
			counter=0;
			propagation();
		}
		animations[0].update(deltaTime);


		super.update(deltaTime);

		if(hp==0) {
			getOwnerArea().unregisterActor(this);
		}
	}
	
	
	private void propagation() {
		if(strength > 0) {
			FireSpell nextFireSpell= new FireSpell(getOwnerArea(), getOrientation(), getFieldOfViewCells().get(0), strength -1);
			if( getOwnerArea().canEnterAreaCells(nextFireSpell, getFieldOfViewCells()) ) {
				getOwnerArea().registerActor( nextFireSpell);
			}
			
		}
	}
	
	
	

	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells(){
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}



	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);

	}


	private class FireSpellHandler implements ARPGInteractionVisitor{

		public void interactWith(Grass grass) {
			grass.slice();
		}

		public void interactWith( ARPGPlayer player) {
			player.damage(0.5f);
		}

		public void interactWith(Monster monster) {	
			monster.damageType(Vulnerabilities.FIRE, 0.5f);
		}

		public void interactWith(Bomb bomb) {
			bomb.setExplosion();
		}

	}


	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
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
	@Override
	public void draw(Canvas canvas) {
		animations[0].draw(canvas);

	}
	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return true;
	}


	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return false;
	}
}
