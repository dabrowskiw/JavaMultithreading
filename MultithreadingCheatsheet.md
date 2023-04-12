# Cheatsheet für Multithreading

Vorsicht - dieses Cheatsheet ist nicht vollumfänglich sondern enthält nur die Grundlagen!

## Erstellen und Starten von Threads

### Von Thread ableiten

Eigene Klasse `MyThread` von [`Thread`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html) ableiten und Logik in [`public void run()`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html#run()) implementieren:

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

Starten des Threads über [`start()`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html#start()) - dieser Aufruf ist nicht blockierend:

```java
MyThread newThread = new MyThread(500000);
newThread.start();
```

<details>
<summary>Komplettes Codebeispiel</summary>

```java
//MyThread.java
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

```java
//Main.java
public class Main {
    public static void main(String[] args) {
        MyThread newThread = new MyThread(100000);
        newThread.start();
    }
}
```
</details>

### Runnable implementieren

Um Mehrfachvererbung zu vermeiden, kann stattdessen das Interface [`Runnable`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Runnable.html) implementiert werden und dort die [`run()`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Runnable.html#run())-Methode implementiert werden:

```java
public class MyRunnable implements Runnable {
	@Override
	public void run() {
		int i;
		for(i=0; i<500000; i++);
		System.out.println("i nach 500000 Inkrementen: " + i);
	}

}
```

Zum Starten muss eine neue `Thread`-Instanz erstellt mit dem `Runnable` erstellt werden - wird diese dann über `start()` gestartet, wird der Code aus dem `public void run()` des im Constructor übergebenen `Runnable` ausgeführt - dieser Aufruf ist nicht blockierend:

```java
Thread newThread = new Thread(new MyRunnable());
newThread.start();
```

<details>
<summary>Komplettes Codebeispiel</summary>

```java
//MyRunnable.java
public class MyRunnable implements Runnable {
    private int numIncrements;
    public MyRunnable(int numIncrements) {
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

```java
//Main.java
public class Main {
    public static void main(String[] args) {
        Thread newThread = new Thread(new MyRunnable());
        newThread.start();    
    }
}
```
</details>

## Warten auf Threads

Der Aufruf von `Thread.start()` blockiert nicht. Um auf die Beendigung eines Threads zu warten, kann die Methode [`join()`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html#join()) des Thread-Objekts verwerndet werden (optional kann eine maximale Wartezeit angegeben werden, nach der der `join()`-Aufruf returned, auch wenn der Thread noch läuft). Falls der Thread während des `join()` unterbrochen wird, wird eine [`InterruptedException`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/InterruptedException.html) geworfen, die entsprechend gefangen werden muss:

```java
MyThread t = new MyThread();
t.start();
try {
    t.join();
} catch(InterruptedException e) {
    System.out.println("Thread was interrupted while trying to join.");
}
```

Ob ein Thread gerade läuft, kann mit der Methode [`isAlive`](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Thread.html#isAlive()) überprüft werden:

```java
MyThread t = new MyThread();
t.start();
System.out.println(t.isAlive()); // Vermutlich true
try {
    t.join();
} catch(InterruptedException e) {
    System.out.println("Thread was interrupted while trying to join.");
}
System.out.println(t.isAlive()); // false
```

<details>
<summary>Komplettes Codebeispiel</summary>

Es wird ein Thread gestartet, der intern einen Counter bis 100'000 hochzählt.

```java
//SimpleCounterThread.java
public class SimpleCounterThread extends Thread {
    private int counter;
    @Override
    public void run() {
        for(counter=0; counter<100000; counter++);
    }

    public int getCounter() {
        return counter;
    }
}
```

```java
//Main.java
public class Main {
    public static void main(String[] args) {
        SimpleCounterThread t = new SimpleCounterThread();
        t.start();
        System.out.println(t.isAlive()); // true
        System.out.println(t.getCounter()); // irgendein Wert unter 100'000 - der Thread läuft noch.
        try {
            t.join();
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted while trying to join.");
        }
        System.out.println(t.isAlive()); // false
        System.out.println(t.getCounter()); // 100'000 - der Thread ist jetzt garantiert fertig.
    }
}
```

</details>


## Synchronisieren von kritischen Bereichen

Gleichzeitiger Zugriff mehrerer Threads auf kritische Bereiche kann durch [Synchronisierung](https://docs.oracle.com/javase/tutorial/essential/concurrency/sync.html) vermieden werden.

### Synchronisierte Methoden

Eine ganze Methode kann mittels [`synchronized`](https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html) davor geschützt werden, dann mehrere Threads sie gleichzeitig ausführen:

```java
public synchronized void increaseTaskCounter() {
    this.numTasks++;
}
```

<details>
<summary>Komplettes Codebeispiel</summary>

Es werden 20 Threads gestartet, die jeweils in einer Schleife 100'000 Mal die statische Methode `ProjectStatus.increaseTaskCounter()` aufrufen. Danach müsste `ProjectStatus.getNumTasks()` den Wert 2'000'000 ausgeben. Ohne Synchronisierung kommt aber kleinerer Wert raus. 

#### Beispiel ohne Synchronisierung

```java
//ProjectStatus.java
public class ProjectStatus {
    private static int numTasks=0;

    public static void increaseTaskCounter() {
        numTasks++;
    }

    public static int getNumTasks() {
        return numTasks;
    }
}
```

```java
//TaskCounterThread.java
public class TaskCounterThread extends Thread {
    @Override
    public void run() {
        for(int i=0; i<100000; i++) {
            ProjectStatus.increaseTaskCounter();
        }
    }
}
```

```java
//Main.java
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<TaskCounterThread> threads = new LinkedList<>();
        // 20 Threads erstellen
        for(int i=0; i<20; i++) {
            threads.add(new TaskCounterThread());
        }
        // Alle Threads starten
        threads.forEach(TaskCounterThread::start);
        // Auf die Beendigung aller Threads warten
        try {
            for(TaskCounterThread t : threads) {
                t.join();
            }
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
        System.out.println(ProjectStatus.getNumTasks());
    }
}
```

#### Beispiel mit Synchronisierung

```java
//ProjectStatus.java
public class ProjectStatus {
    private static int numTasks=0;

    public static synchronized void increaseTaskCounter() {
        numTasks++;
    }

    public static int getNumTasks() {
        return numTasks;
    }
}
```

```java
//TaskCounterThread.java
public class TaskCounterThread extends Thread {
    @Override
    public void run() {
        for(int i=0; i<100000; i++) {
            ProjectStatus.increaseTaskCounter();
        }
    }
}
```

```java
//Main.java
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<TaskCounterThread> threads = new LinkedList<>();
        // 20 Threads erstellen
        for(int i=0; i<20; i++) {
            threads.add(new TaskCounterThread());
        }
        // Alle Threads starten
        threads.forEach(TaskCounterThread::start);
        // Auf die Beendigung aller Threads warten
        try {
            for(TaskCounterThread t : threads) {
                t.join();
            }
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
        System.out.println(ProjectStatus.getNumTasks());
    }
}
```

</details>

### Synchronisierung von Codebereichen auf Objekten

Für feinere Kontrolle können mit dem [`synchronized` statement](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html) einzelne Codebereiche vor gleichzeitigem Zugriff geschützt werden. Dabei wird ein Objekt angegeben, welches als Schlüssel verwendet wird - nur ein Thread kann gleichzeitig ein Objekt als Schlüsel halten. Daraus ergibt sich:

* Nur ein Thread kann in einem auf das selbe Objekt synchronisierten `synchronized`-Block sein
* Unterschiedliche Threads können auf unterschiedlichen Objekten synchronisierten `synchronized`-Blöcken sein

```java
public class ExtendedProjectStatus {
    private static int tasksTodo;
    private static int tasksDone;
    private static Object todoLock = new Object();
    private static Object doneLock = new Object();

    public static void taskPerformed() {
        synchronized(todoLock) {
            tasksTodo--;
        }
        synchronized (doneLock) {
            tasksDone++;
        }
    }
```

<details>
<summary>Komplettes Codebeispiel</summary>

Es werden 20 Threads gestartet, die jeweils in einer Schleife 100'000 Mal die statische Methode `ExtendedProjectStatus.taskPerformed()` aufrufen. Danach müsste `ExtendedProjectStatus.getTasksTodo()` den Wert 0 und `ExtendedProjectStatus.getTasksDone()` den Wert 2'000'000 ausgeben. Ohne Synchronisierung kommen aber inkonsistente Werte raus.

#### Beispiel ohne Synchronisierung

```java
//ExtendedProjectStatus.java
public class ExtendedProjectStatus {
    private static int tasksTodo = 2000000;
    private static int tasksDone = 0;

    public static void taskPerformed() {
        tasksTodo--;
        tasksDone++;
    }

    public static int getTasksTodo() {
        return tasksTodo;
    }

    public static int getTasksDone() {
        return tasksDone;
    }
}
```

```java
//ExtendedTaskCounterThread.java
public class ExtendedTaskCounterThread extends Thread {
    @Override
    public void run() {
        for(int i=0; i<100000; i++) {
            ExtendedProjectStatus.taskPerformed();
        }
    }
}
```

```java
//Main.java
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<ExtendedTaskCounterThread> threads = new LinkedList<>();
        // 20 Threads erstellen
        for(int i=0; i<20; i++) {
            threads.add(new ExtendedTaskCounterThread());
        }
        // Alle Threads starten
        threads.forEach(ExtendedTaskCounterThread::start);
        // Auf die Beendigung aller Threads warten
        try {
            for(ExtendedTaskCounterThread t : threads) {
                t.join();
            }
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
        System.out.println("Tasks done: " + ExtendedProjectStatus.getTasksDone());
        System.out.println("Tasks to do: " + ExtendedProjectStatus.getTasksTodo());
    }
}
```

#### Beispiel mit Synchronisierung

```java
//ExtendedProjectStatus.java
public class ExtendedProjectStatus {
    private static int tasksTodo = 2000000;
    private static int tasksDone = 0;
    private static Object todoLock = new Object();
    private static Object doneLock = new Object();

    public static void taskPerformed() {
        synchronized(todoLock) {
            tasksTodo--;
        }
        synchronized (doneLock) {
            tasksDone++;
        }
    }

    public static int getTasksTodo() {
        return tasksTodo;
    }

    public static int getTasksDone() {
        return tasksDone;
    }
}
```

```java
//ExtendedTaskCounterThread.java
public class ExtendedTaskCounterThread extends Thread {
    @Override
    public void run() {
        for(int i=0; i<100000; i++) {
            ExtendedProjectStatus.taskPerformed();
        }
    }
}
```

```java
//Main.java
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<ExtendedTaskCounterThread> threads = new LinkedList<>();
        // 20 Threads erstellen
        for(int i=0; i<20; i++) {
            threads.add(new ExtendedTaskCounterThread());
        }
        // Alle Threads starten
        threads.forEach(ExtendedTaskCounterThread::start);
        // Auf die Beendigung aller Threads warten
        try {
            for(ExtendedTaskCounterThread t : threads) {
                t.join();
            }
        } catch(InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
        System.out.println("Tasks done: " + ExtendedProjectStatus.getTasksDone()); // 2'000'000
        System.out.println("Tasks to do: " + ExtendedProjectStatus.getTasksTodo()); // 0
    }
}
```

</details>

## Warten und wartende Threads wecken

### Warten

Ein Thread, der gerade in einem synchronisierten Bereich ist, kann sich in einen Wartezustand begeben und den Bereich für andere Threads freigeben. Dafür wird die [`wait()`-Methode](https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/Object.html#wait()) aufgerufen:

* Die Methode `wait()` wird auf dem Objekt aufgerufen, auf dem synchronisiert wird.
* Der Thread, der `wait()` auf einem Objekt aufruft, muss sich in einem auf dieses Objekt synchronisierten Bereich befinden.
* Es kann eine `InterruptedException` geworfen werden.

```java
while(tasks.isEmpty()) {
    try {
        tasks.wait();
    } catch(InterruptedException e) {
        return;
    }
}
```

### Wartende Threads wecken

Mit den Methoden `notify()` und `notifyAll()` können wartende Threads geweckt werden - mit `notify()` ein zufälliger, mit `notifyAll()` alle:
* 
* Die Methoden `notify()` und `notifyAll()` werden auf dem Objekt aufgerufen, auf dem synchronisiert wird.
* Der Thread, der `notify()` oder `notifyAll()` auf einem Objekt aufruft, muss sich in einem auf dieses Objekt synchronisierten Bereich befinden.
* Ein geweckter Thread kann nicht sofort weiterlaufen, sondern muss vorher wieder die Synchronisation auf dem Objekt erhalten (stellt sich also erstmal in der Warteschleife an, weil ja der Thread, der `notify()` aufgrufen hat, gerade zwingend in einem auf das selbe Objekt synchronisierten Codeblock ist).

```java
synchronized(tasks) {
    tasks.add(new Task());
    tasks.notify();
}
```

<details>
<summary>Komplettes Codebeispiel</summary>

Ein `CountingTaskProducer`-Thread erstellt `CountingTask`-Objekte, die von mehreren `CountingTaskConsumer`-Threads verbraucht werden.

```java
//CountingTask.java
public class CountingTask {
    private int target;

    public CountingTask(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }
}
```

```java
//CountingTaskProducer.java
import java.util.Random;

public class CountingTaskProducer extends Thread {
    private int numTasks;

    public CountingTaskProducer(int numTasks) {
        this.numTasks = numTasks;
    }

    @Override
    public void run() {
        Random random = new Random(System.currentTimeMillis());
        for(int i=0; i<numTasks; i++) {
            int countTo = 100000+random.nextInt(100000); // Zufälliger Wert zwischen 100k-200k
            synchronized(Main.tasks) {
                Main.tasks.add(new CountingTask(countTo));
                // Neuer Task ist dazugekommen - einen zufälligen Producer aufwecken.
                Main.tasks.notify();
            }
        }
        synchronized(Main.tasks) {
            // Ferting mit der Produktion - alle eventuell wartenden Producer aufwecken.
            Main.productionDone = true;
            Main.tasks.notifyAll();
        }
    }
}
```

```java
//CountingTaskConsumer.java
public class CountingTaskConsumer extends Thread {
    private static int numThreads = 0;
    private int numThread;

    public CountingTaskConsumer() {
        // Jedem Thread eine eigene Nummer geben
        this.numThread = numThreads++;
    }

    @Override
    public void run() {
        while(true) {
            CountingTask task = null;
            synchronized(Main.tasks) {
                while(Main.tasks.isEmpty() && !Main.productionDone) {
                    try {
                        System.out.println("Thread " + numThread + " waiting for more tasks...");
                        Main.tasks.wait();
                    } catch(InterruptedException e) {
                        System.out.println("Thread " + numThread + " was interrupted");
                        return;
                    }
                }
                if(!Main.tasks.isEmpty()) {
                    task = Main.tasks.pop();
                }
                else if(Main.productionDone) {
                    // Aufgabenliste ist leer und es werden keine neuen mehr produziert
                    return;
                }
            }
            int counter;
            for(counter = 0; counter < task.getTarget(); counter++);
            System.out.println("Thread " + numThread + " counted to: " + counter);
        }
    }
}
```

```java
//Main.java
import java.util.LinkedList;

public class Main {
    public static LinkedList<CountingTask> tasks = new LinkedList<>();
    public static boolean productionDone = false;

    public static void main(String[] args) {
        CountingTaskProducer producer = new CountingTaskProducer(20);
        // 8 Consumer erstellen
        CountingTaskConsumer consumers[] = new CountingTaskConsumer[8];
        for(int i=0; i<consumers.length; i++) {
            consumers[i] = new CountingTaskConsumer();
        }
        // Alle consumer starten
        for(CountingTaskConsumer consumer : consumers) {
            consumer.start();
        }
        // Producer starten
        producer.start();
        // Auf alle consumer warten
        for(CountingTaskConsumer consumer : consumers) {
            try {
                consumer.join();
            } catch(InterruptedException e) {
                // Ist OK - wir wollen hier ja nur sicherstellen, dass keiner mehr läuft.
            }
        }
        System.out.println("Fertig.");
    }
}
```
</details>