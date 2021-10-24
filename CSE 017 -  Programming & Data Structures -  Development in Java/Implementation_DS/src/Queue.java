public class Queue<E> {
	private LinkedList<E> list;
	public Queue(){ 
		list=new LinkedList<>(); 
	} 
	public void offer(E item){ 
		list.addLast(item); 
	} 
	public E poll(){
	E value = list.getFirst();
	list.removeFirst(); 
	return value; 
	}
	public E peek(){ 
		return list.getFirst(); 
	} 
	public String toString(){
		return "Queue: " + list.toString(); 
	}
	public int size(){ 
		return list.size();
	}
	public void clear(){ 
		list.clear(); 
	}
	public boolean isEmpty(){ 
		return list.size()==0; 
	}
}