package ch.epfl.cs107.play.game.arpg.actor.characters.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public enum Digits {
	ZERO(new int[] {16, 32, 16, 16}),
	ONE(new int[] {0,0,16,16}),
	TWO(new int[] {16,0,16,16}),
	THREE(new int[] {32,0,16,16}),
	//Viva l'Algérie
	
	FOUR(new int[] {48,0,16,16}),
	FIVE(new int[] {0, 16, 16, 16}),
	SIX(new int[] {16, 16, 16, 16}),
	SEVEN(new int[] {32, 16, 16, 16}),
	EIGHT(new int[] {48, 16, 16, 16}),
	NINE(new int[] {0, 32, 16, 16});
	
	
	private Digits (int [] coords) {
		this.coords=coords;
	}
	
	ImageGraphics graphics;
	static final String spritename="zelda/digits";
	int[] coords;
	//static final float[] coeff=new float[] {};
	static final float[] size=new float[] {0.75f, 0.75f};
	
	public void draw(Canvas canvas, float[] coeff) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		
		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector(width*coeff[0], coeff[1]*height));
				ImageGraphics graphics = new
				ImageGraphics(ResourcePath.getSprite(spritename),
				size[0], size[1], new RegionOfInterest(coords[0], coords[1], coords[2], coords[3]),
				anchor.add(new Vector(0, height - 1.75f)), 1, 1050f);
		
		graphics.draw(canvas);
	}
}
