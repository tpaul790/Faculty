#include <iostream>
#include <fstream>

using namespace std;
/*
ifstream fin("in.txt");
ofstream fout("out.txt");
*/

void increm(int* p){
    *p+=1;
}

int main(){
    int n;
    cin>>n;
    int* adress=&n;
    n+=1;
    printf("%d",*adress);
    return 0;
}