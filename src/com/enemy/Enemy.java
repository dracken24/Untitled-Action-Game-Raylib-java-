/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 12-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 12-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: Enemy.java                                                 */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.enemy;

import com.enums.SpriteMovement;
import com.raylib.Vector2;
import static com.raylib.Raylib.drawRectangleRec;
import static com.raylib.Raylib.WHITE;
import static com.raylib.Raylib.KeyboardKey.KEY_B;
import static com.raylib.Raylib.KeyboardKey.KEY_R;
import static com.raylib.Raylib.isKeyPressed;

import com.raylib.Color;

import com.raylib.Rectangle;

import com.interfaces.IMovable;

public class Enemy implements IMovable
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	public EnemyMovement	movement;
	Vector2		position;
	Vector2		size;
	Rectangle	colisionBox;
	Rectangle	weaponColisionBox;
	int			scale;
	Vector2		offset;
	Vector2		collisionBoxOffset;

	Vector2		initialPosition;
	Rectangle	initialColisionBox;

	float		bounceForce;

	public static Color RED_SHADOW = new Color((byte)230, (byte)41, (byte)55, (byte)105);
	public static Color DARKPURPLE_SHADOW = new Color((byte)122, (byte)141, (byte)136, (byte)105);

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

	public Enemy(Vector2 position, Vector2 size, Rectangle colisionBox,
		Rectangle weaponColisionBox, int scale, Vector2 offset)
	{
		movement = new EnemyMovement();
		this.position = position;
		this.size = size;
		this.scale = scale;
		this.colisionBox = colisionBox;
		this.weaponColisionBox = weaponColisionBox;
		this.offset = offset;
		initialPosition = new Vector2(position.getX(), position.getY());
		initialColisionBox = new Rectangle(colisionBox.getX(), colisionBox.getY(), colisionBox.getWidth(), colisionBox.getHeight());
		bounceForce = 0.0f;
		collisionBoxOffset = new Vector2(colisionBox.getX(), colisionBox.getY());
	}

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

	boolean isDebug = true;
	public void update()
	{
		// drawSize();
		if (isKeyPressed(KEY_B))
		{
			isDebug = !isDebug;
		}
		if (isDebug)
		{
			drawColisionBox();
			// System.out.println("Weapon coll box x: " + this.weaponColisionBox.getX() + "  y: " + this.weaponColisionBox.getY() );
		}

		// System.out.println("Enemy Position: " + position.getX() + " y: " + position.getY());
		// System.out.println("Enemy CollBox Position: " + colisionBox.getX() + " y: " + colisionBox.getY());	
		// adjustWeaponCollBox();

		movement.applyMovement(position, colisionBox, movement.getVelocity());
		movement.update(position, offset);
		
		if (movement.getWeaponHitCounter() > 0)
		{
			// drawWeaponCollisionBox();
		}

		// TODO: For debug
		// Reset position and colision box to initial position
		if (isKeyPressed(KEY_R))
		{
			position = initialPosition;
			colisionBox = initialColisionBox;
			initialPosition = new Vector2(position.getX(), position.getY());
			initialColisionBox = new Rectangle(colisionBox.getX(), colisionBox.getY(), colisionBox.getWidth(), colisionBox.getHeight());
		}
	}

	void drawColisionBox()
	{
		Rectangle colBox = new Rectangle(
			this.colisionBox.getX() + offset.getX(), 
			this.colisionBox.getY() + offset.getY(), 
			this.colisionBox.getWidth() * scale,
			this.colisionBox.getHeight() * scale
		);
		
		drawRectangleRec(colBox, DARKPURPLE_SHADOW);
	}

	void drawWeaponCollisionBox()
	{
		Rectangle colBox = new Rectangle(
			this.weaponColisionBox.getX() + offset.getX(), 
			this.weaponColisionBox.getY() + offset.getY(), 
			this.weaponColisionBox.getWidth() * scale,
			this.weaponColisionBox.getHeight() * scale
		);

		drawRectangleRec(colBox, RED_SHADOW);
	}

/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/
	
	public Vector2 getPosition()
	{
		return this.position;
	}

	public Rectangle getColisionBox()
	{
		return this.colisionBox;
	}

	public Vector2 getVelocity()
	{
		return this.movement.getVelocity();
	}

	public boolean getIsJumping()
	{
		return this.movement.getIsJumping();
	}

	public boolean getIsAtRest()
	{
		return this.movement.getIsAtRest();
	}

/***********************************************************************************/
/***                                 SETTERS                                       */
/***********************************************************************************/

	public void setColisionBox(Rectangle colisionBox)
	{
		this.colisionBox = colisionBox;
	}

	public void setIsJumping(boolean isJumping)
	{
		this.movement.setIsJumping(isJumping);
	}

	public void setIsAtRest(boolean isAtRest)
	{
		this.movement.setIsAtRest(isAtRest);
	}

	public void setIsWallCollide(boolean isWallCollide)
	{
		this.movement.setIsWallCollide(isWallCollide);
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	public void setBounceForce(float bounceForce)
	{
		this.bounceForce = bounceForce;
	}
}
