package common.util.anim;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import common.CommonStatic;
import common.CommonStatic.EditLink;
import common.io.InStream;
import common.io.OutStream;
import common.system.VImg;
import common.system.fake.FakeImage;
import common.system.files.VFile;
import main.MainBCU;
import main.Opts;

public class AnimCE extends AnimCI {

	public static interface AnimLoader {
		public VImg getEdi();

		public ImgCut getIC();

		public MaAnim[] getMA();

		public MaModel getMM();

		public String getName();

		public FakeImage getNum();

		public int getStatus();

		public VImg getUni();
	}

	static class AnimCELoader implements AnimCI.AnimLoader {

		private static VImg optional(String str) {
			VFile<?> fv = VFile.getFile(str);
			if (fv == null)
				return null;
			return new VImg(fv);
		}

		private final String pre, name;

		private AnimCELoader(String str) {
			name = str;
			pre = "./res/anim/" + str + "/";
		}

		@Override
		public VImg getEdi() {
			return optional(pre + "edi.png");
		}

		@Override
		public ImgCut getIC() {
			return ImgCut.newIns(pre + name + ".imgcut");
		}

		@Override
		public MaAnim[] getMA() {
			MaAnim[] anims = new MaAnim[7];
			for (int i = 0; i < 4; i++)
				anims[i] = MaAnim.newIns(pre + name + "0" + i + ".maanim");
			for (int i = 0; i < 3; i++)
				anims[i + 4] = MaAnim.newIns(pre + name + "_zombie0" + i + ".maanim");
			return anims;
		}

		@Override
		public MaModel getMM() {
			return MaModel.newIns(pre + name + ".mamodel");
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public FakeImage getNum() {
			return VFile.getFile(pre + name + ".png").getData().getImg();
		}

		@Override
		public int getStatus() {
			return 0;
		}

		@Override
		public VImg getUni() {
			return optional(pre + "uni.png");
		}

	}

	public static String getAvailable(String str) {
		File folder = CommonStatic.def.route("./res/anim/");
		if (!folder.exists())
			return str;
		File[] fs = CommonStatic.def.route("./res/anim/").listFiles();
		Set<String> strs = new HashSet<>();
		for (int i = 0; i < fs.length; i++)
			strs.add(fs[i].getName());
		while (strs.contains(str))
			str += "'";
		return str;
	}

	private boolean saved = false;
	public int inPool;
	public EditLink link;
	public Stack<History> history = new Stack<>();
	public String prev = "./res/anim/";

	public AnimCE(InStream is, CommonStatic.ImgReader r) {
		super(CommonStatic.def.loadAnim(is, r));
		name = loader.getName();
		inPool = loader.getStatus();
	}

	public AnimCE(String st) {
		super(new AnimCELoader(st));
		inPool = 0;
		name = st;
	}

	public AnimCE(String str, AnimD ori) {
		super(new AnimCELoader(str));
		inPool = 0;
		name = str;
		loaded = true;
		partial = true;
		imgcut = ori.imgcut.clone();
		mamodel = ori.mamodel.clone();
		if (mamodel.confs.length < 1)
			mamodel.confs = new int[2][6];
		anims = new MaAnim[7];
		for (int i = 0; i < 7; i++)
			if (i < ori.anims.length)
				anims[i] = ori.anims[i].clone();
			else
				anims[i] = new MaAnim();
		loader.setNum(ori.getNum());
		parts = imgcut.cut(ori.getNum());
		File f = CommonStatic.def.route(prev + name + "/" + name + ".png");
		CommonStatic.def.check(f);
		try {
			FakeImage.write(ori.getNum(), "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reloImg();
		if (ori instanceof AnimU<?>) {
			AnimU<?> au = (AnimU<?>) ori;
			setEdi(au.getEdi());
			setUni(au.getUni());
		}
		saveIcon();
		saveUni();
		standardize();
		history("initial");
	}

	public void createNew() {
		loaded = true;
		partial = true;
		imgcut = new ImgCut();
		mamodel = new MaModel();
		anims = new MaAnim[7];
		for (int i = 0; i < 7; i++)
			anims[i] = new MaAnim();
		parts = imgcut.cut(getNum());
		history("initial");
	}

	public void delete() {
		CommonStatic.def.delete(CommonStatic.def.route(prev + name + "/"));
	}

	public String getUndo() {
		return history.peek().name;
	}

	public void hardSave(String str) {
		if (prev == null)
			prev = "./res/anim/";
		if (name == null)
			name = AnimCE.getAvailable(MainBCU.validate(str));
		saved = false;
		save();
		saveImg();
		saveIcon();
		saveUni();
	}

	public void ICedited() {
		check();
		parts = imgcut.cut(getNum());
	}

	public boolean isSaved() {
		return saved;
	}

	@Override
	public void load() {
		try {
			super.load();
			history("initial");
		} catch (Exception e) {
			Opts.loadErr("Error in loading custom animation: " + name);
			e.printStackTrace();
			CommonStatic.def.exit(false);
		}
		validate();
	}

	public void merge(AnimCE a, int x, int y) {
		ImgCut ic0 = imgcut;
		ImgCut ic1 = a.imgcut;
		int icn = ic0.n;
		ic0.n += ic1.n;
		ic0.cuts = Arrays.copyOf(ic0.cuts, ic0.n);
		for (int i = 0; i < icn; i++)
			ic0.cuts[i] = ic0.cuts[i].clone();
		ic0.strs = Arrays.copyOf(ic0.strs, ic0.n);
		for (int i = 0; i < ic1.n; i++) {
			int[] data = ic0.cuts[i + icn] = ic1.cuts[i].clone();
			data[0] += x;
			data[1] += y;
			ic0.strs[i + icn] = ic1.strs[i];
		}

		MaModel mm0 = mamodel;
		MaModel mm1 = a.mamodel;
		int mmn = mm0.n;
		mm0.n += mm1.n;
		mm0.parts = Arrays.copyOf(mm0.parts, mm0.n);
		for (int i = 0; i < mmn; i++)
			mm0.parts[i] = mm0.parts[i].clone();
		mm0.strs0 = Arrays.copyOf(mm0.strs0, mm0.n);
		int[] fir = mm0.parts[0];
		for (int i = 0; i < mm1.n; i++) {
			int[] data = mm0.parts[i + mmn] = mm1.parts[i].clone();
			if (data[0] != -1)
				data[0] += mmn;
			else {
				data[0] = 0;
				data[8] = data[8] * 1000 / fir[8];
				data[9] = data[9] * 1000 / fir[9];
				data[4] = data[6] * data[8] / 1000 - fir[6];
				data[5] = data[7] * data[9] / 1000 - fir[7];
			}
			data[2] += icn;
			mm0.strs0[i + mmn] = mm1.strs0[i];
		}

		for (int i = 0; i < 7; i++) {
			MaAnim ma0 = anims[i];
			MaAnim ma1 = a.anims[i];
			int man = ma0.n;
			ma0.n += ma1.n;
			ma0.parts = Arrays.copyOf(ma0.parts, ma0.n);
			for (int j = 0; j < man; j++)
				ma0.parts[j] = ma0.parts[j].clone();
			for (int j = 0; j < ma1.n; j++) {
				Part p = ma0.parts[j + man] = ma1.parts[j].clone();
				p.ints[0] += mmn;
				if (p.ints[1] == 2)
					for (int[] data : p.moves)
						data[1] += icn;
			}
		}
	}

	public void reloImg() {
		setNum(VFile.getFile(prev + name + "/" + name + ".png").getData().getImg());
		ICedited();
	}

	public void renameTo(String str) {
		if (getUni() != null)
			getUni().check();
		if (getEdi() != null)
			getEdi().check();
		CommonStatic.def.delete(CommonStatic.def.route(prev + name + "/"));
		name = str;
		saveImg();
		saveIcon();
		saveUni();
		unSave("rename (not applicapable for undo)");
	}

	public void resize(double d) {
		for (int[] l : imgcut.cuts)
			for (int i = 0; i < l.length; i++)
				l[i] *= d;
		mamodel.parts[0][8] /= d;
		mamodel.parts[0][9] /= d;
		for (int[] l : mamodel.parts) {
			l[4] *= d;
			l[5] *= d;
			l[6] *= d;
			l[7] *= d;
		}
		for (MaAnim ma : anims)
			for (Part p : ma.parts)
				if (p.ints[1] >= 4 && p.ints[1] <= 7)
					for (int[] x : p.moves)
						x[1] *= d;
	}

	public void restore() {
		history.pop();
		InStream is = history.peek().data.translate();
		imgcut.restore(is);
		ICedited();
		mamodel.restore(is);
		int n = is.nextInt();
		anims = new MaAnim[n];
		for (int i = 0; i < n; i++) {
			anims[i] = new MaAnim();
			anims[i].restore(is);
		}
		is = history.peek().mms.translate();
		n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int ind = is.nextInt();
			int val = is.nextInt();
			if (ind >= 0 && ind < mamodel.n)
				mamodel.status.put(mamodel.parts[ind], val);
		}
		saved = false;
	}

	@Override
	public void revert() {
		super.revert();
		unSave("revert");
	}

	public void save() {
		if (!loaded || isSaved() || mismatch)
			return;
		saved = true;
		String pre = prev + name + "/" + name;
		save$g(pre, 0, 0);
		save$g(pre, 1, 0);

		for (int i = 0; i < 4; i++)
			save$g(pre + "0" + i, 2, i);
		if (anims.length == 7)
			for (int i = 0; i < 3; i++)
				save$g(pre + "_zombie0" + i, 2, i + 4);
	}

	public void saveIcon() {
		if (getEdi() == null || getEdi().getImg() == null || prev == null)
			return;
		try {
			File f = CommonStatic.def.route(prev + name + "/edi.png");
			CommonStatic.def.check(f);
			FakeImage.write(getEdi().getImg(), "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveImg() {
		try {
			File f = CommonStatic.def.route(prev + name + "/" + name + ".png");
			CommonStatic.def.check(f);
			if (!FakeImage.write(getNum(), "PNG", f))
				if (Opts.writeErr0(f.getPath()))
					if (FakeImage.write(getNum(), "PNG", f))
						Opts.ioErr("failed to write");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveUni() {
		if (getUni() == null || getUni().getImg() == null)
			return;
		try {
			File f = CommonStatic.def.route(prev + name + "/uni.png");
			CommonStatic.def.check(f);
			FakeImage.write(getUni().getImg(), "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setEdi(VImg uni) {
		loader.setEdi(uni);
	}

	public void setNum(FakeImage fimg) {
		loader.setNum(fimg);
		if (loaded)
			ICedited();
	}

	public void setUni(VImg uni) {
		loader.setUni(uni);
	}

	public void unSave(String str) {
		saved = false;
		history(str);
		if (link != null)
			link.review();
	}

	public void updateStatus() {
		OutStream mms = OutStream.getIns();
		mms.writeInt(mamodel.status.size());
		mamodel.status.forEach((d, s) -> {
			int ind = -1;
			for (int i = 0; i < mamodel.n; i++)
				if (mamodel.parts[i] == d)
					ind = i;
			mms.writeInt(ind);
			mms.writeInt(s);
		});
		mms.terminate();
		history.peek().mms = mms;
	}

	@Override
	protected void partial() {
		super.partial();
		standardize();
	}

	private void history(String str) {
		OutStream os = OutStream.getIns();
		imgcut.write(os);
		mamodel.write(os);
		os.writeInt(anims.length);
		for (MaAnim ma : anims)
			ma.write(os);
		os.terminate();
		History h = new History(str, os);
		history.push(h);
		updateStatus();
	}

	private void save$g(String pre, int type, int para) {
		try {
			save$s(pre, type, para);
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				save$s(pre, type, para);
			} catch (Exception e2) {
				e2.printStackTrace();
				String str = type == 0 ? ".imgcut" : type == 1 ? ".mamodel" : ".maanim";
				Opts.ioErr("cannot save " + pre + str);
			}
		}
	}

	private void save$ic(String pre) throws Exception {
		File f = CommonStatic.def.route(pre + ".imgcut");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		imgcut.write(ps);
		ps.close();
		new ImgCut(readLine(f));
	}

	private void save$ma(String pre, int i) throws Exception {
		File f = CommonStatic.def.route(pre + ".maanim");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		anims[i].write(ps);
		ps.close();
		new MaAnim(readLine(f));
	}

	private void save$mm(String pre) throws Exception {
		File f = CommonStatic.def.route(pre + ".mamodel");
		CommonStatic.def.check(f);
		PrintStream ps = new PrintStream(f, "UTF-8");
		mamodel.write(ps);
		ps.close();
		new MaModel(readLine(f));
	}

	private void save$s(String pre, int type, int para) throws Exception {
		if (type == 0)
			save$ic(pre);
		if (type == 1)
			save$mm(pre);
		if (type == 2)
			save$ma(pre, para);
	}

	private void standardize() {
		if (mamodel.parts.length == 0 || mamodel.confs.length == 0)
			return;
		int[] dat = mamodel.parts[0];
		int[] con = mamodel.confs[0];
		dat[6] -= con[2];
		dat[7] -= con[3];
		con[2] = con[3] = 0;

		int[] std = mamodel.ints;
		for (int[] data : mamodel.parts) {
			data[8] = data[8] * 1000 / std[0];
			data[9] = data[9] * 1000 / std[0];
			data[10] = data[10] * 3600 / std[1];
			data[11] = data[11] * 1000 / std[2];
		}
		for (MaAnim ma : anims)
			for (Part p : ma.parts) {
				if (p.ints[1] >= 8 && p.ints[1] <= 10)
					for (int[] data : p.moves)
						data[1] = data[1] * 1000 / std[0];
				if (p.ints[1] == 11)
					for (int[] data : p.moves)
						data[1] = data[1] * 3600 / std[1];
				if (p.ints[1] == 12)
					for (int[] data : p.moves)
						data[1] = data[1] * 1000 / std[2];
			}
		std[0] = 1000;
		std[1] = 3600;
		std[2] = 1000;
	}

}

class History {

	protected final OutStream data;

	protected final String name;

	protected OutStream mms;

	protected History(String str, OutStream os) {
		name = str;
		data = os;
	}

}
