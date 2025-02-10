namespace Sem11;

public class AngajatiService : Service<AngajatiInFileRepository,string,Angajat>
{
    public AngajatiService(AngajatiInFileRepository repo) : base(repo) { }
}