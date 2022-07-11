package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;

import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.collectables.StaffWater;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Temple extends ARPGArea {

	@Override
	public String getTitle() {
		return "zelda/Temple";
	}
	
	@Override
	protected void createArea() {
		// TODO Auto-generated method stub
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		
		Door door1=new Door("zelda/RouteTemple", new DiscreteCoordinates(5, 5),  Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(4, 0));
		//LightHalo light=new LightHalo(this);
		StaffWater staff=new StaffWater(this, Orientation.DOWN, new DiscreteCoordinates(4, 3));
		
		
		registerActor(door1);
		//registerActor(light);
		registerActor(staff);
		
	}
	
}
