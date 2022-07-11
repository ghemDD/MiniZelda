package ch.epfl.cs107.play.game.arpg.actor.characters.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class InventorySlotsGUI {
	private final static String SPRITENAME="zelda/inventory.slot";
	private final static int[] COORDS=new int[] {0, 0, 64, 64};
	private final static float[] SIZE= new float[] {1.85f, 1.85f};
	private float[] coeff;
	private boolean selected;
	
	private static ImageGraphics selector;
	
	public InventorySlotsGUI (float[] coeff) {
		this.coeff=coeff;
		
		/*
		for(int i=0; i<2; ++i) {
			sprites[0][i]=new RPGSprite("zelda/inventory.selector", 2, 2, (Positionable) this, new RegionOfInterest(i*64, 0, 64, 64));
		}
		
		Animation[] animations=RPGSprite.createAnimations(8, sprites, true);
	*/
	}
	
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width*coeff[0], coeff[1]*height));
		anchor.add(new Vector(0, height - 1.75f));
		
		/*
		if (selected) {
			animations[0].setAnchor(anchor);
			animations[0].draw(canvas);
		}
		*/
		
			
			ImageGraphics graphics = new
			ImageGraphics(ResourcePath.getSprite(SPRITENAME),
			SIZE[0], SIZE[1], new RegionOfInterest(COORDS[0], COORDS[1], COORDS[2], COORDS[3]),
			anchor.add(new Vector(0, height - 1.75f)), 1, 1000f);
			
			if (selected) {
				selector=new ImageGraphics(ResourcePath.getSprite("zelda/inventory.selector"),  SIZE[0], SIZE[1],
						new RegionOfInterest(COORDS[0], COORDS[1], COORDS[2], COORDS[3]),
						anchor.add(new Vector(0, height - 1.75f)), 1, 1001f, false);
				
				selector.draw(canvas);
			}	
				
			graphics.draw(canvas);
	}
	
	/*
	public void update(float deltaTime) {
		if (selected)
			animations[0].update(deltaTime);
	}
	*/
	
	public float[] getCoeff() {
		float[] coeff=new float[2];
		
		for(int i=0; i<coeff.length; ++i) {
			coeff[i]=this.coeff[i]-0.05f;
		}
		
		return coeff;
	}
	
	public void switchSelected() {
		selected=true;
	}
}
