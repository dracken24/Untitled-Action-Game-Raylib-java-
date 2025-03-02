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

	Platform	platformTest;

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
		platformTest = new Platform(WindowSize.getX() / 2 - 150, WindowSize.getY() / 2 + 200, 300, 40);

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

		if (physicCore.getGravity().checkGroundCollision(player.getColisionBoxPlusOffset(), platformTest.getPlatform()))
		{
			if (player.getActionInProgress() == SpriteMovement.FALL || player.getActionInProgress() == SpriteMovement.JUMP)
			{
				player.setActionCounter(0);
			}
			
			player.getVelocity().setY(0);
			player.setIsJumping(false);
		}
		else
		{
			player.setIsJumping(true);
		}

		beginDrawing();
			beginTextureMode(cameras.getMainTexture());
				clearBackground(LIGHTGRAY);
				beginMode2D(cameras.getMainCamera());
				
					Vector2 mousePosition = getMousePosition();
					drawText("Mouse position: " + mousePosition.getX() + ", " + mousePosition.getY(), 10, 10, 20, VIOLET);
				
					// Draw the platform
					platformTest.drawPlatform();
					
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
			-(playerSize.getY() / 2 * playerScale) + ((playerSize.getY() - collBoxSize.getY()) * playerScale),
			collBoxSize.getX(),
			collBoxSize.getY()
		);

		Vector2 playerOffset = new Vector2(WindowSize.getX() / 2, WindowSize.getY() / 2);

		player = new Player(playerPos, playerSize, playerColisionSize, playerScale, playerOffset);
		new InitPlayer(PlayerType.ICHIGO, player, playerPos, playerSize);
	}
}
