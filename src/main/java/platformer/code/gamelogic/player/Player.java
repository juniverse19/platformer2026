package platformer.code.gamelogic.player;

import java.awt.Color;
import java.awt.Graphics;

import platformer.code.gameengine.PhysicsObject;
import platformer.code.gameengine.graphics.MyGraphics;
import platformer.code.gameengine.hitbox.RectHitbox;
import platformer.code.gamelogic.Main;
import platformer.code.gamelogic.level.Level;
import platformer.code.gamelogic.tiles.Tile;

public class Player extends PhysicsObject{
	public float walkSpeed = 400;
	public float jumpPower = 1350;
	private float speedMultiplier = 1.0f;
	private int jumpsUsed = 0;
	private int maxJumps = 2;
	private boolean jumpPressedLastFrame = false;

	private boolean isJumping = false;

	public Player(float x, float y, Level level) {
	
		super(x, y, level.getLevelData().getTileSize(), level.getLevelData().getTileSize(), level);
		int offset =(int)(level.getLevelData().getTileSize()*0.1); //hitbox is offset by 10% of the player size.
		this.hitbox = new RectHitbox(this, offset,offset, width -offset, height - offset);
	}

	public void setSpeedMultiplier(float multiplier) {
    	speedMultiplier = multiplier;

		if(PlayerInput.isLeftKeyDown()) {
    	movementVector.x = -walkSpeed * speedMultiplier;
		}

		if(PlayerInput.isRightKeyDown()) {
    		movementVector.x = walkSpeed * speedMultiplier;
		}
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		movementVector.x = 0;
		if(PlayerInput.isLeftKeyDown()) {
			movementVector.x = -walkSpeed;
		}
		if(PlayerInput.isRightKeyDown()) {
			movementVector.x = +walkSpeed;
		}
		if(PlayerInput.isJumpKeyDown() && !isJumping) {
			movementVector.y = -jumpPower;
			isJumping = true;
		}
		
		isJumping = true;
		if(collisionMatrix[BOT] != null) isJumping = false;

		boolean jumpPressed = PlayerInput.isJumpKeyDown();

		//if pressed the jump and is not the same jump like holding it down and used jumps is less than 2
		if(jumpPressed && !jumpPressedLastFrame && jumpsUsed < maxJumps) {
    		movementVector.y = -jumpPower;
    		jumpsUsed++;
		}

		jumpPressedLastFrame = jumpPressed;

		if(collisionMatrix[BOT] != null) {
    		jumpsUsed = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		MyGraphics.fillRectWithOutline(g, (int)getX(), (int)getY(), width, height);
		
		if(Main.DEBUGGING) {
			for (int i = 0; i < closestMatrix.length; i++) {
				Tile t = closestMatrix[i];
				if(t != null) {
					g.setColor(Color.RED);
					g.drawRect((int)t.getX(), (int)t.getY(), t.getSize(), t.getSize());
				}
			}
		}
		
		hitbox.draw(g);
	}
}
