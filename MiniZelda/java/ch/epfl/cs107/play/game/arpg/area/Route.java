package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;



import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.background.Orb;
import ch.epfl.cs107.play.game.arpg.actor.background.Rock;
import ch.epfl.cs107.play.game.arpg.actor.background.WaterFall;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.monsters.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.monsters.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
//ROUTE

public class Route extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/Route";
	}
	
	protected void createArea() {
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		registerActor(new WaterFall(this, Orientation.DOWN, new DiscreteCoordinates(15, 3)));
		
		Door door1=new Door("zelda/Ferme", new DiscreteCoordinates(18, 15),  Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(0, 15), new DiscreteCoordinates(0, 16));
		Door door2=new Door("zelda/Village", new DiscreteCoordinates(29, 18), Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0));
		Door door3=new Door("zelda/RouteChateau", new DiscreteCoordinates(9, 1),  Logic.TRUE, this, Orientation.UP, new DiscreteCoordinates(9, 19), new DiscreteCoordinates(10, 19));
		Door door4=new Door("zelda/RouteTemple", new DiscreteCoordinates(1, 5),  Logic.TRUE, this, Orientation.RIGHT, new DiscreteCoordinates(19,10));
		Orb orbe=new Orb(this, Orientation.DOWN, new DiscreteCoordinates(19, 8));
		Rock rock=new Rock(this, Orientation.DOWN, new DiscreteCoordinates(15, 10));
		
		
		registerActor(door1);
		registerActor(door2);
		registerActor(door3);
		registerActor(door4);
		registerActor(orbe);
		registerActor(rock);
		
		registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(4, 4), 5));
		registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 10), 5, 5));
		
		for(int i=5; i<8; ++i) {
			for(int j=6; j<12; ++j) {
				registerActor(new Grass(this, Orientation.DOWN, new DiscreteCoordinates(i, j)));
			}
		}
		
	}
	
}
