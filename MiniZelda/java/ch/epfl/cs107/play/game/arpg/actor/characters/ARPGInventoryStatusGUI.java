package ch.epfl.cs107.play.game.arpg.actor.characters;



import java.util.List;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.ARPGStatusGUI;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.Digits;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.GenericComponentsGUI;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.InventorySlotsGUI;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ARPGInventoryStatusGUI extends ARPGStatusGUI {
	
	private ARPGInventory inventory;
	private ARPGInventory buyerInventory;
	//private String name;
	
	private List<ARPGItem> equip;
	private int selectedIndex;
	private boolean sale;
	
	
	//Context
	private Keyboard keyboard;
	private Window window;
	//Sounds
	private SoundAcoustics itemCancelled=new SoundAcoustics(ResourcePath.getSounds("cancelled"));
	private SoundAcoustics selector=new SoundAcoustics(ResourcePath.getSounds("selector"));
	
	
	//Constant for Digits
	private final float[] digitsHundreds=new float[] {-0.30f, 1.13f};
	private final float[] digitsDozens=new float[] {-0.35f, 1.13f};
	private float[] digitsDecimals=new float[] {-0.40f, 1.13f};
	
	
	public ARPGInventoryStatusGUI (ARPGInventory inventory, List<ARPGItem> equip, Keyboard keyboard,Window window,  boolean sale) {
		this.inventory=inventory;
		this.equip=equip;
		this.keyboard=keyboard;
		this.inventory=inventory;
		selectedIndex=0;
		this.sale=sale;
		this.window=window;
	}
	
	public void draw(Canvas canvas) {
		GenericComponentsGUI.INVENTORY_BACKGROUND.draw(canvas);
		drawInventorySlots(canvas);
		if (!sale)
		GenericComponentsGUI.INVENTORY_NAME.draw(canvas);
		
		else
			GenericComponentsGUI.SHOP_NAME.draw(canvas);
	}
	
	
	public void drawInventorySlots(Canvas canvas) {
		InventorySlotsGUI[] slots=new InventorySlotsGUI[8];
		
		for(int i=0; i<8; ++i) {
			if (i<4) 
				slots[i]=new InventorySlotsGUI(new float[] {0.44f-i*0.23f, 0.82f});

				else {
					int y=i-4;
					slots[i]=new InventorySlotsGUI(new float[] {0.44f-y*0.23f, 1.05f});
				}
				
				if (i<equip.size())
					drawItem(canvas, equip.get(i), slots[i].getCoeff());
				
				if (i==selectedIndex)
					slots[i].switchSelected();
				
				slots[i].draw(canvas);
			}
		}
	
	public void drawItem(Canvas canvas, ARPGItem item, float[] coeff) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		int[] coords=new int[] {0, 0, 16, 16};
		float[] size=new float[] {1.f, 1.f};
		String spritename;
		
		if (inventory.remainsInInventory(item))
			spritename=item.getSpriteName();
		
		else
			spritename=item.getEmptyName();
				
		
		
		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector(width*coeff[0], coeff[1]*height));
				ImageGraphics graphics = new
				ImageGraphics(ResourcePath.getSprite(spritename),
				size[0], size[1], new RegionOfInterest(coords[0], coords[1], coords[2], coords[3]),
				anchor.add(new Vector(0, height - 1.75f)), 1, 1000f);
				
		if (selectedIndex<equip.size() && sale) 
			drawDigits(canvas, equip.get(selectedIndex).getPrice());
			
		graphics.draw(canvas);
	}
	
	public void drawDigits(Canvas canvas, int money) {
		//Par simplification on suppose que l'argent est limité à un certain seuil par exemple 1000
		float[][] locations=new float[][] {digitsHundreds, digitsDozens, digitsDecimals};
		if (money>1000)
			money=999;
		
		String gold=Integer.toString(money);
		
		for(int i=0; i<gold.length(); ++i) {
			Digits.values()[Integer.valueOf(""+gold.charAt(i))].draw(canvas, locations[i]);
		}
	}
	
	public void updateGUI(float deltaTime, List<ARPGItem> equip) {
		if (inventory.getOpen()) {
			this.equip=equip;
			
			if (keyboard.get(Keyboard.RIGHT).isPressed()) {
				selectedIndex=(selectedIndex+1)%8;
				selector.shouldBeStarted();
				selector.bip(window);
			}
	
			if (keyboard.get(Keyboard.DOWN).isPressed()) {
				selectedIndex=(selectedIndex+4)%8;
				selector.shouldBeStarted();
				selector.bip(window);
			}
				
			
			if (keyboard.get(Keyboard.UP).isPressed()) {
				selectedIndex=Math.abs((selectedIndex-4))%8;
				selector.shouldBeStarted();
				selector.bip(window);
			}
			
			if (keyboard.get(Keyboard.LEFT).isPressed()) {
				if (selectedIndex==0)
					selectedIndex=7;
						
				else 
					--selectedIndex;
				
				selector.shouldBeStarted();
				selector.bip(window);
			}	
				
			if (keyboard.get(Keyboard.ENTER).isPressed()) {
				
				if (selectedIndex<equip.size()) {
					
					if (!sale) {
						if (inventory.remainsInInventory(equip.get(selectedIndex)))
							inventory.switchCurrentItem(equip.get(selectedIndex));
						
						else {
							itemCancelled.shouldBeStarted();
			    			itemCancelled.bip(window);
						}
					
					}
				}
				
				if (sale) {
					
					if (inventory.remainsInInventory(equip.get(selectedIndex))) {
						if (buyerInventory.getMoney()>=equip.get(selectedIndex).getPrice()) {
							//buyerInventory.
							inventory.transfer(equip.get(selectedIndex), buyerInventory);
							buyerInventory.getQuantity(equip.get(selectedIndex));
						}	
					}	
				}
			}
			
			if (keyboard.get(Keyboard.U).isPressed())
				inventory.switchClose();
		}	
	}
	
	public void setBuyerInventory(ARPGInventory buyerInventory) {
		this.buyerInventory=buyerInventory;
	}
}
