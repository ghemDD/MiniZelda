package ch.epfl.cs107.play.game.arpg.actor.monsters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.FireSpell;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomEvent;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class DarkLord extends Monster{
	private final static RandomEvent randomInstance= new RandomEvent();

	private final DarkLordHandler handler= new DarkLordHandler();
	private State currentState;
	private static int ANIMATION_DURATION=5;
	private final static float PROBABILITY_TO_ATTACK=0.5f;
	private static int MAX_SPELL_WAIT_DURATION= 150;
	private static int MIN_SPELL_WAIT_DURATION= 100;
	private static int TELEPORTATION_RADIUS = 6;
	private float inactiveDelta;
	private int n;
	private final static int nombreDEssais =20;
	private final static int interactionRadius = 2;
	// Constant animations for different States
	private Sprite[][] spritesMove = RPGSprite.extractSprites("zelda/darkLord", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

	private Animation[] animationsMove=RPGSprite.createAnimations(ANIMATION_DURATION, spritesMove, false);

	private Sprite[][] spritesSpell = RPGSprite.extractSprites("zelda/darkLord.spell", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});

	private Animation[] animationsSpell=RPGSprite.createAnimations(ANIMATION_DURATION, spritesSpell,false);

	private Animation[] animationsSpellTeleport=RPGSprite.createAnimations(ANIMATION_DURATION/3, spritesSpell,false);


	public DarkLord(Area area, Orientation orientation, DiscreteCoordinates coor, float health, int max) {
		super(area, orientation, coor, health, max, new Vulnerabilities[]{Vulnerabilities.MAGIC});
		currentState= State.IDLE;
		setAnimations(animationsMove);
		n=randomInstance.nextInt(MIN_SPELL_WAIT_DURATION, MAX_SPELL_WAIT_DURATION );

		inactiveDelta=0;

	}


	private void resetSpellAnimations() {
		animationsSpell[getOrientation().ordinal()].reset();
		animationsSpellTeleport[getOrientation().ordinal()].reset();
	}


	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells(){

		List<DiscreteCoordinates> neighboursCoord= new ArrayList<DiscreteCoordinates>();
		DiscreteCoordinates actual= getCurrentMainCellCoordinates();
		// Adding the neighboors cells

		//Adding the neighboors cells

		for(int x=actual.x-interactionRadius; x<=actual.x+interactionRadius; ++x) {
			for(int y=actual.y-interactionRadius; y<=actual.y+interactionRadius; ++y) {
				if (x>=0 && y>=0) 
					neighboursCoord.add(new DiscreteCoordinates(x, y));
			}
		}

		return neighboursCoord;
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



	private class DarkLordHandler implements ARPGInteractionVisitor{



		public void interactWith(ARPGPlayer player) {


			if(!getDead() && currentState != State.SPELL && currentState != State.TELEPORTATION) {


				currentState = State.SPELL;
				resetSpellAnimations();
				resetMotion();
			}

		}


	}


	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}


	private enum State{
		IDLE,
		ATTACK,
		SUMMON,
		SPELL,
		TELEPORTATION,
		INACTIVE;
	}

	

	public void switchState() {
		switch(currentState) {
		
		
		
		
		
		case INACTIVE : 
			
			if (!super.isDisplacementOccurs())
				super.animationsReset();
			
			if (inactiveDelta<=0)
				currentState=State.IDLE;
		
		
		case IDLE:
			setAnimations(animationsMove);
			animationsReset();

			randomMove(ANIMATION_DURATION * 2);
			
			
			//Generation of a random value for the moment of inactivity of the monster
			int random=RandomGenerator.getInstance().nextInt(100);

			if (random>=94) {
				int deltaIdle=RandomGenerator.getInstance().nextInt(24);
				inactiveDelta=deltaIdle+50;
				currentState=State.INACTIVE;
			}
			break;

		case ATTACK:
			setAnimations(animationsSpell);


			//animationCompletion();
			if(animationCompletion()) {
				resetSpellAnimations();
				getOwnerArea().registerActor(new FireSpell(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),4 ));
				currentState=State.IDLE;

			}	





			break;
		case SUMMON:


			setAnimations(animationsSpell);
			if(animationCompletion()) {
				resetSpellAnimations();
				getOwnerArea().registerActor(new FlameSkull(getOwnerArea(),getOrientation(),getCurrentMainCellCoordinates().jump(getOrientation().toVector()),5 ));
				currentState=State.IDLE;

			}



			break;

		case SPELL:
			if(!isDisplacementOccurs() ) {
				setAnimations(animationsSpellTeleport);




			}

			//animationsEquals(animationsSpellTeleport) && animationCompletion();
			if(animationsEquals(animationsSpellTeleport) && animationCompletion()) {

				currentState=State.TELEPORTATION;

			}

			break;
		case TELEPORTATION:
			setAnimations(animationsMove);
			boolean teleported=false;
			resetMotion();
			int compteur = nombreDEssais;
			DiscreteCoordinates actual= getCurrentMainCellCoordinates();
			do {
				List<DiscreteCoordinates> teleportCoordList =  new ArrayList<DiscreteCoordinates>();


				int randomX = randomInstance.nextInt(actual.x  - TELEPORTATION_RADIUS, actual.x + TELEPORTATION_RADIUS +1);
				int randomY = randomInstance.nextInt(actual.y  - TELEPORTATION_RADIUS, actual.y + TELEPORTATION_RADIUS +1);

				teleportCoordList.add(new DiscreteCoordinates(randomX,randomY));

				if(randomX == 0 && randomY==0 || getOwnerArea().canEnterAreaCells(this, teleportCoordList) ) {
					Vector displacementVector= new Vector( randomX,  randomY);
					List<DiscreteCoordinates> leavingCells=getFieldOfViewCells();
					//System.out.println("Cellules quitées :"+ leavingCells.get(0).x+" "+leavingCells.get(0).y);
					
					getOwnerArea().leaveAreaCells( this, leavingCells);
					//getOwnerArea().unregisterActor(this);
					
					

					//setCurrentPosition(displacementVector);
					getOwnerArea().enterAreaCells((Interactable) this, teleportCoordList);

					setCurrentPosition(displacementVector);
					//getOwnerArea().registerActor(new DarkLord(getOwnerArea(), Orientation.UP, teleportCoordList.get(0), getHealth(), getMaxHealth() ));
					teleported = true;
				}
				compteur -= 1;

			}while(teleported == false && compteur !=0);


			currentState=State.IDLE;
			inactiveDelta=10;


			break;
		}

	}

	public List<DiscreteCoordinates> getOrientationSingleField() { 
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}


	public void chooseSpell(){
		double randomAction = RandomGenerator.getInstance().nextDouble();

		if(randomAction < PROBABILITY_TO_ATTACK) {
			currentState= State.ATTACK;
		} else {
			currentState=State.SUMMON;
		}

		FireSpell placementTest= new FireSpell(getOwnerArea(), getOrientation(),  getOrientationSingleField().get(0), 4);
		if( getOwnerArea().canEnterAreaCells((Interactable) placementTest,getOrientationSingleField())){

			return;
		} else {
			List<DiscreteCoordinates> actionCoordList =  new ArrayList<DiscreteCoordinates>();
			int compteur = nombreDEssais;
			boolean orientationOK=false;
			do {
				int random=randomInstance.nextInt(0,4);
				Orientation randomOrientation= Orientation.values()[random];
				DiscreteCoordinates neighOrientation=getCurrentMainCellCoordinates().jump(randomOrientation.toVector());
				actionCoordList.add(neighOrientation);
				if(getOwnerArea().canEnterAreaCells((Interactable) placementTest,actionCoordList)){
					resetMotion();
					orientate(randomOrientation);


					orientationOK=true;

				} else {
					actionCoordList.clear();


				}
				compteur -=1;





			} while(!orientationOK && compteur !=0 );


		}
	}

	public void update(float deltaTime) {
		if (inactiveDelta>0)
			--inactiveDelta;

		else if(inactiveDelta<0)
			inactiveDelta=0;

		n --;


		switchState();

		if(n<=0) {
				chooseSpell();
				n=randomInstance.nextInt(MIN_SPELL_WAIT_DURATION, MAX_SPELL_WAIT_DURATION );
			}	
		
		
		
		super.update(deltaTime);


		
		}

	

	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	protected void postMortem() {	

		

		getOwnerArea().registerActor(new CastleKey(getOwnerArea(), Orientation.DOWN,  getCurrentCells().get(0)));
	
	}
}
