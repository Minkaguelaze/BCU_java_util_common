package common.battle.data;

import java.util.Queue;

import common.CommonStatic;
import common.system.files.VFile;
import common.util.Data;
import common.util.unit.UnitStore;
import main.Opts;

public class PCoin extends Data {

	public static void read() {
		Queue<String> qs = VFile.readLine("./org/data/SkillAcquisition.csv");
		qs.poll();
		for (String str : qs) {
			String[] strs = str.trim().split(",");
			if (strs.length == 62)
				new PCoin(strs);
		}
	}

	private final int id;
	private final DataUnit du;

	public final DataUnit full;
	public final int[] max;
	public final int[][] info = new int[5][12];

	private PCoin(String[] strs) {
		id = CommonStatic.parseIntN(strs[0]);
		max = new int[6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 12; j++)
				info[i][j] = CommonStatic.parseIntN(strs[2 + i * 12 + j]);
			max[i + 1] = info[i][1];
			if (max[i + 1] == 0)
				max[i + 1] = 1;
		}
		du = (DataUnit) UnitStore.get(id, 2, false).du;
		du.pcoin = this;
		full = improve(max);
	}

	public DataUnit improve(int[] lvs) {
		DataUnit ans = du.clone();
		for (int i = 0; i < 5; i++) {
			if (lvs[i + 1] == 0)
				continue;
			if (info[i][0] >= PC_CORRES.length) {
				Opts.pop("warning: new PCoin ability not yet included in BCU: " + info[i][0], "warning");
				continue;
			}
			int[] type = PC_CORRES[info[i][0]];
			if (type.length > 2 || type[0] == -1) {
				Opts.pop("warning: new PCoin ability not yet included in BCU: " + info[i][0], "warning");
				continue;
			}

			int maxlv = info[i][1];
			int[] modifs = new int[4];
			if (maxlv > 1) {
				for (int j = 0; j < 4; j++) {
					int v0 = info[i][2 + j * 2];
					int v1 = info[i][3 + j * 2];
					modifs[j] = (v1 - v0) * (lvs[i + 1] - 1) / (maxlv - 1) + v0;
				}
			}
			if (maxlv == 0)
				for (int j = 0; j < 4; j++)
					modifs[j] = info[i][3 + j * 2];

			if (type[0] == PC_P) {
				int[] tar = ans.proc[type[1]];
				for (int j = 0; j < Math.min(tar.length, 4); j++)
					tar[j] += modifs[j];
				if (type[1] == P_STRONG)
					tar[0] = 100 - tar[0];
				if (type[1] == P_WEAK)
					tar[2] = 100 - tar[2];
			} else if (type[0] == PC_AB)
				ans.abi |= type[1];
			else if (type[0] == PC_BASE)
				if (type[1] == PC2_HP)
					ans.hp *= 1 + modifs[0] * 0.01;
				else if (type[1] == PC2_ATK) {
					double atk = 1 + modifs[0] * 0.01;
					ans.atk *= atk;
					ans.atk1 *= atk;
					ans.atk2 *= atk;
				} else if (type[1] == PC2_SPEED)
					ans.speed += modifs[0];
				else if (type[1] == PC2_CD)
					ans.respawn -= modifs[0];
				else if (type[1] == PC2_COST)
					ans.price -= modifs[0];
				else
					;
			else if (type[0] == PC_IMU)
				ans.proc[type[1]][0] = 100;
			else if (type[0] == PC_TRAIT)
				ans.type |= type[1];

		}
		return ans;
	}

}
