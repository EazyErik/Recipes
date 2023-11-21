class NumbersThread extends Thread {

    public NumbersThread(int from, int to) {
        // implement the constructor
       run(from,to);
    }

    // you should override some method here



    public void run(int first,int second) {
        for(int i = first; i <= second;i++){
            if(first == second){
                System.out.println(first);
            }else{
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        NumbersThread thread = new NumbersThread(2,2);
        thread.start();
    }
}