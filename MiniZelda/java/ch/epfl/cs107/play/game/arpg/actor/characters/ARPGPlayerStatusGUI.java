package ch.epfl.cs107.play.game.arpg.actor.characters;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.ARPGStatusGUI;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.Digits;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.GenericComponentsGUI;
import ch.epfl.cs107.play.game.arpg.actor.characters.gui.HeartGUI;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayerStatusGUI extends ARPGStatusGUI {

	//List<ARPGStatusGUI> status=new ArrayList<>();
	private int playerMoney=0;
	private float playerHp=0;
	private ARPGItem current=null;

	/**
	 * 
	 * @param deltaTime
	 * @param money
	 * @param hp
	 */
	public void update(float deltaTime, int money, float hp, ARPGItem current) {
		playerMoney=money;
		playerHp=hp;
		this.current=current;
	}

	//Constant for Digits
	float[] digitsHundreds=new float[] {0.40f, 1.26f};
	float[] digitsDozens=new float[] {0.35f, 1.26f};
	float[] digitsDecimals=new float[] {0.30f, 1.26f};

	//Constant for Hearts
	float[] first=new float[] {0.33f, 0.47f};
	float[] second=new float[] {0.23f, 0.47f};
	float[] third=new float[] {0.13f, 0.47f};
	float[] fourth=new float[] {0.03f, 0.47f};
	float[] fifth=new float[] {-0.07f, 0.47f};


	//Constant coefficients for height
	//Height
	public final static float BOTTOM=1.3f;
	public final static float UP=0.5f;

	//Width
	public final static float RIGHT=-0.35f;
	public final static float LEFT=0.5f;

	@Override
	/**
	 * 
	 */
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		//Equipment/Digits background

		GenericComponentsGUI.GEAR.draw(canvas);
		GenericComponentsGUI.COINS.draw(canvas);

		//Equipment 
		drawDigits(canvas, playerMoney);
		drawHearts(canvas, playerHp);
		drawCurrent(canvas, current);
	}

	/**
	 * 
	 * @param canvas
	 * @param money
	 */
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

	/**
	 * 
	 * @param canvas
	 * @param hp
	 */
	private void drawHearts(Canvas canvas, float hp) {
		int totalHearts=ARPGPlayer.MAX_HEALTH/2;
		int full=(int) hp/2;
		int half=(int) hp%2;

		int empty=totalHearts-(full+half);
		int index=0;
		float[][] locations=new float[][] {first, second, third, fourth, fifth};

		for(int i=0; i<full; ++i) {
			HeartGUI.FULL_HEART.draw(canvas, locations[i]);
			++index;
		}

		for(int i=0; i<half; ++i) {
			HeartGUI.MID_HEART.draw(canvas, locations[index+i]);
			++index;
		}

		for(int i=0; i<empty; ++i) {
			HeartGUI.EMP_HEART.draw(canvas, locations[index+i]);
		}	
	}

	public void drawCurrent(Canvas canvas, ARPGItem current) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		float[] coeff=new float[] {0.475f, 0.475f};
		int[] coords=new int[] {0, 0, 16, 16};
		float[] size=new float[] {1.f, 1.f};


		Vector anchor = canvas.getTransform().getOrigin().sub(new
				Vector(width*coeff[0], coeff[1]*height));
		ImageGraphics graphics = new
				ImageGraphics(ResourcePath.getSprite(current.getSpriteName()),
						size[0], size[1], new RegionOfInterest(coords[0], coords[1], coords[2], coords[3]),
						anchor.add(new Vector(0, height - 1.75f)), 1f, 1050f);

		graphics.draw(canvas);
	}

}


