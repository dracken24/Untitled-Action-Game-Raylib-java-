/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 12-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 12-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: InitEnemy.java                                             */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.enemy;

import com.enums.SpriteMovement;
import com.objects.SpriteSheet;

import com.raylib.Vector2;

public class InitEnemy
{
/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/
    public InitEnemy(Enemy enemy, Vector2 enemyPos, Vector2 enemySize)
    {
		initEnemy01(enemy, enemyPos, enemySize);
    }

/***********************************************************************************/
/***                                 FUNCTIONS                                   ***/
/***********************************************************************************/

    private void initEnemy01(Enemy enemy, Vector2 enemySize, Vector2 enemyPos)
    {
		int scale = 2;

		// Idle
        enemy.movement.setIdle(new SpriteSheet(
			"assets/Players/ichigo/ichigo_idle/ichigo_All_idle_01.png",
			4,
			enemySize,
			18,
			scale,
			enemyPos
		));

		// Run
		enemy.movement.setRun(new SpriteSheet(
			"assets/Players/ichigo/ichigo_move/ichigo_all_move_01.png",
			8,
			enemySize,
			5,
			scale,
			enemyPos
		));


		// Jump
		enemy.movement.setJump(new SpriteSheet(
			"assets/Players/ichigo/ichigo_jump/ichigo_all_jump_01.png",
			4,
			enemySize,
			18,
			scale,
			enemyPos
		));

		// Fall
		enemy.movement.setFall(new SpriteSheet(
			"assets/Players/ichigo/ichigo_fall/ichigo_all_fall_01.png",
			4,
			enemySize,
			18,
			2,
			enemyPos
		));

		// Attack 1
		enemy.movement.setAttack01(new SpriteSheet(
			"assets/Players/ichigo/ichigo_attack_1/ichigo_all_attack_01.png",
			7,
			enemySize,
			4,
			2,
			enemyPos
		));

		enemy.movement.setMovement(SpriteMovement.IDLE);
    }
}
