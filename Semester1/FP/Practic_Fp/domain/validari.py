from domain.entitati import Melodie
from datetime import datetime
class Validator:
    def __init__(self) -> None:
        pass

    def validare_melodie(gen:str,data:str):
        """
        Functia valideaza genul si data unei melodii
        gen: genul care trebuie validat
        data: data care trebuie validata
        return:
        """
        erori=[]
        genuri=["Rock","Pop","Jazz"]
        if gen not in genuri:
            erori.append("Gen invalid")
        try:
            datetime.strptime(data,"%d.%m.%Y").date()
            ok=True
        except ValueError:
            ok=False
        if ok==False:
            erori.append("Data invalida")
        
        if len(erori) > 0:
            erori_string = '\n'.join(erori)
            raise ValueError(erori_string)
        
            

