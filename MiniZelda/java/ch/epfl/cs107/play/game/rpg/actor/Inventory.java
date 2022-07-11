package ch.epfl.cs107.play.game.rpg.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Inventory {
private static float MAX_WEIGHT; 
protected Map<InventoryItem, Integer> items=new HashMap<>();

	protected Inventory (float weight) {
		MAX_WEIGHT=weight;
	}

	protected boolean addItem(InventoryItem item, int quantity) {
		if (items.containsKey(item)) {
			if ((items.get(item)+quantity)*item.getWeight()<=MAX_WEIGHT) {
				items.put(item, items.get(item)+quantity);
				return true;
			}
		}
		
		else if (quantity*item.getWeight()<=MAX_WEIGHT) {
			items.put(item, quantity);
			return true;
		}
		
		return false;
	}
	
	protected boolean removeItem(InventoryItem item, int quantity) {
		if (quantity<0)
			return false;
		
		else if (quantity<=items.get(item)) {
			items.put(item,items.get(item)-quantity);
			return true;
		}
		return false;	
	}
	
	public int getQuantity(InventoryItem item) {
		return items.get(item);
	}
}
