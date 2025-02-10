from repository.melodie_repo import MelodieInFileRepo
from service.service import Controler
from ui.consola import Consola

repo=MelodieInFileRepo("/Users/titienipaul/Desktop/Practic_Fp/data/melodii.txt")
controler=Controler(repo)
consola=Consola(controler)

consola.start()