package common.util.pack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.CommonStatic;
import common.CommonStatic.ImgReader;
import common.CommonStatic.ImgWriter;
import common.io.InStream;
import common.io.OutStream;
import common.system.FixIndexList;
import common.system.VImg;
import common.util.Data;

public class BGStore extends FixIndexList<Background> {

	public static List<Background> getAll(Pack p) {
		List<Background> ans = new ArrayList<>();
		if (p != null) {
			ans.addAll(p.bg.getList());
			for (int rel : p.rely)
				ans.addAll(Pack.map.get(rel).bg.getList());
		} else
			for (Pack pac : Pack.map.values())
				ans.addAll(pac.bg.getList());
		return ans;
	}

	public static Background getBG(int ind) {
		int pid = ind / 1000;
		Pack pack = Pack.map.get(pid);
		if (pack == null)
			return null;
		return pack.bg.get(ind % 1000);
	}

	private Pack pack;

	protected BGStore(Pack p) {
		super(new Background[1000]);
		pack = p;
	}

	public Background add(VImg img) {
		int id = nextInd();
		String name = pack.id + Data.trio(id);
		img.name = name;
		Background bg = new Background(pack, img, id);
		add(bg);
		return bg;
	}

	public int getID(Background img) {
		int ind = indexOf(img);
		if (ind < 0 || img == null)
			ind = 0;
		return pack.id * 1000 + ind;
	}

	public String nameOf(Background img) {
		return Data.trio(indexOf(img));
	}

	@Override
	public String toString() {
		return pack.toString();
	}

	protected OutStream packup(ImgWriter w) {
		OutStream os = OutStream.getIns();
		os.writeString("0.4.0");
		Map<Integer, Background> mbg = getMap();
		os.writeInt(mbg.size());
		for (int ind : mbg.keySet()) {
			os.writeInt(ind);
			ImgWriter.writeImg(os, w, mbg.get(ind).img.getImg());
			Background bg = mbg.get(ind);
			os.writeInt(bg.top ? 1 : 0);
			os.writeInt(bg.ic);
			for (int[] c : bg.cs)
				os.writeInt(c[0] << 16 | c[1] << 8 | c[2]);
		}
		os.terminate();
		return os;
	}

	protected OutStream write() {
		OutStream os = OutStream.getIns();
		os.writeString("0.4.0");
		Map<Integer, Background> mbg = getMap();
		os.writeInt(mbg.size());
		for (int ind : mbg.keySet()) {
			os.writeInt(ind);
			Background bg = mbg.get(ind);
			os.writeInt(bg.top ? 1 : 0);
			os.writeInt(bg.ic);
			for (int[] c : bg.cs)
				os.writeInt(c[0] << 16 | c[1] << 8 | c[2]);
		}
		os.terminate();
		return os;
	}

	protected void zreadp(int ver, InStream is, ImgReader r) {
		if (ver >= 309)
			ver = getVer(is.nextString());
		if (ver >= 400)
			zreadp$000400(is, r);
		else if (ver >= 306)
			is.nextInt();
	}

	protected void zreadt(int ver, InStream is) {
		if (ver >= 309)
			ver = getVer(is.nextString());
		if (ver >= 400)
			zreadt$000400(is);
		else if (ver >= 309)
			;
		else if (ver >= 306)
			is.nextInt();
	}

	private void zreadp$000400(InStream is, ImgReader r) {
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int ind = is.nextInt();
			VImg vimg = ImgReader.readImg(is, r);
			vimg.name = Data.trio(ind);
			Background bg = new Background(pack, vimg, ind);
			set(ind, bg);
			bg.top = is.nextInt() > 0;
			bg.ic = is.nextInt();
			for (int j = 0; j < 4; j++) {
				int p = is.nextInt();
				bg.cs[j] = new int[] { p >> 8 & 255, p >> 8 & 255, p & 255 };
			}
		}
	}

	private void zreadt$000400(InStream is) {
		File f = CommonStatic.def.route("./res/img/" + pack.id + "/bg/");
		if (f.exists()) {
			File[] fs = f.listFiles();
			for (File fi : fs) {
				String str = fi.getName();
				if (str.length() != 7)
					continue;
				if (!str.endsWith(".png"))
					continue;
				int val = -1;
				try {
					val = Integer.parseInt(str.substring(0, 3));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
				VImg bimg = CommonStatic.def.readReal(fi);
				if (val >= 0 && bimg != null)
					set(val, new Background(pack, bimg, val));
			}
		}
		int n = is.nextInt();
		for (int i = 0; i < n; i++) {
			int ind = is.nextInt();
			Background bg = get(ind);
			if (bg == null)
				continue;
			bg.top = is.nextInt() > 0;
			bg.ic = is.nextInt();
			for (int j = 0; j < 4; j++) {
				int p = is.nextInt();
				bg.cs[j] = new int[] { p >> 16 & 255, p >> 8 & 255, p & 255 };
			}
		}
	}

}