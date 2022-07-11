package ch.epfl.cs107.play.game.arpg.actor.characters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import ch.epfl.cs107.play.game.rpg.actor.Inventory;
import ch.epfl.cs107.play.game.rpg.actor.InventoryItem;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class ARPGInventory extends Inventory{
	private int money;
	private int fortune=money;
	//private ARPGItem current;
	private List<ARPGItem> equip=new ArrayList<ARPGItem>(Arrays.asList(ARPGItem.SWORD, ARPGItem.BOW, ARPGItem.ARROW, ARPGItem.BOMB, ARPGItem.STAFF));
	private ARPGItem currentItem;
	private int index;
	private ARPGInventoryStatusGUI status;
	private boolean isOpen;
	
	
	public ARPGInventory (int money, float weight, Keyboard keyboard, boolean sale, Window window) {
		super(weight);
		this.money=money;
		fortuneCalculator();	
		index=0;
		status=new ARPGInventoryStatusGUI(this, equip, keyboard, window, sale);
		currentItem=ARPGItem.SWORD;
	}
	
	public boolean addItem(InventoryItem item, int quantity) {
		boolean done= super.addItem(item, quantity);
		
		if (done)
			fortuneCalculator();
		
		return done;
	}
	
	public boolean removeItem(InventoryItem item, int quantity) {
		boolean done= super.removeItem(item, quantity);
		
		if (done)
			fortuneCalculator();
		
		return done;
	}
	
	/**
	 * Calculate the attribute fortune it is necessary : Constructor, addItem, removeItem
	 */
	private void fortuneCalculator() {
		fortune=money;
		for(Entry<InventoryItem, Integer> object : items.entrySet()) {
			fortune+=object.getKey().getPrice()*object.getValue();
		}
	}
	
	public void addMoney(int money) {
		this.money+=money;
	}
	
	public void removeMoney(int price) {
		money-=price;
	}
	
	public int getMoney() {
		return money;
	}
	
	public int getFortune() {
		return fortune;
	}
	
	
	protected boolean isInInventory(InventoryItem item) {
		return (items.containsKey(item));
	} 
	
	protected void getQuantity(ARPGItem item) {
		if (remainsInInventory(item))
			System.out.println(super.getQuantity(item));
		
		else
			System.out.println("0");
	}
	
	protected boolean remainsInInventory(InventoryItem item) {
		if (isInInventory(item) && items.get(item)>0) {
			return true;
		}
			
		return false;
	}
	
	protected ARPGItem switchCurrent(ARPGItem current) {
		do  {
			if (index==equip.size()-1) 
				index=0;
			
			else 
				++index;
			
			current=equip.get(index);
			
		} while (!remainsInInventory((equip.get(index))));
		
		return current;
	}
	
	protected ARPGItem switchCurrentInInventory() {
		return currentItem;
	}
	
	protected void switchCurrentItem(ARPGItem item) {
		currentItem=item;
	}
	
	public interface Holder {	
		public boolean possess(InventoryItem item);
		public boolean remains(InventoryItem item);
	}
	
	/**
	 * 
	 * @param canvas : canvas in which the Inventory is drawn
	 */
	public void draw(Canvas canvas) {
		if (isOpen)
			status.draw(canvas);
	}
	
	public void update(float deltaTime) {
		status.updateGUI(deltaTime, equip);
	}
	
	/**
	 * @return value of the state of the inventory 
	 */
	
	public boolean getOpen() {	
		return isOpen;
	}
	
	
	/**
	 * Methods that changes the state of the inventory : open or not
	 */
	public void switchState() {
		isOpen=!isOpen;
	}
	
	public void switchClose() {
		isOpen=false;
	}
	
	public void switchOpen() {
		// TODO Auto-generated method stub
		isOpen=true;
	}
	
	/**
	 *
	 * @param item : ARPGItem to transfer from this inventory to the other inventory
	 * @param otherInventory : Inventory in which the item is transferred
	 */
	
	public void transfer(ARPGItem item, ARPGInventory otherInventory) {
		if (otherInventory.addItem(item, 1)) {
			this.removeItem(item, 1);
			otherInventory.removeMoney(item.getPrice());
			System.out.println("Sold");
		}
		
		else
			System.out.println("False");
	}
	
	/**
	 * @param buyerInventory : shares the information of the Inventory to the status in order to enable the transaction
	 */
	
	public void setBuyerInventory(ARPGInventory buyerInventory) {
		status.setBuyerInventory(buyerInventory);
	}

	
}
