package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.background.Portal;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGPNJ;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGSalesman;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.Sage;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.Skull;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Village extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/Village";
	}
	
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		
		Door door1=new Door("zelda/Ferme", new DiscreteCoordinates(4, 1),  Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(4, 19), new DiscreteCoordinates(5, 19));
		Door door2=new Door("zelda/Ferme", new DiscreteCoordinates(14, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(13, 19), new DiscreteCoordinates(14, 19), new DiscreteCoordinates(15,19));
		Door door3=new Door("zelda/Route", new DiscreteCoordinates(9, 1), Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(29, 19), new DiscreteCoordinates(30, 19));
		ARPGPNJ pnj=new ARPGPNJ(this, Orientation.DOWN, new DiscreteCoordinates(15, 8));
		Sage sage=new Sage(this, Orientation.DOWN, new DiscreteCoordinates(14, 7));
		Skull skull=new Skull(this, Orientation.DOWN, new DiscreteCoordinates(13, 11));
		Portal portal=new Portal(this, Orientation.DOWN, new DiscreteCoordinates(5, 10));
		
		registerActor(door1);
		registerActor(door2);
		registerActor(door3);
		registerActor(pnj);
		registerActor(sage);
		registerActor(skull);
		registerActor(portal);
		
		registerActor(new ARPGSalesman(this, Orientation.DOWN, new DiscreteCoordinates(17, 11)));
	}
}
