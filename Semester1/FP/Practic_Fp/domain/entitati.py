
class Melodie:
    def __init__(self,titlu:str,artist:str,gen:str,data:str) -> None:
        self.__titlu=titlu
        self.__artist=artist
        self.__gen=gen
        self.__data=data

    def get_titlu(self):
        """
        returneaza titlul unei melodii
        """
        return self.__titlu
    
    def get_artist(self):
        """
        returneaza artistul unei melodii
        """
        return self.__artist
    
    def get_gen(self):
        """
        returneaza genul unei melodii
        """
        return self.__gen
    
    def get_data(self):
        """
        returneaza data unei melodii
        """
        return self.__data
    
    def set_data(self,new):
        """
        seteaza data unei melodii
        new: noua data
        """
        self.__data=new

    def set_gen(self,new):
        """
        seteaza genul unei melodii
        new: noul gen
        """
        self.__gen=new
