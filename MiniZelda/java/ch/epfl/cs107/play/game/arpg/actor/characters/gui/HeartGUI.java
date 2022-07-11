package ch.epfl.cs107.play.game.arpg.actor.characters.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public enum HeartGUI {
	EMP_HEART(new int[] {0, 0, 16, 16}),
	MID_HEART(new int[] {16, 0, 16, 16}),
	FULL_HEART(new int[] {32, 0, 16, 16});
	
	
	private HeartGUI(int[] coords) {
		this.coords=coords;
	}
	
	ImageGraphics graphics;
	static final String spritename="zelda/heartDisplay";
	int[] coords;
	static final float[] size=new float[] {1.f, 1.f};
	
	public void draw(Canvas canvas, float[] coeff) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		
		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector(width*coeff[0], coeff[1]*height));
				ImageGraphics graphics = new
				ImageGraphics(ResourcePath.getSprite(spritename),
				size[0], size[1], new RegionOfInterest(coords[0], coords[1], coords[2], coords[3]),
				anchor.add(new Vector(0, height - 1.75f)), 1, 1000f);
		
		graphics.draw(canvas);
	}
}
