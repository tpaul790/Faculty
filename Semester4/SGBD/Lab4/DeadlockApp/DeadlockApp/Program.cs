
using log4net;
using log4net.Config;
using System.Threading;
using Microsoft.Data.SqlClient;
using System.Reflection;

public class Program
{
    private static readonly string connectionString = "Server=DESKTOP-4NSQ3BK\\SQLEXPRESS;Database=Laborator3;Integrated Security=true;TrustServerCertificate=True;";
    private static readonly int maxRetries = 3;
    private static readonly ILog log = LogManager.GetLogger(typeof(Program));

    public static void Main(string[] args)
    {
        var logRepository = LogManager.GetRepository(Assembly.GetEntryAssembly());
        XmlConfigurator.Configure(logRepository, new FileInfo("log4net.config"));

        Thread thread1 = new Thread(() => ExecuteWithRetry("transaction1"));
        Thread thread2 = new Thread(() => ExecuteWithRetry("transaction2"));

        thread1.Start();
        thread2.Start();

        thread1.Join();
        thread2.Join();
    }

    public static void ExecuteWithRetry(string procedure)
    {
        log.Debug("trying to execute procedure: " + procedure);
        int retries = 0;
        bool succes = false;
        while(retries < maxRetries && !succes)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    using(SqlCommand command = new SqlCommand(procedure, connection))
                    {
                        command.CommandType = System.Data.CommandType.StoredProcedure;
                        command.ExecuteNonQuery();
                        log.Debug($"{procedure} was successfully on try {retries + 1}");
                        succes = true;
                    }
                }
            }
            catch(SqlException ex)
            {
                if(ex.Number == 1205) //deadlock error
                {
                    log.Debug($"{procedure} - Deadlock detected. Retry!");
                    retries++;
                    Thread.Sleep(1000);
                }
                else
                {
                    log.Debug($"{procedure} - SqlException:{ex.Message}");
                    break;
                }
            } 
            catch(Exception ex)
            {
                log.Debug($"{procedure} - Exception:{ex.Message}");
                break;
            }
        }
        if (!succes)
        {
            log.Debug($"{procedure} aborted after {maxRetries} retries");
        }
    }
}