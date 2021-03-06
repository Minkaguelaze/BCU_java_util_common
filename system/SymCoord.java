package common.system;

import common.system.fake.FakeGraphics;
import common.system.fake.FakeImage;

public class SymCoord {

	public FakeGraphics g;
	public double r, x, y;
	public int type;

	private P size = new P(0, 0);
	private P pos = new P(0, 0);

	public SymCoord(FakeGraphics fg, double R, double X, double Y, int t) {
		g = fg;
		r = R;
		x = X;
		y = Y;
		type = t;
	}

	public P draw(FakeImage... fis) {
		setSize(0, 0);
		for (FakeImage f : fis) {
			size.x += f.getWidth();
			size.y = Math.max(size.y, f.getHeight());
		}
		size.times(r);
		setPos(x, y);
		if ((type & 1) > 0)
			pos.x -= size.x;
		if ((type & 2) > 0)
			pos.y -= size.y;
		for (FakeImage f : fis) {
			if (r == 1)
				g.drawImage(f, pos.x, pos.y);
			else
				g.drawImage(f, pos.x, pos.y, f.getWidth() * r, f.getHeight() * r);
			pos.x += f.getWidth() * r;
		}
		return size;
	}

	private void setPos(double x, double y) {
		pos.x = x;
		pos.y = y;
	}

	private void setSize(double x, double y) {
		size.x = x;
		size.y = y;
	}

}
