package common.util.stage;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import common.battle.StageBasis;
import common.io.InStream;
import common.io.OutStream;
import common.system.Copable;
import common.system.FixIndexList;
import common.util.Data;
import common.util.pack.Pack;
import common.util.unit.AbEnemy;
import common.util.unit.Enemy;
import common.util.unit.EnemyStore;

public class SCDef implements Copable<SCDef> {

	public static final int SIZE = 13;

	public static final int E = 0, N = 1, S0 = 2, R0 = 3, R1 = 4, C0 = 5, L0 = 6, L1 = 7, B = 8, M = 9, S1 = 10,
			C1 = 11, G = 12;

	public static SCDef zread(InStream is) {
		int t = is.nextInt();
		int ver = Data.getVer(is.nextString());
		if (t == 0) {
			if (ver >= 402) {
				int n = is.nextInt();
				int m = is.nextInt();
				SCDef scd = new SCDef(n);
				for (int i = 0; i < n; i++)
					for (int j = 0; j < m; j++)
						scd.datas[i][j] = is.nextInt();
				scd.sdef = is.nextInt();
				n = is.nextInt();
				for (int i = 0; i < n; i++)
					scd.smap.put(is.nextInt(), is.nextInt());
				n = is.nextInt();
				for (int i = 0; i < n; i++) {
					SCGroup scg = SCGroup.zread(is);
					scd.sub.set(scg.id, scg);
				}
				return scd;
			}
			if (ver >= 401) {
				int n = is.nextInt();
				int m = is.nextInt();
				SCDef scd = new SCDef(n);
				for (int i = 0; i < n; i++)
					for (int j = 0; j < m; j++)
						scd.datas[i][j] = is.nextInt();
				scd.sdef = is.nextInt();
				n = is.nextInt();
				for (int i = 0; i < n; i++)
					scd.smap.put(is.nextInt(), is.nextInt());
				n = is.nextInt();
				int id;
				for (int i = 0; i < n; i++)
					scd.sub.set(id = is.nextInt(), new SCGroup(id, is.nextInt()));
				return scd;
			} else if (ver >= 400) {
				int n = is.nextInt();
				int m = is.nextInt();
				SCDef scd = new SCDef(n);
				for (int i = 0; i < n; i++)
					for (int j = 0; j < m; j++)
						scd.datas[i][j] = is.nextInt();
				return scd;
			}
		}
		return null;
	}

	public int[][] datas;

	public final FixIndexList<SCGroup> sub = new FixIndexList<>(new SCGroup[1000]);
	public final Map<Integer, Integer> smap = new TreeMap<>();
	public int sdef = 0;

	protected SCDef(InStream is, int ver) {
		if (ver >= 305) {
			int n = is.nextByte();
			datas = new int[n][SIZE];
			for (int i = 0; i < n; i++)
				for (int j = 0; j < 10; j++)
					datas[i][j] = is.nextInt();
		} else if (ver >= 203) {
			int n = is.nextByte();
			datas = new int[n][SIZE];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < 10; j++)
					datas[i][j] = is.nextInt();
				if (datas[i][5] < 100)
					datas[i][2] *= -1;
			}
		} else
			datas = new int[0][SIZE];
	}

	protected SCDef(int s) {
		datas = new int[s][SIZE];
	}

	public int allow(StageBasis sb, AbEnemy e) {
		Integer o = smap.get(e.getID());
		o = o == null ? sdef : o;
		if (allow(sb, o))
			return o;
		return -1;
	}

	public boolean allow(StageBasis sb, int val) {
		if (sb.entityCount(1) >= sb.st.max)
			return false;
		if (val < 0 || val > 1000 || sub.get(val) == null)
			return true;
		SCGroup g = sub.get(val);
		return sb.entityCount(1, val) < g.getMax(sb.est.star);
	}

	public boolean contains(Enemy e) {
		for (int[] dat : datas)
			if (dat[E] == e.id)
				return true;
		return false;
	}

	@Override
	public SCDef copy() {
		SCDef ans = new SCDef(datas.length);
		for (int i = 0; i < datas.length; i++)
			ans.datas[i] = datas[i].clone();
		ans.sdef = sdef;
		smap.forEach((e, i) -> ans.smap.put(e, i));
		sub.forEach((i, e) -> ans.sub.set(i, e.copy(i)));
		return ans;
	}

	public Set<Enemy> getAllEnemy() {
		Set<Enemy> l = new TreeSet<>();
		for (int[] dat : datas)
			l.addAll(EnemyStore.getAbEnemy(dat[E], false).getPossible());
		for (AbEnemy e : getSummon())
			l.addAll(e.getPossible());
		return l;
	}

	public int[][] getSimple() {
		return datas;
	}

	public int[] getSimple(int i) {
		return datas[i];
	}

	public int[][] getSMap() {
		int[][] ans = new int[smap.size()][2];
		int[] i = new int[1];
		smap.forEach((e, g) -> {
			ans[i[0]][0] = e;
			ans[i[0]++][1] = g;
		});
		return ans;
	}

	public Set<AbEnemy> getSummon() {
		Set<AbEnemy> ans = new TreeSet<>();
		Set<AbEnemy> temp = new TreeSet<>();
		Set<Enemy> pre = new TreeSet<>();
		Set<Enemy> post = new TreeSet<>();
		for (int[] line : datas) {
			AbEnemy e = EnemyStore.getAbEnemy(line[E], false);
			if (e != null)
				pre.addAll(e.getPossible());
		}
		while (pre.size() > 0) {
			for (Enemy e : pre)
				temp.addAll(e.de.getSummon());
			ans.addAll(temp);
			post.addAll(pre);
			pre.clear();
			for (AbEnemy e : temp)
				pre.addAll(e.getPossible());
			pre.removeAll(post);
			temp.clear();
		}
		return ans;
	}

	public boolean isSuitable(Pack p) {
		for (int[] ints : datas) {
			if (ints[E] < 1000)
				continue;
			int pac = ints[E] / 1000;
			boolean b = pac == p.id;
			for (int rel : p.rely)
				b |= pac == rel;
			if (!b)
				return false;
		}
		return true;
	}

	public boolean isTrail() {
		for (int[] data : datas)
			if (data[C0] > 100)
				return true;
		return false;
	}

	public void merge(int id, int pid, int[] esind) {
		for (int[] dat : datas)
			if (dat[E] / 1000 == pid)
				dat[E] = esind[dat[E] % 1000] + id * 1000;
	}

	public int relyOn(int p) {
		for (int[] data : datas)
			if (data[E] / 1000 == p)
				return Pack.RELY_ENE;
		return -1;
	}

	public void removePack(int p) {
		for (int[] data : datas)
			if (data[E] / 1000 == p)
				data[E] = 0;
	}

	public OutStream write() {
		OutStream os = OutStream.getIns();
		os.writeInt(0);
		os.writeString("0.4.2");
		os.writeInt(datas.length);
		os.writeInt(SIZE);
		for (int i = 0; i < datas.length; i++)
			for (int j = 0; j < SIZE; j++)
				os.writeInt(datas[i][j]);
		os.writeInt(sdef);
		os.writeInt(smap.size());
		smap.forEach((e, i) -> os.writeIntsN(e, i));
		os.writeInt(sub.size());
		sub.forEach((i, e) -> e.write(os));
		os.terminate();
		return os;
	}

}
