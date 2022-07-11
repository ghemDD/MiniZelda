package ch.epfl.cs107.play.game.arpg.actor.monsters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

abstract public class Monster extends MovableAreaEntity implements Interactor, Interactable {
	private float health;
	protected static int maxHealth;
	private boolean dead=false;
	protected final static int ANIMATION_DURATION=2;
	private Sprite[][] sprites=new Sprite[4][7];
	private Animation[] animationsDeath=new Animation[4];
	private Animation[] currentAnimations;

	//Vulnerabilities
	Monster.Vulnerabilities[] vulnerabilities;

	public Monster(Area area, Orientation orientation, DiscreteCoordinates coor, float health, int max, Monster.Vulnerabilities[] vul) {
		super(area, orientation, coor);
		this.health=health;
		maxHealth=max;
		vulnerabilities=vul;

		for(int i=0; i<7; ++i) {
			sprites[0][i]=new RPGSprite("zelda/vanish", 2, 2, this, new RegionOfInterest(i*32, 0, 32, 32));
		}

		animationsDeath=RPGSprite.createAnimations(ANIMATION_DURATION, sprites, false);

	}

	//Getter
	public int getMaxHealth() {
		return maxHealth;
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

		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	//Interactor
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

	//Interactable
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		if (dead)
			return false;

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
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}


	public void draw(Canvas canvas) {
		//Death animation
		if (dead) {
			animationsDeath[0].setAnchor(new Vector(-0.5f,0));
			animationsDeath[0].draw(canvas);

		}	
		else{
			currentAnimations[getOrientation().ordinal()].setAnchor(new Vector(-0.5f,0));


			currentAnimations[getOrientation().ordinal()].draw(canvas);
		}
	}

	public void update(float deltaTime) {
		//Death Condition
		if (health<=0) 
			dead=true;
		
			
		

		//Check is the monster is dead : if so its previous movement is stopped, the death animation is updated
		//When it is completed, the monster drops an item with the method postMortem which will be redefined in the sub-classes
		if (dead) {

			
			



			
			
			animationsDeath[0].update(deltaTime);
			
			resetMotion();

			if (animationsDeath[0].isCompleted()) {

				
				
				DiscreteCoordinates leavingCells=getCurrentMainCellCoordinates().jump(getOrientation().toVector());	
				List<DiscreteCoordinates> leave=new ArrayList<>();
				leave.add(leavingCells);
				List<DiscreteCoordinates> leavingCells2=getCurrentCells();
				getOwnerArea().leaveAreaCells(this, leave);
				getOwnerArea().leaveAreaCells(this, leavingCells2);
				
				//System.out.println(leavingCells2);
				
				/*
				System.out.println(leavingCells+" "+getOwnerArea().areaBehavior.canEnter(this, leave));
				System.out.println(leavingCells2+" "+getOwnerArea().areaBehavior.canEnter(this, leavingCells2));
				*/
				
				postMortem();
				getOwnerArea().unregisterActor(this);
				
				
				

				
				
				
				
				
				
				

			}
		}

		else {
			currentAnimations[getOrientation().ordinal()].update(deltaTime);
		}

		super.update(deltaTime);
	}

	//A vulnerability is characterized by its name which is more of less anecdotal 
	public enum Vulnerabilities {
		FIRE,
		PHYS,
		MAGIC;
	}

	//Deduce the monster's hp of a number damage hp
	private void damage(float damage) {
		health-=damage;
	}

	//Check if the type of damage correspond to one of the vulnerabilities of the monster: 
	//if so the monster is damaged by a value damage : public because ARPGPlayer and other actors such as the projectiles need an access to this method
	public void damageType(Monster.Vulnerabilities type, float damage) {
		if (Arrays.asList(vulnerabilities).contains(type))
			damage(damage);	
	}

	//Setters restricted to Monster
	public void setDead() {
		dead=true;
	}
	//Getters
	public boolean getDead() {
		return dead;
	}
	
	public float getHealth() {
		return health;
	}

	/*
	 * Utils methods for Animation manipulation
	 */
	public void setAnimations(Animation[] animations) {
		currentAnimations=animations;
	}

	public void animationsReset() {
		currentAnimations[getOrientation().ordinal()].reset();
	}

	public boolean animationCompletion() {
		return currentAnimations[getOrientation().ordinal()].isCompleted();
	}

	public boolean animationsEquals(Animation[] animations) {
		return currentAnimations==animations;
	}

	//We suppose to simplify that each Monster has to drop something when it dies (it can be nothing)
	abstract protected void postMortem();
	

}
