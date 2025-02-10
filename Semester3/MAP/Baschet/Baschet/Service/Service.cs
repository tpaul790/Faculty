using System.Runtime.InteropServices.JavaScript;

namespace Baschet;

public class Service
{
    delegate bool Predicate<E>(E e);
    private JucatorFileRepo jucatorFileRepo;
    private JucatorActivFileRepo jucatorActivFileRepo;
    private MeciFileRepo meciFileRepo;
    private EchipaFileRepo echipaFileRepo;

    public Service(){ }

    public void setJucatorRepo(JucatorFileRepo jucatorFileRepo)
    {
        this.jucatorFileRepo = jucatorFileRepo;
    }

    public void setJucatorActivRepo(JucatorActivFileRepo jucatorActivFileRepo)
    {
        this.jucatorActivFileRepo = jucatorActivFileRepo;
    }

    public void setEchipaRepo(EchipaFileRepo echipaFileRepo)
    {
        this.echipaFileRepo = echipaFileRepo;
    }

    public void setMeciFileRepo(MeciFileRepo meciFileRepo)
    {
        this.meciFileRepo = meciFileRepo;
    }
    
    public List<Jucator> findAllPlayersForTeam(int idEchipa)
    {
        return jucatorFileRepo.findAll().Where(player => player.Echipa == idEchipa).ToList();
    }

    public List<Jucator> findAllTitularsForTeamAtMatch(int idEchipa, Triple<int, int, DateTime> idMeci)
    {
        List<Jucator> result = new List<Jucator>();
        findAllActiviForTeamAtMatch(idEchipa, idMeci).ForEach(j => result.Add(jucatorFileRepo.findOne(j.IdJucator)));
        return result;
    }
    
    public List<JucatorActiv> findAllActiviForTeamAtMatch(int idEchipa, Triple<int, int, DateTime> idMeci)
    {
        Func<JucatorActiv, bool> titular = jucator => jucator.TipJucator == TipJucator.TITULAR;
        Func<JucatorActiv, bool> apartenentaLaEchipa = jucator => jucatorFileRepo.findOne(jucator.IdJucator).Echipa == idEchipa;
        Func<JucatorActiv, bool> participantLaMeci = jucator => jucator.IdMeci.First == idMeci.First && jucator.IdMeci.Second == idMeci.Second && jucator.IdMeci.Third == idMeci.Third;
        return jucatorActivFileRepo.findAll().Where(apartenentaLaEchipa).Where(titular).Where(participantLaMeci).ToList();
    }

    public List<Triple<Echipa,Echipa,DateTime>> findAllMeciBetween(DateTime startDate, DateTime endDate)
    {
        List<Meci> meciuri = meciFileRepo.findAll().ToList().Where(m => m.DataMeci >= startDate && m.DataMeci <= endDate).ToList();
        List<Triple<Echipa,Echipa,DateTime>> result = new List<Triple<Echipa,Echipa,DateTime>>();
        meciuri.ForEach(m => result.Add(new Triple<Echipa, Echipa, DateTime>(echipaFileRepo.findOne(m.IdEchipa1),echipaFileRepo.findOne(m.IdEchipa2),m.DataMeci)));
        return result;
    }

    public Tuple<int,int> findScorForMeci(int idEchipa1, int idEchipa2, DateTime meciDate)
    {
        var idMeci1 = new Triple<int, int, DateTime>(idEchipa1, idEchipa2, meciDate);
        int scorEchipa1 = findAllActiviForTeamAtMatch(idEchipa1, idMeci1).Sum(j => j.NrPuncteInscrise);
        var idMeci2 = new Triple<int, int, DateTime>(idEchipa2, idEchipa1, meciDate);
        int scorEchipa2 = findAllActiviForTeamAtMatch(idEchipa2, idMeci2).Sum(j => j.NrPuncteInscrise);
        return new Tuple<int, int>(scorEchipa1, scorEchipa2);
    }
}