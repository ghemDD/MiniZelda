
package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends ARPGCollectableAreaEntity{
	
	public final static int COIN=50;

	private int value;
	private RPGSprite sprite;
	Sprite[][] sprites=new Sprite[4][4];
	Animation[] animations=new Animation[4];
	private final static int ANIMATION_DURATION=8;
	public Coin(Area area, Orientation orientation, DiscreteCoordinates position){
		super(area, orientation, position);
		
		sprite = new RPGSprite("zelda/coin", 1.f,1.f, this, new RegionOfInterest(0,0,16,16));
		
		

		
		for(int i=0; i<4; ++i) {
			sprites[0][i]=new RPGSprite("zelda/coin", 1, 1, this, new RegionOfInterest(i*16, 0, 16, 16));
		}
		
		animations=RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
	}
	
	public int getValue() {
		return this.value;
	}
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		super.acceptInteraction(v);
        ((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	@Override
	public void draw(Canvas canvas) {
		animations[0].draw(canvas);
	}
	
	public void update(float deltaTime) {	
		animations[0].update(deltaTime);
		super.update(deltaTime);
	}
	
	public void addMoney(ARPGInventory inventory) {
		inventory.addMoney(COIN);
	}
}
