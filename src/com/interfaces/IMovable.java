/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 03-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 03-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: IMovable.java                                              */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.interfaces;

import com.raylib.Vector2;
import com.raylib.Rectangle;

public interface IMovable
{
    Vector2 getPosition();
    void setPosition(Vector2 position);
    Rectangle getColisionBox();
    void setColisionBox(Rectangle colisionBox);
    Vector2 getVelocity();
    void setIsJumping(boolean isJumping);
    void setIsWallCollide(boolean isWallCollide);
    boolean getIsJumping();
    boolean getIsAtRest();
}
