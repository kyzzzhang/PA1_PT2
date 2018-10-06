package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
//		while(!isDone()) {
//			processLine(input.poll());
//		}
//		System.out.println("enter print");
//		System.out.println(!input.isEmpty());
//		for(String s : input) {
//			System.out.println("input"+s);
//		}
		while(!input.isEmpty()) {
			String line = input.poll();
			if(line !=null && !line.equals("COMPLETED")) {
				processLine(line);
//				System.out.println(line);
			}
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}
	
	public String toString() {
		return "print";
	}
	
//	public boolean isDone() {
//		return output.contains("COMPLETED");
//	}
}
