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

public class Movement
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

    Vector2 velocity;
    Vector2 lastPlayerPosition;

    boolean isJumping;

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

    public Movement()
    {
        actionInProgress = SpriteMovement.IDLE;
		rightSide = false;
        currentAction = idle;
        lastAction = null;
        actionCounter = 0;
        attackCounter = 0;
        velocity = new Vector2(0, 0);
        lastPlayerPosition = new Vector2(0, 0);
        isJumping = false;
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
        // System.out.println("isJumping: " + isJumping);
        // System.out.println("actionCounter: " + actionCounter);

        if (velocity.getY() >= 8 && attackCounter  == 0)
        {
            this.actionInProgress = SpriteMovement.FALL;
            actionCounter = 0;
        }

        lastPlayerPosition = playerPosition;
	}

    void adjustVelocity()
    {
        if (isJumping)
        {
            // Ajoute une accélération constante (gravité)
            velocity.setY(velocity.getY() + 0.5f);
            
            // Limite la vitesse de chute maximale
            if (velocity.getY() > 10)
            {
                velocity.setY(10);
            }

            velocity.setY(velocity.getY() + 0.0001f);
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
            if (actionCounter == 0 && isJumping == false)
            {
                this.actionInProgress = SpriteMovement.RUN;
                velocity.setX(4);
            }
		}
		if (isKeyDown(KEY_A))
		{
			rightSide = true;
            isKeyDown = true;
            if (actionCounter == 0 && isJumping == false)
            {
                this.actionInProgress = SpriteMovement.RUN;
                velocity.setX(-4);
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
                velocity.setY(-14);
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

    public int getActionCounter()
    {
        return actionCounter;
    }

    public SpriteMovement getActionInProgress()
    {
        return actionInProgress;
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

    public void setActionCounter(int actionCounter)
    {
        this.actionCounter = actionCounter;
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
            case DEATH:
                setCurrentAction(death);
                break;
            case HURT:
                setCurrentAction(hurt);
                break;
            case FALL:
                setCurrentAction(fall);
                break;
        }
    }
}

