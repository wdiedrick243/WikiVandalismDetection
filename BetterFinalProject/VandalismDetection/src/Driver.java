import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please input an edit to the Krueger Library Wikipedia Page:\n");
		String insertion = sc.nextLine();
		double threshold = .5;
		double bayVal;
		
		Bayesian bay = new Bayesian();
		
		bayVal = bay.getBayVal(insertion, threshold);
		
		System.out.println("\nThe Bayesian Value returned is:\n");

		System.out.println(bayVal);
		
		if(bayVal>=threshold) {
			System.out.println("\nThis is Constructive. Insertion added to Constructive Words.");
		} else {
			System.out.println("\nThis is Vandalism. Insertion added to Vandalism Words.");
		}
		
	}
}
