package ch.epfl.cs107.play.game.arpg.actor.monsters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class LogMonster extends Monster {
	//Handler for the interactions of LogMonster
	private LogMonsterHandler handler=new LogMonsterHandler();
	//private boolean inactive;
	//Describes the time during which LogMonster is inactive or asleep
	private float inactiveDelta;
	private float sleepingTime;
	//Constant as defined in the documentation
	private static final float MIN_SLEEPING_DURATION=50.f;
	private static final float MAX_SLEEPING_DURATION=200.f;

	//Damage done to the player 
	private static final float DAMAGE=1.f;
	//Reach of field of view in terms of cells
	private static final int RANGE=8;

	//Number of frames
	private final static int ANIMATION_DURATION=8;

	//Current State of the logMonster
	private State currentState; 

	//Determine if an attack has been done against the player
	private boolean detected;
	//Sound Design : alert sound from Metal Gear Solid, indicates if the player has been detected by a LogMonster
	SoundAcoustics mySound=new SoundAcoustics(ResourcePath.getSounds("alert"));


	//Constant animations for the different States
	Sprite[][] spritesAwake = RPGSprite.extractSprites("zelda/logMonster", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
	Animation[] animationsIdle=RPGSprite.createAnimations(ANIMATION_DURATION, spritesAwake);
	Animation[] animationsAttack=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesAwake);

	Sprite[][] spritesSleeping=new Sprite[4][4];
	Animation[] animationsSleeping=new Animation[4];

	Sprite[][] spritesWaking=new Sprite[4][3];
	Animation[] animationsWaking=new Animation[4];

	Sprite excla=new Sprite("excla", 1.f, 1.f, this, new RegionOfInterest(0, 0, 64, 64), new Vector(0.5f, 1.75f));

	public LogMonster(Area area, Orientation orientation, DiscreteCoordinates coor, float health, int max) {
		// TODO Auto-generated constructor stub
		super(area, orientation, coor, health, max, new Vulnerabilities[]{Vulnerabilities.FIRE,Vulnerabilities.PHYS});
		currentState=State.IDLE;
		inactiveDelta=0;


		//Sprite and animations construction
		for(int y=0; y<4; ++y) {
			for(int i=0; i<4; ++i) {
				spritesSleeping[y][i]=new RPGSprite("zelda/logMonster.sleeping", 2, 2, this, new RegionOfInterest(0, i*32, 32, 32));

				if (i<3)
					spritesWaking[y][i]=new RPGSprite("zelda/logMonster.wakingUp", 2, 2, this, new RegionOfInterest(0, i*32, 32, 32));
			}
		}

		animationsWaking=RPGSprite.createAnimations(ANIMATION_DURATION*2, spritesWaking, false);
		animationsSleeping=RPGSprite.createAnimations(ANIMATION_DURATION*2, spritesSleeping, true);
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		List<DiscreteCoordinates> idleFov=new ArrayList<>();
		for(int i=0; i<RANGE; ++i) {
			idleFov.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));
		}

		if (currentState==State.ATTACK) 
			return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));

		return idleFov;	
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
	}

	private class LogMonsterHandler implements ARPGInteractionVisitor {
		public void interactWith(ARPGPlayer player) {
			if (currentState==State.ATTACK) {
				player.damage(DAMAGE);
			}

			if (currentState==State.IDLE || currentState==State.INACTIVE) {
				mySound.shouldBeStarted();
				mySound.bip(getOwnerArea().getWindow());
				setAnimations(animationsAttack);
				currentState=State.ATTACK;
				detected=true;
			}	
		}
	}

	//Enumeration describing the different states a Log Monster can take : each State has two attributes
	// 1) boolean move : indicates whether LogMonster can move while being in this state
	// 2) int animationDuration
	private enum State {
		IDLE(16),
		ATTACK( 4),
		FALLING(8),
		SLEEPING(8),
		WAKING(8),
		INACTIVE(8);

		private State(int animationDuration) {
			this.animationsDuration=animationDuration;
		}

		int animationsDuration;

		public int getAnimationDuration() {
			return animationsDuration;
		}
	}


	public void update(float deltaTime) {
		//First check the inactive Delta value : time during which LogMonster is inactive
		if (inactiveDelta>0)
			--inactiveDelta;

		else if(inactiveDelta<0)
			inactiveDelta=0;


		//Same thing applies for sleeping counter 
		if (sleepingTime>0)
			--sleepingTime;

		if (sleepingTime<0)
			sleepingTime=0;

		//Call of the method which actualizes the State of our monster
		switchState();

		super.update(deltaTime);	
	}


	public void switchState() {

		switch(currentState) {
		//For each state, we have to redefined the current animations attached to this State and to ensure that the transitions from one state to another is corresponding to the documentation

		case IDLE:
			detected=false;
			setAnimations(animationsIdle);

			//Generic displacement method similar to the movement of FlameSkull
			randomMove(State.IDLE.getAnimationDuration());

			//Generation of a random value for the moment of inactivity of the monster
			int random=RandomGenerator.getInstance().nextInt(100);

			if (random>=94) {
				int deltaIdle=RandomGenerator.getInstance().nextInt(24);
				inactiveDelta=deltaIdle+50;
				currentState=State.INACTIVE;
			}
			break;

		case ATTACK:
			move(State.ATTACK.getAnimationDuration());

			if (!isDisplacementOccurs())
				currentState=State.FALLING;

			break;

		case FALLING:
			detected=false;
			//Enhance
			int min=(int) MIN_SLEEPING_DURATION;
			int max=(int) MAX_SLEEPING_DURATION;
			sleepingTime=min+RandomGenerator.getInstance().nextInt(max-min);

			resetMotion();

			setAnimations(animationsSleeping);
			currentState=State.SLEEPING;

			break;

		case SLEEPING:	
			//When the sleep counter reaches 0, the current State switches to awake
			if (sleepingTime<=0) {
				currentState=State.WAKING;
				setAnimations(animationsWaking);
			}

			break;

		case WAKING : 
			//The animation of the awakening has to be completed in order to transition to the State IDLE
			if (super.animationCompletion()) 
				currentState=State.IDLE;	

			break;

		case INACTIVE : 
			if (!super.isDisplacementOccurs())
				super.animationsReset();

			if (inactiveDelta<=0)
				currentState=State.IDLE;
		}
	}

	public void draw(Canvas canvas) {
		//Call to super class to draw the death Animation if the LogMonster has no hp
		super.draw(canvas);

		if (detected)
			excla.draw(canvas);
	}

	//Method inherited from the super class : indicates what it is the behavior of the LogMonster when he dies
	//In this case as described in the doc : drop a coin from where he stood
	protected void postMortem() {
		getOwnerArea().registerActor(new Heart(getOwnerArea(), Orientation.DOWN,getCurrentMainCellCoordinates()));
	}
}
