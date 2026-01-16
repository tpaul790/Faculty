/*
roblema: Procesarea Matricei și Agregarea Statistică
Vrem să procesăm o matrice (sau un array mare) distribuită și să calculăm simultan(1) suma
totală și (2) media elementelor, asigurându-ne că toată lumea știe valoarea medie la final.

Obiectiv: Folosiți funcțiile colective pentru a împărți o problemă mare,
a o procesa, a agrega rezultatele și a disemina rezultatul final (media) către toate procesele.
*/

#include <iostream>
#include <mpi.h>
#include <vector>

using namespace std;

const int GLOBAL_SIZE = 10000;
const int SUM_TAG = 1;
const int AVG_TAG = 2;

void init(vector<double>& v, int n) {
    v.resize(n);
    srand(time(NULL));
    for (int i = 0; i < n; i++) {
        v[i] = rand() % 100;
    }
}

double secv_avg(const vector<double>& v) {
    double sum = 0;
    for ( int i = 0; i < GLOBAL_SIZE; i++ ) {
        sum += v[i];
    }
    return sum / GLOBAL_SIZE;
}

int main(int argc, char** argv) {

    MPI_Init(&argc, &argv);

    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    vector<double> global;
    const int LOCAL_SIZE = GLOBAL_SIZE / size;
    vector<double> local(LOCAL_SIZE);
    double expected_avg = 0;
    double global_sum;

    if (rank == 0) {
        init(global, GLOBAL_SIZE);
        expected_avg = secv_avg(global);
    }

    MPI_Scatter(global.data(), LOCAL_SIZE, MPI_DOUBLE, local.data(), LOCAL_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    double local_sum = 0;
    for (int i = 0 ; i < LOCAL_SIZE ; i++) {
        local_sum += local[i];
    }
    cout<<"[ Rank = "<< rank << " ] Local sum: "<< local_sum << endl;

    MPI_Reduce(&local_sum, &global_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

    double global_avg;

    if ( rank == 0 ) {
        global_avg = global_sum / GLOBAL_SIZE;
        cout << "[ Rank = "<< rank << " ] Expected avg: " << expected_avg << endl;
    }

    MPI_Bcast(&global_avg, 1, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    cout <<"[ Rank = "<< rank << " ] Actual avg: " << global_avg << endl;

    MPI_Finalize();

    return 0;
}