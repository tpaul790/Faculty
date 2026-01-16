#include <iostream>
#include <thread>
#include <cassert>
#include <chrono>
#include <cmath>
#include <cstring>

using namespace std;
using namespace std::chrono;

const int N = 100000;
const int P = 16;
const int MAX_VAL = 6000;
int a[N], b[N], c[N], c_secv[N];

void init(int* v, int len){
    for(int i=0;i < len; i++)
        v[i] = rand() % 6000;
}

void sum(const int* a, const int* b, int* c, int len){
    auto start_secv = high_resolution_clock::now();
    for(int i=0; i < len; i++){
//        c[i] = a[i] + b[i];
        c[i] = sqrt(pow(a[i],2) + pow(b[i],2))- pow(3, a[i]);;
    }
    auto end_secv = high_resolution_clock::now();
    duration<double, milli> delta_secv = end_secv - start_secv;

    cout<<"Secv: "<<delta_secv.count()<<" ms"<<endl;

}

void work(const int* a, const int* b, int* c, int start, int end){
    for(int i=start; i < end; i++){
//        c[i] = a[i] + b[i];
        c[i] = sqrt(pow(a[i],2) + pow(b[i],2)) - pow(3, a[i]);
    }
}

void functie_noua(const int* a, const int* b, int* c){
    int elems_slice = N/P;
    int r = N%P;

    int start_idx = 0;
    int end_idx = elems_slice;

    thread t[P];

    auto start_p = high_resolution_clock::now();
    for(int tid = 0; tid < P; tid++){
        if(r > 0){
            end_idx ++;
            r --;
        }

//        cout<< "start:" << start_idx<< " end:" << end_idx<< endl;
        t[tid] = thread(work, a, b, c, start_idx, end_idx);

        start_idx = end_idx;
        end_idx += elems_slice;
    }

    for(auto & th : t)
        th.join();

    auto end_p = high_resolution_clock::now();
    duration<double, milli> delta_p = end_p - start_p;

    for(int i=0; i<N; i++)
        assert(c[i] == c_secv[i]);
    cout<<"Paralel: "<<delta_p.count()<<" ms"<<endl;
}

int main() {
    init(a,N);
    init(b,N);

    sum(a,b,c_secv, N);

    functie_noua(a,b,c);

    int *all = new int[3*N];


    int *a_dyn = new int[N];
    int *b_dyn = new int[N];
    int *c_dyn = new int[N];

    memcpy(a_dyn, a, N* sizeof(int));
    memcpy(b_dyn, b, N* sizeof(int));

    memcpy(all, a, N* sizeof(int));
    memcpy(all+N, b, N* sizeof(int));

    functie_noua(a_dyn, b_dyn, c_dyn);
    functie_noua(all, all+N, all+N+N);

    delete[] a_dyn;
    delete[] b_dyn;
    delete[] c_dyn;
    delete[] all;

    return 0;
}

/*
 *
 * n linii => i = clamp(i, 0, n-1) daca i<0 sau i>n
 * m coloane => j = clamp(j, 0, m-1) daca j<0 sau j>m
 * a_ij = i * m + j = i + j * n
 *
 * i = a_ij / m = a_ij % n
 * j = a_ij / n = a_ij % m
 */
