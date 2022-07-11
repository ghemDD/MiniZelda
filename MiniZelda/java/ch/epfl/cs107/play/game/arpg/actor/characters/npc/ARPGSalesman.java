package ch.epfl.cs107.play.game.arpg.actor.characters.npc;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGInventory;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGItem;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGSalesman extends ARPGPNJ{
	private ARPGInventory inventory=new ARPGInventory(20, 10, getOwnerArea().getKeyboard(), true, getOwnerArea().getWindow());
	private Sprite sprite=new RPGSprite("zelda/character", 1, 2, this, new RegionOfInterest(0, 64, 16, 32));

	public ARPGSalesman(Area area, Orientation orientation, DiscreteCoordinates position) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position);
		
		inventory=new ARPGInventory(20, 10, getOwnerArea().getKeyboard(), true, getOwnerArea().getWindow());
		inventory.addItem(ARPGItem.BOMB, 2);
		inventory.addItem(ARPGItem.SWORD, 1);
		inventory.addItem(ARPGItem.BOW, 1);
		inventory.addItem(ARPGItem.ARROW, 10);
		inventory.addItem(ARPGItem.STAFF, 0);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		List<DiscreteCoordinates> cells=new ArrayList<>();
		cells.add(getCurrentMainCellCoordinates());
		cells.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
		
		return cells;
	}
	
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	@Override
	public boolean isViewInteractable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}
	
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
		inventory.draw(canvas);
	}
	
	public void update(float deltaTime) {
		inventory.update(deltaTime);
	}
	
	public void setTransaction(ARPGInventory buyerInventory) {
		inventory.switchOpen();
		inventory.setBuyerInventory(buyerInventory);
	}
	
	public void finishTransaction() {
		inventory.switchClose();
	}
	
}