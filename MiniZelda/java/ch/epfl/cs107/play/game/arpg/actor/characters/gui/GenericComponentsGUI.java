package ch.epfl.cs107.play.game.arpg.actor.characters.gui;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayerStatusGUI;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public enum GenericComponentsGUI {
	COINS("zelda/coinsDisplay", new int[]{0, 0, 64, 32}, new float[]{ARPGPlayerStatusGUI.LEFT, ARPGPlayerStatusGUI.BOTTOM}, new float[]{3.0f, 1.5f}, 1000f),
	GEAR("zelda/gearDisplay", new int[]{0, 0, 32, 32}, new float []{ARPGPlayerStatusGUI.LEFT, ARPGPlayerStatusGUI.UP}, new float[]{1.5f, 1.5f}, 1050f),
	INVENTORY_BACKGROUND("zelda/inventory.background", new int[] {0, 0, 240, 240}, new float[] {ARPGPlayerStatusGUI.LEFT, 1.175f}, new float[] {10.f, 8.f}, 1000f),
	INVENTORY_NAME("inventory.name", new int[] {0, 0, 196, 32}, new float[] {ARPGPlayerStatusGUI.LEFT-0.3f, 0.5f}, new float[] {3.5f, 0.875f}, 1000f),
	SHOP_NAME("shop.name", new int[] {0, 0, 196, 32}, new float[] {ARPGPlayerStatusGUI.LEFT-0.3f, 0.5075f}, new float[] {3.5f, 1.00375f}, 1000f);
	
	private GenericComponentsGUI (String spritename, int[] coords, float[] coeff, float[] size, float depth) {
		this.spritename=spritename;
		this.coords=coords;
		this.coeff=coeff;
		this.size=size;
	}
	
	private String spritename;
	private int[] coords;
	private float[] coeff;
	private float[] size;
	private ImageGraphics graphics; 
	float depth;
	
	
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		
		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector(width*coeff[0], coeff[1]*height));
				graphics = new
				ImageGraphics(ResourcePath.getSprite(spritename),
				size[0], size[1], new RegionOfInterest(coords[0], coords[1], coords[2], coords[3]),
				anchor.add(new Vector(0, height - 1.75f)), 1, 1000f);
		
		graphics.draw(canvas);
	}
	
	public ImageGraphics getImageGraphics() {
		return graphics;
	}
}

