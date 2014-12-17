package observer;

import java.util.Observable;
import java.util.Observer;

class Stock extends Observable {
	private String name;
	private float price;

	public Stock(String name, float price) {
		      this.name = name;
		      this.price = price;
		      System.out.println("ConcreteSubject created: " + name + " at " + price);
		   }

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers(name);
	}

	public void setPrice(float price) {
		this.price = price;
		setChanged();
		notifyObservers(new Float(price));
	}
}

class NameObserver implements Observer {
	private String name;
    public NameObserver() {
        name = null;
        System.out.println("NameObserver created: Name is " + name);
    }
	public void update(Observable obj, Object arg) {
		if (arg instanceof String) {
	         name = (String)arg;
	         System.out.println("NameObserver: Name changed to " + name);
	    }
	}
}
class PriceObserver implements Observer {
	   private float price;
	   public PriceObserver() {
	      price = 0;
	      System.out.println("PriceObserver created: Price is " + price);
	   }

	   public void update(Observable obj, Object arg) {
	      if (arg instanceof Float) {
	         price = ((Float)arg).floatValue();
	         System.out.println("PriceObserver: Price changed to " + price);
	      }
	   }
}
/*class View1 implements Observer {
	public void update(Observable sub, Object content) {

	}
}*/

public class ObservableStock {
	 public static void main(String args[]) {
	      // Create the Subject and Observers.
	      Stock s = new Stock("Corn Pops", 1.29f);
	      NameObserver nameObs = new NameObserver();
	      PriceObserver priceObs = new PriceObserver();

	      // Add those Observers!
	      s.addObserver(nameObs);
	      s.addObserver(priceObs);

	      //make changes to the Subject.
	      s.setName("Frosted Flakes");
	      s.setPrice(4.57f);
	      s.setPrice(9.22f);
	      s.setName("Surge Crispies");
	   }
}
