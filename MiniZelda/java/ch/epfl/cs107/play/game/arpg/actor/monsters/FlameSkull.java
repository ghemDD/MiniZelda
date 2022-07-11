package ch.epfl.cs107.play.game.arpg.actor.monsters;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.FlyableEntity;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class FlameSkull extends Monster implements FlyableEntity {
	private FlameSkullHandler handler=new FlameSkullHandler();
	private final static int MIN_LIFE_TIME=60;
	private final static int MAX_LIFE_TIME=350;
	private float lifeTime;

	public FlameSkull(Area area, Orientation orientation, DiscreteCoordinates coor, float health) {
		// TODO Auto-generated constructor stub
		super(area, orientation, coor, health, maxHealth, new Vulnerabilities[]{Vulnerabilities.MAGIC,Vulnerabilities.PHYS});

		//Modification random
		int min=MIN_LIFE_TIME;
		int max=MAX_LIFE_TIME;
		lifeTime=min+RandomGenerator.getInstance().nextInt(max-min);

		Sprite[][] spritesF = RPGSprite.extractSprites("zelda/flameSkull", 3, 2, 2, this, 32, 32, new Orientation[] {Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT});
		
		
		Animation[] animationsF=RPGSprite.createAnimations(ANIMATION_DURATION*2, spritesF);
		setAnimations(animationsF);

	}

	@Override
	public void interactWith(Interactable other) {
		// TODO Auto-generated method stub
		other.acceptInteraction(handler);
	}

	private class FlameSkullHandler implements ARPGInteractionVisitor {

		public void interactWith(ARPGPlayer player) {
			player.damage(1.f);
		}

		public void interactWith(Monster monster) {
			monster.damageType(Monster.Vulnerabilities.FIRE, 1.f);
		}

		public void interactWith(Grass grass) {
			grass.slice();
		}

		public void interactWith(Bomb bomb) {
			bomb.setExplosion();
		}
	}


	public void update(float deltaTime) {
		lifeTime-=deltaTime;

		if (lifeTime<=0) 
			setDead();

		randomMove(ANIMATION_DURATION*2);	

		super.update(deltaTime);
	}

	public void draw(Canvas canvas) {
		super.draw(canvas);	
	}

	@Override
	protected void postMortem() {
		// TODO Auto-generated method stub
		//Does nothing
	}
}
