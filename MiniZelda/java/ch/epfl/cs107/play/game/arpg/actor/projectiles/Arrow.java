package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.background.Orb;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class Arrow extends Projectile {
	private ArrowHandler handler = new ArrowHandler();
	private static final float DAMAGE=1.f;
	private Sprite[] spritesArrow=new Sprite[4];
	private Sprite currentSprite;

	//Constructor
	public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int distance) {
		// TODO Auto-generated constructor stub
		super(area, orientation, position, speed, distance);

		for(int i=0; i<4; ++i) {
			spritesArrow[i]=new RPGSprite("zelda/arrow", 1.f, 1.f, this, new RegionOfInterest(i*32, 0, 32, 32));
		}

		currentSprite=spritesArrow[getOrientation().ordinal()];
	}

	//Cells
	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		// TODO Auto-generated method stub
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	//Interactor
	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
	}

	@Override
	public boolean wantsCellInteraction() {
		// TODO Auto-generated method stub
		return true;
	}

	//Interactable
	@Override
	public boolean takeCellSpace() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		// TODO Auto-generated method stub
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	//Arrow interaction handler
	private class ArrowHandler implements ARPGInteractionVisitor{

		public void interactWith(Monster monster) {
			monster.damageType(Monster.Vulnerabilities.PHYS, DAMAGE);
			setCollision(true);
		}

		public void interactWith(Grass grass) {
			grass.slice();
			//System.out.println("Moussa");
		}

		public void interactWith(Bomb bomb) {
			bomb.setExplosion();
			setCollision(true);
		}

		public void interactWith(Orb orbe) {
			orbe.setState(true);
		}
	}

	//Graphics
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		currentSprite.draw(canvas);
	}

	//Actor
	public void update(float deltaTime) {
		currentSprite=spritesArrow[getOrientation().ordinal()];

		move(getSpeed());

		super.update(deltaTime);
	}

}
