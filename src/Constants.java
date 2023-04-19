import java.io.FileWriter;
import java.io.IOException;

public class Constants {

    public static int i = 0;
    public static boolean IN_INFO = false;
    public static boolean START_GAME = false;
    public static long TIME_USER_DIED = 0;
    public static long TIME_LAST_BUM;
    public static long TIME_UPGRADE_BOOM = 30000;
    public static boolean IN_SETTING = false;
    public static boolean IN_RECORDS = false;
    public static int TYPE_OF_RECORDS = 0;
    public static boolean MUST_PLAY_MUSIC = true;
    public static boolean MUST_PLAY_SOUND = true;
    public static double SQRT_LEN_AIM = 0.0;
    public static double V_BULLET;
    public static long TIME_DIED_BOOM = 3000;
    public static double DAMAGE_USER_BULLET = 0.3;
    public static double DAMAGE_BOT_BULLET = 0.05;
    public static int GAME_OVER = 0;
    public static int MUSIC_GAME = 0;

    public static long TIME_HIT = 0;
    public static boolean RESTART_GAME = false;
    public static long TIME_LIFE_BACKGROUND_BLOOD = 1000;

    public static long TIME_LIFE_AFTER_DIED = 3000; // время существование следа после смерти пульки
    public static int R = 20;

    public static int COUNT_CELL_DOWN = 13;

    public static double V_POVOROT_PESOK = 0.001;
    public static long TIME_LAST_POVOROT_PESOK = 0;
    public static int SDVIG = 80;
    public static long TIME_LIVE_BOT_AFTER_DIED = 5000;
    public static MyString CNT_DIED_BOTS;
    public static MyString CNT_WAY;

    public static int LEFT_END = 3000;
    public static int DOWN_END = 810;

    public static int SEED = 5254;

    public static double V_NORMAL;
    public static long TIME_STOP = 0;
    public static double V_NORMAL_1 = 3000;

    public static int NUM_GEN_PARALLEL = 20;
    public static int NUM_GEN_VERTICAL = 20;
    public static boolean YES_KID_ON_PRIVIOS_SECTOR = true;
    public static boolean PAUSE_MENU = false;

    public static double V_POLE;
    // скорость заполнения квадратиком

    //10.0
    public static double V_POLE_1 = 10.0;
    public static boolean USER_DIED_FIRST = true;
    public static boolean FLAG_CHIT_BULLET = false;
    public static double V_BOTS;
    public static String TIME_DIED_RESULT;

    //время запуска программы
    public static long TIME_START_PROGRAM;
    public static boolean BOOM_IS_READY = true;

    //время до начала разрушения лабиринта 5000
    public static long TIME_TO_DIED_LABIRINT = 5000;

    //умер ли персоонаж
    public static boolean USER_DIED = false;

    //скорость поворота
    public static double V_ANGLE = 0.1;

    public static int FRAME_WIGHT;
    public static int FRAME_HEIGHT;
    public static int SIZE_BULLET;

    public static boolean DEVELORER = false;
    public static void writeConstants() throws IOException {
        FileWriter writer = new FileWriter("files\\Constants.txt", false);
        if (Constants.MUST_PLAY_MUSIC) {
            writer.write("1");
        } else {
            writer.write("0");
        }

        if (Constants.MUST_PLAY_SOUND) {
            writer.write("1");
        } else {
            writer.write("0");
        }
        writer.flush();
    }

    public static void rebuild(){
        i = 0;
        START_GAME = false;
        TIME_USER_DIED = 0;
        TIME_UPGRADE_BOOM = 30000;
        IN_SETTING = false;
        IN_RECORDS = false;
        TYPE_OF_RECORDS = 0;
        MUST_PLAY_MUSIC = true;
        MUST_PLAY_SOUND = true;
        SQRT_LEN_AIM = 0.0;
        TIME_DIED_BOOM = 3000;
        DAMAGE_USER_BULLET = 0.3;
        DAMAGE_BOT_BULLET = 0.05;
        GAME_OVER = 0;
        MUSIC_GAME = 0;

        TIME_HIT = 0;
        RESTART_GAME = false;
        TIME_LIFE_BACKGROUND_BLOOD = 1000;

        TIME_LIFE_AFTER_DIED = 3000; // время существование следа после смерти пульки
        R = 20;

        COUNT_CELL_DOWN = 13;

        V_POVOROT_PESOK = 0.001;
        TIME_LAST_POVOROT_PESOK = 0;
        SDVIG = 80;
        TIME_LIVE_BOT_AFTER_DIED = 5000;

        LEFT_END = 3000;
        DOWN_END = 810;

        SEED = 5254;

        TIME_STOP = 0;
        V_NORMAL_1 = 3000;

        NUM_GEN_PARALLEL = 20;
        NUM_GEN_VERTICAL = 20;
        YES_KID_ON_PRIVIOS_SECTOR = true;
        PAUSE_MENU = false;


        // скорость заполнения квадратиком

        //10.0
        V_POLE_1 = 10.0;
        USER_DIED_FIRST = true;
        FLAG_CHIT_BULLET = false;

        //время запуска программы
        BOOM_IS_READY = true;

        //время до начала разрушения лабиринта 5000
        TIME_TO_DIED_LABIRINT = 5000;

        //умер ли персоонаж
        USER_DIED = false;

        //скорость поворота
        V_ANGLE = 0.1;

        DEVELORER = false;
        IN_INFO = false;
    }

}
