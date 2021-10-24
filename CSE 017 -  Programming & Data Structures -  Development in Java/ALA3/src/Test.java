import java.util.Date;

public class Test {

	public static void main(String[] args) {
		Person p = new Person("Helen Brown", "(610)334-2288", "222 10th Street, Bethlehem", "hbrown@gmail.com");
		String pp = p.toString();

		Student s = new Student("Gary Leister", "(202)331-7177", "972 4th Street, Emmaus", "gleister@gmail.com", "CSE",
				12345, 3.50);
		String ss = s.toString();

		Employee e = new Employee("Beth Down", "(610)222-4433", "234 Main Street, Philadelphia", "bdown@gmail.com",
				"Systems Administrator", 33442, 75000.00, new Date());
		String ee = e.toString();

		Faculty f = new Faculty("Mark Jones", "(610)333-2211", "21 Orchid Street, Bethlehem", "mjones@gmail.com",
				"Faculty", 22222, 90000.00, new Date(), "Professor");
		String ff = f.toString();

		System.out.println("Person Information");
		System.out.println(pp);
		System.out.println("Student Information");
		System.out.println(ss);
		System.out.println("Employee Information");
		System.out.println(ee);
		System.out.println("Faculty Information");
		System.out.println(ff);
	}

}
