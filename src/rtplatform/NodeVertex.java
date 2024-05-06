package rtplatform;

import mh_heft.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * 2015-04-23
 * @author Ake
 * @see 《算法导论》 page.355 ---拓扑排序
 * @version v.1
 * @class 这个类只是在模块进行实时任务注册的时候进行实时任务拓扑排序时被临时创建
 * 在实时任务注册完成以后不再存在该类实例化的对象。
 */
public class NodeVertex implements Comparable<NodeVertex>{
   
	public final static int WHITE = 0;  //未被探知
	public final static int GRAY = 1;   //正在被探知（这个提法不妥）
	public final static int BLACK = 2;  //已经完全被探知
    
	public Node srcNode;  //保存了该节点模块的 ID
	public List<Node> tarID = new  LinkedList<>(); //保存了 下游组件的模块ID的链表
	
	public int time = -1; //这个点在深度优先搜索被发现的时间
	public int color = WHITE ; 
	public NodeVertex(Node id){
		this.srcNode = id;
	}
	
	public void insertTarget(Node id){
	    tarID.add(id);
	}

	public int compareTo(NodeVertex arg0) {
		if(this.time > arg0.time)return -1;
		if(this.time < arg0.time)return 1;
		return 0;
	}
		
	
}
