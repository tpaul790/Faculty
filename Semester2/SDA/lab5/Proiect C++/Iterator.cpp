#include "Iterator.h"
#include "DO.h"

using namespace std;
//Presupunem ca arborii sunt echilibrati
/*
 * Complexitatea tetha(h),deoarece adaugarea si scoaterea din stiva sunt constante
 */
Iterator::Iterator(const DO& d) : dict(d){
    curent = dict.rad;
    while(curent != NULL) {
        stiva.push(curent);
        curent = dict.st[curent];
    }
    if(!stiva.empty())
        curent = stiva.top();
}

/*
 * Complexitatea tetha(h),deoarece adaugarea si scoaterea din stiva sunt constante
 */
void Iterator::prim(){
    while(!stiva.empty())
        stiva.pop();
    curent = dict.rad;
    while(curent != NULL){
        stiva.push(curent);
        curent = dict.st[curent];
    }
    if(!stiva.empty())
        curent = stiva.top();
}

/*
 * Complexitatea tetha(1+alfa)
 */
void Iterator::urmator(){
    if(!valid())
        throw exception();
    stiva.pop();
    if(dict.dr[curent] != NULL){
        curent = dict.dr[curent];
        while(curent != NULL){
            stiva.push(curent);
            curent = dict.st[curent];
        }
    }
    if(!stiva.empty())
        curent = stiva.top();
    else
        curent = NULL;
}

/*
 * Complexitate constanta
 */
bool Iterator::valid() const{
    return curent != NULL;
}

/*
 * Complexitatea constanta
 */
TElem Iterator::element() const{
    if(!valid())
        throw exception();
    return dict.e[curent];
}


