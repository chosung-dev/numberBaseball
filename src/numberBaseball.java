import java.util.Arrays;
import java.util.Random;

class stricke_ball{
    private int strike;
    private int ball;
    public stricke_ball(){
        this.strike = 0;
        this.ball = 0;
    }

    public stricke_ball(int strike, int ball){
        this.strike = strike;
        this.ball = ball;
    }

    public int getStrike(){
        return this.strike;
    }

    public int getBall(){
        return this.ball;
    }

    public void setStrike(int num){
        this.strike = num;
    }

    public void setBall(int num){
        this.ball = num;
    }
}

public class numberBaseball {
    static int numberDigits = 3;
    static Random rand = new Random();
    private int answer_count;
    private int[][] bot_answerable_list;
    private int bot_answerable_size;
    private int[][] player_answerable_list;
    private int player_answerable_size;
    int[] nomal_rightAnswer;

    public numberBaseball(){
        answer_count=0;
        int answer_total_count=1;
        for(int i=0; i<numberDigits; i++){ answer_total_count *= (9-i); }
        bot_answerable_list = new int[answer_total_count][numberDigits];
        int array_num = 0;
        for(int i=1; i<10; i++){
            for(int j=1; j<10; j++){
                for(int k=1; k<10; k++){
                    if (i==j || j==k || k==i){
                        continue;
                    }else{
                        // 정답 가능한 모든 경우의 수
                        // 3의자리일 경우 123 ~ 987 까지의 값 저장
                        bot_answerable_list[array_num][0] = i;
                        bot_answerable_list[array_num][1] = j;
                        bot_answerable_list[array_num++][2] = k;
                    }
                }
            }
        }
        player_answerable_list = Arrays.copyOf(bot_answerable_list, bot_answerable_list.length);
        this.bot_answerable_size = bot_answerable_list.length;
        this.player_answerable_size = player_answerable_list.length;
        this.nomal_rightAnswer = getRandom_botAnswerableList();
    }

    private int[] getRandom_botAnswerableList(){
        return bot_answerable_list[rand.nextInt(bot_answerable_size)];
    }

    public int[] getAuto_answer(){
        answer_count++;
        // ** 업데이트 예정
        // 가장 많은 경우의 수 지울 수 있는 정답으로 반환
        return getRandom_botAnswerableList();
    }

    public void filter_botAnswerableList(int[] insert_number, int strike, int ball){
        for(int i=0; i<this.bot_answerable_size; i++) {
            if(Arrays.equals(bot_answerable_list[i],insert_number)){
                for(int k=i; k<this.bot_answerable_size-1; k++){
                    bot_answerable_list[k] = bot_answerable_list[k+1];
                }
                this.bot_answerable_size--;
                break;
            }
        }
        filter_botAnswerableList(insert_number, 0, strike, ball);
    }

    public void filter_botAnswerableList(int[] insert_number, int index_number, int strike, int ball){
        if(bot_answerable_size<=index_number){ return; }
        stricke_ball sb = get_strickeAndBall(bot_answerable_list[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<this.bot_answerable_size-1; k++){
                bot_answerable_list[k] = bot_answerable_list[k+1];
            }
            this.bot_answerable_size--;
            filter_botAnswerableList(insert_number, index_number, strike, ball);
        }
        else { filter_botAnswerableList(insert_number, index_number+1, strike, ball); }
    }

    public stricke_ball get_strickeAndBall(int[] insert_number, int[] test_answer){
        stricke_ball sb = new stricke_ball();
        for (int i=0; i<insert_number.length; i++) {
            for (int j=0; j<test_answer.length; j++) {
                if (insert_number[i] == test_answer[j]){
                    sb.setBall(sb.getBall()+1);
                }
            }
            if (insert_number[i] == test_answer[i]){
                sb.setStrike(sb.getStrike()+1);
            }
        }
        sb.setBall(sb.getBall()-sb.getStrike());
        return sb;
    }

    public void filter_player_answerable_list(int[] insert_number, int strike, int ball){
        int deleteFlag = 0;
        for(int i=0; i<this.player_answerable_size; i++) {
            if(Arrays.equals(player_answerable_list[i], insert_number)){
                for(int k=i; k<this.player_answerable_size-1; k++){
                    player_answerable_list[k] = player_answerable_list[k+1];
                }
                if(this.player_answerable_size == 1) {
                    return;
                }
                this.player_answerable_size = this.player_answerable_size-1;
                deleteFlag++;
                break;
            }
        }
        filter_player_answerable_list(insert_number, 0, strike, ball, deleteFlag);
    }

    public void filter_player_answerable_list(int[] insert_number, int index_number, int strike, int ball, int deleteNum){
        if(player_answerable_size<=index_number){
            return ;
        }
        stricke_ball sb = get_strickeAndBall(player_answerable_list[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<this.player_answerable_size-1; k++){
                player_answerable_list[k] = player_answerable_list[k+1];
            }
            this.player_answerable_size = this.player_answerable_size-1;
            filter_player_answerable_list(insert_number, index_number, strike, ball, ++deleteNum);
        }
        else {
            filter_player_answerable_list(insert_number, index_number + 1, strike, ball, deleteNum);
        }
    }

    public int get_playerAnswerableList_numberOfDeletions(int[] insert_number, int strike, int ball){
        int answerable_size = this.player_answerable_size;
        int[][] answerable_list = Arrays.copyOf(player_answerable_list, player_answerable_list.length);
        for(int i=0; i<answerable_size; i++) {
            if(Arrays.equals(answerable_list[i], insert_number)){
                for(int k=i; k<answerable_size-1; k++){
                    answerable_list[k] = answerable_list[k+1];
                }
                answerable_size--;
                return get_playerAnswerableList_numberOfDeletions(answerable_list, answerable_size, insert_number, 0, strike, ball, 1);
            }
        }
        return get_playerAnswerableList_numberOfDeletions(answerable_list, answerable_size, insert_number, 0, strike, ball, 0);
    }

    public int get_playerAnswerableList_numberOfDeletions(int[][] answerable_list, int answerable_size, int[] insert_number, int index_number, int strike, int ball, int deleteNum){
        if(answerable_size<=index_number){
            return deleteNum;
        }
        int[][] answerable_list_temp = Arrays.copyOf(answerable_list, answerable_list.length);
        stricke_ball sb = get_strickeAndBall(answerable_list_temp[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<answerable_size-1; k++){
                answerable_list_temp[k] = answerable_list_temp[k+1];
            }
            answerable_size--;
            return get_playerAnswerableList_numberOfDeletions(answerable_list_temp, answerable_size, insert_number, index_number, strike, ball, deleteNum+1);
        }
        else {
            return get_playerAnswerableList_numberOfDeletions(answerable_list_temp, answerable_size, insert_number, index_number + 1, strike, ball, deleteNum);
        }
    }

    public int[] getDab(){
        return player_answerable_list[rand.nextInt(player_answerable_size)];
    }

    public stricke_ball getDab_strickeAndBall(int[] insert){
        int goodI=-1, goodJ=-1;
        int low_delectCount= player_answerable_list.length;
        for(int i=0; i<=3; i++){
            for(int j=0; i+j<=3; j++){
                int delectCount = get_playerAnswerableList_numberOfDeletions(insert, i, j);
                if(delectCount<=low_delectCount && delectCount!=player_answerable_size){
                    if(delectCount==low_delectCount){
                        if(rand.nextInt(100)<50){
                            continue;
                        }
                    }
                    low_delectCount = delectCount;
                    goodI = i;
                    goodJ = j;
                }
            }
        }
        if(goodI==-1 || goodJ==-1) {
            stricke_ball newStrikeBall = new stricke_ball(3, 0);
            return newStrikeBall;
        }
        filter_player_answerable_list(insert, goodI, goodJ);
        stricke_ball newStrikeBall = new stricke_ball(goodI, goodJ);

        return newStrikeBall;
    }

    public static void main(String args[]){
        numberBaseball newtest = new numberBaseball();
        while(true){
            System.out.println("=============================================================");
            int[] insert;
            insert = newtest.getAuto_answer();

            System.out.print("입력 : ");
            System.out.println(""+insert[0]+""+insert[1]+""+insert[2]);

            stricke_ball sb = newtest.getDab_strickeAndBall(insert);
            newtest.filter_botAnswerableList(insert, sb.getStrike(), sb.getBall());
            System.out.println("strike : "+ sb.getStrike()+ "\tball : "+ sb.getBall());
            if( sb.getStrike() == 3) {
                System.out.println("=============================================================");
                int[] dab_temp = newtest.getDab();
                System.out.println(newtest.answer_count+"회 시도");
                System.out.print("정답 : ");
                System.out.println(dab_temp[0]+""+dab_temp[1]+""+dab_temp[2]);
                break;
            }
        }
    }
}
