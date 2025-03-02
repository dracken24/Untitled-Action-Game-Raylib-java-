/* =============================================================================== */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/*               -------------------------------------------------                 */
/*                PROJET: Java Dev          PAR: Dracken24                         */
/*               -------------------------------------------------                 */
/*                CREATED: 28-2nd-2025                                             */
/*                MODIFIED BY: Dracken24                                           */
/*                LAST MODIFIED: 28-2nd-2025                                       */
/*               -------------------------------------------------                 */
/*                FILE: platform.java                                              */
/*               -------------------------------------------------                 */
/* ---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~---~--- */
/* =============================================================================== */

package com.objects;

import com.raylib.Rectangle;
import com.raylib.Vector2;
import com.raylib.Color;

import static com.raylib.Raylib.getScreenWidth;
import static com.raylib.Raylib.getScreenHeight;
import static com.raylib.Raylib.DARKGRAY;
import static com.raylib.Raylib.drawRectangleRec;

public class Platform
{
/***********************************************************************************/
/***                                 VARIABLES                                     */
/***********************************************************************************/

	Rectangle platform = new Rectangle();
	Color color = new Color();

/***********************************************************************************/
/***                                 CONSTRUCTOR                                   */
/***********************************************************************************/

	public Platform()
	{
		Vector2 position = new Vector2(getScreenWidth() / 2 - 50, getScreenHeight() / 2 - 10);
		
		platform.setX(position.getX());
		platform.setY(position.getY());
		platform.setWidth(100);
		platform.setHeight(20);

		color = DARKGRAY;
	}

	public Platform(float x, float y, float width, float height)
	{
		platform.setX(x);
		platform.setY(y);
		platform.setWidth(width);
		platform.setHeight(height);

		color = DARKGRAY;
	}

	public Platform(float x, float y, float width, float height, Color color)
	{
		platform.setX(x);
		platform.setY(y);
		platform.setWidth(width);
		platform.setHeight(height);

		this.color = color;
	}

/***********************************************************************************/
/***                                 FUNCTIONS                                     */
/***********************************************************************************/
	
	public void drawPlatform()
	{
		drawRectangleRec(platform, color);
	}

/***********************************************************************************/
/***                                 GETTERS                                       */
/***********************************************************************************/

	public Rectangle getPlatform()
	{
		return platform;
	}
	
	public Vector2 getPlatformPosition()
	{
		return new Vector2(platform.getX(), platform.getY());
	}
	
	public Vector2 getPlatformSize()
	{
		return new Vector2(platform.getWidth(), platform.getHeight());
	}

	public Color getPlatformColor()
	{
		return color;
	}
	
/***********************************************************************************/
/***                                 SETTERS                                       */	
/***********************************************************************************/

	public void setPlatform(Rectangle platform)
	{
		this.platform = platform;
	}
	
	public void setPlatformPosition(Vector2 position)
	{
		platform.setX(position.getX());
		platform.setY(position.getY());
	}
	
	public void setPlatformSize(Vector2 size)
	{
		platform.setWidth(size.getX());
		platform.setHeight(size.getY());
	}

	public void setPlatformColor(Color color)
	{
		this.color = color;
	}
}
