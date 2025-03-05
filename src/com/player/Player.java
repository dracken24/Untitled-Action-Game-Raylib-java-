/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 28-2nd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 28-2nd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: Player.java                                                */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.player;

import com.enums.SpriteMovement;
import com.raylib.Vector2;
import static com.raylib.Raylib.drawRectangleRec;
import static com.raylib.Raylib.DARKGRAY;
import static com.raylib.Raylib.WHITE;
import static com.raylib.Raylib.DARKPURPLE;
import static com.raylib.Raylib.KeyboardKey.KEY_B;
import static com.raylib.Raylib.KeyboardKey.KEY_R;
import static com.raylib.Raylib.isKeyPressed;

import com.raylib.Rectangle;

import com.interfaces.IMovable;

public class Player implements IMovable
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	PlayerMovement	movement;
	Vector2		position;
	Vector2		size;
	Rectangle	colisionBox;
	int			scale;
	Vector2		offset;
	Vector2		collisionBoxOffset;

	Vector2		initialPosition;
	Rectangle	initialColisionBox;

	private float bounceForce;
/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

	public Player(Vector2 position, Vector2 size, Rectangle colisionBox, int scale, Vector2 offset)
	{
		movement = new PlayerMovement();
		this.position = position;
		this.size = size;
		this.scale = scale;
		this.colisionBox = colisionBox;
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
		}
		
		movement.applyMovement(position, colisionBox, movement.getVelocity());
		movement.update(position, offset);

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
			colisionBox.getX() + offset.getX(), 
			colisionBox.getY() + offset.getY(), 
			colisionBox.getWidth() * scale,
			colisionBox.getHeight() * scale
		);
		drawRectangleRec(colBox, DARKPURPLE);
	}

	void drawSize()
	{
		drawRectangleRec(
			new Rectangle(position.getX() - (size.getX() / 2 * scale) + offset.getX(),
			position.getY() - (size.getY() / 2 * scale) + offset.getY(),
			size.getX() * scale,
			size.getY() * scale), WHITE
		);
	}

/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/

	public Vector2 getPosition()
	{
		return position;
	}

	public Vector2 getVelocity()
	{
		return movement.getVelocity();
	}

	public Rectangle getColisionBox()
	{
		return colisionBox;
	}

	public Rectangle getColisionBoxPlusOffset()
	{
		return new Rectangle(
			colisionBox.getX() + offset.getX(), 
			colisionBox.getY() + offset.getY(), 
			colisionBox.getWidth() * scale,
			colisionBox.getHeight() * scale
		);
	}

	public Vector2 getOffset()
	{
		return offset;
	}

	public boolean getIsJumping()
	{
		return movement.getIsJumping();
	}

	public boolean getIsAtRest()
	{
		return movement.getIsAtRest();
	}

	public SpriteMovement getActionInProgress()
	{
		return movement.getActionInProgress();
	}

	public float getBounceForce()
	{
		return bounceForce;
	}

	public Vector2 getCollisionBoxOffset()
	{
		return collisionBoxOffset;
	}

/***********************************************************************************/
/***                                 SETTERS                                       */
/***********************************************************************************/

	public void setMovementSide(boolean rightSide)
	{
		movement.setRightSide(rightSide);
	}

	public void setMovement(SpriteMovement movement)
	{
		this.movement.setMovement(movement);
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	public void setColisionBox(Rectangle colisionBox)
	{
		this.colisionBox = colisionBox;
	}

	public void setOffset(Vector2 offset)
	{
		this.offset = offset;
	}

	public void setIsJumping(boolean isJumping)
	{
		movement.setIsJumping(isJumping);
	}

	public void setActionCounter(int actionCounter)
	{
		movement.setActionCounter(actionCounter);
	}

	public void setIsWallCollide(boolean isWallCollide)
	{
		movement.setIsWallCollide(isWallCollide);
	}

	public void setBounceForce(float bounceForce)
	{
		this.bounceForce = bounceForce;
	}
}