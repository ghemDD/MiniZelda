package ch.epfl.cs107.play.game.arpg.area;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.collectables.StaffWater;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.FireSpell;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Ferme extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/Ferme";
	}	
	
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this));

		Door door1=new Door("zelda/Route", new DiscreteCoordinates(1, 15),  Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19, 15), new DiscreteCoordinates(19, 16));
		Door door2=new Door("zelda/Village", new DiscreteCoordinates(4, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4, 0), new DiscreteCoordinates(5, 0));
		Door door3=new Door("zelda/Village", new DiscreteCoordinates(14, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(13, 0), new DiscreteCoordinates(14, 0));
		registerActor(door1);
		registerActor(door2);
		registerActor(door3);

		


	}
	
}
