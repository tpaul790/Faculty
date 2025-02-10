#ifndef LAB6_ISTVAN_MYVECTOR_H
#define LAB6_ISTVAN_MYVECTOR_H


template<typename TElem>
class MyVectorIterator;

template<typename TElem>
class MyVector {
    friend class MyVectorIterator<TElem>;
private:
    int cp;
    int n;
    TElem* elems;
    void redimensionare();
public:
    MyVector();

    MyVector(const MyVector& ot);

    MyVector<TElem>& operator=(MyVector<TElem>&& ot);

    TElem& operator[](int poz) const;

    void add(const TElem& p);

    void pop_back();

    int size() const;

    bool empty() const;

    int get_cp() const;

    MyVectorIterator<TElem> iterator(){
        return MyVectorIterator<TElem>(*this);
    }

    MyVectorIterator<TElem> begin(){
        return MyVectorIterator<TElem>(*this,0);
    }

    MyVectorIterator<TElem> end(){
        return MyVectorIterator<TElem>(*this,n);
    }

    TElem& get(int poz) const;


    MyVector(MyVector<TElem>&& ot);

    ~MyVector();
};

template<typename TElem>
MyVector<TElem>::MyVector(MyVector<TElem>&& ot) {
    // Copy the data pointer from other
    elems = ot.elems;
    n = ot.n;
    cp = ot.cp;

    ot.elems = nullptr;
    ot.n = 0;
    ot.cp = 0;
}

template<typename TElem>
TElem& MyVector<TElem>::get(int poz) const {
    return elems[poz];
}

template<typename TElem>
MyVector<TElem>::MyVector() : cp{2},n{0},elems{new TElem[cp]}{}

template<typename TElem>
MyVector<TElem>::MyVector(const MyVector& ot){
    auto* new_elems=new TElem[ot.cp];
    cp=ot.cp;
    n=ot.n;
    for(int i=0;i<ot.cp;i++)
        new_elems[i]=ot.elems[i];
    elems=new_elems;
}

template<typename TElem>
MyVector<TElem>& MyVector<TElem>::operator=(MyVector<TElem>&& ot){
    if (this == &ot) {
        return *this;
    }
    delete[] elems;
    // Copy the data pointer from other
    elems = ot.elems;
    n = ot.n;
    cp = ot.cp;

    // Release the data pointer from the source object so that
    // the destructor does not free the memory multiple times.
    ot.elems = nullptr;
    ot.n = 0;
    ot.cp = 0;
    return *this;
}

template<typename TElem>
TElem& MyVector<TElem>::operator[](int poz) const{
    return elems[poz];
}

template<typename TElem>
void MyVector<TElem>::MyVector::add(const TElem &p) {
    if(n==cp)
        redimensionare();
    elems[n++]=p;
}

template<typename TElem>
void MyVector<TElem>::pop_back(){
    n--;
}

template<typename TElem>
int MyVector<TElem>::MyVector::size() const{
    return n;
}


template<typename TElem>
void MyVector<TElem>::MyVector::redimensionare() {
    auto* new_elems=new TElem[cp*2];
    cp*=2;
    for(int i=0;i<n;i++)
        new_elems[i]=elems[i];
    delete[] elems;
    elems=new_elems;
}

template<typename TElem>
int MyVector<TElem>::MyVector::get_cp() const{
    return cp;
}

template<typename TElem>
bool MyVector<TElem>:: empty() const{
    return n==0;
}

template<typename TElem>
MyVector<TElem>::~MyVector(){
    delete[] elems;
}


#endif //LAB6_ISTVAN_MYVECTOR_H
