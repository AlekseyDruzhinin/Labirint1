public class MyString {
    int number = 0;

    public MyString(int number) {
        this.number = number;
    }

    public void update(){
        number++;
    }

    public String getString(){
        Integer integer = number;
        String ans = integer.toString();
        if (number < 10){
            return "0000"+ans;
        }else if (number < 100){
            return "000" + ans;
        }else if (number < 1000){
            return "00" + ans;
        }else if (number < 10000){
            return "0" + ans;
        }else{
            return ans;
        }
    }
}
