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

    public boolean checkGroundCollision(Rectangle colisionBox, Rectangle platform)
    {
        if (checkCollisionRecs(colisionBox, platform))
        {
            return true;
        }
        return false;
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
