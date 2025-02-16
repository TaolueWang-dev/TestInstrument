public class TestGoto {
    public static void sampleMethod(){
        label1:
        for (int i = 0; i < 5; i++){
            if(i == 3){
                break label1;
            }
        }
    }

    public static void main(String[] args) {
        sampleMethod();
    }
}