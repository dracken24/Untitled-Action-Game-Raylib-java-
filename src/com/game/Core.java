/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 28-2nd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 28-2nd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: init.java                                                  */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.game;

import static com.raylib.Raylib.initWindow;
import static com.raylib.Raylib.setTargetFPS;

import static com.raylib.Raylib.beginDrawing;
import static com.raylib.Raylib.beginTextureMode;
import static com.raylib.Raylib.clearBackground;
import static com.raylib.Raylib.endDrawing;
import static com.raylib.Raylib.drawText;
import static com.raylib.Raylib.drawTextureRec;
import static com.raylib.Raylib.getMousePosition;
import static com.raylib.Raylib.VIOLET;
import static com.raylib.Raylib.LIGHTGRAY;

import com.raylib.Vector2;
import com.raylib.Rectangle;

import com.enums.PlayerType;
import com.player.Player;
import com.player.InitPlayer;
import com.physic.PhysicCore;
import com.game.Cameras;
import com.raylib.Texture;
import com.raylib.Rectangle;
import static com.raylib.Raylib.beginMode2D;
import static com.raylib.Raylib.endMode2D;
import static com.raylib.Raylib.endTextureMode;
import static com.raylib.Raylib.WHITE;
import static com.raylib.Raylib.BLUE;

import com.enums.SpriteMovement;
import com.objects.Platform;
import com.objects.MovableObject;
import com.raylib.Camera2D;
import com.interfaces.IMovable;

public class Core
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	Vector2		WindowSize;
	String		title;

	Player		player;

	PhysicCore	physicCore;

	Platform[]	platformTest;
	MovableObject[] movableObjectTest;

	Cameras	cameras;

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

	public Core(Vector2 windowSize, String title)
	{
		// Initialize the window
		WindowSize = windowSize;
		this.title = title;
		initWindow((int)WindowSize.getX(), (int)WindowSize.getY(), title);
        setTargetFPS(60);

		// Initialize the player
		initPlayer();

		// Initialize the platform
		platformTest = new Platform[4];
		platformTest[0] = new Platform(WindowSize.getX() / 2 - 150, WindowSize.getY() / 2 + 200, 300, 40);
		platformTest[1] = new Platform(WindowSize.getX() / 2 - 480, WindowSize.getY() / 2 + 360, 300, 40);
		platformTest[2] = new Platform(WindowSize.getX() / 2 + 180, WindowSize.getY() / 2 + 360, 300, 40);
		platformTest[3] = new Platform(WindowSize.getX() / 2 - 600, WindowSize.getY() / 2 + 520, 1200, 60);

		// Initialize the movable object
		movableObjectTest = new MovableObject[1];
		movableObjectTest[0] = new MovableObject(new Vector2(WindowSize.getX() / 2 - 12 + 100, WindowSize.getY() / 2 + 12 - 200), new Vector2(24, 24), new Rectangle(0, 0, 24, 24), 1, new Vector2(12, 12), BLUE);
		movableObjectTest[0].setBounceForce(0.7f);
		
		physicCore = new PhysicCore();

		cameras = new Cameras();

		Rectangle recForCam = new Rectangle(0, 0, windowSize.getX(), windowSize.getY());
		cameras.initOneCamera(cameras.getMainCamera(), recForCam, windowSize);
		cameras.getMainCamera().setOffset(new Vector2(0, 0));
		cameras.setTargetToFollow(player.getPosition());
	}

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

	public void update()
	{
		// For adjust the camera to follow the player
		followCamera(player.getPosition());

		// Check collisions between the player and the platforms
		checkObjectCollisions(player, platformTest);

		beginDrawing();
			beginTextureMode(cameras.getMainTexture());
				clearBackground(LIGHTGRAY);
				beginMode2D(cameras.getMainCamera());
				
					drawText("Deplacement Gauche Droite: A & D", 10, 150, 20, VIOLET);
					drawText("Sauter : Espace", 10, 170, 20, VIOLET);
					drawText("Attaque: Clic Gauche", 10, 190, 20, VIOLET);
					drawText("B: Set Colision Box Visible", 10, 220, 20, VIOLET);
					drawText("R: Reset Player Position", 10, 240, 20, VIOLET);
				
					// Draw the platform
					for (int i = 0; i < platformTest.length; i++)
					{
						platformTest[i].drawPlatform();
					}

					// Draw the movable object
					for (int i = 0; i < movableObjectTest.length; i++)
					{
						
						checkObjectCollisions(movableObjectTest[i], platformTest);
						applyGravity(movableObjectTest[i]);
						movableObjectTest[i].update();
					}
					
					// Update the player
					player.update();

					// Apply gravity to the player
					applyGravity(player);

				endMode2D();
			endTextureMode();

			// draw the final texture
			drawTextureRec(
				cameras.getMainTexture().getTexture(),
				new Rectangle(
					0,                                                    // x
					0,                                                    // y
					cameras.getMainTexture().getTexture().getWidth(),     // width
					-cameras.getMainTexture().getTexture().getHeight()    // height (negative to invert)
				),
				new Vector2(0, 0),
				WHITE
			);
		endDrawing();
	}

	void followCamera(Vector2 targetPosition)
	{
		Camera2D mainCam = cameras.getMainCamera();
		mainCam.setTarget(targetPosition);
	}

	// /*
	//  * Check collisions between an object and an array of objects
	//  * @param arrayToCheck: The array of platforms to check
	//  * @param objectToCheck: The object to check
	//  */
	<T> void checkObjectCollisions(IMovable objectToCheck, T[] arrayToCheck)
	{
		boolean onGround = false;
		for (int i = 0; i < arrayToCheck.length; i++)
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

			Rectangle platformRect = arrayToCheck[i] instanceof Platform ? 
				((Platform)arrayToCheck[i]).getPlatform() : null;

			String collisionSide = physicCore.gravity.checkCollision(
				objectCollisionBox,
				platformRect
			);
			
			if (!collisionSide.equals("NONE"))
			{
				if (objectToCheck instanceof MovableObject)
				{
					System.out.println("COLLISION MOVABLE: " + collisionSide);
				}
				// Ajuster la position selon le côté de collision
				float adjustment = 0;
				switch(collisionSide)
				{
					case "BOTTOM":
						adjustment = objectCollisionBox.getY() + objectCollisionBox.getHeight() - platformRect.getY();
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
							
							// Si la vitesse est très faible, arrêter complètement
							if (Math.abs(currentVelocityY) < 2.0f)
							{
								obj.getVelocity().setY(0);
								obj.setIsJumping(false);
								onGround = true;
							}
							else
							{
								// Sinon, appliquer le rebond
								float newVelocityY = -currentVelocityY * obj.getBounceForce();
								obj.getVelocity().setY(newVelocityY);
								obj.setIsJumping(true);
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
			else if (objectToCheck instanceof MovableObject)
			{
				MovableObject obj = (MovableObject)objectToCheck;

				System.out.println("*****************");
				System.out.println("isJumping: " + obj.getIsJumping());
				System.out.println("isAtRest: " + obj.getIsAtRest());
				System.out.println("velocity: " + obj.getVelocity().getY());
				System.out.println("*****************");

				obj.setIsJumping(true);
				obj.setIsWallCollide(false);
				obj.setIsAtRest(false);
				return;
			}
		}

		if (!onGround)
		{
			objectToCheck.setIsJumping(true);
			if (objectToCheck instanceof Player)
			{
				Player player = (Player)objectToCheck;
				if (player.getVelocity().getY() > 0)
				{
					player.setMovement(SpriteMovement.FALL);
				}
			}
		}
	}

	void applyGravity(IMovable object)
	{
		if (object instanceof MovableObject)
		{
			MovableObject obj = (MovableObject)object;
			if (obj.getIsAtRest() || obj.getIsJumping() == false || obj.getVelocity().getY() == 0)
			{
				return;
			}
		}

		// Appliquer uniquement la gravité (mouvement vertical)
		Vector2 newPosition = physicCore.gravity.applyGravity(
			object.getPosition(),
			object.getVelocity(),
			object.getIsJumping()
		);
		object.setPosition(newPosition);

		// Mettre à jour la boîte de collision
		Rectangle colisionBox = object.getColisionBox();
		Vector2 colisionBoxPosition = physicCore.gravity.applyGravity(
			new Vector2(colisionBox.getX(), colisionBox.getY()),
			object.getVelocity(),
			object.getIsJumping()
		);
		
		colisionBox.setX(colisionBoxPosition.getX());
		colisionBox.setY(colisionBoxPosition.getY());
		object.setColisionBox(colisionBox);
	}

	// TODO: Adjust this function with player choice lather
	void initPlayer()
	{
		Vector2 playerPos = new Vector2(0, 0);
		Vector2 playerSize = new Vector2(70, 70);
		Vector2 collBoxSize = new Vector2(22, 55);
		int playerScale = 2;

		Rectangle playerColisionSize = new Rectangle(
			-(11 * playerScale),
			-(playerSize.getY() / 2 * playerScale) + ((playerSize.getY() - collBoxSize.getY()) * playerScale) - playerScale,
			collBoxSize.getX(),
			collBoxSize.getY()
		);

		Vector2 playerOffset = new Vector2(WindowSize.getX() / 2, WindowSize.getY() / 2);

		player = new Player(playerPos, playerSize, playerColisionSize, playerScale, playerOffset);
		new InitPlayer(PlayerType.ICHIGO, player, playerPos, playerSize);
		player.setBounceForce(0.0f);
	}
}
