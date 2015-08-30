package RandamPackage;
import Agent.Agent_RWP;

public class RandomWay_RWP {
	public static int RandamPickNearAgent(int p, int n, Agent_RWP agent[], int DI){
		for(int i=0; i<n; i++){
			if(p!=i){
				if((agent[p].getx() - agent[i].getx())*(agent[p].getx() - agent[i].getx())
						+(agent[p].gety() - agent[i].gety())*(agent[p].gety() - agent[i].gety())  <= DI)
				return i;
			}
		}
		return -1;
	}
}
