package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.Playable;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Window;

abstract public class ARPGArea extends Area implements Playable {
	private ARPGBehavior behavior;
	private SoundAcoustics mySound=new SoundAcoustics(ResourcePath.getSounds("Tara"));
	private static boolean started=false;
	
	protected abstract void createArea();
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getCameraScaleFactor() {
		// TODO Auto-generated method stub
		//return ARPG.CAMERA_SCALE_FACTOR;
		return 10.f;
	}
	
	
	@Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
        	if (!started) {
        		mySound.shouldBeStarted();
            	mySound.bip(window);
            	started=true;
        	}
        	
        	behavior = new ARPGBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }
   
	/**
    public boolean isDoor(DiscreteCoordinates coord) {
    	return (behavior.isDoor(coord));
    } 

**/

}
