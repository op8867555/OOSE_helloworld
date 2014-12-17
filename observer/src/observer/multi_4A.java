package observer;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.ArrayList;

//�B�z���k��class
class Multiplication extends Observable {
	//��Ӳզ�table
	ArrayList<Object> row = new ArrayList();
	ArrayList<ArrayList> column = new ArrayList();
	creator Creator;
	//�Ĥ@���]�wrow�n��add����N���set�H�K�@���W�[element�ƶq
	int set_row_finished = 0; 

	public Multiplication(creator Creator) {
		this.Creator = Creator;
		multiplyAll();
	}

	// multiplyAll�|�N���list�ۭ�����s�itable�̭�
	final public void multiplyAll() {
		for (int i = 0; i < Creator.xlist.size(); i++) {
			for (int j = 0; j < Creator.ylist.size(); j++) {
				if(set_row_finished == 0) 
					row.add(Creator.xlist.get(i).multiply(Creator.ylist.get(j)));
				else {
					row.set(j, Creator.xlist.get(i).multiply(Creator.ylist.get(j)));
				}
			}
			column.add(row);
			set_row_finished++;
		}
	}

	public void setList() {
		Scanner scanner = new Scanner(System.in);
		int index;
		int val;
		int selection = 0;
		// �@���P�_��s����list��
		char list;
		// �q��observer�Ϊ�object index = 0�s���O���(������) 1�s���O���ܫ᪺table
		Object arg = new Object();

		while (selection != 4) {
			System.out.println("What do you want to do?\n1. Add a new row / column\n2. Delete a row / column\n3. Change value\n4. Exit");
			selection = scanner.nextInt();

			switch (selection) {
			case 1:
				System.out.println("Which list do you want to add element?(x / y)");
				list = (char) scanner.next().charAt(0);
				System.out.println("What is the value?");
				val = scanner.nextInt();

				if (list == 'x' || list == 'X') {
					Creator.xlist.add(new NNInteger(val));
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				} else if (list == 'y' || list == 'Y') {
					Creator.ylist.add(new NNInteger(val));
					row.add(new Object());
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				}
				break;
			case 2:
				System.out.println("Which list do you want to delete element?(x / y)");
				list = (char) scanner.next().charAt(0);

				if (list == 'x' || list == 'X') {
					Creator.xlist.remove(Creator.xlist.size() - 1);
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				} else if (list == 'y' || list == 'Y') {
					Creator.ylist.remove(Creator.ylist.size() - 1);
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				}
				break;
			case 3:
				System.out.println("Which list do you want to change?(x / y)");
				list = (char) scanner.next().charAt(0);

				System.out.println("Which one between the list do you want to change?");
				index = scanner.nextInt();
				System.out.println("What is the value?");
				val = scanner.nextInt();

				if (list == 'x' || list == 'X') {
					Creator.xlist.set(index, new NNInteger(val));
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				} else if (list == 'y' || list == 'Y') {
					Creator.ylist.set(index, new NNInteger(val));
					multiplyAll();
					arg = this;
					setChanged();
					notifyObservers(arg);
				}
				break;
			case 4:
				break;
			}
		}

	}
}

abstract class NNEntity {
	Object val;
	abstract Object multiply(NNEntity thing);
}

class NNInteger extends NNEntity {
	public NNInteger(int val) {
		this.val = val;
	}

	Object multiply(NNEntity thing) {
		Integer output;
		output = (Integer) val * (Integer) ((NNInteger) thing).val;
		return output;
	}
}

class NNString extends NNEntity {
	public NNString(String val) {
		this.val = val;
	}

	Object multiply(NNEntity thing) {
		String output;
		output = (String) val + (String) ((NNString) thing).val;
		return output;
	}
}

// TableDisplayer������
interface TableDisplayer {
	public void display();
}

class numberTable implements TableDisplayer, Observer {
	// �@���P�_��s����list�Ϊ�
	char list;
	Multiplication multi;
	public numberTable(Multiplication multi) {
		this.multi = multi;
	}

	public void display() {
		for (int i = 0; i < multi.Creator.xlist.size(); i++) {
			for (int j = 0; j < multi.Creator.ylist.size(); j++) {
				System.out.printf("%s * %s = %s\t", multi.Creator.xlist.get(i).val, multi.Creator.ylist.get(j).val,
						multi.Creator.xlist.get(i).multiply(multi.Creator.ylist.get(j)));
			}
			System.out.printf("\n");
		}
	}

	public void update(Observable obs, Object arg) {
		// ��arg�qObject��^Object[]
		multi = (Multiplication)arg;
		System.out.println("updated...");
		display();
	}
}

class stringTable implements TableDisplayer, Observer {
	Object[][] table;
	NNEntity[] xlist;
	NNEntity[] ylist;

	public void setTable(Object[][] table) {
		this.table = table;
	}

	public void setList(NNEntity[] xlist, NNEntity[] ylist) {
		this.xlist = xlist;
		this.ylist = ylist;
	}

	public void display() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				System.out.printf("'%s' + '%s' = '%s'\t", xlist[i].val,
						ylist[j].val, table[i][j]);
			}
			System.out.printf("\n");
		}
	}

	public void update(Observable obs, Object arg) {

	}
}

// factory ����
class creator {
	public ArrayList<NNEntity> xlist = new ArrayList();
	public ArrayList<NNEntity> ylist = new ArrayList();
}

public class multi_4A {
	public static void main(String args[]) {
		// �Ʀr�ۭ�
		creator Creat = new creator();
		for (int i = 0; i < 4; i++) {
			Creat.xlist.add(new NNInteger(i + 1));
			Creat.ylist.add(new NNInteger(i + 2));
		}
		
		Multiplication demo = new Multiplication(Creat);
		numberTable nTable = new numberTable(demo);
		demo.addObserver(nTable);
		
		nTable.display();
		demo.setList();
		// �Ʀr�ۭ�����
		/*
		 * //�r��ۥ[ StringCreator strCreate = new StringCreator();
		 * strCreate.createTable(); //�]�wxlist strCreate.xlist[0] = new
		 * NNString("�w"); strCreate.xlist[1] = new NNString("�A");
		 * strCreate.xlist[2] = new NNString("�T"); //�]�wylist strCreate.ylist[0]
		 * = new NNString("�w"); strCreate.ylist[1] = new NNString("�n");
		 * strCreate.ylist[2] = new NNString("��"); strCreate.show =
		 * strCreate.make_Table(); strCreate.showTable(); //�r��ۥ[����
		 */
	}
}
