package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior;
import ch.epfl.cs107.play.game.arpg.actor.background.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.background.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.background.Grass;
import ch.epfl.cs107.play.game.arpg.actor.background.Orb;
import ch.epfl.cs107.play.game.arpg.actor.background.Rock;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.characters.ARPGPlayer.Cat;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGKing;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGPNJ;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.ARPGSalesman;
import ch.epfl.cs107.play.game.arpg.actor.characters.npc.Sage;
import ch.epfl.cs107.play.game.arpg.actor.collectables.ARPGCollectableAreaEntity;
import ch.epfl.cs107.play.game.arpg.actor.collectables.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Coin;
import ch.epfl.cs107.play.game.arpg.actor.collectables.Heart;
import ch.epfl.cs107.play.game.arpg.actor.collectables.StaffWater;
import ch.epfl.cs107.play.game.arpg.actor.monsters.DarkLord;
import ch.epfl.cs107.play.game.arpg.actor.monsters.Monster;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.projectiles.MagicWaterProjectile;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor{
	
	default void interactWith(ARPGBehavior.ARPGCell cell) {};
	default void interactWith(ARPGPlayer player) {};
	default void interactWith(Grass grass) {};
	default void interactWith(Coin coin) {};
	default void interactWith(Heart heart) {};
	default void interactWith(CastleDoor door) {};
	default void interactWith(CastleKey key) {};
	default void interactWith(Monster monster) {};
	default void interactWith(Bomb bomb) {};
	default void interactWith(StaffWater staff) {};
	default void interactWith(Orb orbe) {};
	default void interactWith(ARPGPNJ pnj) {};
	default void interactWith(ARPGKing king) {};
	default void interactWith(ARPGSalesman salesman) {};
	default void interactWith(Rock rock) {};
	default void interactWith(Sage sage) {};
	default void interactWith(Cat cat) {};
	default void interactWith(DarkLord dark) {};
	default void interactWith(FireSpell fireSpell) {};
	default void interactWith(MagicWaterProjectile waterProjectile) {};
	default void interactWith(ARPGCollectableAreaEntity collectable) {};

}
