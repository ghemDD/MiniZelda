package ch.epfl.cs107.play.game.arpg.actor.projectiles;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster.Vulnerabilities;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;

public class MagicWaterProjectile extends Projectile {
	private MagicWaterHandler handler=new MagicWaterHandler();
	private static final float DAMAGE=1.f;
	Sprite[][] sprites=new Sprite[4][4];
	Animation[] animations=new Animation[4];

	public MagicWaterProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed,
			int distance) {
		super(area, orientation, position, speed, distance);

		for(int i=0; i<4; ++i) {
			sprites[0][i]=new RPGSprite("zelda/magicWaterProjectile", 1, 1, this, new RegionOfInterest(i*32, 0, 32, 32));
		}

		animations=RPGSprite.createAnimations(getSpeed(), sprites);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
	}

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

	private class MagicWaterHandler implements ARPGInteractionVisitor {

		public void interactWith(Monster monster) {
			monster.damageType(Vulnerabilities.MAGIC, DAMAGE);
		}

		//Flammes magiques
		public void interactWith(FireSpell firespell) {
			getOwnerArea().unregisterActor(firespell);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		animations[0].draw(canvas);
	}

	public void update(float deltaTime) {
		animations[0].update(deltaTime);

		move(getSpeed());

		super.update(deltaTime);
	}

}
