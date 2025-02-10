import sys
sys.path.append("/Users/titienipaul/Desktop/Practic_Fp")
from domain.entitati import Melodie
from repository.melodie_repo import MelodieInFileRepo
from service.service import Controler

def test_modificare():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    controler=Controler(repo)
    controler.modificare_melodii("Acum","Ian","Jazz","11.11.2011")
    lista=repo.get_all()
    assert lista[0].get_titlu()=="Acum"
    assert lista[0].get_artist()=="Ian"
    assert lista[0].get_gen()=="Jazz"
    assert lista[0].get_data()=="11.11.2011"

    repo.clear_all()

def test_generare():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    controler=Controler(repo)
    numar1=controler.generare_melodii(3,"El,Ea,Eu","Ian,Azteca,Costan")
    lista=controler.get_all()
    assert len(lista)==3
    assert numar1==3
    repo.clear_all()

    numar2=controler.generare_melodii(100,"El,Ea,Eu","Ian,Azteca,Costan")
    lista=controler.get_all()
    assert len(lista)==9
    assert numar2==9

    repo.clear_all()

def test_export():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    controler=Controler(repo)
    
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.02.2023")
    melodie3=Melodie("Maine","Andra","pop","14.03.2024")
    melodie4=Melodie("Ieri","Maria","rock","10.05.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    repo.store(melodie3)
    repo.store(melodie4)

    controler.export("test_file_export.txt")
    repo.clear_all()

def test_get_all():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    controler=Controler(repo)
    
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.02.2023")
    melodie3=Melodie("Maine","Andra","pop","14.03.2024")
    melodie4=Melodie("Ieri","Maria","rock","10.05.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    repo.store(melodie3)
    repo.store(melodie4)

    lista=controler.get_all()
    assert len(lista)==4
    repo.clear_all()



test_modificare()
test_generare()
test_export()
test_get_all()