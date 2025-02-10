from domain.entitati import Melodie
from datetime import datetime

def merge_sort(l:list[Melodie]):
    if len(l)<=1:
        return l 
    mij=len(l)//2
    st=merge_sort(l[:mij])
    dr=merge_sort(l[mij:])
    i=0
    j=0
    lf=[]
    while i<len(st) and j<len(dr):
        data1=datetime.strptime(st[i].get_data(),"%d.%m.%Y").date()
        data2=datetime.strptime(dr[j].get_data(),"%d.%m.%Y").date()
        if data1<data2:
            lf.append(st[i])
            i+=1
        else:
            lf.append(dr[j])
            j+=1
    lf+=st[i:]
    lf+=dr[j:]
    return lf
