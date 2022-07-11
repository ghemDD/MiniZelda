package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;

import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.monsters.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.monsters.FlameSkull;
import ch.epfl.cs107.play.game.arpg.actor.monsters.LogMonster;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;

public class RouteChateau extends ARPGArea{

	@Override
	public String getTitle() {
		return "zelda/RouteChateau";
	}
	
	
	@Override
	protected void createArea() {
		// TODO Auto-generated method stub
		registerActor(new Background(this));
		registerActor(new Foreground(this));
		
		Door door1=new Door("zelda/Route", new DiscreteCoordinates(9, 18),  Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0));
		CastleDoor door2=new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7, 1), Logic.FALSE, this, Orientation.UP, new DiscreteCoordinates(9, 13), new DiscreteCoordinates(10, 13));
		DarkLord dark=new DarkLord(this, Orientation.DOWN, new DiscreteCoordinates(9, 12), 5, 5);
		
		
		registerActor(door1);
		registerActor(door2);
		registerActor(dark);
	}

	
	public void update(float deltaTime) {
		
		super.update(deltaTime);
		Keyboard keyboard=getKeyboard();
		
		
		if (keyboard.get(Keyboard.S).isPressed()) 
			registerActor(new FlameSkull(this, Orientation.DOWN, new DiscreteCoordinates(10, 10), 5));
		
		if (keyboard.get(Keyboard.B).isPressed())
			registerActor(new Bomb(this, Orientation.DOWN, new DiscreteCoordinates(9, 6)));	
		
		if (keyboard.get(Keyboard.L).isPressed())
			registerActor(new LogMonster(this, Orientation.DOWN, new DiscreteCoordinates(9, 9), 8, 8));
	}
	
}
