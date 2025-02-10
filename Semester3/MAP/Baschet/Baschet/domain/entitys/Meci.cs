namespace Baschet;

public class Meci : Entity<Triple<int,int,DateTime>>{
    
    public Triple<int,int,DateTime> Id { get => base.Id; set => base.Id = value; }
    public int IdEchipa1 { get => base.Id.First; set => base.Id.First = value; }
    
    public int IdEchipa2 { get => base.Id.Second; set => base.Id.Second = value; }
    
    public DateTime DataMeci { get => base.Id.Third; set => base.Id.Third = value; }
    
    public override string ToString() => $"{IdEchipa1}-{IdEchipa2}: {DataMeci}";
}