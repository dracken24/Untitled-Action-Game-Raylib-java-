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

import static com.raylib.Raylib.checkCollisionBoxes;
import static com.raylib.Raylib.checkCollisionRecs;

import com.raylib.Rectangle;
public class Gravity
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

    double	gravity;

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
    public Gravity()
    {
        gravity = 1;
    }
    
/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

    public Vector2 applyGravity(Vector2 position, Vector2 velocity, boolean isJumping)
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

    public String checkGroundCollision(Rectangle colisionBox, Rectangle platform)
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
            if (minOverlap == overlapTop) return "BOTTOM"; // Le bas du joueur touche le haut de la plateforme
            if (minOverlap == overlapBottom) return "TOP"; // Le haut du joueur touche le bas de la plateforme
            if (minOverlap == overlapLeft) return "RIGHT"; // La droite du joueur touche la gauche de la plateforme
            if (minOverlap == overlapRight) return "LEFT"; // La gauche du joueur touche la droite de la plateforme
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
