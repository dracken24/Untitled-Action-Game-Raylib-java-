/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 03-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 03-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: MovableObject.java                                         */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.objects;

import com.raylib.Vector2;
import com.raylib.Rectangle;
import com.raylib.Color;

import static com.raylib.Raylib.DARKPURPLE;
import static com.raylib.Raylib.drawRectangleRec;
import static com.raylib.Raylib.drawRectangleV;

import com.interfaces.IMovable;

public class MovableObject implements IMovable
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	Vector2 position;
	Vector2 size;
	Rectangle colisionBox;
	int scale;
	Vector2 offset;
    Color color;

	Vector2 initialPosition;
	Rectangle initialColisionBox;

	Vector2 velocity;
    Vector2 lastVelocity;
	Vector2 lastPosition;

	boolean isJumping;
	boolean isWallCollide;

	float fallSpeedMax;

	private float bounceForce = 0.7f; // Force du rebond (0 = pas de rebond, 1 = rebond parfait)

	private boolean isAtRest = false;  // Nouvel état pour indiquer si l'objet est au repos

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

	public MovableObject(Vector2 position, Vector2 size, Rectangle colisionBox, int scale, Vector2 offset, Color color)
	{
		this.position = position;
		this.size = size;
		this.colisionBox = colisionBox;
		this.scale = scale;
		this.offset = offset;
		this.color = color;

		velocity = new Vector2(0, 0);
		lastPosition = new Vector2(0, 0);
		isJumping = false;
		isWallCollide = false;
		fallSpeedMax = 10;
		lastVelocity = new Vector2(0, 0);

		initialPosition = new Vector2(position.getX(), position.getY());
		initialColisionBox = new Rectangle(colisionBox.getX(), colisionBox.getY(), colisionBox.getWidth(), colisionBox.getHeight());
	}

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

	public void update()
	{
        // System.out.println("MovableObject isJumping: " + isJumping);
        // System.out.println("MovableObject velocity: " + velocity.getY());
        adjustVelocity();
		
		if (position != new Vector2(this.colisionBox.getX(), this.colisionBox.getY()))
		{
			this.colisionBox.setX(this.position.getX());
			this.colisionBox.setY(this.position.getY());
		}
		// drawCollisionBox();
        draw();

        lastPosition = position;
        lastVelocity = velocity;
	}

	public void draw()
	{
		drawRectangleV(position, size, color);
	}

	public void drawCollisionBox()
	{
		drawRectangleRec(this.colisionBox, DARKPURPLE);
	}

    void adjustVelocity()
    {
        if (isJumping)
        {
            // Add a constant acceleration (gravity)
            velocity.setY(velocity.getY() + 0.5f);
            
            // Limit the maximum fall speed
            if (velocity.getY() > fallSpeedMax)
            {
                velocity.setY(fallSpeedMax);
            }
			// Add a little horizontal friction
			velocity.setX(velocity.getX() * 0.98f);
        }
		else
		{
			velocity.setX(velocity.getX() * 0.92f);
		}
        
        // Stop the movement if the speed is very low
        if (Math.abs(velocity.getX()) < 0.01f)
        {
            velocity.setX(0);
        }
        
        // If the object is not jumping and has a very low velocity
        if (!isJumping && Math.abs(velocity.getY()) < 0.01f)
        {
            velocity.setY(0);
        }

        float velocityDiff = Math.abs(lastVelocity.getY() - velocity.getY());
        if (velocityDiff < 0.01f)
        {
            isAtRest = true;
            isJumping = false;
        }
        else
        {
            isAtRest = false;
            isJumping = true;
        }
    }

    public Vector2 applyMovement(Vector2 position, Rectangle colisionBox, Vector2 velocity)
    {
        position.setX(position.getX() + velocity.getX());
        colisionBox.setX(colisionBox.getX() + velocity.getX());

        // Slow down gradually the player when he is in the air
        if (isJumping || isAtRest == false)
        {
            velocity.setX(velocity.getX() * 0.99f);
        }

        return position;
    }
    
/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/

	public Vector2 getPosition()
	{
		return position;
	}

	public Vector2 getSize()
	{
		return size;
	}

	public Rectangle getColisionBox()
	{
		return colisionBox;
	}
    
	public int getScale()
	{
		return scale;
	}   

	public Vector2 getOffset()
	{
		return offset;
	}

	public Color getColor()
	{
		return color;
	}

	public Vector2 getCollBoxPosition()
	{
		return new Vector2(colisionBox.getX(), colisionBox.getY());
	}

	public Vector2 getVelocity()
	{
		return velocity;
	}

	public boolean getIsJumping()
	{
		return isJumping;
	}

	public float getBounceForce()
    {
		return bounceForce;
	}

	public boolean getIsAtRest()
    {
        return isAtRest;
    }

    public Vector2 getLastVelocity()
    {
        return lastVelocity;
    }

/***********************************************************************************/
/***                                 SETTERS                                       */
/***********************************************************************************/

	public void setPosition(Vector2 position)
	{
		this.position = position;
        this.colisionBox.setX(position.getX());
        this.colisionBox.setY(position.getY());
	}

	public void setSize(Vector2 size)
	{
		this.size = size;
	}
    
	public void setColisionBox(Rectangle colisionBox)
	{
		this.colisionBox = colisionBox;
	}

	public void setColisionBoxPosition(Vector2 colisionBoxPos)
	{
		this.position = colisionBoxPos;
	}

	public void setScale(int scale)
	{
		this.scale = scale;
	}

	public void setOffset(Vector2 offset)
	{
		this.offset = offset;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity;
	}
	
    public void setIsJumping(boolean isJumping)
    {
        this.isJumping = isJumping;
    }

    public void setIsWallCollide(boolean isWallCollide)
    {
        this.isWallCollide = isWallCollide;
    }

    public void setBounceForce(float bounceForce)
    {
        this.bounceForce = bounceForce;
    }

    public void setIsAtRest(boolean isAtRest)
	{
        this.isAtRest = isAtRest;
    }
}
