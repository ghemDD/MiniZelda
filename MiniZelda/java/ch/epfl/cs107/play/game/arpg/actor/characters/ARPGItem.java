package ch.epfl.cs107.play.game.arpg.actor.characters;
import ch.epfl.cs107.play.game.rpg.actor.InventoryItem;

public enum ARPGItem implements InventoryItem {
	ARROW("Arrow", 0, 5, "zelda/arrow.icon", "arrowEmpty.icon"),
	SWORD("Sword", 0, 100, "zelda/sword.icon", "swordEmpty.icon"),
	STAFF("Staff", 0, 500, "zelda/staff_water.icon", "staff_waterEmpty.icon"),
	BOW("Bow", 0, 200, "zelda/bow.icon", "bowEmpty.icon"),
	BOMB("Bomb", 0, 60, "zelda/bomb", "bombEmpty.icon"),
	CASTLEKEY("Key", 0, 1, "zelda/key", null);
	
	private String name;
	private int price;
	private float weight;
	private String spritename;
	private String emptySpriteName;
	
	private ARPGItem (String name, float weight, int price, String spritename, String empty) {
		this.name=name;
		this.weight=weight;
		this.price=price;
		this.spritename=spritename;
		emptySpriteName=empty;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmptyName() {
		return emptySpriteName;
	}
	
	public int getPrice() {
		return price;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public String getSpriteName() {
		return spritename;
	}
}
