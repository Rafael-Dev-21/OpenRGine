package br.com.rgine.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

public class Sprite extends Layer {

  public static final int ANIM_STOP = 0;
  public static final int ANIM_GO = 1;
  public static final int ANIM_GOBACK = 2;
  public static final int ANIM_JUST_GO = 3;
  public static final int ANIM_JUST_GOBACK = 4;

  private int anim = ANIM_STOP;

  private Bitmap source;
  private Paint paint;

  private float rotate;

  private PointF scale;

  private Point refPixel;

  private int cols;
  private int rows;

  private int[] rawFrames;
  private int[] frameSeq;

  private int frame;

  public Sprite(Bitmap bmp) {
    setSource(bmp, bmp.getWidth(), bmp.getHeight());
    init();
  }

  public Sprite(Bitmap bmp, int frameWidth, int frameHeight) {
    setSource(bmp, frameWidth, frameHeight);
    init();
  }

  public Sprite(Sprite old) {
    setSource(old.source, old.getWidth(), old.getHeight());
    setBounds(new Rect(old.getBounds()));
    paint.set(old.paint);
    rotate = old.rotate;
    scale = new PointF(old.scale.x, old.scale.y);
    refPixel = new Point(old.refPixel);
  }

  private void init() {
    this.paint = new Paint();
    this.rotate = 0f;
    this.scale = new PointF(1f, 1f);
    this.refPixel = new Point(0, 0);
  }

  public void defineRefPixel(int x, int y) {
    refPixel.x = x;
    refPixel.y = y;
  }

  public void defineRefPixel(float x, float y) {
    defineRefPixel(Math.round(x), Math.round(y));
  }

  public void setRefPosition(int x, int y) {
    setPosition(x - getRefX(), y - getRefY());
  }

  public void setRefPosition(float x, float y) {
    setRefPosition(Math.round(x), Math.round(y));
  }

  public int getRefX() {
    return refPixel.x;
  }

  public int getRefY() {
    return refPixel.y;
  }

  public int getAnimState() {
    return anim;
  }

  public void setAnimState(int a) {
    if (a < 0 || a > 4) throw new IllegalArgumentException();
    anim = a;
  }

  public void draw(Canvas c) {
    if (c == null) throw new NullPointerException();
    if (isVisible()) {
      c.save();

      c.scale(scale.x, scale.y, refPixel.x, refPixel.y);

      c.rotate(rotate, refPixel.x, refPixel.y);

      c.drawBitmap(source, getX(), getY(), paint);

      c.restore();
    }
  }

  public void update(float delta) {
    switch (anim) {
      case ANIM_STOP:
        break;
      case ANIM_GO:
        nextFrame();
        break;
      case ANIM_GOBACK:
        prevFrame();
        break;
      case ANIM_JUST_GO:
        if (frame != getFrameSequenceLength() - 1) nextFrame();
        break;
      case ANIM_JUST_GOBACK:
        if (frame != 0) prevFrame();
    }
  }

  public int getRawFrameCount() {
    return rawFrames.length;
  }

  public int getFrameSequenceLength() {
    return frameSeq.length;
  }

  public void setFrame(int nf) {
    if (nf < 0 || nf >= getFrameSequenceLength()) {
      throw new IndexOutOfBoundsException();
    }

    frame = nf;
  }

  public int getFrame() {
    return frame;
  }

  public void nextFrame() {
    frame++;
    if (frame >= getFrameSequenceLength()) {
      frame = 0;
    }
  }

  public void prevFrame() {
    frame--;
    if (frame < 0) {
      frame = getFrameSequenceLength() - 1;
    }
  }

  public void setFrameSequence(int[] seq) {
    for (int i : seq) {
      if (i > getRawFrameCount() - 1) throw new IndexOutOfBoundsException();
    }

    frameSeq = seq.clone();
  }

  public void setRotate(float rot) {
    rotate = rot;
  }

  public void setScale(float sx, float sy) {
    scale.x = sx;
    scale.y = sy;
  }

  public void setSource(Bitmap bmp, int frameWidth, int frameHeight) {
    if (bmp == null) throw new NullPointerException();
    if (frameWidth < 0
        || frameHeight < 0
        || frameWidth > bmp.getWidth()
        || frameHeight > bmp.getHeight()
        || bmp.getWidth() % frameWidth != 0
        || bmp.getHeight() % frameHeight != 0) throw new IllegalArgumentException();
    this.source = bmp;
    setDimens(frameWidth, frameHeight);
    this.cols = source.getWidth() / getWidth();
    this.rows = source.getHeight() / getHeight();

    rawFrames = new int[cols * rows];

    for (int i = 0; i < cols; i++) {
      for (int j = 0; j < rows; j++) {
        rawFrames[i * cols + j] = i * cols + j;
      }
    }

    if (frameSeq == null || frameSeq.length < rawFrames.length) {
      frameSeq = rawFrames;
      frame = 0;
    }
  }

  public void setAlpha(int alpha) {
    paint.setAlpha(alpha);
  }

  public void setColorFilter(ColorFilter filter) {
    paint.setColorFilter(filter);
  }

  @Deprecated
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  public Paint getPaint() {
    return paint;
  }
}
