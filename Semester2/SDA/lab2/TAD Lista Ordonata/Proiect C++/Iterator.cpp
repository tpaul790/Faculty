#include "Iterator.h"
#include <exception>
#include "LO.h"
using namespace std;

Iterator::Iterator(const LO& lo) : lo(lo){
    this->curent= lo.first;
}
/*
 * Complexitate timp constanta,Tetha(1)
 */
void Iterator::prim(){
    this->curent=lo.first;
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
void Iterator::urmator(){
    if(!valid())
        throw exception();
    this->curent=this->curent->get_urm();
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
bool Iterator::valid() const{
    if(this->curent!= nullptr)
        return true;
    return false;
}

/*
 * Complexitate timp constanta,Tetha(1)
 */
TElement Iterator::element() const{
    if(!valid())
        throw exception();
    return this->curent->get_elem();
}


