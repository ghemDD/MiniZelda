package ch.epfl.cs107.play.game.tutos.area.tuto1;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.tutos.actor.SimpleGhost;
import ch.epfl.cs107.play.game.tutos.area.SimpleArea;
import ch.epfl.cs107.play.math.Vector;

public class Ferme extends SimpleArea {

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "zelda/Ferme";
	}

	@Override
	protected void createArea() {
		//registerActor(new SimpleGhost(new Vector(18, 7), "ghost.2"));
		registerActor(new Background(this));
		// TODO Auto-generated method stub
		
	}

}
