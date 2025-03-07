/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 03-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 03-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: Collision.java                                             */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.physic;

import com.raylib.Vector2;
import com.raylib.Rectangle;

import com.objects.MovableObject;
import com.interfaces.IMovable;
import com.player.Player;
import com.objects.Platform;
import com.enums.SpriteMovement;

import java.util.List;

public class Collision
{
	float veloxityMosifierY = 6;
	float veloxityMosifierX = 6;

	public void checkPlayerMovableObjectCollision(IMovable player,  IMovable movableObject, Gravity gravity)
	{
		Rectangle playerCollBox = ((Player)player).getColisionBoxPlusOffset();
		Rectangle objectCollBox = movableObject.getColisionBox();

		String collisionSide = gravity.checkCollision(
			playerCollBox,
			objectCollBox
		);

		
		// System.out.println("objectCollBox: " + objectCollBox.getX() + " y: " + objectCollBox.getY());
		// System.out.println("Collision side: " + collisionSide);
		// System.out.println("velocity: " + movableObject.getVelocity().getX());

		switch(collisionSide)
		{
			case "BOTTOM":
				// System.out.println("Collision side: " + collisionSide);
				// ((MovableObject)movableObject).setVelocity(new Vector2(
				// 	movableObject.getVelocity().getX(),
				// 	veloxityMosifierX
				// ));
				break;
			case "TOP":
				// ((MovableObject)movableObject).setVelocity(new Vector2(
				// 	movableObject.getVelocity().getX(),
				// 	-veloxityMosifierX
				// ));
				break;
			case "LEFT":
				// System.out.println(movableObject.getPosition().getX() - (playerCollBox.getX() - (objectCollBox.getX()) - objectCollBox.getWidth()));
				// Push movable to the left
				((MovableObject)movableObject).setVelocity(new Vector2(
					-veloxityMosifierY,
					movableObject.getVelocity().getY()
				));
				break;
			case "RIGHT":
				// System.out.println(movableObject.getPosition().getX() - (playerCollBox.getX() - (objectCollBox.getX()) - objectCollBox.getWidth()));
				// Push movable to the left

				((MovableObject)movableObject).setVelocity(new Vector2(
					veloxityMosifierY,
					movableObject.getVelocity().getY()
				));
				break;
		}
	}

    // /*
	//  * Check collisions between an object and an array of objects
	//  * @param arrayToCheck: The array of platforms to check
	//  * @param objectToCheck: The object to check
	//  */
	public <T> void checkObjectCollisions(IMovable objectToCheck, List<T> arrayToCheck, Gravity gravity)
	{
		boolean onGround = false;
		for (T element : arrayToCheck)
		{
			Rectangle objectCollisionBox;
			if (objectToCheck instanceof Player)
			{
				objectCollisionBox = ((Player)objectToCheck).getColisionBoxPlusOffset();
			}
			else
			{
				objectCollisionBox = objectToCheck.getColisionBox();
			}

			Rectangle platformRect = element instanceof Platform ?
				((Platform)element).getPlatform() : null;

			String collisionSide = gravity.checkCollision(
				objectCollisionBox,
				platformRect
			);

			// System.out.println( "collisionSide: " + collisionSide);
			
			if (!collisionSide.equals("NONE"))
			{
				float adjustment = 0;
				switch(collisionSide)
				{
					case "BOTTOM":
						adjustment = objectCollisionBox.getY() + objectCollisionBox.getHeight() - platformRect.getY();

						// Ajuster la position avant d'appliquer le rebond
						objectToCheck.setPosition(new Vector2(
							objectToCheck.getPosition().getX(),
							objectToCheck.getPosition().getY() - adjustment
						));
						
						if (objectToCheck instanceof Player)
						{
							Player player = (Player)objectToCheck;
							if (player.getActionInProgress() == SpriteMovement.FALL || 
								player.getActionInProgress() == SpriteMovement.JUMP)
							{
								player.setActionCounter(0);
							}
							if (player.getVelocity().getY() > 0)
							{
								player.getVelocity().setY(0);
								player.setIsJumping(false);
							}
							onGround = true;
						}
						else if (objectToCheck instanceof MovableObject)
						{
							MovableObject obj = (MovableObject)objectToCheck;
							float currentVelocityY = obj.getVelocity().getY();

							// System.out.println("currentVelocityY: " + adjustment);
							
							// Si la vitesse est très faible, arrêter complètement
							if (Math.abs(currentVelocityY) < 2.0f)
							{
								// System.out.println("STOP");
								obj.getVelocity().setY(0);
								obj.setIsJumping(false);
								obj.setIsAtRest(true);
								onGround = true;
							}
							else
							{
								// System.out.println("REBOUND");
								// Sinon, appliquer le rebond
								float newVelocityY = -currentVelocityY * obj.getBounceForce();
								obj.getVelocity().setY(newVelocityY);
								obj.setIsJumping(true);
								obj.setIsAtRest(false);
							}
						}
						break;

					case "TOP":
						adjustment = platformRect.getY() + platformRect.getHeight() - objectCollisionBox.getY();
						objectToCheck.setPosition(new Vector2(
							objectToCheck.getPosition().getX(),
							objectToCheck.getPosition().getY() + adjustment
						));
						
						if (objectToCheck instanceof MovableObject)
						{
							MovableObject obj = (MovableObject)objectToCheck;
							obj.getVelocity().setY(-obj.getVelocity().getY() * obj.getBounceForce());
						}
						break;

					case "LEFT":
					case "RIGHT":
						adjustment = collisionSide.equals("LEFT") ? 
							platformRect.getX() + platformRect.getWidth() - objectCollisionBox.getX() :
							objectCollisionBox.getX() + objectCollisionBox.getWidth() - platformRect.getX();
						
						objectToCheck.setPosition(new Vector2(
							objectToCheck.getPosition().getX() + (collisionSide.equals("LEFT") ? adjustment : -adjustment),
							objectToCheck.getPosition().getY()
						));
						
						if (objectToCheck instanceof MovableObject)
						{
							MovableObject obj = (MovableObject)objectToCheck;
							obj.getVelocity().setX(-obj.getVelocity().getX() * obj.getBounceForce());
						}

						objectToCheck.setIsWallCollide(true);
						
						break;
				}

				// System.out.println("***************** ((Player)objectToCheck).getOffset().getX()" + ((Player)objectToCheck).getOffset().getX());

				// Ajuster la boîte de collision après le déplacement
				Rectangle colBox = objectToCheck.getColisionBox();
				colBox.setX(objectToCheck.getPosition().getX() + (objectToCheck instanceof Player ? 
					((Player)objectToCheck).getCollisionBoxOffset().getX() : 0));
				colBox.setY(objectToCheck.getPosition().getY() + (objectToCheck instanceof Player ? 
					((Player)objectToCheck).getCollisionBoxOffset().getY() : 0));
				objectToCheck.setColisionBox(colBox);
			}
			else if (objectToCheck instanceof MovableObject && !onGround)
			{
				MovableObject obj = (MovableObject)objectToCheck;

				obj.setIsJumping(true);
				obj.setIsWallCollide(false);
				obj.setIsAtRest(false);
			}
			else if (objectToCheck instanceof MovableObject && onGround)
			{
				MovableObject obj = (MovableObject)objectToCheck;

				obj.setIsJumping(false);
				obj.setIsWallCollide(false);
				obj.setIsAtRest(true);
			}
		}

		if (!onGround)
		{
			objectToCheck.setIsJumping(true);
		}
	}
}
