package home.mutant.dl.gaussian;

public class Globals {
	public enum FILL_TYPE{UNIFORM, ROUND, SQUARE}
	public enum OFFSET_TYPE{MOST_FREQUENT, MIDDLE, LEAST_FREQUENT}
	public static final int NO_TRAIN = 6000;
	public static final int NO_TEST = 1000;
	public static int SUBIMG_SIZE = 25;
	public static int NO_COLUMNS = 200;
	public static FILL_TYPE FILL = FILL_TYPE.SQUARE;
	public static double SIMILARITY = 0.68;
	public static final double RADIUS = 3;
	public static int MAX_PIXEL_VALUE = 255;
	public static int SUB_LIST_NEURONS = 1600;
	public static OFFSET_TYPE OFFSET = OFFSET_TYPE.MIDDLE;
}
