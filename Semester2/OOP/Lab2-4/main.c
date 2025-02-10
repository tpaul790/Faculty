#include "ui/ui.h"
#include "repo/store.h"
#include "teste/teste.h"


int main(){
    Lista l=creare_lista();
    run_teste();
    start_program(&l);
    distruge_lista(&l);
}