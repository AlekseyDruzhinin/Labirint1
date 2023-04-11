public class Time {
    public static String timeToString(){
        long time = (System.currentTimeMillis() - Constants.TIME_START_PROGRAM)/1000;
        Integer time1 = (int)(time/60);
        Integer time2 = (int)(time%60);
        String s = "";
        if (time2 >= 10 && time1 >= 10){
            s = time1.toString() + ":" + time2.toString();
        }
        else if (time2 < 10 && time1 >= 10){
            s = time1.toString() + ":0" + time2.toString();
        }
        else if (time2 >= 10 && time1 < 10){
            s = "0" + time1.toString() + ":" + time2.toString();
        }else if (time1 < 10 && time2 < 10){
            s = "0" + time1.toString() + ":0" + time2.toString();
        }
        return s;
    }
}
