namespace Sem11;

public class SarcinaService : Service<SarcinaInFileRepository, string, Sarcina>
{
    public SarcinaService(SarcinaInFileRepository repository) : base(repository){ }
}