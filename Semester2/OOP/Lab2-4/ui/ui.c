#include <stdio.h>
#include "../validari/validator.h"
#include "../service/manager.h"

void print_m(){
    printf("p. Afisare lista produse\n");
    printf("1. Adauga un produs\n");
    printf("2. Actualizare produs\n");
    printf("3. Sterge produs\n");
    printf("4. Ordonare produse\n");
    printf("5. Vizualizare lista produse filtrate dupa un criteriu\n");
    printf("6. Iesire\n");
}

void afisare_produse(Lista* list){
    if(list->lg==0)
        printf("Nu exista produse\n");
    else{
        for(int i=0;i<list->lg;i++){
            int id=get_id(list->lista_produse[i]);
            char* tip=get_tip(list->lista_produse[i]);
            char* producator=get_producator(list->lista_produse[i]);
            char* model=get_model(list->lista_produse[i]);
            int pret=get_pret(list->lista_produse[i]);
            int cantitate=get_cantitate(list->lista_produse[i]);
            printf("Id: %d | Tip: %s | Producator: %s | Model: %s | Pret: %d | Cantitate: %d\n",id,tip,producator,model,pret,cantitate);
        }
    }
}

void adaugare_produs_ui(Lista* list){
    int id;
    int c=1;
    while(c==1){
        printf("Introduceti id-ul: ");
        scanf("%d",&id);
        if(validare_id(id))
            c=2;
        else
            printf("Id invalid\n");
    }

    char tip[21];
    c=1;
    while(c==1){
        printf("Introduceti tipul: ");
        scanf("%s",tip);
        if(validare_tip(tip))
            c=2;
        else
            printf("Tip invalid\n");
    }

    char producator[21];
    c=1;
    while(c==1){
        printf("Introduceti producatorul: ");
        scanf("%s",producator);
        if(validare_producator(producator))
            c=2;
        else
            printf("Producator invalid\n");
    }
    char model[21];
    printf("Introduceti modelul: ");
    scanf("%s",model);

    int pret;
    c=1;
    while(c==1){
        printf("Introduceti pretul: ");
        scanf("%d",&pret);
        if(validare_pret(pret))
            c=2;
        else
            printf("Pret invalid\n");
    }

    int cantitate;
    c=1;
    while(c==1){
        printf("Introduceti cantitatea: ");
        scanf("%d",&cantitate);
        if(validare_cantitate(cantitate))
            c=2;
        else
            printf("Cantitate invalida\n");
    }

    Produs* p=creare_produs(id,tip,producator,model,pret,cantitate);
    adaugare_produs(list,p);
}

void actualizare_produs_ui(Lista* list){
    int id;
    int c=1;
    while(c==1){
        printf("Introduceti id-ul produsului pentru care doriti actualizarea: ");
        scanf("%d",&id);
        if(validare_id(id))
            c=2;
        else
            printf("Id invalid\n");
    }
    Produs* p=find(list,id);
    if(p!=NULL){
        int cmd;
        printf("Ce doriti sa actualizati?\n");
        printf("    1. Pretul\n");
        printf("    2. Cantitatea\n");
        printf("    3. Ambele\nComanda: ");
        scanf("%d",&cmd);
        if(cmd==1){
            int new_pret;
            c=1;
            while(c==1){
                printf("Introduceti noul pret: ");
                scanf("%d",&new_pret);
                if(validare_pret(new_pret))
                    c=2;
                else
                    printf("Pret invalid\n");
            }
            actualizare_pret(list,id,new_pret);
        }
        else if(cmd==2){
            int new_cant;
            c=1;
            while(c==1){
                printf("Introduceti noua cantitate: ");
                scanf("%d",&new_cant);
                if(validare_cantitate(new_cant))
                    c=2;
                else
                    printf("Cantitate invalida\n");
            }
            actualizare_cantitate(list,id,new_cant);
        }
        else if(cmd==3){
            int new_pret;
            c=1;
            while(c==1){
                printf("Introduceti noul pret: ");
                scanf("%d",&new_pret);
                if(validare_pret(new_pret))
                    c=2;
                else
                    printf("Pret invalid\n");
            }
            actualizare_pret(list,id,new_pret);
            int new_cant;
            c=1;
            while(c==1){
                printf("Introduceti noua cantitate: ");
                scanf("%d",&new_cant);
                if(validare_cantitate(new_cant))
                    c=2;
                else
                    printf("Cantitate invalida\n");
            }
            actualizare_cantitate(list,id,new_cant);
        }
        else{
            printf("Comanda invalida\n");
        }
    }
    else
        printf("Nu exista niciun produs cu id-ul dat\n");

}

void sterge_produs_ui(Lista* list){
    int id;
    int c=1;
    while(c==1){
        printf("Introduceti id-ul produsului pe care doriti sa-l stergeti: ");
        scanf("%d",&id);
        if(validare_id(id))
            c=2;
        else
            printf("Id invalid\n");
    }
    Produs* p=find(list,id);
    if(p!=NULL){
        sterge_produs(list,id);
    }
    else{
        printf("Nu exista niciun produs cu id-ul dat\n");
    }
}

void sortare_ui(Lista* list){
    if(list->lg>0){
        int ord;
        int c1=1;
        while(c1==1){
            printf("Cum doriti sa se sorteze lista?\n");
            printf("    1. Crescator\n");
            printf("    2. Descrescator\nAlegeti optiunea: ");
            scanf("%d",&ord);
            if(ord==1 || ord==2)
                c1=2;
            else
                printf("Optiune invalida\n");
        }
        int c2=1;
        int criteriu;
        int(*fct1)(Produs*,Produs*);
        fct1=comparare_pret;
        int(*fct2)(Produs*,Produs*);
        fct2=comparare_cantitate;
        while(c2==1){
            printf("Care doriti sa fie criteriul de sortare?\n");
            printf("    1. Dupa pret\n");
            printf("    2. Dupa cantitate\nAlegeti optiunea: ");
            scanf("%d",&criteriu);
            if(criteriu==1)
                sortare(list,fct1,ord),afisare_produse(list),c2=2;
            else if(criteriu==2)
                sortare(list,fct2,ord),afisare_produse(list),c2=2;
            else
                printf("Optiune invalida\n");
        }
    }
    else
        printf("Nu exista niciun produs in lista\n");
}

void filtrare_ui(Lista* list){
    if(list->lg>0){
        int criteriu;
        int c=1;
        while(c==1){
            printf("Care doriti sa fie criteriul de filtrare?\n");
            printf("    1.Producator\n");
            printf("    2.Pret\n");
            printf("    3.Cantitate\nAlegeti optiunea: ");
            scanf("%d",&criteriu);
            if(criteriu==1){
                c=2;
                char producator[21];
                int c1=1;
                while(c1==1){
                    printf("Introduceti producatorul: ");
                    scanf("%s",producator);
                    if(validare_producator(producator))
                        c1=2;
                    else
                        printf("Producator invalid\n");
                }
                Lista l=filtrare_producator(list,producator);
                afisare_produse(&l);
                distruge_lista(&l);
            }
            else if(criteriu==2){
                c=2;
                int pret;
                int c1=1;
                while(c1==1){
                    printf("Introduceti pretul: ");
                    scanf("%d",&pret);
                    if(validare_pret(pret))
                        c1=2;
                    else
                        printf("Pret invalid\n");
                }
                Lista l=filtrare_pret(list,pret);
                afisare_produse(&l);
                distruge_lista(&l);
            }
            else if(criteriu==3){
                c=2;
                int cantitate;
                int c1=1;
                while(c1==1){
                    printf("Introduceti cantiatea: ");
                    scanf("%d",&cantitate);
                    if(validare_cantitate(cantitate))
                        c1=2;
                    else
                        printf("Pret invalid\n");
                }
                Lista l=filtrare_cantitate(list,cantitate);
                afisare_produse(&l);
                distruge_lista(&l);
            }
            else
                printf("Optiune invalida\n");
        }
    }
    else
        printf("Nu exista niciun produs in lista\n");
}

void start_program(Lista* list){
    int c=1;
    while(c==1){
        char optiune;
        print_m();
        printf("Alegeti o optiune: ");
        scanf(" %c",&optiune);
        switch (optiune)
        {
            case 'p':
                afisare_produse(list);
                break;
            case '1':
                adaugare_produs_ui(list);
                break;
            case '2':
                actualizare_produs_ui(list);
                break;
            case '3':
                sterge_produs_ui(list);
                break;
            case '4':
                sortare_ui(list);
                break;
            case '5':
                filtrare_ui(list);
                break;
            case '6':
                c=2;
                break;
            default:
                printf("Optiune invalida\n");
                break;
        }
    }
}
