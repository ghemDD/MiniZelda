package ch.epfl.cs107.play.game.arpg.actor.characters.npc;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGKing extends ARPGPNJ {
	Sprite sprite = new RPGSprite("zelda/king", 1, 2, this, new RegionOfInterest(0, 64, 16, 32));
	
	public ARPGKing(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {	
	}
	
}
