package ch.epfl.cs107.play.game.rpg.actor;

public interface InventoryItem {
	
	public default String getName() {return "";}
	public default float getWeight() {return 0;}
	public default int getPrice() {return 0;}
	
}
