/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 02-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 02-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: Gravity.java                                               */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.physic;

import com.raylib.Vector2;

import com.raylib.Rectangle;

import com.interfaces.IMovable;

public class Gravity
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

    double	gravity;

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

    public Gravity()
    {
        gravity = 1;
    }

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

    public void applyGravity(IMovable object)
	{
        if (object.getIsAtRest() || object.getIsJumping() == false
            || object.getVelocity().getY() == 0)
        {
            return;
        }

		// Appliquer uniquement la gravité (mouvement vertical)
		Vector2 newPosition = useGravity(
			object.getPosition(),
			object.getVelocity(),
			object.getIsJumping()
		);
		object.setPosition(newPosition);

		// Mettre à jour la boîte de collision
		Rectangle colisionBox = object.getColisionBox();
		Vector2 colisionBoxPosition = useGravity(
			new Vector2(colisionBox.getX(), colisionBox.getY()),
			object.getVelocity(),
			object.getIsJumping()
		);
		
		colisionBox.setX(colisionBoxPosition.getX());
		colisionBox.setY(colisionBoxPosition.getY());
		object.setColisionBox(colisionBox);
	}

    public Vector2 useGravity(Vector2 position, Vector2 velocity, boolean isJumping)
    {
        if (isJumping)
        {
            position.setY((float)(position.getY() + velocity.getY() + gravity));   
        }
        else
        {
            position.setY((float)(position.getY() + velocity.getY()));
        }
        return position;
    }

    public String checkCollision(Rectangle colisionBox, Rectangle platform)
    {
        // Get the edges of the player
        float playerLeft = colisionBox.getX();
        float playerRight = colisionBox.getX() + colisionBox.getWidth();
        float playerTop = colisionBox.getY();
        float playerBottom = colisionBox.getY() + colisionBox.getHeight();
        
        float platformLeft = platform.getX();
        float platformRight = platform.getX() + platform.getWidth();
        float platformTop = platform.getY();
        float platformBottom = platform.getY() + platform.getHeight();

        // Check if there is a collision
        if (playerRight >= platformLeft && playerLeft <= platformRight &&
            playerBottom >= platformTop && playerTop <= platformBottom)
        {
            // Get the penetration distances
            float overlapLeft = playerRight - platformLeft;
            float overlapRight = platformRight - playerLeft;
            float overlapTop = playerBottom - platformTop;
            float overlapBottom = platformBottom - playerTop;

            // Get the smallest penetration
            float minOverlap = Math.min(Math.min(overlapLeft, overlapRight), 
                                      Math.min(overlapTop, overlapBottom)); 

            // Return the side with the smallest penetration
            if (minOverlap == overlapTop)
            {
                return "BOTTOM"; // The bottom of the player touches the top of the platform
            }
            if (minOverlap == overlapBottom)
            {
                return "TOP"; // The top of the player touches the bottom of the platform
            }
            if (minOverlap == overlapLeft)
            {
                return "RIGHT"; // The right side of the player touches the left side of the platform
            }
            if (minOverlap == overlapRight)
            {
                return "LEFT"; // The left side of the player touches the right side of the platform
            }
        }
        
        return "NONE"; // No collision
    }

/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/

    public double getGravity()
    {
        return gravity;
    }

/***********************************************************************************/
/***                                 SETTERS                                       */
/***********************************************************************************/

    public void setGravity(double gravity)
    {
        this.gravity = gravity;
    }
}
