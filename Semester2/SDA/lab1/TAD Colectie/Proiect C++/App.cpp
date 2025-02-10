#include <iostream>
#include "Colectie/Colectie.h"
#include "Colectie/IteratorColectie.h"
#include "Teste/TestExtins.h"
#include "Teste/TestScurt.h"

using namespace std;


int main() {
	testAll();
	testAllExtins();
    Colectie c;
    IteratorColectie it=c.iterator();
    for(int i=1;i<=3;i++)
        for(int j=1;j<=3;j++)
            c.adauga(j);
    while(it.valid())
        cout<<it.element()<<' ',it.urmator();
    //Dureaza aproximativ 4 minute si 10 sec :)
    cout<<"End";
}
