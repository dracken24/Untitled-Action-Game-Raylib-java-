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
import static com.raylib.Raylib.KeyboardKey.KEY_R;

import java.util.ArrayList;
import java.util.List;

import static com.raylib.Raylib.beginDrawing;
import static com.raylib.Raylib.beginTextureMode;
import static com.raylib.Raylib.clearBackground;
import static com.raylib.Raylib.endDrawing;
import static com.raylib.Raylib.drawText;
import static com.raylib.Raylib.drawTextureRec;
import static com.raylib.Raylib.VIOLET;
import static com.raylib.Raylib.LIGHTGRAY;
import static com.raylib.Raylib.isKeyPressed;

import static com.raylib.Raylib.KeyboardKey.KEY_A;
import static com.raylib.Raylib.KeyboardKey.KEY_D;
import static com.raylib.Raylib.KeyboardKey.KEY_S;
import static com.raylib.Raylib.KeyboardKey.KEY_W;
import static com.raylib.Raylib.isKeyDown;

import com.raylib.Vector2;
import com.raylib.Camera2D;
import com.raylib.Rectangle;
import static com.raylib.Raylib.beginMode2D;
import static com.raylib.Raylib.endMode2D;
import static com.raylib.Raylib.endTextureMode;
import static com.raylib.Raylib.getScreenWidth;
import static com.raylib.Raylib.WHITE;
import static com.raylib.Raylib.BLUE;
import static com.raylib.Raylib.PURPLE;
import static com.raylib.Raylib.DARKBLUE;

import com.objects.Platform;
import com.enums.PlayerType;
import com.objects.MovableObject;
import com.player.Player;
import com.enemy.Enemy;
import com.enemy.InitEnemy;
import com.player.InitPlayer;
import com.physic.PhysicCore;

public class Core
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	Vector2		WindowSize;
	String		title;

	Player		player;
	Enemy		enemyTest;

	PhysicCore	physicCore;

	List<MovableObject> movableObjectTest;
	List<Platform>	platformTest;

	Cameras	cameras;

	Vector2 positionFreeCam = new Vector2(0, 0);

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
		initEnemy01();

		// Initialize the platform
		platformTest = new ArrayList<Platform>();
		platformTest.add(new Platform(- 150, 200, 300, 40));
		platformTest.add(new Platform(- 480, 360, 300, 40));
		platformTest.add(new Platform(180, 360, 300, 40));
		platformTest.add(new Platform(- 800, 520, 1600, 60));

		// Initialize the movable object
		movableObjectTest = new ArrayList<MovableObject>();
		movableObjectTest.add(new MovableObject(
			new Vector2(-16 + 100,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			BLUE
		));

		movableObjectTest.add(new MovableObject(
			new Vector2(-16 + 150,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			PURPLE
		));

		movableObjectTest.add(new MovableObject(
			new Vector2(-16 + 200,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			DARKBLUE
		));

		movableObjectTest.add(new MovableObject(
			new Vector2(-16 - 100,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			BLUE
		));

		movableObjectTest.add(new MovableObject(
			new Vector2(-16 - 150,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			PURPLE
		));

		movableObjectTest.add(new MovableObject(
			new Vector2(-16 - 200,
			16 - 200),
			new Vector2(32, 32), new Rectangle(0, 0, 32, 32),
			1,
			new Vector2(16, 16),
			DARKBLUE
		));

		movableObjectTest.get(0).setPosition(new Vector2(- 16 + 100, 16 - 200));
		movableObjectTest.get(1).setPosition(new Vector2(- 16 + 150, 16 - 200));
		movableObjectTest.get(2).setPosition(new Vector2(- 16 + 200, 16 - 200));
		movableObjectTest.get(3).setPosition(new Vector2(- 16 - 100, 16 - 200));
		movableObjectTest.get(4).setPosition(new Vector2(- 16 - 150, 16 - 200));
		movableObjectTest.get(5).setPosition(new Vector2(- 16 - 200, 16 - 200));
		// movableObjectTest.get(0).setBounceForce(0.70f);
		
		physicCore = new PhysicCore();

		cameras = new Cameras();

		Rectangle recForCam = new Rectangle(0, 0, windowSize.getX(), windowSize.getY());
		cameras.initOneCamera(cameras.getMainCamera(), recForCam, windowSize);
		cameras.getMainCamera().setOffset(new Vector2(windowSize.getX() / 2, windowSize.getY() / 2));
		cameras.setTargetToFollow(player.getPosition());
	}

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

	public void update()
	{
		// For adjust the camera to follow the player
		followCamera(player.getPosition());
		// followCamera(enemyTest.getPosition());
		// followCamera(positionFreeCam);
		// freeCamera(positionFreeCam);

		// Check collisions between the player and the platforms
		physicCore.collision.checkObjectCollisions(player, platformTest, physicCore.gravity);

		beginDrawing();
			beginTextureMode(cameras.getMainTexture());
				clearBackground(LIGHTGRAY);
				beginMode2D(cameras.getMainCamera());
				
					onDrawning();

				endMode2D();
			endTextureMode();

				renderOnScreen();

		endDrawing();

		if (isKeyPressed(KEY_R))
		{
			movableObjectTest.get(0).setPosition(new Vector2(- 16 + 100, 16 - 200));
			movableObjectTest.get(1).setPosition(new Vector2(- 16 + 150, 16 - 200));
			movableObjectTest.get(2).setPosition(new Vector2(- 16 + 200, 16 - 200));
			movableObjectTest.get(3).setPosition(new Vector2(- 16 - 100, 16 - 200));
			movableObjectTest.get(4).setPosition(new Vector2(- 16 - 150, 16 - 200));
			movableObjectTest.get(5).setPosition(new Vector2(- 16 - 200, 16 - 200));
			// new Vector2(WindowSize.getX() / 2 - 25 + 100,
			// WindowSize.getY() / 2 + 25 - 200)
		}
	}

	void followCamera(Vector2 targetPosition)
	{
		Camera2D mainCam = cameras.getMainCamera();
		mainCam.setTarget(targetPosition);
	}

	void onDrawning()
	{
		drawText("Deplacement Gauche Droite: A & D", -(int)(WindowSize.getX() / 2) + 10, -(int)(WindowSize.getY() / 2) + 150, 20, VIOLET);
		drawText("Sauter : Espace", -(int)(WindowSize.getX() / 2) + 10, -(int)(WindowSize.getY() / 2) + 170, 20, VIOLET);
		drawText("Attaque: Clic Gauche", -(int)(WindowSize.getX() / 2) + 10, -(int)(WindowSize.getY() / 2) + 190, 20, VIOLET);
		drawText("B: Set Colision Box Visible", -(int)(WindowSize.getX() / 2) + 10, -(int)(WindowSize.getY() / 2) + 220, 20, VIOLET);
		drawText("R: Reset Player Position", -(int)(WindowSize.getX() / 2) + 10, -(int)(WindowSize.getY() / 2) + 240, 20, VIOLET);
	
		// Draw the platform
		for (Platform platform : platformTest)
		{
			platform.drawPlatform();
		}
		
		// Apply gravity to the player
		physicCore.gravity.applyGravity(player);
		// physicCore.gravity.applyGravity(enemyTest);
		
		// Draw the movable object
		for (MovableObject movableObject : movableObjectTest)
		{
			physicCore.collision.checkObjectCollisions(movableObject, platformTest, physicCore.gravity);
			physicCore.gravity.applyGravity(movableObject);
			physicCore.collision.checkPlayerMovableObjectCollision(player, movableObject, physicCore.gravity, true);

			if (player.movement.getWeaponHitCounter() > 0)
			{
				physicCore.collision.checkPlayerWeaponCollision(player, movableObject, physicCore.gravity);
			}

			for (MovableObject movableObject2 : movableObjectTest)
			{
				if (movableObject == movableObject2)
				{
					continue;
				}
				physicCore.collision.checkPlayerMovableObjectCollision(movableObject2, movableObject, physicCore.gravity, false);
			}

			movableObject.applyMovement(movableObject.getPosition(), movableObject.getColisionBox(), movableObject.getVelocity());
			movableObject.update();
		}
		
		// Update the player 
		player.update();
		// enemyTest.update();

		// physicCore.collision.checkObjectCollisions(player, movableObjectTest, physicCore.gravity);
	}

	void renderOnScreen()
	{
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
	}

	void freeCamera(Vector2 position)
	{
		if (isKeyDown(KEY_W))
		{
			position.setY(position.getY() - 4);
		}
		if (isKeyDown(KEY_S))
		{
			position.setY(position.getY() + 4);
		}
		if (isKeyDown(KEY_A))
		{
			position.setX(position.getX() - 4);
		}
		if (isKeyDown(KEY_D))
		{
			position.setX(position.getX() + 4);
		}
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

		Rectangle weaponColisionBox = new Rectangle(
			playerColisionSize.getX(),
			playerColisionSize.getY(),
			34,
			38
		);

		Vector2 playerOffset = new Vector2(0, 0);

		player = new Player(
			playerPos,
			playerSize,
			playerColisionSize,
			weaponColisionBox,
			playerScale,
			playerOffset
		);

		new InitPlayer(PlayerType.ICHIGO, player, playerPos, playerSize);
		player.setBounceForce(0.0f);
	}

	void initEnemy01()
	{
		Vector2 enemyPos = new Vector2(0, 0);
		Vector2 enemySize = new Vector2(70, 70);
		Vector2 collBoxSize = new Vector2(22, 55);
		int enemyScale = 2;

		Rectangle enemyColisionSize = new Rectangle(
			-(11 * enemyScale),
			-(enemySize.getY() / 2 * enemyScale) + ((enemySize.getY() - collBoxSize.getY()) * enemyScale) - enemyScale,
			collBoxSize.getX(),
			collBoxSize.getY()
		);

		Rectangle weaponColisionBox = new Rectangle(
			enemyColisionSize.getX(),
			enemyColisionSize.getY(),
			34,
			38
		);

		Vector2 enemyOffset = new Vector2(0, 0);

		enemyTest = new Enemy(
			enemyPos,
			enemySize,
			enemyColisionSize,
			weaponColisionBox,
			enemyScale,
			enemyOffset
		);

		new InitEnemy(enemyTest, enemyPos, enemySize);

		enemyTest.setBounceForce(0.0f);
		enemyTest.setPosition(new Vector2(WindowSize.getX() / 2, WindowSize.getY() / 2));

		Rectangle rec = enemyTest.getColisionBox();
		rec.setX(WindowSize.getX() / 2);
		rec.setY(WindowSize.getY() / 2);
		enemyTest.setColisionBox(weaponColisionBox);
	}
}
