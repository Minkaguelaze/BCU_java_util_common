package common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Queue;

public class Data {

	public static final int restrict_name = 32;

	public static final int SE_HIT_0 = 20;
	public static final int SE_HIT_1 = 21;
	public static final int SE_DEATH_0 = 23;
	public static final int SE_DEATH_1 = 24;
	public static final int SE_HIT_BASE = 22;
	public static final int SE_ZKILL = 59;
	public static final int SE_CRIT = 44;
	public static final int SE_SATK = 90;
	public static final int SE_WAVE = 26;
	public static final int SE_LETHAL = 50;
	public static final int SE_WARP_ENTER = 73;
	public static final int SE_WARP_EXIT = 74;
	public static final int SE_BOSS = 45;
	public static final int SE_SPEND_FAIL = 15;// TODO
	public static final int SE_SPEND_SUC = 19;// TODO
	public static final int SE_SPEND_REF = 27;// TODO
	public static final int SE_BARRIER_ABI = 70;
	public static final int SE_BARRIER_NON = 71;
	public static final int SE_BARRIER_ATK = 72;
	public static final int SE_POISON = 110;
	public static final int SE_VOLC_START = 111;
	public static final int SE_VOLC_LOOP = 112;
	public static final int[][] SE_CANNON = { { 25, 26 }, { 60 }, { 61 }, { 36, 37 }, { 65, 83 }, { 84, 85 }, { 86 } };

	public static final int[] SE_ALL = { 15, 19, 20, 21, 22, 23, 24, 25, 26, 27, 36, 37, 44, 45, 50, 59, 60, 61, 65, 73,
			74, 83, 84, 85, 86, 90, 110, 111, 112 };

	public static final int RARITY_TOT = 6;

	// trait bit filter
	public static final int TB_WHITE = 1;
	public static final int TB_RED = 2;
	public static final int TB_FLOAT = 4;
	public static final int TB_BLACK = 8;
	public static final int TB_METAL = 16;
	public static final int TB_ANGEL = 32;
	public static final int TB_ALIEN = 64;
	public static final int TB_ZOMBIE = 128;
	public static final int TB_RELIC = 256;
	public static final int TB_EVA = 512;
	public static final int TB_WITCH = 1024;
	public static final int TB_INFH = 2048;

	// trait index
	public static final int TRAIT_WHITE = 0;
	public static final int TRAIT_RED = 1;
	public static final int TRAIT_FLOAT = 2;
	public static final int TRAIT_BLACK = 3;
	public static final int TRAIT_METAL = 4;
	public static final int TRAIT_ANGEL = 5;
	public static final int TRAIT_ALIEN = 6;
	public static final int TRAIT_ZOMBIE = 7;
	public static final int TRAIT_RELIC = 8;
	public static final int TRAIT_EVA = 9;
	public static final int TRAIT_WITCH = 10;
	public static final int TRAIT_INFH = 11;
	public static final int TRAIT_TOT = 12;

	// treasure
	public static final int T_RED = 0;
	public static final int T_FLOAT = 1;
	public static final int T_BLACK = 2;
	public static final int T_ANGEL = 3;
	public static final int T_METAL = 4;
	public static final int T_ALIEN = 5;
	public static final int T_ZOMBIE = 6;

	// default tech value
	public static final int[] MLV = new int[] { 30, 30, 30, 30, 30, 30, 30, 10, 30 };

	// tech index
	public static final int LV_RES = 0;
	public static final int LV_ACC = 1;
	public static final int LV_BASE = 2;
	public static final int LV_WORK = 3;
	public static final int LV_WALT = 4;
	public static final int LV_RECH = 5;
	public static final int LV_CATK = 6;
	public static final int LV_CRG = 7;
	public static final int LV_XP = 8;
	public static final int LV_TOT = 9;

	// default treasure value
	public static final int[] MT = new int[] { 300, 300, 300, 300, 300, 300, 600, 600, 600, 300, 300 };

	// treasure index
	public static final int T_ATK = 0;
	public static final int T_DEF = 1;
	public static final int T_RES = 2;
	public static final int T_ACC = 3;
	public static final int T_WORK = 4;
	public static final int T_WALT = 5;
	public static final int T_RECH = 6;
	public static final int T_CATK = 7;
	public static final int T_BASE = 8;
	public static final int T_XP1 = 9;
	public static final int T_XP2 = 10;
	public static final int T_TOT = 11;

	// abi bit filter
	public static final int AB_GOOD = 1 << 0;
	public static final int AB_RESIST = 1 << 1;
	public static final int AB_MASSIVE = 1 << 2;
	public static final int AB_ONLY = 1 << 3;
	public static final int AB_EARN = 1 << 4;
	public static final int AB_BASE = 1 << 5;
	public static final int AB_METALIC = 1 << 6;
	public static final int AB_MOVEI = 1 << 7;
	public static final int AB_WAVES = 1 << 8;
	public static final int AB_SNIPERI = 1 << 9;
	public static final int AB_TIMEI = 1 << 10;
	public static final int AB_GHOST = 1 << 11;
	public static final int AB_POII = 1 << 12;
	public static final int AB_ZKILL = 1 << 13;
	public static final int AB_WKILL = 1 << 14;
	public static final int AB_GLASS = 1 << 15;
	public static final int AB_THEMEI = 1 << 16;
	public static final int AB_EKILL = 1 << 17;
	public static final int AB_SEALI = 1 << 18;
	public static final int AB_IMUSW = 1 << 19;
	public static final int AB_RESISTS = 1 << 20;
	public static final int AB_MASSIVES = 1 << 21;

	// 0111 1010 1110 0001 0111 1111
	@Deprecated
	public static final int AB_ELIMINATOR = 0x7ae17f;

	// abi index
	public static final int ABI_GOOD = 0;
	public static final int ABI_RESIST = 1;
	public static final int ABI_MASSIVE = 2;
	public static final int ABI_ONLY = 3;
	public static final int ABI_EARN = 4;
	public static final int ABI_BASE = 5;
	public static final int ABI_METALIC = 6;
	public static final int ABI_MOVEI = 7;
	public static final int ABI_WAVES = 8;
	public static final int ABI_SNIPERI = 9;
	public static final int ABI_TIMEI = 10;
	public static final int ABI_GHOST = 11;
	public static final int ABI_POII = 12;
	public static final int ABI_ZKILL = 13;
	public static final int ABI_WKILL = 14;
	public static final int ABI_GLASS = 15;
	public static final int ABI_THEMEI = 16;
	public static final int ABI_EKILL = 17;
	public static final int ABI_SEALI = 18;
	public static final int ABI_IMUSW = 19;
	public static final int ABI_RESISTS = 20;
	public static final int ABI_MASSIVES = 21;
	public static final int ABI_TOT = 30;// 20 currently

	// proc index
	public static final int P_KB = 0;
	public static final int P_STOP = 1;
	public static final int P_SLOW = 2;
	public static final int P_CRIT = 3;
	public static final int P_WAVE = 4;
	public static final int P_WEAK = 5;
	public static final int P_BREAK = 6;
	public static final int P_WARP = 7;
	public static final int P_CURSE = 8;
	public static final int P_STRONG = 9;
	public static final int P_LETHAL = 10;
	public static final int P_BURROW = 11;
	/**
	 * body proc: 0: add revive time for zombies, -1 to make it infinite, revivable
	 * zombies only 1: revive time 2: revive health 3: point 1 4: point 2 5: type:
	 * 0/1/2/3: duration: in range and normal/in range/ master lifetime/permanent
	 * +4: make Z-kill unusable +8: revive non-zombie also +16: applicapable to
	 * others
	 */
	public static final int P_REVIVE = 12;
	public static final int P_IMUKB = 13;
	public static final int P_IMUSTOP = 14;
	public static final int P_IMUSLOW = 15;
	public static final int P_IMUWAVE = 16;
	public static final int P_IMUWEAK = 17;
	public static final int P_IMUWARP = 18;
	public static final int P_IMUCURSE = 19;
	public static final int P_SNIPER = 20;
	public static final int P_TIME = 21;
	public static final int P_SEAL = 22;
	/** 0:prob, 1:ID, 2:location, 3: buff, 4:conf, 5:time */
	/**
	 * +0: direct, +1: warp, +2:burrow, +4:disregard limit, +8: fix buff, +16: same
	 * health, +32: diff layer, +64 on hit, +128 on kill
	 */
	public static final int P_SUMMON = 23;
	/**
	 * 0:prob, 1:speed, 2:width (left to right), 3:time, 4:origin (center), 5:itv
	 */
	public static final int P_MOVEWAVE = 24;
	/** 0:prob, 1:time (-1 means infinite), 2:ID */
	public static final int P_THEME = 25;
	/**
	 * 0:prob, 1:time, 2:dmg, 3:itv, 4: conf +0: normal, +1: of total, +2: of
	 * current, +3: of lost, +4: unstackable
	 */
	public static final int P_POISON = 26;
	public static final int P_BOSS = 27;
	/**
	 * body proc: 1: type: protect itself only (0) or effect the attack also (1)
	 */
	public static final int P_CRITI = 28;
	public static final int P_SATK = 29;
	public static final int P_IMUATK = 30;
	/** official poison */
	public static final int P_POIATK = 31;
	public static final int P_VOLC = 32;
	public static final int P_IMUPOIATK = 33;

	public static final int PROC_TOT = 40;// 32

	public static final int PROC_WIDTH = 6;
	public static final int WT_WAVE = 1;
	public static final int WT_MOVE = 2;
	public static final int WT_CANN = 2;
	public static final int PC_P = 0, PC_AB = 1, PC_BASE = 2, PC_IMU = 3, PC_TRAIT = 4;

	public static final int PC2_HP = 0;
	public static final int PC2_ATK = 1;
	public static final int PC2_SPEED = 2;
	public static final int PC2_COST = 3;
	public static final int PC2_CD = 4;
	public static final int[][] PC_CORRES = new int[][] { // NP value table
			{ -1, 0 }, // 0:
			{ 0, P_WEAK }, // 1: weak, reversed health
			{ 0, P_STOP }, // 2: stop
			{ 0, P_SLOW }, // 3: slow
			{ 1, AB_ONLY, 0 }, // 4:
			{ 1, AB_GOOD, 0 }, // 5:
			{ 1, AB_RESIST, 0 }, // 6:
			{ 1, AB_MASSIVE, 0 }, // 7:
			{ 0, P_KB }, // 8: kb
			{ 0, P_WARP, 0 }, // 9:
			{ 0, P_STRONG }, // 10: berserker, reversed health
			{ 0, P_LETHAL }, // 11: lethal
			{ 1, AB_BASE, 0 }, // 12:
			{ 0, P_CRIT }, // 13: crit
			{ 1, AB_ZKILL }, // 14: zkill
			{ 0, P_BREAK }, // 15: break
			{ 1, AB_EARN }, // 16: 2x income
			{ 0, P_WAVE }, // 17: wave
			{ 0, P_IMUWEAK }, // 18: res weak
			{ 0, P_IMUSTOP }, // 19: res stop
			{ 0, P_IMUSLOW }, // 20: res slow
			{ 0, P_IMUKB }, // 21: res kb
			{ 0, P_IMUWAVE }, // 22: res wave
			{ 1, AB_WAVES, 0 }, // 23:
			{ 0, P_IMUWARP, 0 }, // 24:
			{ 2, PC2_COST }, // 25: reduce cost
			{ 2, PC2_CD }, // 26: reduce cooldown
			{ 2, PC2_SPEED }, // 27: inc speed
			{ -1, 0 }, // 28:
			{ 3, P_IMUCURSE }, // 29: imu curse
			{ 0, P_IMUCURSE }, // 30: res curse
			{ 2, PC2_ATK }, // 31: inc ATK
			{ 2, PC2_HP }, // 32: inc HP
			{ 4, TB_RED, 0 }, // 33:
			{ 4, TB_FLOAT, 0 }, // 34:
			{ 4, TB_BLACK, 0 }, // 35:
			{ 4, TB_METAL, 0 }, // 36:
			{ 4, TB_ANGEL }, // 37: targeting angle
			{ 4, TB_ALIEN }, // 38: targeting alien
			{ 4, TB_ZOMBIE }, // 39: targeting zombie
			{ 4, TB_RELIC }, // 40: targeting relic
			{ 4, TB_WHITE, 0 }, // 41:
			{ -1, 0 }, // 42:
			{ -1, 0 }, // 43:
			{ -1, 0 }, // 44:
			{ -1, 0 }, // 45:
			{ -1, 0 }, // 46:
			{ -1, 0 }, // 47:
			{ 3, P_IMUWAVE }, // 48: immune to wave
			{ 3, P_IMUWARP }, // 49: immune to warp
			{ -1, 0 }, // 50:
			{ 0, P_IMUATK }, // 51: immune to attack
			{ -1, 0 } };

	// foot icon index used in battle
	public static final int INV = -1;
	public static final int INVWARP = -2;
	public static final int STPWAVE = -3;
	public static final int BREAK_ABI = -4;
	public static final int BREAK_ATK = -5;
	public static final int BREAK_NON = -6;
	// Combo index
	public static final int C_ATK = 0;
	public static final int C_DEF = 1;
	public static final int C_SPE = 2;
	public static final int C_GOOD = 14;
	public static final int C_MASSIVE = 15;
	public static final int C_RESIST = 16;
	public static final int C_KB = 17;
	public static final int C_SLOW = 18;
	public static final int C_STOP = 19;
	public static final int C_WEAK = 20;
	public static final int C_STRONG = 21;
	public static final int C_WKILL = 22;
	public static final int C_EKILL = 23;
	public static final int C_CRIT = 24;
	public static final int C_C_INI = 3;
	public static final int C_C_ATK = 6;
	public static final int C_C_SPE = 7;
	public static final int C_BASE = 10;
	public static final int C_M_INI = 5;
	public static final int C_M_LV = 4;
	public static final int C_M_MAX = 9;
	public static final int C_RESP = 11;
	public static final int C_MEAR = 12;
	public static final int C_XP = 13;// abandoned
	public static final int C_TOT = 25;

	// Effects Anim index
	public static final int A_KB = 29;
	public static final int A_CRIT = 28;
	public static final int A_SHOCKWAVE = 27;
	public static final int A_ZOMBIE = 26;
	public static final int A_EFF_INV = 18;
	public static final int A_EFF_DEF = 19;// unused
	public static final int A_Z_STRONG = 20;
	public static final int A_B = 21;
	public static final int A_E_B = 22;
	public static final int A_W = 23;
	public static final int A_W_C = 24;
	public static final int A_CURSE = 25;
	public static final int A_DOWN = 0;
	public static final int A_UP = 2;
	public static final int A_SLOW = 4;
	public static final int A_STOP = 6;
	public static final int A_SHIELD = 8;
	public static final int A_FARATTACK = 10;
	public static final int A_WAVE_INVALID = 12;
	public static final int A_WAVE_STOP = 14;
	public static final int A_WAVEGUARD = 16;// unused
	public static final int A_E_DOWN = 1;
	public static final int A_E_UP = 3;
	public static final int A_E_SLOW = 5;
	public static final int A_E_STOP = 7;
	public static final int A_E_SHIELD = 9;
	public static final int A_E_FARATTACK = 11;
	public static final int A_E_WAVE_INVALID = 13;
	public static final int A_E_WAVE_STOP = 15;
	public static final int A_E_WAVEGUARD = 17;// unused
	public static final int A_SNIPER = 30;
	public static final int A_U_ZOMBIE = 31;
	public static final int A_U_B = 32;
	public static final int A_U_E_B = 33;
	public static final int A_SEAL = 34;
	public static final int A_POI0 = 35;
	public static final int A_POI1 = 36;
	public static final int A_POI2 = 37;
	public static final int A_POI3 = 38;
	public static final int A_POI4 = 39;
	public static final int A_POI5 = 40;
	public static final int A_POI6 = 41;
	public static final int A_POI7 = 42;
	public static final int A_SATK = 43;
	public static final int A_IMUATK = 44;
	public static final int A_POISON = 45;
	public static final int A_VOLC = 46;
	public static final int A_E_VOLC = 47;

	public static final int[] A_POIS = { A_POI0, A_POI1, A_POI2, A_POI3, A_POI4, A_POI5, A_POI6, A_POI7 };
	public static final int A_TOT = 48;

	// atk type index used in filter page
	public static final int ATK_SINGLE = 0;
	public static final int ATK_AREA = 1;
	public static final int ATK_LD = 2;
	public static final int ATK_OMNI = 4;
	public static final int ATK_TOT = 6;

	// base and canon level
	public static final int BASE_H = 0;
	public static final int BASE_SLOW = 1;
	public static final int BASE_WALL = 2;
	public static final int BASE_STOP = 3;
	public static final int BASE_WATER = 4;
	public static final int BASE_GROUND = 5;
	public static final int BASE_BARRIER = 6;
	public static final int BASE_TOT = 7;

	// touchable ID
	public static final int TCH_N = 1;
	public static final int TCH_KB = 2;
	public static final int TCH_UG = 4;
	public static final int TCH_CORPSE = 8;
	public static final int TCH_SOUL = 16;
	public static final int TCH_EX = 32;
	public static final int TCH_ZOMBX = 64;
	public static final String[] A_PATH = new String[] { "down", "up", "slow", "stop", "shield", "farattack",
			"wave_invalid", "wave_stop", "waveguard" };

	// After this line all number is game data

	public static final int INT_KB = 0, INT_HB = 1, INT_SW = 2, INT_ASS = 3, INT_WARP = 4;

	public static final int[] KB_PRI = new int[] { 2, 4, 5, 1, 3 };
	public static final int[] KB_TIME = new int[] { 11, 23, 47, 11, -1 };
	public static final int[] KB_DIS = new int[] { 165, 345, 705, 55, -1 };

	public static final int W_E_INI = -33;
	public static final int W_U_INI = -67;
	public static final int W_PROG = 200;
	public static final int W_E_WID = 500;
	public static final int W_U_WID = 400;
	public static final int W_TIME = 3;
	public static final int E_IMU = -1;
	public static final int E_IWAVE = -2;
	public static final int E_SWAVE = -3;

	public static final int W_VOLC = 375;
	public static final int W_VOLC_INNER = 250; // volcano inner width
	public static final int W_VOLC_PIERCE = 125; // volcano pierce width
	public static final int VOLC_ITV = 20;

	public static final int VOLC_PRE = 15; // volcano pre-atk
	public static final int VOLC_POST = 10; // volcano post-atk

	public static final int VOLC_SE = 30; // volcano se loop duration

	public static final int[] NYPRE = new int[] { 18, 2, -1, 28, 37, 18, 10 };// not sure
	public static final int[] NYRAN = new int[] { 710, 600, -1, 500, 500, 710, 100 };// not sure
	public static final int SNIPER_CD = 300;// not sure
	public static final int SNIPER_PRE = 5;// not sure
	public static final int SNIPER_POS = -500;// not sure
	public static final int REVIVE_SHOW_TIME = 16;

	private static final Deque<int[][]> queue = new ArrayDeque<>();

	public static final String[] SUFX = new String[] { "f", "c", "s" };

	public static int[][] get() {
		if (queue.size() > 0) {
			int[][] ans = queue.pop();
			for (int[] a : ans)
				Arrays.fill(a, 0);
			return ans;
		}
		return new int[PROC_TOT][PROC_WIDTH];
	}

	public static int getVer(String ver) {
		int ans = 0;
		String[] strs = ver.split("\\.");
		for (String str : strs) {
			ans *= 100;
			ans += Integer.parseInt(str);
		}
		return ans;
	}

	public static String hex(int id) {
		return trio(id / 1000) + trio(id % 1000);
	}

	public static Queue<String> readLine(File f) {
		try {
			return new ArrayDeque<String>(Files.readAllLines(f.toPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String restrict(String str) {
		if (str.length() < restrict_name)
			return str;
		return str.substring(0, restrict_name);
	}

	public static void ret(int[][] proc) {
		queue.push(proc);
	}

	public static String revVer(int ver) {
		return ver / 1000000 % 100 + "-" + ver / 10000 % 100 + "-" + ver / 100 % 100 + "-" + ver % 100;
	}

	public static void set(int[] dst, int[] src) {
		for (int i = 0; i < Math.min(dst.length, src.length); i++)
			dst[i] = src[i];
	}

	public static String trio(int i) {
		i %= 1000;
		String str = "";
		if (i < 100)
			str += "0";
		if (i < 10)
			str += "0";
		return str + i;
	}

}
