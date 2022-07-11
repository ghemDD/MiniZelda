package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.background.AnimatedBackground;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.FireSpell;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior{
	
	public enum ARPGCellType{
		//https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
		NULL(0, false, false, false),
		WALL(-16777216, false, false, false),
		IMPASSABLE(-8750470, false, true, false),
		INTERACT(-256, true, true, true),
		DOOR(-195580, true, true, true),
		WALKABLE(-1, true, true, true),;
		
		
		
		
		final int type;
		final boolean isWalkable;
		final boolean isFlyable;
		final boolean isFirePropagatable;

		ARPGCellType(int type, boolean isWalkable, boolean isFlyable, boolean isFirePropagatable){
			this.type = type;
			this.isWalkable = isWalkable;
			this.isFlyable=isFlyable;
			this.isFirePropagatable=isFirePropagatable;
		}

		public static ARPGCellType toType(int type){
			for(ARPGCellType ict : ARPGCellType.values()){
				if(ict.type == type)
					return ict;
			}
			// When you add a new color, you can print the int value here before assign it to a type
			System.out.println(type);
			return NULL;
		}
	}

	/**
	 * Default Tuto2Behavior Constructor
	 * @param window (Window), not null
	 * @param name (String): Name of the Behavior, not null
	 */
	public ARPGBehavior(Window window, String name){
		super(window, name);
		int height = getHeight();
		int width = getWidth();
		for(int y = 0; y < height; y++) {
			for (int x = 0; x < width ; x++) {
				ARPGCellType color = ARPGCellType.toType(getRGB(height-1-y, x));
				setCell(x,y, new ARPGCell(x,y,color));
			}
		}
	}
	public boolean isDoor(DiscreteCoordinates coord) {
		return (((ARPGCell)getCell(coord.x, coord.y)).isDoor());
	}
	/**
	 * Cell adapted to the Tuto2 game
	 */
	public class ARPGCell extends AreaBehavior.Cell {
		/// Type of the cell following the enum
		private final ARPGCellType type;

		/**
		 * Default Tuto2Cell Constructor
		 * @param x (int): x coordinate of the cell
		 * @param y (int): y coordinate of the cell
		 * @param type (EnigmeCellType), not null
		 */
		public  ARPGCell(int x, int y, ARPGCellType type){
			super(x, y);
			this.type = type;
		}
		public boolean isDoor() {
			return type == ARPGCellType.DOOR;
		}
		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		@Override
		protected boolean canEnter(Interactable entity) {
			if (entity instanceof AnimatedBackground) {
				return true;}

			if (entity instanceof FlyableEntity) { 
				return type.isFlyable;}

			
			if(entity instanceof FireSpell) {
				return type.isFirePropagatable;
			}
			else if(this.hasNonTraversableContent()) {
				return false;}

			return type.isWalkable;
		}


		@Override
		public boolean isCellInteractable() {
			return true;
		}

		@Override
		public boolean isViewInteractable() {
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
		}

	}
}

