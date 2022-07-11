package ch.epfl.cs107.play.game.arpg.actor.characters.npc;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Skull extends ARPGPNJ{
	Sprite[][] spritesSkull = RPGSprite.extractSprites("skull", 3, 1.5f, 1.5f, this, 32, 32, new Orientation[] {Orientation.DOWN, Orientation.LEFT, Orientation.RIGHT, Orientation.UP});
	Animation[] animations=RPGSprite.createAnimations(ANIMATION_DURATION/2, spritesSkull);
	
	public Skull(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		setAnimation(animations);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}
}