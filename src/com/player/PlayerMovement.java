/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 01-3rd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 01-3rd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: Movement.java                                              */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.player;

import static com.raylib.Raylib.KeyboardKey.KEY_A;
import static com.raylib.Raylib.KeyboardKey.KEY_D;
import static com.raylib.Raylib.KeyboardKey.KEY_SPACE;
import static com.raylib.Raylib.isKeyDown;
import static com.raylib.Raylib.isKeyPressed;
import static com.raylib.Raylib.isMouseButtonPressed;
import static com.raylib.Raylib.MouseButton.MOUSE_BUTTON_LEFT;

import com.raylib.Vector2;
import com.raylib.Rectangle;

import com.objects.SpriteSheet;
import com.enums.SpriteMovement;

public class PlayerMovement
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

    SpriteSheet	attack01;
    SpriteSheet	attack02;
    SpriteSheet	attack03;
    SpriteSheet	idle;
    SpriteSheet	jump;
    SpriteSheet	fall;
    SpriteSheet	run;
    SpriteSheet	walk;
    SpriteSheet	death;
    SpriteSheet	hurt;
    SpriteSheet	shild;

	SpriteSheet	currentAction;
	SpriteSheet	lastAction;

    SpriteMovement actionInProgress;

	boolean		rightSide;

    int actionCounter;
    int attackCounter;
    int weaponHitCounter;

    Vector2 velocity;
    Vector2 velocityMinMax_Y;
    Vector2 velocityMinMax_X;
    Vector2 lastPlayerPosition;
    float fallSpeedMax;
    float jumpForce;

    boolean isJumping;
    boolean isAtRest;
    boolean isWallCollide;

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

    public PlayerMovement()
    {
        actionInProgress = SpriteMovement.IDLE;
		rightSide = false;
        currentAction = idle;
        lastAction = null;
        actionCounter = 0;
        attackCounter = 0;
        weaponHitCounter = 0;
        velocity = new Vector2(0, 0);
        velocityMinMax_Y = new Vector2(0, 12);
        velocityMinMax_X = new Vector2(0, 4);
        lastPlayerPosition = new Vector2(0, 0);
        jumpForce = -14;
        fallSpeedMax = 16;
        isJumping = false;
        isWallCollide = false;
    }

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/

	public void update(Vector2 playerPosition, Vector2 offset)
	{
        catchInput();
        setMovement(actionInProgress);

		if (lastAction != currentAction)
		{
			currentAction.resetCounter();
		}

		currentAction.updateSprite(false, rightSide, playerPosition, offset);
        
        adjustVelocity();
        // System.out.println("velocity: " + velocity.getY());
        // System.out.println("PlayerMovement isJumping: " + isJumping);
        // System.out.println("actionCounter: " + actionCounter);
        // System.out.println("actionInProgress: " + actionInProgress);

        if (velocity.getY() >= 8 && attackCounter  == 0)
        {
            this.actionInProgress = SpriteMovement.FALL;
            actionCounter = 0;
        }
        else if (velocity.getY() > 0 && attackCounter  == 0)
        {
            this.actionInProgress = SpriteMovement.JUMP;
        }

        lastPlayerPosition = playerPosition;
	}

    void adjustVelocity()
    {
        if (isJumping || isAtRest)
        {
            // add gravity
            // velocity.setY(velocity.getY() + 0.5f);
            
            // limit the max fall speed
            if (velocity.getY() > fallSpeedMax)
            {
                velocity.setY(fallSpeedMax);
            }

            velocity.setY(velocity.getY() + 0.4f);
        }
        else
        {
            velocity.setY(0);
        }
    }

    public Vector2 applyMovement(Vector2 position, Rectangle colisionBox, Vector2 velocity)
    {
        position.setX(position.getX() + velocity.getX());
        colisionBox.setX(colisionBox.getX() + velocity.getX());

        // Slow down gradually the player when he is in the air
        if (isJumping)
        {
            velocity.setX(velocity.getX() * 0.99f);
        }

        return position;
    }

    void catchInput()
	{
		boolean isKeyDown = false;
        if (actionCounter > 0)
        {
            isKeyDown = true;
        }
        // System.out.println("actionCounter: " + actionCounter);
		
		if (isKeyDown(KEY_D))
		{
			rightSide = false;
            isKeyDown = true;
            velocity.setX(velocityMinMax_X.getY());
            if (actionCounter == 0 && isJumping == false )
            {
                if (attackCounter == 0)
                {
                    this.actionInProgress = SpriteMovement.RUN;
                }
            }
		}
		if (isKeyDown(KEY_A))
		{
			rightSide = true;
            isKeyDown = true;
            velocity.setX(-velocityMinMax_X.getY());
            if (actionCounter == 0 && isJumping == false)
            {
                if (attackCounter == 0)
                {
                    this.actionInProgress = SpriteMovement.RUN;
                }
            }
		}
        if (isKeyPressed(KEY_SPACE))
		{
            if (actionCounter == 0 && isJumping == false)
            {
                this.actionInProgress = SpriteMovement.JUMP;
                actionCounter = jump.getAnimationTotalFrame();
            }
            if (velocity.getY() == 0)
            {
                velocity.setY(jumpForce);
            }
            isKeyDown = true;
            isJumping = true;
		}
		if (isMouseButtonPressed(MOUSE_BUTTON_LEFT))
		{
            isKeyDown = true;
            if (attackCounter == 0)
            {
                this.actionInProgress = SpriteMovement.ATTACK01;
                attackCounter = attack01.getAnimationTotalFrame();
                weaponHitCounter = 10;
            }
		}

        // System.out.println("actionCounter: " + actionCounter);
        // System.out.println("actionInProgress: " + actionInProgress);
		if (!isKeyDown && isJumping == false && attackCounter == 0)
		{
			this.actionInProgress = SpriteMovement.IDLE;
            velocity.setX(0);
		}
        if (actionCounter > 0)
        {
            actionCounter--;
        }
        if (attackCounter > 0)
        {
            attackCounter--;
        }
        if (weaponHitCounter > 0)
        {
            weaponHitCounter--;
        }
	}

/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/

    public SpriteSheet getAttack01()
    {
        return attack01;
    }

    public SpriteSheet getAttack02()
    {
        return attack02;
    }

    public SpriteSheet getAttack03()
    {
        return attack03;
    }   

    public SpriteSheet getIdle()
    {
        return idle;
    }   

    public SpriteSheet getJump()
    {
        return jump;
    }   

    public SpriteSheet getRun()
    {
        return run;
    }      

    public SpriteSheet getWalk()
    {
        return walk;
    }   

    public SpriteSheet getDeath()
    {
        return death;
    }       

    public SpriteSheet getHurt()
    {
        return hurt;
    }   

    public SpriteSheet getShild()
    {
        return shild;
    }   

    public SpriteSheet getFall()
    {
        return fall;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public boolean getIsJumping()
    {
        return isJumping;
    }

    public boolean getIsAtRest()
    {
        return isAtRest;
    }

    public boolean getRightSide()
    {
        return rightSide;
    }

    public int getActionCounter()
    {
        return actionCounter;
    }

    public SpriteMovement getActionInProgress()
    {
        return actionInProgress;
    }

    public int getWeaponHitCounter()
    {
        return weaponHitCounter;
    }

/***********************************************************************************/
/***                                 SETTERS                                       */
/***********************************************************************************/

    public void setAttack01(SpriteSheet attack01)
    {
        this.attack01 = attack01;
    }

    public void setAttack02(SpriteSheet attack02)
    {
        this.attack02 = attack02;
    }   

    public void setAttack03(SpriteSheet attack03)
    {
        this.attack03 = attack03;
    }   

    public void setIdle(SpriteSheet idle)
    {
        this.idle = idle;
    }

    public void setJump(SpriteSheet jump)
    {
        this.jump = jump;
    }

    public void setRun(SpriteSheet run)
    {
        this.run = run;
    }

    public void setWalk(SpriteSheet walk)
    {
        this.walk = walk;
    }

    public void setDeath(SpriteSheet death)
    {
        this.death = death;
    }

    public void setHurt(SpriteSheet hurt)
    {
        this.hurt = hurt;
    }

    public void setShild(SpriteSheet shild)
    {
        this.shild = shild;
    }

    public void setCurrentAction(SpriteSheet currentAction)
    {
        this.lastAction = this.currentAction;
        this.currentAction = currentAction;
    }

    public void setRightSide(boolean rightSide)
    {
        this.rightSide = rightSide;
    }

    public void setFall(SpriteSheet fall)
    {
        this.fall = fall;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }

    public void setIsJumping(boolean isJumping)
    {
        this.isJumping = isJumping;
    }

    public void setIsAtRest(boolean isAtRest)
    {
        this.isAtRest = isAtRest;
    }

    public void setActionCounter(int actionCounter)
    {
        this.actionCounter = actionCounter;
    }

    public void setIsWallCollide(boolean isWallCollide)
    {
        this.isWallCollide = isWallCollide;
    }

    public void setMovement(SpriteMovement movement)
    {
        switch (movement)
        {
            case IDLE:
                if (actionCounter == 0)
                {
                    setCurrentAction(idle);
                }
                break;
            case RUN:
                if (actionCounter == 0)
                {
                    setCurrentAction(run);
                }
                break;
            case JUMP:
                setCurrentAction(jump);
                break;
            case ATTACK01:
                setCurrentAction(attack01);
                break; 
            case FALL:
                setCurrentAction(fall);
                break;
        }
    }
}

