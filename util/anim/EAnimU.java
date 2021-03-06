package common.util.anim;

import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;

public class EAnimU extends EAnimD {

	protected EAnimU(AnimU<?> ani, int i) {
		super(ani, ani.mamodel, ani.anims[i]);
		type = i;
	}

	@Override
	public AnimU<?> anim() {
		return (AnimU<?>) a;
	}

	/** change the animation state, for entities only */
	@Override
	public void changeAnim(int t) {
		if (t >= anim().anims.length)
			return;
		f = -1;
		ma = anim().anims[t];
		type = t;
	}

	@Override
	public void draw(FakeGraphics g, P ori, double siz) {
		if (f == -1) {
			f = 0;
			setup();
		}
		set(g);
		FakeTransform at = g.getTransform();
		g.translate(ori.x, ori.y);
		if (ref && !battle) {
			P p0 = P.newP(-200, 0).times(siz);
			P p1 = P.newP(400, 100).times(siz);
			P p2 = P.newP(0, -300).times(siz);
			g.drawRect((int) p0.x, (int) p0.y, (int) p1.x, (int) p1.y);
			g.setColor(FakeGraphics.RED);
			g.drawLine(0, 0, (int) p2.x, (int) p2.y);

			P.delete(p0);
			P.delete(p1);
			P.delete(p2);
		}
		for (EPart e : order) {
			P p = P.newP(siz, siz);
			e.drawPart(g, p);
			P.delete(p);
		}
		if (sele >= 0 && sele < ent.length) {
			P p = P.newP(siz, siz);
			ent[sele].drawScale(g, p);
			P.delete(p);
		}

		g.setTransform(at);
		g.delete(at);
	}

	/** make this animation a component of another, used in warp and kb */
	public void paraTo(EAnimD base) {
		if (base == null)
			ent[0].setPara(null);
		else
			ent[0].setPara(base.ent[1]);
	}

}
