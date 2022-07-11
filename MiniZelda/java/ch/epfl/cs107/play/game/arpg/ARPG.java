package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.area.Chateau;
import ch.epfl.cs107.play.game.arpg.area.Ferme;
import ch.epfl.cs107.play.game.arpg.area.Route;
import ch.epfl.cs107.play.game.arpg.area.RouteChateau;
import ch.epfl.cs107.play.game.arpg.area.RouteTemple;
import ch.epfl.cs107.play.game.arpg.area.Temple;
import ch.epfl.cs107.play.game.arpg.area.Village;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPG extends RPG {
	public final static float CAMERA_SCALE_FACTOR = 13.f;
	public final static float STEP = 0.05f;


	private final String[] areas = {"zelda/Ferme", 
									"zelda/Village", 
									"zelda/Route",
									"zelda/RouteChateau", 
									"zelda/Chateau", 
									"zelda/RouteTemple", 
									"zelda/Temple"};
	
	private final DiscreteCoordinates[] startingPositions = {new DiscreteCoordinates(6,10), 
			new DiscreteCoordinates(5,15)};

	private int areaIndex;

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "GTA V";
	}

	/**
	 * Add all the areas
	 */
	private void createAreas(){
		addArea(new Ferme());
		addArea(new Village());
		addArea(new Route());
		addArea(new RouteChateau());
		addArea(new Chateau());
		addArea(new RouteTemple());
		addArea(new Temple());
	}


	public boolean begin(Window window, FileSystem fileSystem) {


		if (super.begin(window, fileSystem)) {
			createAreas();
			areaIndex = 0;
			Area area = setCurrentArea(areas[areaIndex], true);
			//System.out.println(getCurrentArea().getTitle());
			//player = ;
			initPlayer(new ARPGPlayer(area, Orientation.DOWN, startingPositions[areaIndex]));
			return true;
		}
		return false;
	}

	public void update(float deltaTime) {	
		super.update(deltaTime);
	}

}
