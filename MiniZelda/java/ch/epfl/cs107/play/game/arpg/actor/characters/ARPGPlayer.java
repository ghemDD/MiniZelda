package ch.epfl.cs107.play.game.arpg.actor.characters;

import java.util.Collections;

import java.util.List;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
//import ch.epfl.cs107.play.game.areagame.arpg.actor.GUI.ARPGPlayerStatusGUI;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.background.Rock;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGKing;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGPNJ;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGSalesman;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.Sage;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.collectables.StaffWater;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster.Vulnerabilities;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class ARPGPlayer extends Player implements ARPGInventory.Holder{
	//Attributes 
	private float hp;
	private boolean dead=false;
	
	//Current State
	private ARPGPlayer.State currentState;
	
	//Boolean especially useful for the Dialog/Inventory GUI : when false the player cannot longer move
	private boolean canMove=true;
	
	
	//Pet
	private final Cat cat;
	private boolean catHere;
	
	//Counter used when the player is hurt as the sprite is alternating
	private int hurtCounter=0;
	
	//Transaction handler
		private boolean saleInteraction=false;
		private boolean regularInteraction=false;
		private boolean view;
		private boolean closeSale=false;
	
	
	//Sound Design : Interactions with the player
	
		//Sound of the Salesman : (Source : Resident Evil 4)
	private SoundAcoustics merchent=new SoundAcoustics(ResourcePath.getSounds("Merchant"));
		//Sound of the Bow : (Source : Minecraft)
	private SoundAcoustics bow=new SoundAcoustics(ResourcePath.getSounds("Bow"));
		//Sound of the Sword : (Source The Witcher)
	private SoundAcoustics sword=new SoundAcoustics(ResourcePath.getSounds("Sword"));
		//Music played when the player finally reaches the final objective : (Source : God of War, Jotunheim)
	private SoundAcoustics ending=new SoundAcoustics(ResourcePath.getSounds("Ending"));
		//Sound of item picked
	private SoundAcoustics itemPicked=new SoundAcoustics(ResourcePath.getSounds("itemPicked"));
		//Sound PNJ
	private SoundAcoustics ear=new SoundAcoustics(ResourcePath.getSounds("ear"));
	
	
	//Handler for the interactions of the player
	private final ARPGPlayerHandler handler;
	
	//Macimum of player's health
	public final static int MAX_HEALTH=10;
	
	//Inventory and current item of the player
	private ARPGInventory inventory;
	private ARPGItem currentItem;
	
	private static int ANIMATION_DURATION=3;
	private Animation[] currentAnimations;
	//GUI
	private ARPGPlayerStatusGUI gui=new ARPGPlayerStatusGUI();
	//Context
	private Keyboard keyboard= getOwnerArea().getKeyboard();
	
	//Dialogue : currentDialogue is depending on the interactions of the player, dialogueCount counts the number of sentences of the speaker
	//Counter which is incremented by one each time a sentence : when reaches its max Value the Dialog no longer appears
	private Dialog currentDialogue;
	private int dialogueCount=0;
    //Determine whether the currentDialogue has to be drawn or not (linked with dialogueCount)
	private boolean dialogue=false;
	//An attribute that when it is superior to 5 : PNJ's will have to act nicely and vice versa
	//If your reputation is too low you might not be able to buy something from the store
    private int reputation=10;
    private int currentDialogueCount;
	
	//Constant sprites and animations
		//State Idle
		private Sprite[][] spritesIdle = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this, 16, 32, new Orientation[] {Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
		private Animation[] animationsIdle=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesIdle);
	
		//State Bow
		private Sprite[][] spritesBow = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		private Animation[] animationsBow=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesBow, false);
	
		//State Sword
		private Sprite[][] spritesSword = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		private Animation[] animationsSword=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesSword, false);
	
		//State Staff
		private Sprite[][] spritesStaff = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});
		private Animation[] animationsStaff=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesStaff, false);
		
		//State Cardboard
		private Sprite[][] spritesCardboard = RPGSprite.extractSprites("cardboard", 3, 1.5f, 1.5f, this, 48, 48, new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.RIGHT, Orientation.DOWN});
		private Animation[] animationsCardboard=RPGSprite.createAnimations(ANIMATION_DURATION, spritesCardboard);
	
	//Constructor
	public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		// TODO Auto-generated constructor stub
		super(area, orientation, coordinates);
		
		cat= new Cat(area, orientation, coordinates);
		
		catHere=false;
		this.handler = new ARPGPlayerHandler();
		this.hp = 10;
		canMove=true;
		
		//Basic inventory
		inventory=new ARPGInventory(20, 10, keyboard, false, getOwnerArea().getWindow());
		inventory.addItem(ARPGItem.BOMB, 2);
		inventory.addItem(ARPGItem.SWORD, 1);
		inventory.addItem(ARPGItem.BOW, 1);
		inventory.addItem(ARPGItem.ARROW, 10);
		inventory.addItem(ARPGItem.STAFF, 0);
		
		//Default state of the player at the start of the game
		currentItem=ARPGItem.SWORD;
		currentState=State.IDLE;
		currentAnimations=animationsIdle;
		
		resetMotion();
	}


	@Override
    public void update(float deltaTime) { 
		/**
		 * Cat Handler : replaces the pet at the right place
		 */
		if(catHere==false) {
			if(getOwnerArea().canEnterAreaCells(cat, getCurrentCells())) {
				cat.enterArea(getOwnerArea(), getCurrentMainCellCoordinates());
				catHere=true;}
		}
		
		
		/**
		 * Life handler
		 */
		if (hp <= 0)
			hp=0;
	 
		if (hp>MAX_HEALTH) 
			hp=MAX_HEALTH;
	 	
		/**
		 * Behavior depending on the State of the player		
		 */
		switch (currentState) {
		case IDLE :
			view=false;
			currentAnimations=animationsIdle;
			if (canMove) {
				/**
				 * Movement of the Player : uses Button LEFT, RIGHT, UP, DOWN Arrow
				 */
				moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
		        moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
		        moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
		        moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
		        
		        /**
		         * Switches the current item by using the method of the inventory (See more details in the class ARPGInventory)
		         */
		        if (keyboard.get(Keyboard.TAB).isPressed()) 
		        	currentItem=inventory.switchCurrent(currentItem);
		        }
		        
		        /**
		         * Switches to Cardboard State : uses the item Cardboard Box 
		         */
		        if (keyboard.get(Keyboard.F).isPressed()) {
		        	currentState=State.CARDBOARD;
		        }
		        
		        /**
		         * Use of the item Bomb 
		         */
		        if (keyboard.get(Keyboard.SPACE).isPressed() && currentItem==ARPGItem.BOMB) {
		        	
		        	if (remains(ARPGItem.BOMB)) {
		        		Bomb throwedBomb=new Bomb(getOwnerArea(), Orientation.DOWN, getFieldOfViewCells().get(0));
		        		if(getOwnerArea().canEnterAreaCells((Interactable) throwedBomb,getFieldOfViewCells() )) {
		        			inventory.removeItem(ARPGItem.BOMB, 1);
			        		getOwnerArea().registerActor(new Bomb(getOwnerArea(), Orientation.DOWN, getFieldOfViewCells().get(0)));
		        		
		        		}
		        	}
		        	
		        		
		        }
		        
		        if (keyboard.get(Keyboard.I).isPressed()) {
		        	inventory.switchState();
		        	currentState=State.INVENTORY;
		        }
		        
		        if (keyboard.get(Keyboard.Z).isDown())
		        	ANIMATION_DURATION=2;
		        else
		        	ANIMATION_DURATION=5;
		        	  
		        useEquipment();
		        
		        if (isDisplacementOccurs()) 
		        	currentAnimations[getOrientation().ordinal()].update(deltaTime);
						
				else 
					currentAnimations[getOrientation().ordinal()].reset();
			
	        break;
	        
		case SWORD : 
			currentAnimations[getOrientation().ordinal()].setAnchor(new Vector(-0.5f,0));

			currentAnimations[getOrientation().ordinal()].update(deltaTime);
				if (currentAnimations[getOrientation().ordinal()].isCompleted()) {
					currentState=State.IDLE;
					canMove=true;
				}
					
			break;
			
		case BOW :
			currentAnimations[getOrientation().ordinal()].setAnchor(new Vector(-0.5f,0));

			currentAnimations[getOrientation().ordinal()].update(deltaTime);
        	
				if (currentAnimations[getOrientation().ordinal()].isCompleted()) {
					if (remains(ARPGItem.ARROW)) {
					getOwnerArea().registerActor(new Arrow(getOwnerArea(), getOrientation(), getCurrentCells().get(0), 2, 8));
					inventory.removeItem(ARPGItem.ARROW, 1);
					}
					currentState=State.IDLE;
				}
				
			break;
			
		case STAFF : 
			currentAnimations[getOrientation().ordinal()].setAnchor(new Vector(-0.5f,0));

			currentAnimations[getOrientation().ordinal()].update(deltaTime);
			if (currentAnimations[getOrientation().ordinal()].isCompleted()) {
				getOwnerArea().registerActor(new MagicWaterProjectile(getOwnerArea(), getOrientation(), getCurrentCells().get(0), 2, 8));
				currentState=State.IDLE;
				canMove=true;
			}
			break;
			
			
			
		case INVENTORY:
			currentItem=inventory.switchCurrentInInventory();
			if (keyboard.get(Keyboard.I).isPressed()) {
	        	inventory.switchClose();
	        	currentState=State.IDLE;
			}
			
			break;
			
		case DIALOGUE:
			canMove=false;
			if (keyboard.get(Keyboard.ENTER).isPressed()) {
	        	++dialogueCount;
	        	
	        	if (dialogueCount==currentDialogueCount) {
	        		canMove=true;
	        		currentState=State.IDLE;
	        			
	        		if (saleInteraction)  {
	        			//System.out.println("Moussa");
	        			view=true;
	        			//saleInteraction=true;
	        		}	
	        		
	        		if (regularInteraction) {
	        			System.out.println("mous");
	        			view=true;
	        		}
	        	}
	        	
	        
	        }
			break;
			
		case CARDBOARD:
			currentAnimations=animationsCardboard;
			if (keyboard.get(Keyboard.F).isPressed())
				currentState=State.IDLE;
			
			moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
	        moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
	        moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
	        moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
			
			if (isDisplacementOccurs())

				currentAnimations[getOrientation().ordinal()].update(deltaTime);
				
			break;
		}
		
		//GUI Update
        gui.update(deltaTime, getMoney(), hp, currentItem);
        inventory.update(deltaTime);
        //System.out.println("Player "+getCurrentCells());
        
  
       //Dialogue handler
        
        		  
        super.update(deltaTime);
    }
	
	/**
     * Orientate or Move this player in the given orientation if the given button is down
     * @param orientation (Orientation): given orientation, not null
     * @param b (Button): button corresponding to the given orientation, not null
     */
    private void moveOrientate(Orientation orientation, Button b){
        if(b.isDown()) {
            if(getOrientation() == orientation) move(ANIMATION_DURATION);
            
            else orientate(orientation);
        }
    }
     
    /**
     * method allowing player to use his equipment under certain conditions
     */
    public void useEquipment() {
    	
    	if (keyboard.get(Keyboard.SPACE).isPressed()) {
    		if (currentItem==ARPGItem.SWORD) {
    			currentAnimations=animationsSword;
    			currentState=State.SWORD;
    			canMove=true;
    			view=true;
    		}
    	}

    	if (keyboard.get(Keyboard.SPACE).isPressed()) {
    		if (currentItem==ARPGItem.BOW) {
    			bow.shouldBeStarted();
            	bow.bip(getOwnerArea().getWindow());
    			currentAnimations=animationsBow;
    			currentState=State.BOW;
    		}
    		
    		if (currentItem==ARPGItem.STAFF) {
    			currentAnimations=animationsStaff;
    			currentState=State.STAFF;
    		}	
    	}
    }
    
	//Interactions handler
    private class ARPGPlayerHandler implements ARPGInteractionVisitor {
    	
    	
    	public void interactWith(ARPGCollectableAreaEntity collectable) {
    		collectable.Collection();
    	}	
    	
    	public void interactWith(Door door) {
    		ARPGPlayer.this.setIsPassingADoor(door);
    		cat.leaveArea();
    		catHere= false;
    	}	
    	
    	public void interactWith(Grass grass) {	
    		if (currentItem==ARPGItem.SWORD) 
    			grass.slice();
    			sword.shouldBeStarted();
        		sword.bip(getOwnerArea().getWindow());
    	}
    	
    	public void interactWith(Heart heart) {
    		heart.Collection();
    		heart.heal(ARPGPlayer.this);
    		itemPicked.shouldBeStarted();
			itemPicked.bip(getOwnerArea().getWindow());
    	}
    	
    	public void interactWith(Coin coin) {
    		coin.Collection();

    		coin.addMoney(inventory);
    		itemPicked.shouldBeStarted();
			itemPicked.bip(getOwnerArea().getWindow());
    	}
    	
    	public void interactWith(Bomb bomb) {
    		if (currentItem==ARPGItem.SWORD)
    			bomb.setExplosion();
    	}
    	
    	public void interactWith(CastleDoor door1) {
    		if (door1.isOpen()) {
    			ARPGPlayer.this.setIsPassingADoor(door1);
    			cat.leaveArea();
        		catHere= false;
    			ending.shouldBeStarted();
            	ending.bip(getOwnerArea().getWindow());
    			door1.setSignal(Logic.FALSE);
    			door1.setState(false);
    		}
    			
    		else if (ARPGPlayer.this.possess(ARPGItem.CASTLEKEY)) {
    			door1.setSignal(Logic.TRUE);
    			door1.setState(true);
    		}
    	}
    	
    	public void interactWith(Monster monster) {
    		if (currentState==State.SWORD)
    			monster.damageType(Vulnerabilities.PHYS, 1.f);
    			sword.shouldBeStarted();
    			sword.bip(getOwnerArea().getWindow());
    	}
    	
    	public void interactWith(CastleKey key) {
    		key.Collection();

    		inventory.addItem(ARPGItem.CASTLEKEY, 1);
    		currentItem=ARPGItem.CASTLEKEY;
    	}
    	
    	public void interactWith(StaffWater staff) {
    		staff.Collection();

    		inventory.addItem(ARPGItem.STAFF, 1);
    		currentItem=ARPGItem.STAFF;
    		staff.playSound();
    	}
    	
    	public void interactWith(ARPGKing king) {
    		currentDialogue=new Dialog("You have protected this land !", "zelda/dialog", getOwnerArea());
    		dialogue=true;
    		dialogueCount=0;
    	}
    	
    	public void interactWith(Rock rock) {
    		currentDialogue=new Dialog("I need a bomb !", "zelda/dialog", getOwnerArea());
    		dialogue=true;
    		dialogueCount=0;
    	}
    	
    	public void interactWith(ARPGSalesman salesman) {
        	reputation=6;	
    		if (currentState==State.IDLE) {
    			
    			if (!saleInteraction) {
        			if (reputation>5) 
            			currentDialogue=new Dialog("What will it be ?", "zelda/dialog", getOwnerArea());
            	
            		
            		else 
            			currentDialogue=new Dialog("You're not accepted in the store", "zelda/dialog", getOwnerArea());
            		
        			closeSale=false;
            		merchent.shouldBeStarted();
        			merchent.bip(getOwnerArea().getWindow());
        			currentState=State.DIALOGUE;
            		currentDialogueCount=1;
            		
            		dialogueCount=0;
            		saleInteraction=true;
        		}
        		
        		else {
        			salesman.setTransaction(inventory);
        			System.out.println("Mousd;d;dsa");
        			System.out.println(closeSale);
        			canMove=false;
        			
        			if (closeSale) {
        				salesman.finishTransaction();
        				canMove=true;
        				closeSale=false;
        				saleInteraction=false;
        			}
        				
        			
        			closeSale=true;
        		}
    		}
        		
        }
        	
        	    	
    	public void interactWith(ARPGPNJ pnj) {
    		reputation=3;
    		//pnj.setMove(false);
    		
    		
    		
    		
    		if (!regularInteraction) {
    			pnj.talkMode(getOrientation());
    			currentState=State.DIALOGUE;
        		currentDialogueCount=1;
        		//dialogue=true;
        		dialogueCount=0;
        		canMove=false;
        		regularInteraction=true;
        		
        		
        		if (reputation>5) {
        			currentDialogue=new Dialog("Welcome", "zelda/dialog", getOwnerArea());
        		}
        		
        		else {
        			currentDialogue=new Dialog("You're not welcome !", "zelda/dialog", getOwnerArea());
        			ear.shouldBeStarted();
        			ear.bip(getOwnerArea().getWindow());
        		}
        		
    		}
    		
    		else {
    			pnj.switchMove();
    			System.out.println("ss");
    			view=false;
    			regularInteraction=false;
    		}
    		
    	}
    	
    	public void interactWith(Sage sage) {
    		currentDialogue=new Dialog("Derrière ton humble demeure tu trouveras, le fruit de la bêtise humaine", "zelda/dialog", getOwnerArea());
    		
    		currentState=State.DIALOGUE;
    		currentDialogueCount=1;
    		dialogue=true;
    		dialogueCount=0;
    		canMove=false;
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
		Button e=keyboard.get(Keyboard.E);
		
		if (e.isPressed()) 
			return true;
		
		
		return view;
	}


	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
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
		return !(currentState==State.CARDBOARD);
	}


	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	//Drawable
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		if( hurtCounter == 0) {

			currentAnimations[getOrientation().ordinal()].draw(canvas);
		} 
		if( hurtCounter !=0) {
			hurtCounter -=1;
			if(hurtCounter %6 >  2) {

				currentAnimations[getOrientation().ordinal()].draw(canvas);

			}
		}
		
		
		gui.draw(canvas);
		inventory.draw(canvas);
		
		if (currentState==State.DIALOGUE)
			currentDialogue.draw(canvas);	
	}


	//Equipment
	@Override
	public boolean possess(InventoryItem item) {
		// TODO Auto-generated method stub
		return inventory.isInInventory(item);
	}
	
	@Override
	public boolean remains(InventoryItem item) {
		return inventory.remainsInInventory(item);
	}
	
	//Interactions with player
	public boolean damage(float f) {
		// TODO Auto-generated method stub
		hurtCounter=24;
		hp-=f;
		
		return true;
	}
	
	public void heal(int heal) {
		hp+=heal;
	}
	
	//Getters
	protected float getHealth() {
		return hp;
	}
	
	protected int getMoney() {
		return inventory.getMoney();
	}
	
	//State
	private enum State {
		IDLE,
		SWORD,
		BOW,
		STAFF,
		INVENTORY,
		DIALOGUE,
		CARDBOARD;
	}
	
	//Dialogue Mode
	
	 /**
     * @author Tanguy
     *Here we have a nested class which represents a pet that will follow the ARPGPlayer, the Cat extends from player because
     *because we need all the methods dealing with leaving a current area and entering a new One.
     */
    public class Cat extends Player{
    	private final CatHandler catHandler= new CatHandler();

    	Sprite[][] spritesDog;
    	Animation[] animationDog;
    	private int followCounter=5;

    	
    	
    	private class CatHandler implements ARPGInteractionVisitor{
    		/*
    		public void interactWith(Door door) {

        		setIsPassingADoor(door);        		
        		}
    		*/
    	}
    	
    	
    	public Cat(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
    		super(area, orientation, coordinates);
    		
    		
    		spritesDog = RPGSprite.extractSprites("zelda/Dog", 3, 1.5f, 1.5f, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT, Orientation.UP});
    		animationDog=   RPGSprite.createAnimations(ANIMATION_DURATION, spritesDog);

    		
    		


    	}
    	@Override
    	public List<DiscreteCoordinates> getCurrentCells() {
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
			return false;
		}

	
		@Override
		public void update(float deltaTime) {

			
			if(ARPGPlayer.this.isDisplacementOccurs() == true) {
				follow();
				animationDog[getOrientation().ordinal()].update(deltaTime);

			}

    	    super.update(deltaTime);

		}
		
		
		
		/**This method allow the cat to smoothly follow the player, if the player 
		 * moves in a different Orientation that the previous one, the cat will 
		 * move slightly in the previous direction before taking the same orientation
		 * as the player, we always make sure that the cat stays always behind the player
		 * when he finished to orientate. 
		 * If the cat has the same orientation as the player, he just keeps moving forward.
		 * 
		 */
		private void follow() {
			--followCounter;
			Orientation orientationChat = getOrientation();

			Orientation orientationPlayer= ARPGPlayer.this.getOrientation();
			if(orientationChat != orientationPlayer){
				if(followCounter>0) {
					move(ANIMATION_DURATION-1);

				} else {
					resetMotion();
					this.orientate(orientationPlayer);

					int index=(getOrientation().ordinal() +2 )% 4;

					DiscreteCoordinates coord = ARPGPlayer.this.getCurrentMainCellCoordinates().jump(Orientation.values()[index].toVector());

					Vector displacementVector= new Vector(coord.x,  coord.y);
					setCurrentPosition(displacementVector);
				}

			}	else {
					followCounter=5;
					move(ANIMATION_DURATION);
			}
		}

		@Override
		public void draw(Canvas canvas) {
			animationDog[getOrientation().ordinal()].setAnchor(new Vector(-0.3f,0));

			animationDog[getOrientation().ordinal()].draw(canvas);
		}
		
    	@Override
    	public List<DiscreteCoordinates> getFieldOfViewCells(){
    		return Collections.singletonList(((getCurrentMainCellCoordinates().jump(getOrientation().toVector())).jump(getOrientation().toVector())).jump(getOrientation().toVector()));
    	}
    	
		@Override
		public boolean wantsCellInteraction() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean wantsViewInteraction() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void interactWith(Interactable other) {
			other.acceptInteraction(catHandler);
		}
		
		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
	    	((ARPGInteractionVisitor) v).interactWith(this);
			
		}
		 /**
		 * same method as Player except the cat doesn't become the View Candidate
		 */
		@Override
		public void enterArea(Area area, DiscreteCoordinates position){
		        area.registerActor(this);

		        setOwnerArea(area);
		        setCurrentPosition(position.toVector());
		        resetMotion();
		    }
    	}
}
