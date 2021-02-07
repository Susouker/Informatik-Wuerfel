import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;

import javax.swing.*;

import java.util.*;
import java.math.*;

public class DreiDimensionalerRasterisierer extends JFrame{
	private float fTheta = 0f; //Rotationswinkel
	private final ArrayList<Line2D> linien = new ArrayList<Line2D>(); //Alle Linien, die zu dem Körper gehören
	static ArrayList<Dreieck> Koerper = new ArrayList<Dreieck>(); //Der Körper selber besteht aus vielen Dreiecken
	static Matrix Projektionsmatrix = new Matrix(4,4);	
	private float bildschirm_breite = 1000f; //selbsterklärend
	private float bildschirm_hoehe = 1000f;
	
	public DreiDimensionalerRasterisierer() {
		 super("3D Rasterisierung");
		//Als Testobjekt definiere ich hier einmal einen Einheitswürfel, die Richtungen weisen auf die jeweilige Fläche hin
		//SÜDEN
		Koerper.add(new Dreieck(new Vektor(0, 0, 0), new Vektor(0, 1, 0), new Vektor(1, 1, 0)));
		Koerper.add(new Dreieck(new Vektor(0, 0, 0), new Vektor(1, 1, 0), new Vektor(1, 0, 0)));
		//OSTEN
		Koerper.add(new Dreieck(new Vektor(1, 0, 0), new Vektor(1, 1, 0), new Vektor(1, 1, 1)));
		Koerper.add(new Dreieck(new Vektor(1, 0, 0), new Vektor(1, 1, 1), new Vektor(1, 0, 1)));
		//NORDEN
		Koerper.add(new Dreieck(new Vektor(1, 0, 1), new Vektor(1, 1, 1), new Vektor(0, 1, 1)));
		Koerper.add(new Dreieck(new Vektor(1, 0, 1), new Vektor(0, 1, 1), new Vektor(0, 0, 1)));
		//WESTEN
		Koerper.add(new Dreieck(new Vektor(0, 0, 1), new Vektor(0, 1, 1), new Vektor(0, 1, 0)));
		Koerper.add(new Dreieck(new Vektor(0, 0, 1), new Vektor(0, 1, 0), new Vektor(0, 0, 0)));
		//OBEN
		Koerper.add(new Dreieck(new Vektor(0, 1, 0), new Vektor(0, 1, 1), new Vektor(1, 1, 1)));
		Koerper.add(new Dreieck(new Vektor(0, 1, 0), new Vektor(1, 1, 1), new Vektor(1, 1, 0)));
		//UNTEN
		Koerper.add(new Dreieck(new Vektor(1, 0, 1), new Vektor(0, 0, 1), new Vektor(0, 0, 0)));
		Koerper.add(new Dreieck(new Vektor(1, 0, 1), new Vektor(0, 0, 0), new Vektor(1, 0, 0)));
		
		float fNah = 0.1f; //Abstand Sicht - Bildschirm
		float fWeit = 1000.0f; //Weitest entferntester noch sichtbarer Punkt
		float fFov = 90.0f; //fov ist glaube ich selbsterklärend
		float Seitenverhaeltnis = bildschirm_breite/bildschirm_hoehe;
		float FovRad = (float) (1f / Math.tan(fFov * 0.5f / 180f * Math.PI));
			
		//Projektionsmatrix initialisieren
		Projektionsmatrix.WertAendern(0, 0, Seitenverhaeltnis * FovRad);
		Projektionsmatrix.WertAendern(1, 1, FovRad);
		Projektionsmatrix.WertAendern(2, 2, fWeit / (fWeit - fNah));
		Projektionsmatrix.WertAendern(3, 2, (-fWeit * fNah) / (fWeit - fNah));
		Projektionsmatrix.WertAendern(2, 3, 1f);
		Projektionsmatrix.WertAendern(3, 3, 0f);
		Projektionsmatrix.Anzeigen();
		KoerperBerechnen();
		
		
		setSize((int) bildschirm_breite, (int) bildschirm_hoehe); //JFrame Sachen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	  }
	
	//Es wird jede Linie der Linien ArrayListe gemalt
	public void dreieckMalen(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		System.out.println("AAA");
		for(Line2D lin : linien) {
			g2d.draw(lin);
		}
	}
	
	//Muss man machen weil diese Java JFrame Bibliothek absoluter Rotz ist
	public void paint(Graphics g) {
		super.paint(g);
		dreieckMalen(g);
	}
	
	//Fragt nicht, warum man hier invokeLater benutzen muss. Java stirbt sonst, wenn man das nicht macht
	//(Hat irgendwas mit Multithreading zu tun
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable() {
			  @Override
			  public void run() {
				  new DreiDimensionalerRasterisierer().setVisible(true);
			  }
		  });
		
	  }
	
	public void KoerperBerechnen() {
		linien.clear();
		Matrix RotationsmatrixZ = new Matrix(4,4);
		Matrix RotationsmatrixX = new Matrix(4,4);
		RotationsmatrixZ.Zuruecksetzen();
		RotationsmatrixX.Zuruecksetzen();
		
		//Siehe Rotationsmatrix Eintrag Wikipedia
		RotationsmatrixZ.WertAendern(0, 0, (float) Math.cos(fTheta));
		RotationsmatrixZ.WertAendern(0, 1, (float) Math.sin(fTheta));
		RotationsmatrixZ.WertAendern(1, 0, (float) -Math.sin(fTheta));
		RotationsmatrixZ.WertAendern(1, 1, (float) Math.cos(fTheta));
		RotationsmatrixZ.WertAendern(2, 2, 1f);
		RotationsmatrixZ.WertAendern(3, 3, 1f);
		
		RotationsmatrixX.WertAendern(0, 0, 1f);
		RotationsmatrixX.WertAendern(1, 1, (float) Math.cos(fTheta * 0.5f));
		RotationsmatrixX.WertAendern(1, 2, (float) Math.sin(fTheta * 0.5f));
		RotationsmatrixX.WertAendern(2, 1, (float) -Math.sin(fTheta * 0.5f));
		RotationsmatrixX.WertAendern(2, 2, (float) Math.cos(fTheta * 0.5f));
		RotationsmatrixX.WertAendern(3, 3, 1f);
		
		//Wir gehen jedes Dreieck des Körpers durch
		for (Dreieck d : Koerper) {
			Dreieck dreieckProjiziert = new Dreieck();
			Dreieck dreieckUebersetzt = new Dreieck();
			Dreieck dreieckRotiertZ = new Dreieck();
			Dreieck dreieckRotiertZX = new Dreieck();
			
			//Wir rotieren es auf der Z-Achse
			dreieckRotiertZ.punkt1 = RotationsmatrixZ.MatrixVektorMultiplizieren(d.punkt1);
			dreieckRotiertZ.punkt2 = RotationsmatrixZ.MatrixVektorMultiplizieren(d.punkt2);
			dreieckRotiertZ.punkt3 = RotationsmatrixZ.MatrixVektorMultiplizieren(d.punkt3);
			
			//Wir rotieren es auf der X-Achse
			dreieckRotiertZX.punkt1 = RotationsmatrixX.MatrixVektorMultiplizieren(dreieckRotiertZ.punkt1);
			dreieckRotiertZX.punkt2 = RotationsmatrixX.MatrixVektorMultiplizieren(dreieckRotiertZ.punkt2);
			dreieckRotiertZX.punkt3 = RotationsmatrixX.MatrixVektorMultiplizieren(dreieckRotiertZ.punkt3); 
			
			//Wir verschieben das Dreieck in unser Sichtfeld
			dreieckUebersetzt = dreieckRotiertZX;
			dreieckUebersetzt.punkt1.z = dreieckRotiertZX.punkt1.z + 3f;
			dreieckUebersetzt.punkt2.z = dreieckRotiertZX.punkt2.z + 3f;
			dreieckUebersetzt.punkt3.z = dreieckRotiertZX.punkt3.z + 3f;

			//Wir projizieren die Punkte des Dreieckes von 3D->2D
			dreieckProjiziert.punkt1 = Projektionsmatrix.MatrixVektorMultiplizieren(dreieckUebersetzt.punkt1);
			dreieckProjiziert.punkt2 = Projektionsmatrix.MatrixVektorMultiplizieren(dreieckUebersetzt.punkt2);
			dreieckProjiziert.punkt3 = Projektionsmatrix.MatrixVektorMultiplizieren(dreieckUebersetzt.punkt3);
					
			//Durch die Addition von 1 hat jede Koordinate eine Spanne von 0-2
			dreieckProjiziert.punkt1.x += 1f;
			dreieckProjiziert.punkt1.y += 1f;			
			dreieckProjiziert.punkt2.x += 1f;
			dreieckProjiziert.punkt2.y += 1f;
			dreieckProjiziert.punkt3.x += 1f;
			dreieckProjiziert.punkt3.y += 1f;
			
			//Wir normalisieren die Punkte und skalieren auf die Größe des Bildschirmes
			dreieckProjiziert.punkt1.x *= 0.5f * bildschirm_breite;
			dreieckProjiziert.punkt1.y *= 0.5f * bildschirm_hoehe;
			dreieckProjiziert.punkt2.x *= 0.5f * bildschirm_breite;
			dreieckProjiziert.punkt2.y *= 0.5f * bildschirm_hoehe;
			dreieckProjiziert.punkt3.x *= 0.5f * bildschirm_breite;
			dreieckProjiziert.punkt3.y *= 0.5f * bildschirm_hoehe;
			
			//Wir müssen pro Dreieck ja 3 Linien zeichen (1->2, 2->3, 3->1)
			linien.add(new Line2D.Float(dreieckProjiziert.punkt1.x, dreieckProjiziert.punkt1.y, dreieckProjiziert.punkt2.x, dreieckProjiziert.punkt2.y));
			linien.add(new Line2D.Float(dreieckProjiziert.punkt2.x, dreieckProjiziert.punkt2.y, dreieckProjiziert.punkt3.x, dreieckProjiziert.punkt3.y));
			linien.add(new Line2D.Float(dreieckProjiziert.punkt1.x, dreieckProjiziert.punkt1.y, dreieckProjiziert.punkt3.x, dreieckProjiziert.punkt3.y));
		}	
	}
}
