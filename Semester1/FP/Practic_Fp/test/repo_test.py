import sys
sys.path.append("/Users/titienipaul/Desktop/Practic_Fp")
from domain.entitati import Melodie
from repository.melodie_repo import MelodieInFileRepo

def test_store():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    lista=repo.get_all()
    assert len(lista)==2
    assert lista[0].get_titlu()=="Acum"
    assert lista[0].get_artist()=="Ian"
    repo.clear_all()

def test_find():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    melodie3=repo.find("Acum","Ian")
    assert melodie3.get_artist()=="Ian"
    melodie4=repo.find("Maine","Andra")
    assert melodie4==None
    repo.clear_all()
    
def test_get_all():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    lista=repo.get_all()
    assert len(lista)==2
    repo.clear_all()

def test_clear_all():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    lista=repo.get_all()
    assert len(lista)==2
    repo.clear_all()
    lista=repo.get_all()
    assert len(lista)==0

def test_load_from_file():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/data/melodii.txt")
    lista=repo.get_all()
    assert len(lista)==10

def test_store_to_file():
    repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/test/test_file.txt")
    lista=repo.get_all()
    assert len(lista)==0
    melodie1=Melodie("Acum","Ian","pop","11.03.2023")
    melodie2=Melodie("Candva","Azteca","jazz","11.03.2023")
    repo.store(melodie1)
    repo.store(melodie2)
    lista=repo.get_all()
    assert len(lista)==2
    repo.clear_all()


test_store()
test_find()
test_get_all()
test_clear_all()
test_load_from_file()
test_store_to_file()