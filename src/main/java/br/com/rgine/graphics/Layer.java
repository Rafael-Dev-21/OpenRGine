package br.com.rgine.graphics;

import android.graphics.drawable.Drawable;

/**
 * represents a drawable game layer, like a sprite layer or a tiled layer.
 */
public abstract class Layer extends Drawable {

  /**
   * Obtains the width of the layer.
   * @return The width
   */
  public int getWidth() {
    return getBounds().width();
  }

  public int getHeight() {
    return getBounds().height();
  }

  public int getX() {
    return getBounds().left;
  }

  public int getY() {
    return getBounds().top;
  }

  public void move(int dx, int dy) {
    getBounds().offset(dx, dy);
  }

  public void move(float dx, float dy) {
    move(Math.round(dx), Math.round(dy));
  }

  public abstract void update(float delta);

  public void setPosition(int x, int y) {
    getBounds().offsetTo(x, y);
  }

  public void setPosition(float x, float y) {
    setPosition(Math.round(x), Math.round(y));
  }

  protected void setDimens(int nw, int nh) {
    getBounds().right = getX() + nw;
    getBounds().bottom = getY() + nh;
  }
}
