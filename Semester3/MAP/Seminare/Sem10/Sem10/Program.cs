
namespace Sem10;

public class Program
{
    public static void Main(string[] args)
    {
        //Program.ShapeMain();
        //Program.MessageTaskMain();
        //Program.SorterTaskMain();
        //Program.StrategyTaskRunnerMain();
        //Program.Decorator1Main();
        Program.Decorator2Main();
    }

    public static List<Task> createMessageTasks()
    {
        Task task1 = new MessageTask("1", "task1", "mesaj1","ana","paul",DateTime.Now);
        Task task2 = new MessageTask("2", "task2", "mesaj2","ana","paul",DateTime.Now);
        Task task3 = new MessageTask("3", "task3", "mesaj3","ana","paul",DateTime.Now);
        Task task4 = new MessageTask("4", "task4", "mesaj4","ana","paul",DateTime.Now);
        Task task5 = new MessageTask("5", "task5", "mesaj5","ana","paul",DateTime.Now);

        List<Task> tasks = new List<Task> { task1, task2, task3, task4, task5 };
        return tasks;
    }
    
    public static void ShapeMain()
    {
        Circle circle1 = new Circle(3);
        Circle circle2 = new Circle(4);
        Circle circle3 = new Circle(5);
        Square square1 = new Square(4);
        Square square2 = new Square(5);
        Square square3 = new Square(6);
        
        Circle[] circles = {circle1, circle2, circle3};
        Square[] squares = {square1, square2, square3};
        
        Console.WriteLine($"Circles: {circles.Length}");
        foreach (Circle circle in circles)
        {
            System.Console.WriteLine(circle.toString());
        }
        
        Console.WriteLine($"\n\nSquares: {squares.Length}");
        foreach (Square square in squares)
        {
            System.Console.WriteLine(square.toString());
        }
        
        Console.WriteLine($"\n\nCircles with area > 60: {circles.Length}");
        circles.ToList().Where(c => c.computeArea()>60).ToList().ForEach(c => System.Console.WriteLine(c.toString()));
    }

    public static void MessageTaskMain()
    {
        Console.WriteLine($"\n\n-------Container---------");
        List<Task> tasks = createMessageTasks();
        
        tasks.ForEach(task => Console.WriteLine(task));
    }

    public static void SorterTaskMain()
    {
        int[] array = {4, 1, 5, 9, 4, 2, 3};
        Task task1 = new SortingTask("1", "task1", array, SortStrategy.BUBBLE);
        task1.execute();
        Task task2 = new SortingTask("1", "task1", array, SortStrategy.QUICK);
        task2.execute();
    }

    public static void StrategyTaskRunnerMain()
    {
        List<Task> tasks = createMessageTasks();
        
        Console.WriteLine("\n-------Strategy FIFO---------");
        StrategyTaskRunner runner1 = new StrategyTaskRunner(Strategy.FIFO);
        tasks.ForEach(tasks1 => runner1.addTask(tasks1));
        runner1.executeAll();
        
        Console.WriteLine("\n-------Strategy LIFO---------");
        StrategyTaskRunner runner2 = new StrategyTaskRunner(Strategy.LIFO);
        tasks.ForEach(tasks2 => runner2.addTask(tasks2));
        runner2.executeAll();
    }
    
    public static void Decorator1Main()
    {
        List<Task> tasks = createMessageTasks();
        
        Console.WriteLine("\n-------Strategy FIFO---------");
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.FIFO);
        tasks.ForEach(task => strategyTaskRunner.addTask(task));
        strategyTaskRunner.executeAll();
        
        Console.WriteLine("\n-------Strategy LIFO with deocrator PrinterTaskRunner---------");
        tasks.ForEach(task => strategyTaskRunner.addTask(task));
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        printerTaskRunner.executeAll();
        
    }
    
    public static void Decorator2Main()
    {
        List<Task> tasks = createMessageTasks();
        
        Console.WriteLine("\n-------Strategy FIFO---------");
        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.FIFO);
        tasks.ForEach(task => strategyTaskRunner.addTask(task));
        strategyTaskRunner.executeAll();
        
        Console.WriteLine("\n-------Strategy LIFO with deocrator PrinterTaskRunner---------");
        tasks.ForEach(task => strategyTaskRunner.addTask(task));
        PrinterTaskRunner printerTaskRunner = new PrinterTaskRunner(strategyTaskRunner);
        printerTaskRunner.executeAll();
        
        Console.WriteLine("\n-------Strategy LIFO with deocrator PrinterTaskRunner + DelayTuskRunner---------");
        tasks.ForEach(task => strategyTaskRunner.addTask(task));
        DelayTaskRunner delayTaskRunner = new DelayTaskRunner(printerTaskRunner);
        delayTaskRunner.executeAll();
    }
}