import java.util.Random;
import Agent.Agent_RWP;
import Interaction.Interaction_RWP;
import RandamPackage.*;

class Loosta_LE_RWP{
	public static final int Gridsize = 100;
	public static final int Roundnum = 1000000;
	
	public static final int s = 96;			//96�ȏ��3n�ȏ�
	public static final int n = 20;
	
	public static final int r = 10;	//vの速度の上限
	public static final int DistanceforInteraction = 5;	//interactionができる距離

	
	public static String RandomMethod = "Torus";	//Torus or RWP(Random Way Point)
	
	public static void main(String args[]){
		Random random = new Random();
		Agent_RWP agent[] = new Agent_RWP[n];
		int CT = 0, HT = 0;
		boolean HT_count_flag = false, CT_count_flag = true;
		double R[] = new double[n];			//RWPによるAgent[i]の行く先を格納(Destination)
		double THETA[] = new double[n];		
		double MoveDis[] = new double[n];	//Destinationが決まってからどれだけ進んでるか
		boolean HaveDest[] = new boolean[n];	//Agent[i]がdestinationを持ってるか
		
		/*Agent Initialization*/
		for(int i=0; i<n; i++){
			agent[i] = new Agent_RWP(random.nextBoolean(), random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble(), s);
			HaveDest[i] = false;
		}
			
		for(int i=0; i<Roundnum; i++){
			int leadercount=0;
			//リーダの数をかぞえる
			for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leadercount++; }
			System.out.println("the number of leaders = " + leadercount);
			//Holding Timeが終了したらぬける
			if(leadercount!=1 && HT_count_flag==true){ break; }
			//リーダが決まったとき
			if(leadercount==1 && HT==0){ 
				HT_count_flag = true;
				CT_count_flag = false;
			}
			//リーダが一個のとき
			if(HT_count_flag==true) HT++;
			if(CT_count_flag==true) CT++;
			
			while(true){					//一回の交流がちゃんと終わるまで				
				for(int j=0; j<n; j++){			//for each node
				/*decide destination begin*/
					if(HaveDest[j] == false){
						R[j] = random.nextInt(Gridsize)+random.nextDouble();
						THETA[j] = random.nextInt(360)+random.nextDouble();
						while(0>agent[j].getx()+R[j]*Math.cos(THETA[j])||agent[j].getx()+R[j]*Math.cos(THETA[j])>Gridsize
								||0>agent[j].gety()+R[j]*Math.sin(THETA[j])||agent[j].gety()+R[j]*Math.sin(THETA[j])>Gridsize){
							R[j] = random.nextInt(Gridsize)+random.nextDouble();
							THETA[j] = random.nextInt(360)+random.nextDouble();
						}
						HaveDest[j] = true;
					}
					/*decide destination end*/
					/*agent move process begin*/
					if(r+MoveDis[j] < R[j]){
						double vr = random.nextInt(r)+random.nextDouble();	//ランダムに次のラウンドで動く距離を決める
						if(MoveDis[j]+vr >= R[j]) {	//超えそうになったとき制御
							vr = R[j]-MoveDis[j];
							HaveDest[j] = false;
						}
						agent[j].ShiftPointForRWP( vr, THETA[j]);	//移動
					}
					/*agent move process end*/
				}
				/*interaction process begin*/
				int p = random.nextInt(n);		//interactionをするagentをランダムで選択
				int q = RandomWay_RWP.RandamPickNearAgent( p, n, agent, DistanceforInteraction);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
				if(q != -1) { 	//pの周りにinteractionが可能なAgentが見つかったとき
					Interaction_RWP.interaction(agent[p], agent[q], s);	
					for(int j=0; j<n; j++) agent[j].Countdown();	//交流したagentのtimerをデクリメント
					break;						//次のラウンドへ
				}
				/*interaction process end*/
			}
		}
		System.out.println("CT = " + CT);
		System.out.println("HT = " + HT);
	}
}
