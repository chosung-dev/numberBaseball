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

public class baseball {
    static int[] number = {1,2,3,4,5,6,7,8,9};
    static int number_length = 3;
    private Random rand = new Random();
    private Random rand2 = new Random();
    int answer_count;
    int[][] answer_all_list;
    int size;
    int[][] dab_answer_all_list;
    int dab_size;
    int[] nomal_dab;

    public baseball(){
        answer_count=0;
        int answer_total_count=1;
        for(int i=0; i<number_length; i++){
            answer_total_count *= (9-i);
        }
        answer_all_list = new int[answer_total_count][number_length];
        dab_answer_all_list = new int[answer_total_count][number_length];
        int array_num = 0;
        for(int i=1; i<10; i++){
            for(int j=1; j<10; j++){
                for(int k=1; k<10; k++){
                    if (i==j || j==k || k==i){
                        continue;
                    }else{
                        answer_all_list[array_num][0] = i;
                        answer_all_list[array_num][1] = j;
                        answer_all_list[array_num][2] = k;
                        dab_answer_all_list[array_num][0] = i;
                        dab_answer_all_list[array_num][1] = j;
                        dab_answer_all_list[array_num][2] = k;
                        array_num++;
                    }
                }
            }
        }
        nomal_dab = getRandomAnswer();
        this.size = answer_all_list.length;
        this.dab_size = dab_answer_all_list.length;
    }

    public int getRandArrayElement(){
        return number[rand.nextInt(number.length)];
    }

    public boolean isInSameNum(int[] array, int arrayLength){
        for(int i=0; i<arrayLength; i++){
            for(int j=i+1; j<arrayLength; j++){
                if(array[i] == array[j]){
                    return true;
                }
            }
        }
        return false;
    }

    public int[] getRandomAnswer(){
        int[] first_number = new int[number_length];
        for(int i=0; i<first_number.length; i++){
            first_number[i] = getRandArrayElement();
            if (isInSameNum(first_number, i+1)){
                i--;
                continue;
            }
        }
        return first_number;
    }

    public int[] getRandomAnswerAllList(){
        return answer_all_list[rand.nextInt(size)];
    }

    public int[] get_auto_answer(){
        if (answer_count == 0){
            return getRandomAnswer();
        }else if(answer_count > 1) {
            return getRandomAnswerAllList();
        }else{
            return getRandomAnswer();
        }
    }

    public void answerAdd(){
        answer_count++;
        return;
    }

    public void delete_answer_list(int[] insert_number, int strike, int ball){
        for(int i=0; i<this.size; i++) {
            if(answer_all_list[i][0]== insert_number[0] && answer_all_list[i][1]== insert_number[1]  && answer_all_list[i][2]== insert_number[2] ){
                for(int k=i; k<this.size-1; k++){
                    answer_all_list[k] = answer_all_list[k+1];
                }
                this.size--;
                break;
            }
        }
        int deleteNum = 0;
        deleteNum = delete_answer(insert_number, 0, strike, ball, deleteNum);
    }

    public int delete_answer(int[] insert_number, int index_number, int strike, int ball, int deleteNum){
        if(size<=index_number){
            return deleteNum;
        }
        stricke_ball sb = get_answer(answer_all_list[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<this.size-1; k++){
                answer_all_list[k] = answer_all_list[k+1];
            }
            this.size--;
            return delete_answer(insert_number, index_number, strike, ball, ++deleteNum);
        }
        else {
            return delete_answer(insert_number, index_number + 1, strike, ball, deleteNum);
        }
    }

    public stricke_ball get_answer(int[] insert_number, int[] test_answer){
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

    public stricke_ball getDab(int[] insert){
        int goodI=0, goodJ=0;
        int low_delectCount= dab_answer_all_list.length;
        for(int i=0; i<=3; i++){
            for(int j=0; i+j<=3; j++){
                int delectCount = dab_delete_answer_list(insert, i, j);
                if(delectCount<=low_delectCount){
                    if(delectCount==low_delectCount){
                        if(rand2.nextInt(100)<50){
                            continue;
                        }
                    }
                    low_delectCount = delectCount;
                    goodI = i;
                    goodJ = j;
                }
            }
        }
        array_dab_delete_answer_list(insert, goodI, goodJ);
        if(dab_size==0){
            goodI = 3;
            goodJ = 0;
        }
        stricke_ball newStrikeBall = new stricke_ball(goodI, goodJ);
        return newStrikeBall;
    }

    public void array_dab_delete_answer_list(int[] insert_number, int strike, int ball){
        for(int i=0; i<this.dab_size; i++) {
            if(dab_answer_all_list[i][0]== insert_number[0] && dab_answer_all_list[i][1]== insert_number[1]  && dab_answer_all_list[i][2]== insert_number[2] ){
                for(int k=i; k<this.dab_size-1; k++){
                    dab_answer_all_list[k] = dab_answer_all_list[k+1];
                }
                this.dab_size = this.dab_size-1;
                break;
            }
        }
        array_dab_delete_answer(insert_number, 0, strike, ball, 1);
    }

    public void array_dab_delete_answer(int[] insert_number, int index_number, int strike, int ball, int deleteNum){
        if(dab_size<=index_number){
            return ;
        }
        stricke_ball sb = get_answer(dab_answer_all_list[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<this.dab_size-1; k++){
                dab_answer_all_list[k] = dab_answer_all_list[k+1];
            }
            this.dab_size = this.dab_size-1;
            array_dab_delete_answer(insert_number, index_number, strike, ball, ++deleteNum);
        }
        else {
            array_dab_delete_answer(insert_number, index_number + 1, strike, ball, deleteNum);
        }
    }

    public int dab_delete_answer_list(int[] insert_number, int strike, int ball){
        int size = this.size;
        int[][] dab_list = new int[dab_answer_all_list.length][dab_answer_all_list[0].length];
        for(int i=0; i<dab_answer_all_list.length; i++){
            for(int j=0; j<dab_answer_all_list[i].length; j++){
                dab_list[i][j] = dab_answer_all_list[i][j];
            }
        }
        for(int i=0; i<size; i++) {
            if(dab_list[i][0]== insert_number[0] && dab_list[i][1]== insert_number[1]  && dab_list[i][2]== insert_number[2] ){
                for(int k=i; k<size-1; k++){
                    dab_list[k] = dab_list[k+1];
                }
                size--;
                break;
            }
        }
        return dab_delete_answer(dab_list, size, insert_number, 0, strike, ball, 1);
    }

    public int dab_delete_answer(int[][] dab_list, int size, int[] insert_number, int index_number, int strike, int ball, int deleteNum){
        if(size<=index_number){
            return deleteNum;
        }
        int[][] dab_list_temp = new int[dab_list.length][dab_list[0].length];
        for(int i=0; i<dab_list.length; i++){
            for(int j=0; j<dab_list[i].length; j++){
                dab_list[i][j] = dab_list[i][j];
            }
        }
        stricke_ball sb = get_answer(dab_list[index_number], insert_number);
        if (sb.getStrike() != strike || sb.getBall() != ball){
            for(int k=index_number; k<size-1; k++){
                dab_list[k] = dab_list[k+1];
            }
            size--;
            return dab_delete_answer(dab_list, size, insert_number, index_number, strike, ball, deleteNum+1);
        }
        else {
            return dab_delete_answer(dab_list, size, insert_number, index_number + 1, strike, ball, deleteNum);
        }
    }

    public int[] getDab(){
        return dab_answer_all_list[0];
    }

    public static void main(String args[]){
        baseball newtest = new baseball();
        while(true){
            int[] insert;
            insert = newtest.get_auto_answer();
            System.out.println(""+insert[0]+""+insert[1]+""+insert[2]);
            stricke_ball sb = newtest.getDab(insert);
            newtest.delete_answer_list(insert, sb.getStrike(), sb.getBall());
            System.out.println("strike : "+ sb.getStrike()+ "\tball : "+ sb.getBall());
            newtest.answerAdd();
            if( sb.getStrike() == 3) {
                System.out.println(newtest.answer_count+"회");
                break;
            }
        }
    }
}
