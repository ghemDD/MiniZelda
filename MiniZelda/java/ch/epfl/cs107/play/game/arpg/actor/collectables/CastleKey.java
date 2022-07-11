package ch.epfl.cs107.play.game.arpg.actor.collectables;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;

public class CastleKey extends ARPGCollectableAreaEntity{
	private RPGSprite sprite;

	public CastleKey(Area area, Orientation orientation, DiscreteCoordinates position){
		super(area, orientation, position);
		
		sprite = new RPGSprite("zelda/key", 1.f,1.f, this, new RegionOfInterest(0,0,16,16));

	}
	

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		super.acceptInteraction(v);
        ((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	

}
