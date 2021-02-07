import java.util.Arrays;

/* Matrix Klasse 
 * Der einzige Unterschied zu der traditionellen LinA Matrix findet sich in der Matrix-Vektor Multiplikation*/
public class Matrix {
	private float[][] matrix;
	public Matrix(int spalten, int zeilen) {
		matrix = new float[spalten][zeilen];
	}
	public void Zuruecksetzen() {
		int n = 0;
		for(float[] x : matrix) {
			int o = 0;
			for (float y : x) {
				matrix[n][o] = 0f;
				o += 1;
			}	
			n += 1;
		}
	}
	public void WertAendern(int spalte, int zeile, float neuerWert) {
		matrix[zeile][spalte] = neuerWert;
	}
	//Siehe Projektionsmatrix auf Wikipedia o.ä. um die Abwandlung nachzuvollziehen
	public Vektor MatrixVektorMultiplizieren(Vektor faktor) { 
		Vektor Produkt = new Vektor(0, 0, 0);
		Produkt.x = (faktor.x * matrix[0][0]) + (faktor.y * matrix[0][1]) + (faktor.z * matrix[0][2]) + matrix[3][0];
		Produkt.y = (faktor.x * matrix[1][0]) + (faktor.y * matrix[1][1]) + (faktor.z * matrix[1][2]) + matrix[3][1];
		Produkt.z = (faktor.x * matrix[2][0]) + (faktor.y * matrix[2][1]) + (faktor.z * matrix[2][2]) + matrix[2][3];
		float w = (faktor.x * matrix[3][0]) + (faktor.y * matrix[3][1]) + (faktor.z * matrix[3][2]) + matrix[3][3];
		if (w != 0f) {
			Produkt.x /= w; Produkt.y /= w; Produkt.z /= w;
		}
		return(Produkt);
	}
	public void Anzeigen() {
		for (float[] x : matrix) {
			System.out.println(Arrays.toString(x));
		}
	}
	
}

