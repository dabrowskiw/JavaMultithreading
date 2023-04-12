# Cheatsheet für Multithreading

Vorsicht - dieses Cheatsheet ist nicht vollumfänglich sondern enthält nur die Grundlagen!

## Erstellen und Starten von Threads

### Von Thread ableiten

Eigene Klasse `MyThread` von [`Thread`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html) ableiten und Logik in `public void run()` implementieren:

```java
public class MyThread extends Thread {
    @Override
    public void run() {
        int i;
        for(i=0; i<10000000; i++);
        System.out.println("i nach 10000000 Inkrementen: " + i);
    }
}
```

Falls Logik parametrisiert werden muss, kann das ohne Probleme über eigene Attribute/Constructor gelöst werden:

```java
public class MyThread extends Thread {
	private int numIncrements;
	public MyThread(int numIncrements) {
        super();
		this.numIncrements = numIncrements;
	}
	
	@Override
	public void run() {
		int i;
		for(i=0; i<numIncrements; i++);
		System.out.println("i nach " + numIncrements + " Inkrementen: " + i);
	}
}
```

