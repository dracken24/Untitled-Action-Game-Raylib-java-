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

import com.enums.SpriteMovement;
import com.objects.Platform;
import com.raylib.Camera2D;

public class Core
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	Player		player;
	Vector2		WindowSize;
	String		title;

	PhysicCore	physicCore;

	Platform[]	platformTest;

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
		Camera2D mainCam = cameras.getMainCamera();
		mainCam.setTarget(player.getPosition());
		cameras.setMainCamera(mainCam);
		// System.out.println("player colisionBox: " + player.getColisionBoxPlusOffset().getX() + ", " + player.getColisionBoxPlusOffset ().getY() + ", " + player.getColisionBox().getWidth() + ", " + player.getColisionBox().getHeight());
		// System.out.println("platformTest: " + platformTest.getPlatform().getX() + ", " + platformTest.getPlatform().getY() + ", " + platformTest.getPlatform().getWidth() + ", " + platformTest.getPlatform().getHeight());

		// Check collisions between the player and the platforms
		checkCollisions();

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
					
					// Update the player
					player.update();

					// Apply gravity to the player
					applyGravity(player.getPosition());

				endMode2D();
			endTextureMode();

			// Dessiner la texture finale
			drawTextureRec(
				cameras.getMainTexture().getTexture(),
				new Rectangle(
					0,                                                     // x
					0,                                                     // y
					cameras.getMainTexture().getTexture().getWidth(),     // width
					-cameras.getMainTexture().getTexture().getHeight()    // height (négatif pour inverser)
				),
				new Vector2(0, 0),
				WHITE
			);
		endDrawing();
	}

	void checkCollisions()
	{
		boolean onGround = false;
		for (int i = 0; i < platformTest.length; i++)
		{
			String collisionSide = physicCore.getGravity().checkGroundCollision(
				player.getColisionBoxPlusOffset(), 
				platformTest[i].getPlatform()
			);
			
			if (collisionSide != "NONE")
			{
				switch(collisionSide)
				{
					case "BOTTOM": // The player lands on the platform
						if (player.getActionInProgress() == SpriteMovement.FALL || 
							player.getActionInProgress() == SpriteMovement.JUMP)
						{
							player.setActionCounter(0);
						}
						player.getVelocity().setY(0);
						player.setIsJumping(false);
						onGround = true;
						
						// Adjust the position of the player and his collision box
						Rectangle playerBox = player.getColisionBoxPlusOffset();
						Rectangle platform = platformTest[i].getPlatform();
						float adjustment = playerBox.getY() + playerBox.getHeight() - platform.getY();
						
						// Adjust the position of the player
						player.setPosition(new Vector2(
							player.getPosition().getX(),
							player.getPosition().getY() - adjustment
						));
						
						// Adjust the position of the collision box
						Rectangle colBox = player.getColisionBox();
						colBox.setY(colBox.getY() - adjustment);
						player.setColisionBox(colBox);
						break;

					case "TOP": // The player hits the bottom of the platform
						player.getVelocity().setY(1);
						break;

					case "LEFT": // The player hits the right side of the platform
					case "RIGHT": // The player hits the left side of the platform
						player.getVelocity().setX(0);
						player.setIsWallCollide(true);
						break;
				}
			}

			player.setIsWallCollide(false);
		}

		// Si le joueur n'est sur aucune plateforme, il doit tomber
		if (!onGround)
		{
			player.setIsJumping(true);
		}
	}

	public void applyGravity(Vector2 position)
	{
		// Apply gravity to the player
		player.setPosition(physicCore.getGravity().applyGravity(player.getPosition(), player.getVelocity(), player.getIsJumping()));

		// Apply gravity to the colision box
		Rectangle colisionBox = player.getColisionBox();
		Vector2 colisionBoxPosition = physicCore.getGravity().applyGravity(new Vector2(colisionBox.getX(), colisionBox.getY()), player.getVelocity(), player.getIsJumping());
		colisionBox.setX(colisionBoxPosition.getX());
		colisionBox.setY(colisionBoxPosition.getY());
	}

	void initPlayer()
	{
		Vector2 playerPos = new Vector2(0, 0);
		Vector2 playerSize = new Vector2(70, 70);
		Vector2 collBoxSize = new Vector2(22, 55);
;		int playerScale = 2;

		Rectangle playerColisionSize = new Rectangle(
			-(11 * playerScale),
			-(playerSize.getY() / 2 * playerScale) + ((playerSize.getY() - collBoxSize.getY()) * playerScale) - playerScale,
			collBoxSize.getX(),
			collBoxSize.getY()
		);

		Vector2 playerOffset = new Vector2(WindowSize.getX() / 2, WindowSize.getY() / 2);

		player = new Player(playerPos, playerSize, playerColisionSize, playerScale, playerOffset);
		new InitPlayer(PlayerType.ICHIGO, player, playerPos, playerSize);
	}
}
