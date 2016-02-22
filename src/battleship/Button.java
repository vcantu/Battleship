package battleship;

import gameEngine.Input;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Button {
	
	private final int x, y, WIDTH, HEIGHT;
	private final Rectangle rect;
	private String text;
	
	private static enum Status {
		highlighted,
		clicked,
	}
	private Status status = null, prevStatus = null;
	
	private Color color;
	public void setColor(Color color) { this.color = color; }
	private int fSize = 10;
	public void setfSize(int size) { fSize = size; }
	private boolean down = false, clicked = false;
	
	public Button(int x, int y, int w, int h, String text) {
		this.x = x;
		this.y = y;
		WIDTH = w;
		HEIGHT = h;
		color = Color.LIGHT_GRAY;
		rect = new Rectangle(x, y, WIDTH, HEIGHT);
		this.text = text;
	}
	
	public void update(Input in) {
		if (rect.contains(in.mouseX(), in.mouseY())) {
			if (in.leftPressed() && (!in.prevLeftPressed() || prevStatus == Status.clicked)) {
				status = Status.clicked;
				clicked = true;
			}
			else {
				status = Status.highlighted;
				clicked = false;
			}
		} else {
			status = null;
			clicked = false;
		}
		if (down)
			status = Status.clicked;
	}
	
	public void endUpdate() {
		prevStatus = status;
	}
	
	public void draw(Graphics2D g2d) {
		if (status == Status.clicked) {
			//draw pressed
			g2d.setColor(darker(color, 1));
			g2d.fillRect(x, y, WIDTH, HEIGHT);
			//bottom-right
			g2d.setColor(darker(color, 6));
			g2d.fillRect(x + WIDTH - 1, y, 1, HEIGHT);
			g2d.fillRect(x, y + HEIGHT - 1, WIDTH, 1);
			//top-left
			g2d.setColor(lighter(color, 2));
			g2d.fillRect(x, y, 1, HEIGHT - 1);
			g2d.fillRect(x, y, WIDTH - 1, 1);
			//Draw Text
			g2d.setColor(darker(color, 5));
			g2d.setFont(new Font("Stencil", Font.BOLD, fSize));
			FontMetrics fm = g2d.getFontMetrics();
	        g2d.drawString(text, ((WIDTH - fm.stringWidth(text)) / 2) + x + 1, ((HEIGHT - fm.getAscent()) / 2) + fm.getAscent() + y + 1);
		} else if (status == Status.highlighted) {
			//draw highlighted
			g2d.setColor(lighter(color, 1));
			g2d.fillRect(x, y, WIDTH, HEIGHT);
			//bottom-right
			g2d.setColor(darker(color, 3));
			g2d.fillRect(x + WIDTH - 1, y, 1, HEIGHT);
			g2d.fillRect(x, y + HEIGHT - 1, WIDTH, 1);
			g2d.setColor(darker(color, 2));
			g2d.fillRect(x + WIDTH - 2, y, 1, HEIGHT);
			g2d.fillRect(x, y + HEIGHT - 2, WIDTH, 1);
			//top-left
			g2d.setColor(lighter(color, 5));
			g2d.fillRect(x, y, 1, HEIGHT - 1);
			g2d.fillRect(x, y, WIDTH - 1, 1);
			g2d.setColor(lighter(color, 4));
			g2d.fillRect(x + 1, y + 1, 1, HEIGHT - 3);
			g2d.fillRect(x + 1, y + 1, WIDTH - 3, 1);
			//Draw Text
			g2d.setColor(darker(color, 4));
			g2d.setFont(new Font("Stencil", Font.BOLD, fSize));
			FontMetrics fm = g2d.getFontMetrics();
	        g2d.drawString(text, ((WIDTH - fm.stringWidth(text)) / 2) + x, ((HEIGHT - fm.getAscent()) / 2) + fm.getAscent() + y);
		} else if (status == null) {
			//draw default
			g2d.setColor(color);
			g2d.fillRect(x, y, WIDTH, HEIGHT);
			//bottom-right
			g2d.setColor(darker(color, 4));
			g2d.fillRect(x + WIDTH - 1, y, 1, HEIGHT);
			g2d.fillRect(x, y + HEIGHT - 1, WIDTH, 1);
			g2d.setColor(darker(color, 3));
			g2d.fillRect(x + WIDTH - 2, y, 1, HEIGHT);
			g2d.fillRect(x, y + HEIGHT - 2, WIDTH, 1);
			//top-left
			g2d.setColor(lighter(color, 4));
			g2d.fillRect(x, y, 1, HEIGHT - 1);
			g2d.fillRect(x, y, WIDTH - 1, 1);
			g2d.setColor(lighter(color, 3));
			g2d.fillRect(x + 1, y + 1, 1, HEIGHT - 3);
			g2d.fillRect(x + 1, y + 1, WIDTH - 3, 1);
			//Draw Text
			g2d.setColor(darker(color, 4));
			g2d.setFont(new Font("Stencil", Font.BOLD, fSize));
			FontMetrics fm = g2d.getFontMetrics();
	        g2d.drawString(text, ((WIDTH - fm.stringWidth(text)) / 2) + x, ((HEIGHT - fm.getAscent()) / 2) + fm.getAscent() + y);
		}		
	}
	
	private static Color darker(Color color, int factor) {
		factor = Math.abs(factor - 8);
		int r, g, b;
		r = color.getRed() * factor / 8;
		g = color.getGreen() * factor / 8;
		b = color.getBlue() * factor / 8;
		
		if (r > 255) { r = 255; } if (r < 0) { r = 0; }
		if (g > 255) { g = 255; } if (g < 0) { g = 0; }
		if (b > 255) { b = 255; } if (b < 0) { b = 0; }
		return new Color(r, g, b);
	}
	
	private static Color lighter(Color color, int factor) {
		//factor = Math.abs(factor - 8);
		int r, g, b;
		r = (255 - color.getRed()) * factor / 8; r += color.getRed();
		g = (255 - color.getGreen()) * factor / 8; g += color.getGreen();
		b = (255 - color.getBlue()) * factor / 8; b += color.getBlue();
		
		if (r > 255) { r = 255; } if (r < 0) { r = 0; }
		if (g > 255) { g = 255; } if (g < 0) { g = 0; }
		if (b > 255) { b = 255; } if (b < 0) { b = 0; }
		return new Color(r, g, b);
	}

	public boolean clicked() { return clicked; }
	public boolean prevClicked() { return prevStatus == Status.clicked; }
	public void setDown(boolean b) {
		if (!b)
			status = null;
		down = b;
		}

}
