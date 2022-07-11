package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.math.Vector;

public class Village extends SimpleArea {

	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "zelda/Village";
	}

	@Override
	protected void createArea() {
		// TODO Auto-generated method stub
		registerActor(new Background(this));
		//registerActor(new Foreground(this));
		registerActor(new SimpleGhost(new Vector(18, 7), "ghost.2"));
		
	}
}
