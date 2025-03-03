/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 28-2nd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 28-2nd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: InitPlayer.java                                            */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.player;

import com.objects.SpriteSheet;
import com.raylib.Vector2;
import com.enums.SpriteMovement;
import com.enums.PlayerType;

public class InitPlayer
{
	public InitPlayer(PlayerType playerType, Player player,
		Vector2 playerPos, Vector2 playerSize)
    {
		switch (playerType)
		{
			case ICHIGO:
                initIchigo(player, playerSize, playerPos);
				break;
			case SHINOBI:
                initShinobi();
				break;
			case FIGHTER:
                initFighter();
				break;
			case SAMURAI:
                initSamurai(player, playerPos, playerSize);
				break;
		}
    }

    private void initIchigo(Player player, Vector2 playerSize, Vector2 playerPos)
    {
		int scale = 2;

		// Idle
        player.movement.setIdle(new SpriteSheet(
			"assets/Players/ichigo/ichigo_idle/ichigo_All_idle_01.png",
			4,
			playerSize,
			18,
			scale,
			playerPos
		));

		// Run
		player.movement.setRun(new SpriteSheet(
			"assets/Players/ichigo/ichigo_move/ichigo_all_move_01.png",
			8,
			playerSize,
			5,
			scale,
			playerPos
		));


		// Jump
		player.movement.setJump(new SpriteSheet(
			"assets/Players/ichigo/ichigo_jump/ichigo_all_jump_01.png",
			4,
			playerSize,
			18,
			scale,
			playerPos
		));

		// Fall
		player.movement.setFall(new SpriteSheet(
			"assets/Players/ichigo/ichigo_fall/ichigo_all_fall_01.png",
			4,
			playerSize,
			18,
			2,
			playerPos
		));

		// Attack 1
		player.movement.setAttack01(new SpriteSheet(
			"assets/Players/ichigo/ichigo_attack_1/ichigo_all_attack_01.png",
			7,
			playerSize,
			4,
			2,
			playerPos
		));

		player.movement.setMovement(SpriteMovement.IDLE);
    }

    private void initShinobi()
    {

    }

    private void initFighter()
    {

    }

    private void initSamurai(Player player, Vector2 playerPos, Vector2 playerSize)
    {
		// Idle
        player.movement.setIdle(new SpriteSheet(
			"assets/shinobi/Samurai/Idle.png",
			6,
			playerSize, 12
		));
		player.movement.getIdle().setScale(3f);
		player.movement.getIdle().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		// Run
		player.movement.setRun(new SpriteSheet(
			"assets/shinobi/Samurai/Walk.png",
			8,
			playerSize, 6
		));
		player.movement.getRun().setScale(3f);
		player.movement.getRun().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		// Jump
		player.movement.setJump(new SpriteSheet(
			"assets/shinobi/Samurai/Jump.png",
			12,
			playerSize, 3
		));
		player.movement.getJump().setScale(3f);
		player.movement.getJump().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		// Attack 1
		player.movement.setAttack01(new SpriteSheet(
			"assets/shinobi/Samurai/Attack_1.png",
			6,
			playerSize, 2
		));
		player.movement.getAttack01().setScale(3f);
		player.movement.getAttack01().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		// Attack 2
		player.movement.setAttack02(new SpriteSheet(
			"assets/shinobi/Samurai/Attack_2.png",
			4,
			playerSize, 3
		));
		player.movement.getAttack02().setScale(3f);
		player.movement.getAttack02().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		// Shield
		player.movement.setShild(new SpriteSheet(
			"assets/shinobi/Samurai/Shield.png",
			2,
			playerSize, 60
		));
		player.movement.getShild().setScale(3f);
		player.movement.getShild().setPosition((int)playerPos.getX(), (int)playerPos.getY());

		player.movement.setMovement(SpriteMovement.IDLE);
    }
}
