#include <omp.h>
#include <iostream>

using namespace std;

const int N = 10000000;
int a[N], b[N], c[N];

void init(){
    for(int i = 0; i < N; i++){
        int nr = rand()%1000;
        a[i]=nr,b[i]=nr,c[i]=0;
    }
}

void validate(){
    for(int i = 0; i < N; i++){
        if(a[i] + b[i] != c[i]){
            cout<<"Sum not valid"<<endl;
            exit(1);
        }
    }
    cout<<"Sum valid"<<endl;
}

int sum(){
    double time = omp_get_wtime();
    #pragma parallel for schedule(dynamic, 1)
    {
        for(int i = 0; i < N; i++){
            c[i] = a[i] + b[i];
        }
    }
    double time2 = omp_get_wtime();
    cout<<"Sum time: "<< time2-time<<endl;
}

int scalar_prod(){
    int sum = 0;
    double time = omp_get_wtime();
//    #pragma parallel for schedule(dynamic, 1)
//    {
//        for(int i = 0; i < N; i++) {
//            #pragma omp atomic
//                sum += a[i] * b[i];
//        }
//    }

    int aux = 0;
    #pragma omp parallel shared(sum) firstprivate(aux)
    {
        #pragma omp for nowait
        for (int i = 0; i < N; i++) {
            #pragma omp atomic
            aux += a[i] * b[i];
        }
        #pragma omp atomic
        sum += aux;
    }
    double time2 = omp_get_wtime();
    cout<<"Sum time: "<< time2-time<<endl;
    return sum;
}

void validate_scalar_prod(int sum){
    for(int i = 0; i < N; i++){
        sum -= a[i] * b[i];
    }
    if(sum == 0) {
        cout << "Scalar sum valid" << endl;
    }
    else{
        cout<<"Scalar sum not valid"<<endl;
    }
}

// Computes the sum of the first 10 natural numbers (1..10) using OpenMP
int sum_first_10_numbers_omp() {
    int total = 0;
    #pragma omp parallel for reduction(+:total)
    for (int i = 1; i <= 10; ++i) {
        total += i;
    }
    return total;
}

void omp_sections(){
#pragma omp parallel
    {
    #pragma omp sections
        {
        #pragma omp section
            {
                cout << "Hello from thread "
                     << omp_get_thread_num() << " / "
                     << omp_get_num_threads() << " / "
                     << omp_get_max_threads()
                     << "\n";
            }

        #pragma omp section
            {
                cout << omp_get_thread_num<<endl;
                for(int i = 0; i < N; i++){
                        c[i] = a[i] * b[i];
                }
                validate();
            }
        }
    }
}

int main() {
//    #pragma omp parallel num_threads(5)
//    {
//        #pragma omp critical
//        {//fork
//            cout << "Hello from thread "
//                 << omp_get_thread_num() << " / "
//                 << omp_get_num_threads() << " / "
//                 << omp_get_max_threads()
//                 << "\n";
//        //join
//        }
//    }

    init();
    int result = scalar_prod();
    validate_scalar_prod(result);

}