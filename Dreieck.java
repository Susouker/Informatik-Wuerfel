/* Dreieck Klasse 
 * Ein Dreieck besteht einfach aus 3 Vektoren*/
public class Dreieck {
	public Vektor punkt1, punkt2, punkt3;
	Dreieck(Vektor punkt1, Vektor punkt2, Vektor punkt3) {
		this.punkt1 = punkt1;
		this.punkt2 = punkt2;
		this.punkt3 = punkt3;
	}
	Dreieck() {
		this.punkt1 = new Vektor(0, 0, 0);
		this.punkt2 = new Vektor(0, 0, 0);
		this.punkt3 = new Vektor(0, 0, 0);
	}
}
